package Floor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

import Utility.Event;
import Utility.Helper;
import Utility.Transition;

/**
 * {@summary The floor subsystem reads in events from the scheduler (time,
 * floor, elevator number, and button). Each line of input from the Storage data
 * structure is sent to the Scheduler}
 */

public class Floor {
	
	public String name = "FLoor";
	private final int SCHEDULER_PORT = 20;
	
	//Datagram Pakcets and Socket
	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendReceiveSocket;
	
	public static final String FILEPATH = "Elevator_System/floor-commands.txt";

	private PriorityQueue<Event> floorEventRequests;
	private ArrayList<Event> pendingEventRequests;
	
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	private FloorState state;
	private ArrayList<FloorState> stateHistory;

	public Floor() {
		
		try {
			sendReceiveSocket = new DatagramSocket(42);
		} 
		catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		
		//scheduler = s;
		
		state = FloorState.getInitialState();		
		stateHistory = new ArrayList<FloorState>();
		
		//add initial state
		stateHistory.add(state);
		
		// event list for the floor subsystem
		pendingEventRequests = new ArrayList<>();
		
		// read floor requests from file
		floorEventRequests = new PriorityQueue<>();
		
		try {
			Scanner fileInput = new Scanner(new File(FILEPATH));
			
			while (fileInput.hasNextLine()) {
				String line = fileInput.nextLine();
				Event event = new Event(line);
				
				floorEventRequests.add(event);
			}
			
			fileInput.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND");
			System.exit(1); //Terminate JVM
		}
	}
	
	public void sendAndReceive() {
		// send a request
		
		Event event = makeFloorRequest();
		while(event != null) {
		
			sendPacket = makeFloorRequestDatagram(event);
			
			Helper.printSend(name, sendPacket, false, "Scheduler");
			try {
				sendReceiveSocket.send(sendPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			// receive visit
			// listen for response from Scheduler
			receivePacket = new DatagramPacket(sendPacket.getData(), sendPacket.getData().length);
	
			try {
				// Block until a datagram is received via sendReceiveSocket.	
				sendReceiveSocket.receive(receivePacket);
			} 
			catch(IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
	
			// Process the received datagram.
			Helper.printReceive(name, receivePacket, false, "Scheduler");
			event = makeFloorRequest();
		}
	}
	
	public static void main(String[] args) {
		Floor floor = new Floor();
		floor.sendAndReceive();
		Thread t = new Thread(new SecondThread(),"Receive Thread");
		t.start();
	}

	/*
	 * Helper function: Simulates all button presses (from data structure)
	 */
	public Event makeFloorRequest() {
		if (!floorEventRequests.isEmpty()) {
			
			changeState(Transition.PRESS_BUTTON);
			
			Event fr = floorEventRequests.poll();
			
			pendingEventRequests.add(fr);
			
			return fr;
		}
		return null;
	}
	
	private DatagramPacket makeFloorRequestDatagram(Event event) {
		byte[] msg = event.toString().getBytes();
		 
		DatagramPacket newPacket;
		
		try {
			newPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), SCHEDULER_PORT);
			return newPacket;
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	
	public void changeState(Transition t) {
		state = state.next(t);
		stateHistory.add(state);
		

	}
	
	/*
	 * Helper function: Adds sleep statement so logs are readable
	 */
	public void log() {
		try {
				Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	/*
	 * Get & set methods for class attributes
	 */

	public PriorityQueue<Event> getFloorEventRequests() {
		return floorEventRequests;
	}

	public void setFloorEventRequests(PriorityQueue<Event> floorEventRequests) {
		this.floorEventRequests = floorEventRequests;
	}

	public ArrayList<Event> getPendingEventRequests() {
		return pendingEventRequests;
	}

	public void setPendingEventRequests(ArrayList<Event> pendingEventRequests) {
		this.pendingEventRequests = pendingEventRequests;
	}

	public DateTimeFormatter getDtf() {
		return dtf;
	}

	public void setDtf(DateTimeFormatter dtf) {
		this.dtf = dtf;
	}

	public FloorState getState() {
		return state;
	}

	public void setState(FloorState state) {
		this.state = state;
	}

	public ArrayList<FloorState> getStateHistory() {
		return stateHistory;
	}

	public void setStateHistory(ArrayList<FloorState> stateHistory) {
		this.stateHistory = stateHistory;
	}

	public static String getFilepath() {
		return FILEPATH;
	}
}

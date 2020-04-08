package Floor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

import Utility.Event;
import Utility.Helper;

/**
 * {@summary The floor subsystem reads in events from the scheduler (time,
 * floor, elevator number, and button). Each line of input from the Storage data
 * structure is sent to the Scheduler}
 */

public class Floor {
	
	public String name = "FLoor";
	public static final String FILEPATH = "Elevator_System/floor-commands.txt";
	
	private final int SCHEDULER_PORT = 20;
	
	//Datagram Packets and Socket
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;
	
	private PriorityQueue<Event> floorEventRequests;
	private ArrayList<Event> pendingEventRequests;
	private HashSet<Event> completedRequests;
	
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	public Floor() {
		
		try {
			sendReceiveSocket = new DatagramSocket(42);
		} 
		catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		// events completed by the Elevator System
		completedRequests = new HashSet<Event>();
		
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
		Thread t = new Thread(new ReceiverThread(floor),"Receive Thread");
		t.start();
	}

	/*
	 * Helper function: Simulates all button presses (from data structure)
	 */
	public Event makeFloorRequest() {
		if (!floorEventRequests.isEmpty()) {
			
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

	public synchronized void completeRequest(Event event) {
		System.out.println("@" + dtf.format(LocalDateTime.now()) + ": " +"Request Completed:" + event);
		completedRequests.add(event);
	}
	
	public void setPendingReq(ArrayList<Event> arr) {
		if(arr != null)
			pendingEventRequests = arr;
	}
	
	public void setFloorEventReq(PriorityQueue<Event> arr) {
		if(arr != null)
			floorEventRequests = arr;
	}
	
	public HashSet<Event> getCompletedReq() {
		return completedRequests;
	}
}

package Elevator;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.PriorityQueue;
import java.util.ArrayList;

import Utility.Direction;
import Utility.Helper;

/**
 * {@summary The elevator subsystem notifies the scheduler that an elevator has
 * reached a floor. Once an elevator has been told to move, the elevator
 * subsystem is informed so that it can send out messages back to the scheduler.}
 */

public class Elevator implements Runnable {

	
	private int currentFloor;
	private Direction dir = Direction.Static;
	private ElevatorState state; // state machine for the elevator subsystem
	
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;
	
	
	public Elevator(String name, int socket) {
		
		currentFloor = 0;
		state = ElevatorState.getInitialState();
		System.out.println("ELEVATOR STATE: " + state);
		
		try {
			sendReceiveSocket = new DatagramSocket(socket);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean checkForStop() {
		
		try {
			byte[] msg = buildMSG();
			sendPacket = new DatagramPacket(msg, msg.length,InetAddress.getLocalHost(),22);
			sendReceiveSocket.send(sendPacket);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		
		try {
			sendReceiveSocket.receive(receivePacket);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.err.println(receivePacket.getData()[1]);
		
		if(receivePacket.getData()[1] == 0)
			dir = Direction.Up;
		else if(receivePacket.getData()[1] == 1)
			dir = Direction.Down;
		else
			dir = Direction.Static;
		
		if(receivePacket.getData()[0] == 0)
			return false;
		else
			return true;
			
	}
	
	/*
	 * Helper function: Simulates travel with state transitions
	 */
	private synchronized void goToNextFloor() {
		
		try {
			state = ElevatorState.MOVING;
			log();
			
			Thread.sleep(600);
			if(dir == Direction.Up)
				currentFloor++;
			else if(dir == Direction.Down)
				currentFloor--;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if(checkForStop())
			stop();
		
		
	}

	private synchronized void stop() {
		
		try {
			state = ElevatorState.STOPPED;
			log();
			Thread.sleep(300);
			state = ElevatorState.DOORS_OPEN;
			log();
		}catch(Exception e) {
			e.printStackTrace();
		
		}
	}
	
	private synchronized void load(int numOfPassengers) {
		
		try {
			Thread.sleep(300 + numOfPassengers*700);
			state = ElevatorState.DOORS_CLOSED;
			log();
			goToNextFloor();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Helper function: Logs elevator states to the console
	 */
	private void log() {
		System.out.println("@" + dtf.format(LocalDateTime.now()) + ": " +Thread.currentThread().getName() + " on Floor:\t\t" + currentFloor + " "+state);
	}

	private byte[] buildMSG() {
		int i = 0;
		byte[] msg = new byte[5];
		for(Byte b: ByteBuffer.allocate(4).putInt(currentFloor).array()) {
			msg[i++] = b;
		}
		msg[4] = (byte) ((dir==Direction.Up)?0:(dir == Direction.Down)?1:2);
		return msg;
	}
	
	private void register() {
		try {
			byte msg[] = buildMSG();
			sendPacket = new DatagramPacket(msg,msg.length,InetAddress.getLocalHost(),21);
			sendReceiveSocket.send(sendPacket);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Run
	 */
	@Override
	public void run() {	
		
		register();
		while (true) {
			if(dir != Direction.Static) {
				if(state == ElevatorState.DOORS_OPEN)
					load(0);
				else 
					goToNextFloor();
			}
			else {
				byte data[] = new byte[100];
				receivePacket = new DatagramPacket(data, data.length);
				try {
					sendReceiveSocket.receive(receivePacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
				
				if(receivePacket.getData()[1] == 0)
					dir = Direction.Up;
				else if(receivePacket.getData()[1] == 1)
					dir = Direction.Down;
				else
					dir = Direction.Static;
			}

			log();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}


	public static void main(String[] args) {
		Thread e1,e2;

		e1 = new Thread(new Elevator("Elevator1",(int) (Math.random()*1000)));
		//e2 = new Thread(new Elevator("Elevator2",(int) (Math.random()*1000)));
		
		e1.start();
		//e2.start();
	}

}

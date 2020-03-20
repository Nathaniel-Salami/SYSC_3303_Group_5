package Scheduler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import Utility.Direction;


public class RegistrationThread implements Runnable {

	private DatagramSocket socket;
	private DatagramPacket request;
	
	private RestructuredSchedulder scheduler;
	
	public RegistrationThread(RestructuredSchedulder scheduler) {
		this.scheduler = scheduler;
		
		try {
			socket = new DatagramSocket(21);
		}catch(Exception e) {e.printStackTrace();}
	}
	
	private void register() {
		request = new DatagramPacket(new byte[100], 100);
		
		try {
			socket.receive(request);
		}catch(Exception e) {}
		
		byte data[] = request.getData();
		ByteBuffer wrapper = ByteBuffer.wrap(data, 0, 4);
		int floor = wrapper.getInt();
		Direction dir = (data[4] == 0)?Direction.Up:(data[4] == 1)?Direction.Down:Direction.Static; 
		
		ElevatorRepresentation representation = new ElevatorRepresentation(request.getAddress(), request.getPort(), floor, dir);
		scheduler.registerElevator(representation);
		
		String msg = String.format("IP %s Port: %d: Registered Elevator Car", socket.getLocalSocketAddress() ,socket.getLocalPort());
		System.out.println(msg);
		/*response = new DatagramPacket(msg.getBytes(), msg.getBytes().length,request.getAddress(),request.getPort());
		
		try {
			socket.send(response);
		}catch(Exception e) {}*/
	}
	
	public void run() {
		while(true) {
			register();
		}
	}
	
}

package Scheduler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import Utility.Event;

public class ArrivalThread implements Runnable {
	
	private RestructuredSchedulder scheduler;
	private DatagramSocket socket;
	private DatagramPacket response;
	
	public ArrivalThread(RestructuredSchedulder scheduler) {
		this.scheduler = scheduler;
		try {
			socket = new DatagramSocket(24);
		}catch(Exception e) {}
	}
	
	public void sendCompletedRequest() {
		Event event = scheduler.requests.getFinishedRequest();
		byte msg[] = event.toString().getBytes();
		try {
			response = new DatagramPacket(msg, msg.length,scheduler.floor,43);
			socket.send(response);
		}catch(Exception e) {}
		
	}
	
	public void run() {
		while(true) {
			sendCompletedRequest();
		}
	}
}

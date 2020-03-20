package Scheduler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import Utility.Event;

public class RequestThread implements Runnable {

	private RestructuredSchedulder scheduler;
	private DatagramSocket socket;
	private DatagramPacket request,response;
	
	public RequestThread(RestructuredSchedulder scheduler) {
		this.scheduler = scheduler;
		try {
			socket = new DatagramSocket(20);
		}catch(Exception e) {e.printStackTrace();}
	}
	 
	private Event receiveRequest() {
		request = new DatagramPacket(new byte[100], 100);
		
		try {
			socket.receive(request);
		}catch(Exception e) {}
		
		scheduler.floor = request.getAddress();
		Event ev = new Event(new String(request.getData()).trim());
		System.out.println(Thread.currentThread().getName() + ": Received Request from floor: " + ev);
		return ev;
	}
	
	private void respond() {
		byte r[] = new byte[0];
		response = new DatagramPacket(r, 0, request.getAddress(), request.getPort()) ;
		
		try {
			socket.send(response);
		}catch(Exception e) {}

		System.out.println(Thread.currentThread().getName() + ": Send Confirmation to floor");
	}
	
	@Override
	public void run() {
		while(true) {
			Event req = receiveRequest();
			scheduler.requests.addPendingRequest(req);
			respond();
		}
	}

}

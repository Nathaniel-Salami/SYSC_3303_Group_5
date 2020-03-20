package Scheduler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;

import Utility.Direction;
import Utility.Event;
import Utility.Helper;

public class ElevatorContolThread implements Runnable {

	private RestructuredSchedulder scheduler;
	private DatagramSocket socket;
	private DatagramPacket request, response;
	
	
	public ElevatorContolThread(RestructuredSchedulder scheduler) {
		this.scheduler = scheduler;
		
		try {
			socket = new DatagramSocket(22);
		}catch(Exception e) {e.printStackTrace();}
	}
	
	private ElevatorRepresentation getElevatorMsg() {
		//wait for a massage from an Elevator
		request = new DatagramPacket(new byte[100], 100);
		try {
			socket.receive(request);
		}catch (Exception e) {e.printStackTrace();}
		
		ElevatorRepresentation rep = Helper.extractInformation(request); 
		return rep;
	}
	
	private void checkForStops(ElevatorRepresentation elevator) {
		
		boolean stop = false;
		HashSet<Event> completedRequests = new HashSet<Event>();
		
		//Check if we need to stop at a particular floor
		System.out.println(scheduler.getRequests(elevator));
		System.out.println(elevator);
		
		for(Event event: scheduler.getRequests(elevator)) {
			if(event.getFloor() == elevator.floor) {
				elevator.dir = event.getDirection();
				stop = true;
			}
			else if(event.getDestination() == elevator.floor && event.getDirection() == elevator.dir) {
				stop = true;
				completedRequests.add(event);
			}
		}
		
		//Update the requests for this Elevator
		for(Event e: completedRequests) {
			scheduler.removeRequest(elevator, e);
			scheduler.requests.addFinishedRequest(e);
		}
		
		if(scheduler.getRequests(elevator).isEmpty()) {
			elevator.dir = Direction.Static;
			elevator.inUse = false;
		}
		
		byte msg[] = Helper.constuctMessage(stop, elevator.dir);
		
		response = new DatagramPacket(msg, 2,elevator.address,elevator.port);
		try {
			socket.send(response);
		}catch(Exception e) {e.printStackTrace();}
		
		scheduler.prioMap.get(scheduler.getElevatorIndex(elevator)).getKey().copy(elevator);
	}
	
	@Override
	public void run() {
		while(true) {
			ElevatorRepresentation rep = getElevatorMsg();
			checkForStops(rep);
		}
	}

}

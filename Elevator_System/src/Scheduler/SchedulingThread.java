package Scheduler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;

import Utility.Direction;
import Utility.Event;
import Utility.Helper;

public class SchedulingThread implements Runnable {
	
	private RestructuredSchedulder scheduler;
	private DatagramSocket socket;
	private DatagramPacket response;
	
	
	public SchedulingThread(RestructuredSchedulder scheduler) {
		this.scheduler = scheduler;
		
		try {
			socket = new DatagramSocket(23);
		}catch(Exception e) {e.printStackTrace();}
	}
	
	private float costFunction(ElevatorRepresentation elevator, Event fr) {return Math.abs(elevator.floor - fr.getFloor());}
	
	private void assignElevators() {

		int index = -1;
		float min;
		HashSet<Event> active = new HashSet<>();
		
		for(int i = 0; i < scheduler.requests.sizePending(); i++) {
			Event request = scheduler.requests.getPrendingRequest(i);
			
			index = -1;
			min = Float.MAX_VALUE;
			
			for(int j = 0; j < scheduler.prioMap.size();j++) {
				if(scheduler.prioMap.get(j).getValue().isEmpty() && !scheduler.prioMap.get(j).getKey().inUse) {
					float cost = costFunction(scheduler.prioMap.get(j).getKey(), request);
					if(cost < min) {
						index = i;
						min = cost;
					}
				}
			}
			
			if(index > -1) {
				scheduler.addRequest(scheduler.prioMap.get(index).getKey(),request);
				scheduler.prioMap.get(index).getKey().inUse = true;
				Direction dir = Direction.Static;
				
				if(scheduler.prioMap.get(index).getKey().floor == request.getFloor())
					dir = request.getDirection();
				else
					dir = (scheduler.prioMap.get(index).getKey().floor > request.getFloor())?Direction.Down:Direction.Up;
				
				System.err.println(dir);
				byte[] msg = Helper.constuctMessage(false, dir);
				
				response = new DatagramPacket(msg, msg.length,scheduler.prioMap.get(index).getKey().address,scheduler.prioMap.get(index).getKey().port);
				try {
					socket.send(response);
				}catch(Exception e) {e.printStackTrace();}
				System.err.println(Thread.currentThread() + ": Directions sent to Elevator: " + scheduler.prioMap.get(index).getKey().address + " "+scheduler.prioMap.get(index).getKey().port);
				active.add(request);
			}
		}
		
		for(Event ev: active)
			scheduler.requests.removePrendingRequest(ev);
		
	}
	
	public void run() {
		
		while(true) {
			try {
				Thread.sleep(1000);
			}catch (Exception e) {
				// TODO: handle exception
			}
			assignElevators();
		}
		
	}
	

}

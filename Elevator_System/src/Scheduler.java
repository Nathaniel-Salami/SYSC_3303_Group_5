import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.PriorityQueue;

import Storage.Event;

public class Scheduler implements Runnable {

	private Event pendingR; // Request received from floor
	private Event pendingV; // Visit confirmation received from elevator
	
	private Elevator elevator;
	//private PriorityQueue<Event> pendingTasks; 
	
	private final static int TIME = 600;
	
	public static void sleep(int t) {
		try {
			Thread.sleep(t);
		} 
		catch (InterruptedException e) {
			System.out.println("INSOMNIA");
		}
	}

	public Scheduler(Elevator elevator) {
		pendingR = null;
		pendingV = null;
		
		this.elevator = elevator;
		//pendingTasks = new PriorityQueue<Event>();
	}

	// receive from Floor to Elevator
	public synchronized void receiveFromFloor(Event fr) {
		
		while (pendingR != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		
		//sleep(TIME);
		//ToolBox.sleep();

		pendingR = fr;
		//pendingTasks.add(pendingR);
	}

	// send to Floor from Elevator
	public synchronized Event sendToFloor() {
		
		 while (pendingV == null) {
		 	try {
		 		wait();
		 		System.out.println("floor waiting1");
		 	} catch (InterruptedException e) {
		 		return null;
		 	}
		 }

		Event out = pendingV;
		pendingV = null;

		//sleep(TIME);
		//ToolBox.sleep();
		
		return out;
	}
		
	private Elevator getBestElevator() {
		//sleep(TIME);
		return elevator; // only one elevator
	}
	
	public Event getNextCompletedTask() {		
		Event completed = elevator.reportCompletedTask();
		
		//sleep(TIME);
		return completed;
	}

	@Override
	public void run() {
		
		
		while (true) {
			// send a floor request to an elevator
			if (pendingR != null) {
				Elevator currentElevator = getBestElevator();
				getBestElevator().recieveFLoorRequest(pendingR);
				
				//ToolBox.sleep();
				System.out.println(ToolBox.getNow() + ": " + Thread.currentThread().getName() + " sent:\t" + pendingR);
				pendingR = null;
			}
			
			// get visited report from elevator
			Event completed = getNextCompletedTask();
			if (completed != null) {
				//sleep(TIME);
				pendingV = completed;
				
				System.out.println(ToolBox.getNow() + ": " + Thread.currentThread().getName() + " found:\t" + pendingV);
			}
		}
	}

	public Event getPendingR() {
		return pendingR;
	}

	public void setPendingR(Event pendingR) {
		this.pendingR = pendingR;
	}

	public Event getPendingV() {
		return pendingV;
	}

	public void setPendingV(Event pendingV) {
		this.pendingV = pendingV;
	}
	
}

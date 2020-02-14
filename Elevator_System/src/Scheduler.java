import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Storage.Event;

public class Scheduler implements Runnable {

	private final int TIME = 300;

	private Event pendingR; // Request received from floor
	private Event pendingV; // Visit confirmation received from elevator
	
	SchedulerState state;
	
	ArrayList<SchedulerState> stateHistory;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public Scheduler() {
		pendingR = null;
		pendingV = null;
		
		state = SchedulerState.START;
		stateHistory = new ArrayList<SchedulerState>();
		
		//add initial state
		stateHistory.add(state);
	}

	public void sleep(int t) {
		try {
			Thread.sleep(t);
		} 
		catch (InterruptedException e) {}
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

		pendingR = fr;

		sleep(TIME);
		
		this.notifyAll();
		
		state = state.next(Transition.RECEIVE);
		stateHistory.add(state);
	}

	// send to Elevator from Floor
	public synchronized Event sendToElevator() {
		
		while (pendingR == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}

		Event out = pendingR;
		pendingR = null;

		sleep(TIME);

		this.notifyAll();
		
		state = state.next(Transition.SEND);
		stateHistory.add(state);
		
		return out;
	}

	// receive from Elevator to Floor
	public synchronized void receiveFromElevator(Event fv) {
		
		while (pendingV != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		
		//pendingR = "";
		pendingV = fv;

		sleep(TIME);
		
		this.notifyAll();
		
		state = state.next(Transition.RECEIVE);
		stateHistory.add(state);
	}

	// send to Floor from Elevator
	public synchronized Event sendToFloor() {
		
		 while (pendingV == null) {
		 	try {
		 		wait();
		 	} catch (InterruptedException e) {
		 		return null;
		 	}
		 }

		Event out = pendingV;
		pendingV = null;

		sleep(TIME);
		
		this.notifyAll();
		
		changeState(Transition.SEND);
		changeState(Transition.RECEIVE); //return to start
		
		System.out.println("SCHEDULER STATE: " + stateHistory);
		
		return out;
	}
	
	public void changeState(Transition t) {
		state = state.next(t);
		stateHistory.add(state);
	}

	@Override
	public void run() {

		while (true) {
//			 try {
//			 	Thread.sleep(1000);
//			 } catch (InterruptedException e) {}
//			 System.out.println("SCHEDULER: " + stateHistory);
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

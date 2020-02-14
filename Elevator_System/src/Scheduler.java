import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Storage.Event;

public class Scheduler implements Runnable {

	private final int TIME = 300;

	private Event pendingR; // Request received from floor
	private Event pendingV; // Visit confirmation received from elevator
	
	SchedulerState state;
	
	Elevator elevator;
	
	ArrayList<SchedulerState> stateHistory;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public Scheduler(Elevator elevator) {
		pendingR = null;
		pendingV = null;
		
		this.elevator = elevator;
		
		state = SchedulerState.START;
		stateHistory = new ArrayList<SchedulerState>();
		
		//add initial state
		stateHistory.add(state);
	}

	// receive from Floor to Elevator
	public synchronized void receiveFromFloor(Event fr) {
		
		while (pendingR != null) {
			try {
				//logThreadWait("receiveFromFloor");
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

	// send to Floor from Elevator
	public synchronized Event sendToFloor() {
		
		 while (pendingV == null) {
		 	try {
		 		//logThreadWait("sendToFloor");
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
		
		//System.out.println("SCHEDULER STATE: " + stateHistory);
		
		return out;
	}
	
	public synchronized void updatePendingValues() {
		pendingV = pendingR;
		pendingR = null;
		
		this.notifyAll();
	}
	
	public void changeState(Transition t) {
		state = state.next(t);
		stateHistory.add(state);
	}

	@Override
	public void run() {

		while (true) {
			
			try {
				Thread.sleep(300);
			} 
			catch (InterruptedException e) {}
			LocalDateTime now = LocalDateTime.now();
			//System.out.println("@" + dtf.format(now) + " " + Thread.currentThread().getName() + ": " + pendingR + " : " + pendingV);
			
			//attempt to fulfill request if one is available
			//check elevator current floor
			if (pendingR != null) {
				//if elevator has not picked up a passenger, move to the floor of the passenger
				if (!elevator.loaded) {
					//move to the floor of the passenger
					if (elevator.currentFloor != pendingR.getFloor()) {
						elevator.moveTo(pendingR.getFloor());
					}
					//elevator is already there
					else {
						//floor loads
						elevator.loaded = true;
					}
				}
				
				//if the elevator is loaded but the elevator is not yet at the destination, move to the destination
				else if (elevator.loaded) {
					//move to the destination of the passenger
					if (elevator.currentFloor != pendingR.getDestination()) {
						elevator.moveTo(pendingR.getDestination());
					}
					//elevator is already there
					else {
						//floor unloads
						elevator.loaded = false;
						
						updatePendingValues();
					}
				}
			}
		}
	}
	
	public void logElevator() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println("@" + dtf.format(now) + " " + Thread.currentThread().getName() + 
				": Elevator: Floor[" + elevator.currentFloor + 
				"] Loaded[" + elevator.loaded + "]");
	}
	
	public void logThreadWait(String function) {
		System.out.println(Thread.currentThread().getName() + " is waiting at " + function);
	}

	public void sleep(int t) {
		try {
			Thread.sleep(t);
		} 
		catch (InterruptedException e) {}
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

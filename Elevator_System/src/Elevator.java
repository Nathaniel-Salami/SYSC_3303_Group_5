import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.PriorityQueue;

import Storage.Event;

public class Elevator implements Runnable {
	
	int currentFloor;
	boolean goingUp;
	boolean loaded;
	
	ElevatorState state;
	ArrayList<ElevatorState> stateHistory;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public Elevator() {
		loaded = false;
		currentFloor = 0;
		goingUp = true;
		
		state = ElevatorState.DOORS_CLOSED;
		stateHistory = new ArrayList<ElevatorState>();
		
		//add initial state
		stateHistory.add(state);
	}
	
	public int goUp() {
		currentFloor++;
		goingUp = true;
		
		return currentFloor;
	}
	
	public int goDown() {
		currentFloor--;
		goingUp = false;
		
		return currentFloor;
	}
	
	public int moveTo(int floor) {
		//System.out.println("move to: " + floor + " from: " + currentFloor);
		
		if (currentFloor > floor) { //elevator is above the requesting floor
			goDown();
		}
		else {
			goUp();
		}
		
		try {
			Thread.sleep(300);
		} 
		catch (InterruptedException e) {}
		
		LocalDateTime now = LocalDateTime.now();
		System.out.println("@" + dtf.format(now) + " " + Thread.currentThread().getName() + ": Elevator Floor[" + currentFloor + "] Loaded[" + loaded + "]");
		
		return currentFloor;
	}
	
	public void changeState(Transition t) {
		state = state.next(t);
		stateHistory.add(state);
	}
	
	@Override
	public void run() {
		while (true) {
			/*
			try {
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {}
			LocalDateTime now = LocalDateTime.now();
			System.out.println("@" + dtf.format(now) + " " + Thread.currentThread().getName() + 
					": Floor[" + currentFloor + 
					"] Loaded[" + loaded + "]");
			*/
		}
	}
}

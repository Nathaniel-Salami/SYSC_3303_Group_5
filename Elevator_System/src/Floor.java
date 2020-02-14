import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

import Storage.Event;

public class Floor implements Runnable {
	
	private Scheduler scheduler;
	
	public static final String FILEPATH = "Elevator_System/floor-commands.txt";

	private PriorityQueue<Event> floorEventRequests;
	private ArrayList<Event> pendingEventRequests;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	FloorState state;
	ArrayList<FloorState> stateHistory;

	public Floor(Scheduler s) {
		scheduler = s;
		
		state = FloorState.START;		
		stateHistory = new ArrayList<FloorState>();
		
		//add initial state
		stateHistory.add(state);
		
		//pendingRequests = new ArrayList<>();
		pendingEventRequests = new ArrayList<>();
		
		// read floor requests from file
		floorEventRequests = new PriorityQueue<>();
		
		try {
			Scanner fileInput = new Scanner(new File(FILEPATH));
			
			while (fileInput.hasNextLine()) {
				String line = fileInput.nextLine();
				Event event = new Event(line);
				
				floorEventRequests.add(event);
				//floorRequests.add(event.toString());
			}
			
			fileInput.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND");
			System.exit(1); //Terminate JVM
		}
	}

	public Event makeFloorRequest() {
		if (!floorEventRequests.isEmpty()) {
			Event fr = floorEventRequests.poll();
			
			pendingEventRequests.add(fr);
			
			changeState(Transition.PRESS_BUTTON);
			
			return fr;
		}
		return null;
	}

	public Event getFloorVisit() {
		Event elevatorVisit = scheduler.sendToFloor();
		
		pendingEventRequests.remove(elevatorVisit);
		
		changeState(Transition.LEAVE);
		changeState(Transition.RESET);
		
		return elevatorVisit;
	}
	
	public void changeState(Transition t) {
		state = state.next(t);
		stateHistory.add(state);
	}

	@Override
	public void run() {
		while (true) {
			// read requests(arrow button from file)
			Event request = makeFloorRequest();
			
			// send request to scheduler
			if (request != null) {
				scheduler.receiveFromFloor(request);
				
				changeState(Transition.ENTER);
				changeState(Transition.WAIT);
				
				LocalDateTime now = LocalDateTime.now();
				System.out.println("@" + dtf.format(now) + " " + Thread.currentThread().getName() + " requests: \t" + request);
			}
			
			// receive confirmation from scheduler
			//if (scheduler.getPendingV() != null) { // if there is a visited record available
				Event visited = getFloorVisit();
				
				LocalDateTime now = LocalDateTime.now();
				System.out.println("@" + dtf.format(now) + " " + Thread.currentThread().getName() + " received: \t" + visited);
				
				System.out.println("FLOOR STATE: " + stateHistory);
			//}
			
			
		}
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public PriorityQueue<Event> getFloorEventRequests() {
		return floorEventRequests;
	}

	public void setFloorEventRequests(PriorityQueue<Event> floorEventRequests) {
		this.floorEventRequests = floorEventRequests;
	}

	public ArrayList<Event> getPendingEventRequests() {
		return pendingEventRequests;
	}

	public void setPendingEventRequests(ArrayList<Event> pendingEventRequests) {
		this.pendingEventRequests = pendingEventRequests;
	}
}

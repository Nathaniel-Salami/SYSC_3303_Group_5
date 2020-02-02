import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

import Storage.Event;

public class Floor implements Runnable {
	
	private Scheduler scheduler;
	
	public static final String FILEPATH = "Elevator_System/floor-commands.txt";

	private PriorityQueue<Event> floorEventRequests;
	private ArrayList<Event> pendingEventRequests;

	public Floor(Scheduler s) {
		scheduler = s;
		
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
			
			return fr;
		}
		return null;
	}

	public Event getFloorVisit() {
		Event elevatorVisit = scheduler.sendToFloor();
		
		pendingEventRequests.remove(elevatorVisit);
		
		return elevatorVisit;
	}

	@Override
	public void run() {
		while (true) {
			// read requests(arrow button from file)
			Event request = makeFloorRequest();
			
			// send request to scheduler
			if (request != null) {
				scheduler.receiveFromFloor(request);
				System.out.println(Thread.currentThread().getName() + " requests: \t" + request);
			}
			
			// receive confirmation from scheduler
			if (scheduler.getPendingV() != null) { // if there is a visited record available
				Event visited = getFloorVisit();
				
				System.out.println(Thread.currentThread().getName() + " received: \t" + visited);
			}
			
			
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

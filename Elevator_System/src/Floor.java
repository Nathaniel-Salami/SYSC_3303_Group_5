import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

import Storage.Event;

public class Floor implements Runnable {
	
	private Scheduler scheduler;
	
	public static final String FILENAME = "floor-commands.txt";
	
	//private PriorityQueue<String> floorRequests;
	//private ArrayList<String> pendingRequests;
	private PriorityQueue<Event> floorEventRequests;
	private ArrayList<Event> pendingEventRequests;

	public Floor(Scheduler s) {
		scheduler = s;
		
		//pendingRequests = new ArrayList<>();
		pendingEventRequests = new ArrayList<>();
		
		// read floor requests from file
		//floorRequests = new PriorityQueue<>();
		floorEventRequests = new PriorityQueue<>();
		
		try {
			Scanner fileInput = new Scanner(new File(FILENAME));
			
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
			// floorRequests.push("15:00:15:0 9 Down 1");
			// floorRequests.push("14:55:15:0 3 Up 7");
			// floorRequests.push("14:45:15:0 7 Down 4");
			// floorRequests.push("14:30:10:0 5 Down 1");
			// floorRequests.push("14:10:00:0 1 Up 2");
			// floorRequests.push("14:05:15:0 2 Up 4");
		}
	}

	public Event makeFloorRequest() {
		if (!floorEventRequests.isEmpty()) {
			Event fr = floorEventRequests.poll();
			
			pendingEventRequests.add(fr);
			//pendingRequests.add(fr.toString());
			
			return fr;
		}
		return null;
	}

	public Event getFloorVisit() {
		Event elevatorVisit = scheduler.sendToFloor();
		
		pendingEventRequests.remove(elevatorVisit);
		//pendingRequests.remove(elevatorVisit.toString());
		
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

/*	public PriorityQueue<String> getFloorRequests() {
		return floorRequests;
	}

	public void setFloorRequests(PriorityQueue<String> floorRequests) {
		this.floorRequests = floorRequests;
	}

	public ArrayList<String> getPendingRequests() {
		return null;
		//return pendingRequests;
	}

	public void setPendingRequests(ArrayList<String> pendingRequests) {
		//this.pendingRequests = pendingRequests;
	}
*/
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

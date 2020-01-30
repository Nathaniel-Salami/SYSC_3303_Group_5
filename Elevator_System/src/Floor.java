import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class Floor implements Runnable {
	private Scheduler scheduler;
	private PriorityQueue<String> floorRequests;
	private ArrayList<String> pendingRequests;

	public Floor(Scheduler s) {
		scheduler = s;
		pendingRequests = new ArrayList<>();

		// read floor requests from file
		floorRequests = new PriorityQueue<>();
		
		try {
			Scanner fileInput = new Scanner(new File("floor-commands.txt"));
			
			while (fileInput.hasNextLine()) {
				floorRequests.add(fileInput.nextLine());
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

	public String makeFloorRequest() {
		if (!floorRequests.isEmpty()) {
			String fr = floorRequests.poll();
			pendingRequests.add(fr);
			return fr;
		}
		return null;
	}

	public String getFloorVisit() {
		String elevatorVisit = scheduler.sendToFloor();
		pendingRequests.remove(elevatorVisit);
		
		return elevatorVisit;
	}

	@Override
	public void run() {
		while (true) {
			// read requests(arrow button from file)
			String request = makeFloorRequest();
			
			// send request to scheduler
			if (request != null) {
				scheduler.receiveFromFloor(request);
				System.out.println(Thread.currentThread().getName() + " requests: " + request);
			}
			
			// receive confirmation from scheduler
			if (!scheduler.getPendingV().isEmpty()) { // if there is a visited record available
				String visited = getFloorVisit();
				
				System.out.println(Thread.currentThread().getName() + " received: " + visited);
			}
			
			
		}
	}

	public PriorityQueue<String> getFloorRequests() {
		return floorRequests;
	}

	public void setFloorRequests(PriorityQueue<String> floorRequests) {
		this.floorRequests = floorRequests;
	}

	public ArrayList<String> getPendingRequests() {
		return pendingRequests;
	}

	public void setPendingRequests(ArrayList<String> pendingRequests) {
		this.pendingRequests = pendingRequests;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
}

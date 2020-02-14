import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Floor implements Runnable {
	
	private Scheduler scheduler;
	
	public static final String FILEPATH = "Elevator_System/floor-commands.txt";

	private PriorityQueue<Event> floorEventRequests;
	private ArrayList<Event> pendingEventRequests;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public Floor(Scheduler s) {
		scheduler = s;
		
		pendingEventRequests = new ArrayList<>();
		
		// read floor requests from file
		floorEventRequests = new PriorityQueue<>();
		
		try {
			Scanner fileInput = new Scanner(new File(FILEPATH));
			
			while (fileInput.hasNextLine()) {
				String line = fileInput.nextLine();
				Event event = new Event(line);
				
				floorEventRequests.add(event);
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
	
	public void log() {
		// not sure why but this "un-freezes" the thread
		try {
			//if (Thread.currentThread().getName() == "FLoor")
				Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("FLOOR = "+floorEventRequests.size() + " : " + pendingEventRequests.size());
	}

	@Override
	public void run() {	
		while (true) {
			log();
			
			// read requests(arrow button from file)
			Event request = this.makeFloorRequest();
			
			// send request to scheduler
			//if ((request != null) && (scheduler.getPendingR() == null)) {
			if (request != null) {				
				scheduler.receiveFromFloor(request);
				
				LocalDateTime now = LocalDateTime.now();
				System.out.println("@" + dtf.format(now) + ": " + Thread.currentThread().getName() + " requests: \t\t" + request);
			}
			
			// receive confirmation from scheduler
			//if (scheduler.getPendingV() != null) { // if there is a visited record available			
				
				Event visited = this.getFloorVisit();
				
				LocalDateTime now = LocalDateTime.now();
				System.out.println("@" + dtf.format(now) + ": " + Thread.currentThread().getName() + " received: \t\t" + visited);
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

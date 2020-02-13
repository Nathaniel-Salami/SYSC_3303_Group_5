import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.PriorityQueue;

import Storage.Event;

public class Elevator implements Runnable {

	//private Scheduler scheduler;
	private PriorityQueue<Event> pendingTasks;
	private ArrayList<Event> completedTasks;
	//private Event pendingTask;
	private ElevatorState state;
	private int currentFloor;

	public Elevator() {
		//scheduler = s;
		pendingTasks = new PriorityQueue<>();
		completedTasks = new ArrayList<Event>();
		currentFloor = 0;
		
		state = ElevatorState.DOORS_OPEN;
	}

	public Event goToNextFloor() {
		Event visited = pendingTasks.poll();
		completedTasks.add(visited);
		
		currentFloor = visited.getDestination();
		
		//ToolBox.sleep();
				
		return visited;
	}
	
	public void recieveFLoorRequest(Event request) {
		// stops elevator from doing more than 1 task, this can be changed later
//		while (pendingTasks.size() > 0) {
//		 	try {
//		 		wait();
//		 	} catch (InterruptedException e) {
//		 		
//		 	}
//	 	}
		
		//pendingTask = request;
		pendingTasks.add(request);
		
	}
	
	public Event reportCompletedTask() {
		Event completed = null;
		if (!completedTasks.isEmpty()) {
			completed = completedTasks.remove(completedTasks.size()-1);
		}
		return completed;
	}
	
	@Override
	public void run() {		
		while (true) {
			// perform any available tasks
			if (pendingTasks.size() > 0) {
				Event visited = goToNextFloor();
				
				System.out.println(ToolBox.getNow() + ": " + Thread.currentThread().getName() + " visited:\t" + visited);
			}
		}
	}

	public PriorityQueue<Event> getPendingTasks() {
		return pendingTasks;
	}

	public void setPendingTasks(PriorityQueue<Event> pendingTasks) {
		this.pendingTasks = pendingTasks;
	}

	public ElevatorState getState() {
		return state;
	}

	public void setState(ElevatorState state) {
		this.state = state;
	}
}

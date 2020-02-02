import java.util.PriorityQueue;

import Storage.Event;

public class Elevator implements Runnable {

	private Scheduler scheduler;
	private PriorityQueue<Event> tasksEvent;

	public Elevator(Scheduler s) {
		scheduler = s;
		tasksEvent = new PriorityQueue<>();
	}

	public Event goToNextFloor() {
		return tasksEvent.poll();
	}
	
	public Event recieveFLoorRequest() {
		Event request = scheduler.sendToElevator();
		tasksEvent.add(request);
		
		return request;
	}
	
	public Event reportFloorVisited() {
		Event visited = goToNextFloor();
		scheduler.receiveFromElevator(visited);
		
		return visited;
	}
	
	@Override
	public void run() {
		while (true) {
			// receive floor request from scheduler 
			if (scheduler.getPendingR() != null) {
				Event request = recieveFLoorRequest();
				System.out.println(Thread.currentThread().getName() + " received:\t" + request);
			}
			
			// report back to scheduler
			if (!tasksEvent.isEmpty()) {
				Event visited = reportFloorVisited();
				System.out.println(Thread.currentThread().getName() + " visited:\t" + visited);
			}
		}
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public PriorityQueue<Event> getTasksEvent() {
		return tasksEvent;
	}

	public void setTasksEvent(PriorityQueue<Event> tasksEvent) {
		this.tasksEvent = tasksEvent;
	}
}

import java.util.PriorityQueue;

public class Elevator implements Runnable {
	//private int currentFloor;
	//private boolean goingUp;

	private Scheduler scheduler;
	private PriorityQueue<String> tasks;

	public Elevator(Scheduler s) {
		scheduler = s;
		//currentFloor = 1;
		//goingUp = true;

		tasks = new PriorityQueue<>();
	}

	public String goToNextFloor() {
		return tasks.poll();
	}
	
	public String recieveFLoorRequest() {
		String request = scheduler.sendToElevator();
		tasks.add(request);
		
		return request;
	}
	
	public String reportFloorVisited() {
		String visited = goToNextFloor();
		scheduler.receiveFromElevator(visited);
		
		return visited;
	}
	
	@Override
	public void run() {
		while (true) {
			// receive floor request from scheduler 
			if (!scheduler.getPendingR().isEmpty()) {
				String request = recieveFLoorRequest();
				System.out.println(Thread.currentThread().getName() + " received: " + request);
			}
			
			// report back to scheduler
			if (!tasks.isEmpty()) {
				String visited = reportFloorVisited();
				System.out.println(Thread.currentThread().getName() + " visited: " + visited);
			}
		}
	}
	
	public PriorityQueue<String> getTasks() {
		return tasks;
	}

	public void setTasks(PriorityQueue<String> tasks) {
		this.tasks = tasks;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
}

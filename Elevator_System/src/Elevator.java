import java.util.PriorityQueue;

public class Elevator implements Runnable {
	Scheduler scheduler;
	int currentFloor;
	boolean goingUp;

	PriorityQueue<String> tasks;

	public Elevator(Scheduler s) {
		scheduler = s;
		currentFloor = 1;
		goingUp = true;

		tasks = new PriorityQueue<>();
	}

	public String goToNextFloor() {
		scheduler.floorsVisited += 1;
		return tasks.poll();
	}

	//can just send back floor(number) elevator is currently on
	
	@Override
	public void run() {
		while (scheduler.floorsVisited != scheduler.limit) {
			// receive floor request from scheduler 
			if (!scheduler.getPendingR().isEmpty()) {
				String fr = scheduler.sendToElevator();
				tasks.add(fr);

				System.out.println(Thread.currentThread().getName() + " received: " + fr);
			}
			// move to floor
			// report back to scheduler
			if (!tasks.isEmpty()) {
				String visited = goToNextFloor();
				scheduler.receiveFromElevator(visited);
	
				System.out.println(Thread.currentThread().getName() + " visited: " + visited);
			}
		}
	}
	
}

import java.util.PriorityQueue;

public class Elevator implements Runnable {
	//private int currentFloor;
	//private boolean goingUp;

	private Scheduler scheduler;
	PriorityQueue<String> tasks;

	public Elevator(Scheduler s) {
		scheduler = s;
		//currentFloor = 1;
		//goingUp = true;

		tasks = new PriorityQueue<>();
	}

	private String goToNextFloor() {
		return tasks.poll();
	}

	//can just send back floor(number) elevator is currently on
	
	@Override
	public void run() {
		while (true) {
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

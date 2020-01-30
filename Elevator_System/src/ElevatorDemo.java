
public class ElevatorDemo {
	public static void main (String[] args) {

		Scheduler scheduler = new Scheduler();

		Thread schedulerThread, floor, elevator;

		floor = new Thread(new Floor(scheduler), "Floor");
		elevator = new Thread(new Elevator(scheduler), "Elevator");
		
		schedulerThread = new Thread(scheduler, "Scheduler");

		schedulerThread.start();
		elevator.start();
		floor.start();
		
	}
}

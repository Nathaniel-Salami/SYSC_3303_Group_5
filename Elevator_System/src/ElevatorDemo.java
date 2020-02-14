
public class ElevatorDemo {
	public static void main (String[] args) {

		
		Elevator elevator = new Elevator();
		Scheduler scheduler = new Scheduler(elevator);
		Floor floor = new Floor(scheduler);

		Thread schedulerThread, floorThread, elevatorThread;

		elevatorThread = new Thread(elevator, "Elevator");
		schedulerThread = new Thread(scheduler, "Scheduler");
		floorThread = new Thread(floor, "Floor");
		
		elevatorThread.start();
		schedulerThread.start();
		floorThread.start();
		
	}
}

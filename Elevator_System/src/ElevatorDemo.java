import java.util.ArrayList;

public class ElevatorDemo {
	public static void main (String[] args) {
 
		Elevator elevator1 = new Elevator();
		Scheduler scheduler = new Scheduler(elevator1);
		Floor floor = new Floor(scheduler);

		Thread schedulerThread, floorThread, elevatorThread;

		floorThread = new Thread(floor, "Floor");
		elevatorThread = new Thread(elevator1, "Elevator");
		schedulerThread = new Thread(scheduler, "Scheduler");
		
		elevatorThread.start();
		schedulerThread.start();
		floorThread.start();	
	}
}

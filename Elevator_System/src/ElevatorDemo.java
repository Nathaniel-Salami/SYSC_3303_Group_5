import java.util.ArrayList;

public class ElevatorDemo {
	public static void main (String[] args) {
 
		Elevator elevator1 = new Elevator();
		
		Scheduler scheduler = new Scheduler(elevator1);
		//Floor floor = new Floor(scheduler);

		Thread schedulerThread, floor, elevator;

		floor = new Thread(new Floor(scheduler), "Floor");
		
		schedulerThread = new Thread(scheduler, "Scheduler");
		
		elevator = new Thread(elevator1, "Elevator");

		
		elevator.start();
		
		schedulerThread.start();
		
		floor.start();	
	}
}

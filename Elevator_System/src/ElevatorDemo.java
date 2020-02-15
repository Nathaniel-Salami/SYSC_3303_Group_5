import java.util.ArrayList;

public class ElevatorDemo {
	public static void main (String[] args) {
 
		Elevator elevator1 = new Elevator();
		Scheduler scheduler = new Scheduler(elevator1);
		Floor floor = new Floor(scheduler);

		Thread schedulerThread, floorThread, elevatorThread;

		//the floor subsystem reads in events (time, floor, elevator number, and button)
		//each line of input from the Storage data structure is sent to the Scheduler
		floorThread = new Thread(floor, "Floor");
		
		//the elevator makes calls to the Scheduler which replies when there is work to be done
		//the elevator then sends data back to the Scheduler who then sends it back to the Floor
		elevatorThread = new Thread(elevator1, "Elevator");
		
		//the Scheduler is only being used as a communication channel from the Floor thread to the Elevator thread and back again
		schedulerThread = new Thread(scheduler, "Scheduler");
		
		elevatorThread.start();
		schedulerThread.start();
		floorThread.start();	
	}
}

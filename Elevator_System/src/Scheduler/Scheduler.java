package Scheduler;
/**
 * {@summary The Scheduler is only being used as a communication channel from
 * the Floor thread to the Elevator thread and back again.}
 */


public class Scheduler {

	public static void main(String[] args) {
	
		RestructuredSchedulder scheduler = new RestructuredSchedulder();
		Thread t1 = new Thread(new RegistrationThread(scheduler),"Registration Thread");
		t1.start();
		Thread t2 = new Thread(new RequestThread(scheduler),"Request Thread");
		t2.start();
		Thread t3 = new Thread(new SchedulingThread(scheduler), "Scheduling Thread");
		t3.start();
		Thread t4 = new Thread(new ElevatorContolThread(scheduler), "Elevator Control Thread");
		t4.start();
		Thread t5 = new Thread(new ArrivalThread(scheduler), "Floor Response Thread");
		t5.start();

		
	}

}

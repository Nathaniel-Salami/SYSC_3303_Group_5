import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Scheduler implements Runnable {

	private Event pendingR; // Request received from floor
	private Event pendingV; // Visit confirmation received from elevator
	
	private Elevator elevator;
	
	private final static int TIME = 300;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public Scheduler(Elevator elevator) {
		pendingR = null;
		pendingV = null;
		
		this.elevator = elevator;
	}

	// receive from Floor to Elevator
	public synchronized void receiveFromFloor(Event fr) {
		while (pendingR != null) {
			try {
				if (Thread.currentThread().getName() == "Floor")
					wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		
		try {
			//System.out.println("Floor sleeps1");
			if (Thread.currentThread().getName() == "Floor")
				Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pendingR = fr;
	}

	// send to Floor from Elevator
	public synchronized Event sendToFloor() {
		while (pendingV == null) {
			try {
				if (Thread.currentThread().getName() == "Floor") 
					wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		
		try {
			//System.out.println("Floor sleeps2");
			if (Thread.currentThread().getName() == "Floor") 
				Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Event temp = pendingV;
		pendingV = null;
	
		return temp;
	}
		
	public synchronized Event sendToElevator() {
		Event temp = pendingR;
		elevator.recieveFLoorRequest(pendingR);
		pendingR = null;
		
		this.notifyAll();
		
		return temp;
	}
	
	public synchronized Event getNextCompletedTask() {		
		Event completed = elevator.reportCompletedTask();
		
		this.notifyAll();
		
		return completed;
	}
	
	public void log() {
		// not sure why but this "un-freezes" the thread
		try {
			//if (Thread.currentThread().getName() == "Scheduler")
				Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("SHEDULER = "+pendingR + " : " + pendingV);
	}

	@Override
	public void run() {
		
		while (true) {
			log();
			
			// send a floor request to an elevator
			if (pendingR != null) {
				Event sent = this.sendToElevator();
				
				LocalDateTime now = LocalDateTime.now();
				System.out.println("@" + dtf.format(now) + ": " + Thread.currentThread().getName() + " sent:\t\t" + sent);
			}
			
			// get visited report from elevator
			if (elevator.completedTask != null) {
				pendingV = getNextCompletedTask();
				
				LocalDateTime now = LocalDateTime.now();
				System.out.println("@" + dtf.format(now) + ": " + Thread.currentThread().getName() + " received:\t" + pendingV);
			}
		}
	}

	public Event getPendingR() {
		return pendingR;
	}

	public void setPendingR(Event pendingR) {
		this.pendingR = pendingR;
	}

	public Event getPendingV() {
		return pendingV;
	}

	public void setPendingV(Event pendingV) {
		this.pendingV = pendingV;
	}
	
}

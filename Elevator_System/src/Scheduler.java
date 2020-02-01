import Storage.Event;

public class Scheduler implements Runnable {

	private final int TIME = 300;

	private Event pendingR; // Request received from floor
	private Event pendingV; // Visit confirmation received from elevator

	public Scheduler() {
		pendingR = null;
		pendingV = null;
	}

	public void sleep(int t) {
		try {
			Thread.sleep(t);
		} 
		catch (InterruptedException e) {}
	}

	// receive from Floor to Elevator
	public synchronized void receiveFromFloor(Event fr) {
		
		while (pendingR != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}

		pendingR = fr;

		sleep(TIME);
	}

	// send to Elevator from Floor
	public synchronized Event sendToElevator() {
		
		while (pendingR == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}

		Event out = pendingR;
		pendingR = null;

		sleep(TIME);

		return out;
	}

	// receive from Elevator to Floor
	public synchronized void receiveFromElevator(Event fv) {
		
		while (pendingV != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		
		//pendingR = "";
		pendingV = fv;

		sleep(TIME);
	}

	// send to Floor from Elevator
	public synchronized Event sendToFloor() {
		
		 while (pendingV == null) {
		 	try {
		 		wait();
		 	} catch (InterruptedException e) {
		 		return null;
		 	}
		 }

		Event out = pendingV;
		pendingV = null;

		sleep(TIME);
		
		return out;
	}

	@Override
	public void run() {

		while (true) {
			// try {
			// 	Thread.sleep(1000);
			// } catch (InterruptedException e) {}
			// System.out.println("R: " + pendingR + ", V: " + pendingV);
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

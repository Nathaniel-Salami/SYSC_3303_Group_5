public class Scheduler implements Runnable {

	// for testing
	int floorsVisited;
	int limit;

	final int TIME = 300;

	private String pendingR; // Request received from floor
	private String pendingV; // Visit confirmation received from elevator

	public Scheduler(int l) {
		limit = l;
		pendingR = "";
		pendingV = "";
	}

	private void sleep(int t) {
		try {
			Thread.sleep(t);
		} 
		catch (InterruptedException e) {}
	}

	// receive from Floor to Elevator
	public synchronized void receiveFromFloor(String fr) {
		while (!pendingR.isEmpty()) {
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
	public synchronized String sendToElevator() {
		while (pendingR.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				return "";
			}
		}

		String out = pendingR;
		pendingR = "";

		sleep(TIME);

		return out;
	}

	// receive from Elevator to Floor
	public synchronized void receiveFromElevator(String fv) {
		while (!pendingV.isEmpty()) {
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
	public synchronized String sendToFloor() {
		// while (pendingV.isEmpty()) {
		// 	try {
		// 		wait();
		// 	} catch (InterruptedException e) {
		// 		return "";
		// 	}
		// }

		String out = pendingV;
		pendingV = "";

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

	public String getPendingR() {
		return pendingR;
	}

	public void setPendingR(String pendingR) {
		this.pendingR = pendingR;
	}

	public String getPendingV() {
		return pendingV;
	}

	public void setPendingV(String pendingV) {
		this.pendingV = pendingV;
	}
	
}

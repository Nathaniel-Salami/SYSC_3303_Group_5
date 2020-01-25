import java.util.ArrayList;
import java.util.Stack;

public class Floor implements Runnable {
	Scheduler scheduler;
	Stack<String> floorRequests;
	ArrayList<String> pendingRequests;

	public Floor(Scheduler s) {
		scheduler = s;
		pendingRequests = new ArrayList<>();

		// read floor requests from file
		floorRequests = new Stack<>();

		floorRequests.push("15:00:15:0 9 Down 1");
		floorRequests.push("14:55:15:0 3 Up 7");
		// floorRequests.push("14:45:15:0 7 Down 4");
		// floorRequests.push("14:30:10:0 5 Down 1");
		// floorRequests.push("14:10:00:0 1 Up 2");
		// floorRequests.push("14:05:15:0 2 Up 4");
	}

	private String makeFloorRequest() {
		if (!floorRequests.empty()) {
			String fr = floorRequests.pop();
			pendingRequests.add(fr);
			return fr;
		}
		return null;
	}

	private boolean getFloorVisit(String fv) {
		return pendingRequests.remove(fv);
	}

	@Override
	public void run() {
		while (scheduler.floorsVisited != scheduler.limit) {
			// read requests(arrow button from file)
			String fr = makeFloorRequest();
			
			// send request to scheduler
			if (fr != null) {
				scheduler.receiveFromFloor(fr);

				System.out.println(Thread.currentThread().getName() + " requests: " + fr);
			}
			
			// receive confirmation form scheduler
			if (!scheduler.getPendingV().isEmpty()) { // if there is a visited record available
				String ev = scheduler.sendToFloor();

				this.getFloorVisit(ev);
				System.out.println(Thread.currentThread().getName() + " received: " + ev);
			}
			
			
		}
	}
}

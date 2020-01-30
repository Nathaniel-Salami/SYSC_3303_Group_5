import static org.junit.jupiter.api.Assertions.*;

import java.util.PriorityQueue;
import java.util.Stack;

import org.junit.jupiter.api.Test;

class FloorTest {

	@Test
	void test() {
		PriorityQueue<String> floorRequests = new PriorityQueue<String>();
		
		//test request
		floorRequests.add("15:00:15:0 9 Down 1");

		Scheduler scheduler = new Scheduler();
		
		Floor floor = new Floor(scheduler);
		
		// read floor request from file 
		floor.setFloorRequests(floorRequests);
		
		// send floor request to elevator
		String fr = floor.makeFloorRequest();
		scheduler.receiveFromFloor(fr);
		
		//no more floor requests
		assertTrue(floor.getFloorRequests().isEmpty());
		
		//floor request is now pending
		assertFalse(floor.getPendingRequests().isEmpty());
		
		// receive floor visited from elevator
		String visited = scheduler.sendToElevator();
		
		//simulate elevator 
		scheduler.setPendingV(visited);
		
		floor.getFloorVisit();
		
		assertTrue(floor.getPendingRequests().isEmpty());
	}

}

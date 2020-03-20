package tests;
import static org.junit.jupiter.api.Assertions.*;

import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

class FloorTest {

	@Test
	void test() {
		PriorityQueue<Event> floorRequests = new PriorityQueue<Event>();
		
		//test request
		floorRequests.add(new Event("15:00:15:0 9 Down 1"));
		Event event = floorRequests.peek();
		
		Elevator elevator = new Elevator();
		Scheduler scheduler = new Scheduler(elevator);
		Floor floor = new Floor(scheduler);
		
		// read floor request from file 
		floor.setFloorEventRequests(floorRequests);
		
		// send floor request to elevator
		Event fr = floor.makeFloorRequest();
		scheduler.receiveFromFloor(fr);
		
		//no more floor requests
		assertTrue(floor.getFloorEventRequests().isEmpty());
		
		//floor request is now pending
		assertFalse(floor.getPendingEventRequests().isEmpty());
		
		// receive floor visited from elevator
		Event visited = scheduler.sendToElevator();
		
		//simulate elevator 
		scheduler.setPendingV(visited);
		
		Event visitRequest = floor.getFloorVisit();
		
		assertEquals(event,visitRequest);
		assertTrue(floor.getPendingEventRequests().isEmpty());
	}

}

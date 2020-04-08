package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import Floor.Floor;
import Utility.Event;

class FloorCreateRequestTest {

	@Test
	void test() {
		PriorityQueue<Event> floorRequests = new PriorityQueue<Event>();
		
		//test request		
		floorRequests.add(new Event("15:00:15:0 9 Down 1"));
		Event event = floorRequests.peek();
		Floor floor = new Floor();
		floor.setFloorEventReq(floorRequests);
		
		assertTrue(event == floor.makeFloorRequest());
		
	}
}

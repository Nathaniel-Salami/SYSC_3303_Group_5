package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import Floor.Floor;
import Scheduler.RequestThread;
import Scheduler.RestructuredSchedulder;
import Utility.Event;

class FloorSendRequestTest {

	@Test
	void test() {
		PriorityQueue<Event> floorRequests = new PriorityQueue<Event>();
		
		//test request		
		floorRequests.add(new Event("15:00:15:0 9 Down 8"));
		Event event = floorRequests.peek();
		
		Floor floor = new Floor();
		floor.setFloorEventReq(floorRequests);
		
		RestructuredSchedulder scheduler = new RestructuredSchedulder(0,22);
		RequestThread thread = new RequestThread(scheduler);
		Thread t = new Thread(thread,"temp");
		t.start();
		
		floor.sendAndReceive();
		Event e = scheduler.getRequests().getPrendingRequest(0);
		
		assertTrue(event.equals(e));
	}

}

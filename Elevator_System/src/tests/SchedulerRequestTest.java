package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import Floor.Floor;
import Scheduler.RequestThread;
import Scheduler.RestructuredSchedulder;
import Utility.Event;

class SchedulerRequestTest {

	@Test
	void test() {
		RestructuredSchedulder scheduler = new RestructuredSchedulder(0,22);
		Thread t1 = new Thread(new RequestThread(scheduler),"Request Thread");
		t1.start();
		
		//Floor representation
		PriorityQueue<Event> floorRequests = new PriorityQueue<Event>();
		floorRequests.add(new Event("15:00:15:0 9 Down 1"));
		Event event = floorRequests.peek();
		Floor floor = new Floor();
		floor.setFloorEventReq(floorRequests);
		floor.sendAndReceive();

		
		//Sleep this thread for a bit, so the elevator can register with the Scheduler
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		assertTrue(scheduler.getRequests().getPrendingRequest(0).equals(event));
	}

}

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Storage.Event;

class SchedulerTest {

	@Test
	void test() {
		String request = "15:00:15:0 9 Down 1";
		Event event = new Event(request);
		
		Scheduler scheduler = new Scheduler();
		
		//receiving a request from floor
		assertTrue(scheduler.getPendingR() == null);
		
		scheduler.receiveFromFloor(event);
		
		assertFalse(scheduler.getPendingR() == null);
		
		//sending a request to elevator
		Event visited = scheduler.sendToElevator();
		
		assertTrue(scheduler.getPendingR() == null);
		
		//receive visited report from elevator
		assertTrue(scheduler.getPendingV() == null);
		
		scheduler.receiveFromElevator(visited);
		
		assertFalse(scheduler.getPendingV() == null);
		
		//send visited report to floor
		scheduler.sendToFloor();
		
		assertTrue(scheduler.getPendingV() == null);
	}

}

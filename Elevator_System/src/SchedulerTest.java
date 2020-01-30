import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SchedulerTest {

	@Test
	void test() {
		String request = "15:00:15:0 9 Down 1";
		
		Scheduler scheduler = new Scheduler();
		
		//receiving a request from floor
		assertTrue(scheduler.getPendingR().isEmpty());
		
		scheduler.receiveFromFloor(request);
		
		assertFalse(scheduler.getPendingR().isEmpty());
		
		//sending a request to elevator
		String visited = scheduler.sendToElevator();
		
		assertTrue(scheduler.getPendingR().isEmpty());
		
		//receive visited report from elevator
		assertTrue(scheduler.getPendingV().isEmpty());
		
		scheduler.receiveFromElevator(visited);
		
		assertFalse(scheduler.getPendingV().isEmpty());
		
		//send visited report to floor
		scheduler.sendToFloor();
		
		assertTrue(scheduler.getPendingV().isEmpty());
	}

}

package tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import Scheduler.Scheduler;

class SchedulerTest {

	@Test
	void test() {
		
		String request = "15:00:15:0 9 Down 1";
		Event event = new Event(request);

		Elevator elevator = new Elevator();
		Scheduler scheduler = new Scheduler(elevator);
		
		//receiving a request from floor
		assertTrue(scheduler.getPendingR() == null);
		
		scheduler.receiveFromFloor(event);
		
		assertFalse(scheduler.getPendingR() == null);

		//sending a request to elevator
		scheduler.sendToElevator();
		
		assertTrue(scheduler.getPendingR() == null);
		
		elevator.goToNextFloor();//Move the Elevator up/down
		
		//receive visited report from elevator and add it to pendingV of the scheduler
		//Used to simulate the runnable interface method
		scheduler.setPendingV(scheduler.getNextCompletedTask());
		
		assertFalse(scheduler.getPendingV() == null);
		
		//send visited report to floor
		Event response = scheduler.sendToFloor();
		
		assertTrue(scheduler.getPendingV() == null);
		assertEquals(event, response);
	}

}

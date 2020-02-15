import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 
 */

/**
 * @author nathanielsalami
 *
 */
class ElevatorTest {

	@Test
	void test() {
		Elevator elevator = new Elevator();
		Scheduler scheduler = new Scheduler(elevator);
		
		
		//create test request
		String request = "15:00:15:0 9 Down 1";
		Event event = new Event(request);
		
		// add test request to scheduler
		scheduler.setPendingR(event);
		
		// elevator starts with no tasks
		assertTrue(elevator.getPendingTask() == null);
		
		//retrieve task/request from scheduler
		Event recieved = scheduler.sendToElevator();
		assertEquals(event, recieved);
		
		//elevator should have a new task
		assertFalse(elevator.getPendingTask() == null);
		
		//elevator goes to the floor 
		Event visited = elevator.goToNextFloor();		
		assertEquals(event, visited);
		
		//send completed task back to scheduler
		Event completedRequest = scheduler.getNextCompletedTask();
		
		//Assert that the initial event from the Scheduler has been processed by the Elevator
		assertEquals(event, completedRequest);
		assertEquals(elevator.getCurrentFloor(), event.getDestination());
	}

}

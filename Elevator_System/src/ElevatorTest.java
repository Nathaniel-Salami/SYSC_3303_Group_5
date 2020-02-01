import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Storage.Event;

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
		Scheduler scheduler = new Scheduler();
		
		Elevator elevator = new Elevator(scheduler);
		
		//create test request
		String request = "15:00:15:0 9 Down 1";
		Event event = new Event(request);
		
		// add test request to scheduler
		scheduler.setPendingR(event);
		
		// elevator starts with no tasks
		assertTrue(elevator.getTasksEvent().isEmpty());
		
		//retrieve task/request from scheduler
		Event recieved = scheduler.sendToElevator();
		elevator.getTasksEvent().add(recieved);
		
		//elevator should have a new task
		assertEquals(1, elevator.getTasksEvent().size());
		
		//elevator goes to the floor 
		Event visited = elevator.goToNextFloor();		
		assertEquals(event, visited);
		
		//send completed task back to scheduler
		scheduler.receiveFromElevator(visited);
		
		assertEquals(event, scheduler.getPendingV());
	}

}

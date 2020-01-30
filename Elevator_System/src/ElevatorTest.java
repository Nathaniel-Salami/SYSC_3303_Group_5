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
		Scheduler scheduler = new Scheduler();
		
		Elevator elevator = new Elevator(scheduler);
		
		//create test request
		String request = "15:00:15:0 9 Down 1";
		
		// add test request to scheduler
		scheduler.setPendingR(request);
		
		// elevator starts with no tasks
		assertTrue(elevator.getTasks().isEmpty());
		
		//retrieve task/request from scheduler
		String recieved = scheduler.sendToElevator();
		elevator.getTasks().add(recieved);
		
		//elevator should have a new task
		assertEquals(1, elevator.getTasks().size());
		
		//elevator goes to the floor 
		String visited = elevator.goToNextFloor();
		assertEquals(request, visited);
		
		//send completed task back to scheduler
		scheduler.receiveFromElevator(visited);
		
		assertEquals(request, scheduler.getPendingV());
	}

}

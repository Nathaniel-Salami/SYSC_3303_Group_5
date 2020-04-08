package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import Floor.Floor;
import Scheduler.Scheduler;
import Utility.Event;

class ElevatorTest {

	@Test
	void test() {
		Scheduler.main(new String[0]);
	
		Elevator el1 = new Elevator("Elevator 1", (int) (Math.random()*1000));
		Thread e1 = new Thread(el1);
		e1.start();
		
		PriorityQueue<Event> floorRequests = new PriorityQueue<Event>();
		floorRequests.add(new Event("15:00:15:0 0 Up 4"));
		
		Event event = floorRequests.peek();
		Floor floor = new Floor();
		floor.setFloorEventReq(floorRequests);
		floor.sendAndReceive();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(el1.getFloor() == event.getDestination());
		
	}

}

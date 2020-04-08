package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import Floor.Floor;
import Floor.ReceiverThread;
import Scheduler.Scheduler;
import Utility.Event;

class FloorReceivedRequest {

	@Test
	void test() {		
	PriorityQueue<Event> floorRequests = new PriorityQueue<Event>();
	
	//test request		
	floorRequests.add(new Event("15:00:15:0 0 Up 2"));
	Event event = floorRequests.peek();
	
	Floor floor = new Floor();
	Thread t = new Thread(new ReceiverThread(floor),"Receive Thread");
	t.start();
	floor.setFloorEventReq(floorRequests);
	

	Scheduler.main(new String[0]);
	
	
	Thread el = new Thread(new Elevator("Elevator1",(int) (Math.random()*1000)));
	el.start();
	
	floor.sendAndReceive();
	
	try {
		Thread.sleep(10000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Iterator<Event> i = floor.getCompletedReq().iterator();
	assertTrue(i.next().equals(event));
	}

}

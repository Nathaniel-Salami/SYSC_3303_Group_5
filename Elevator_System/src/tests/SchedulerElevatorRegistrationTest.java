package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import Scheduler.ElevatorRepresentation;
import Scheduler.RegistrationThread;
import Scheduler.RestructuredSchedulder;
import Utility.Event;

class SchedulerElevatorRegistrationTest {

	@Test
	void test() {
		RestructuredSchedulder scheduler = new RestructuredSchedulder(0,22);
		Thread t1 = new Thread(new RegistrationThread(scheduler),"Registration Thread");
		t1.start();
		
		
		Elevator e = new Elevator("Elevator1",(int) (Math.random()*1000));
		Thread el = new Thread(e);
		el.start();
		
		//Sleep this thread for a bit, so the elevator can register with the Scheduler
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		boolean flag = false;
		for(SimpleEntry<ElevatorRepresentation, ArrayList<Event>> entry: scheduler.getElevatorMap()) {
			if((entry.getKey()).getDir() == e.getDir() && ((ElevatorRepresentation) entry.getKey()).getFloor() == e.getFloor()) 
				flag = true;
		}
		
		assertTrue(flag);
	}

}

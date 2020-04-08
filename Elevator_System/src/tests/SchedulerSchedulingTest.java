package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.AbstractMap.SimpleEntry;

import org.junit.jupiter.api.Test;

import Elevator.Elevator;
import Floor.Floor;
import Scheduler.ArrivalThread;
import Scheduler.ElevatorContolThread;
import Scheduler.RegistrationThread;
import Scheduler.RequestThread;
import Scheduler.RestructuredSchedulder;
import Scheduler.SchedulingThread;
import Utility.Event;

class SchedulerSchedulingTest {

	@Test
	void test() {
		
		RestructuredSchedulder scheduler = new RestructuredSchedulder(0,22);
		Thread t1 = new Thread(new RegistrationThread(scheduler),"Registration Thread");
		t1.start();
		Thread t2 = new Thread(new RequestThread(scheduler),"Request Thread");
		t2.start();
		Thread t3 = new Thread(new SchedulingThread(scheduler), "Scheduling Thread");
		t3.start();
		Thread t4 = new Thread(new ElevatorContolThread(scheduler), "Elevator Control Thread");
		t4.start();
		Thread t5 = new Thread(new ArrivalThread(scheduler), "Floor Response Thread");
		t5.start();
		
		//test request		
		PriorityQueue<Event> floorRequests = new PriorityQueue<Event>();
		floorRequests.add(new Event("15:00:15:0 4 Down 3"));
		
		Floor floor = new Floor();
		floor.setFloorEventReq(floorRequests);
		floor.sendAndReceive();
		
		Thread e1 = new Thread(new Elevator("Elevator1",(int) (Math.random()*1000)));
		e1.start();
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Elevator el2 = new Elevator("Elevator2",(int) (Math.random()*1000));
		Thread e2 = new Thread(el2);
		e2.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		floorRequests = new PriorityQueue<Event>();
		floorRequests.add(new Event("15:00:15:0 0 Up 1"));
		floor.setFloorEventReq(floorRequests);
		floor.sendAndReceive();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		assertTrue(!scheduler.getElevatorMap().get(1).getValue().isEmpty() && scheduler.getElevatorMap().get(0).getValue().isEmpty());
		
	}

}

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.PriorityQueue;

import Storage.Event;

public class Elevator implements Runnable {

	private Scheduler scheduler;
	private PriorityQueue<Event> tasksEvent;
	
	ElevatorState state;
	ArrayList<ElevatorState> stateHistory;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public Elevator(Scheduler s) {
		scheduler = s;
		tasksEvent = new PriorityQueue<>();
		
		state = ElevatorState.DOORS_OPEN;
		stateHistory = new ArrayList<ElevatorState>();
		
		//add initial state
		stateHistory.add(state);
	}

	public Event goToNextFloor() {
		//simulate travel with state transitions
		changeState(Transition.THROTTLE_UP);
		changeState(Transition.THROTTLE_BACK);
		changeState(Transition.BRAKE);
		changeState(Transition.STOPPING);
		
		return tasksEvent.poll();
	}
	
	public Event recieveFLoorRequest() {
		Event request = scheduler.sendToElevator();
		tasksEvent.add(request);
		
		changeState(Transition.CLOSE_DOORS);
		
		return request;
	}
	
	public Event reportFloorVisited() {
		Event visited = goToNextFloor();
		scheduler.receiveFromElevator(visited);
		
		changeState(Transition.OPEN_DOORS);
		
		return visited;
	}
	
	public void changeState(Transition t) {
		state = state.next(t);
		stateHistory.add(state);
	}
	
	@Override
	public void run() {
		while (true) {
			// receive floor request from scheduler 
			//if (scheduler.getPendingR() != null) {
				Event request = recieveFLoorRequest();
				
				LocalDateTime now = LocalDateTime.now();
				System.out.println("@" + dtf.format(now) + " " + Thread.currentThread().getName() + " received:\t" + request);
			//}
			
			// report back to scheduler
			if (!tasksEvent.isEmpty()) {
				Event visited = reportFloorVisited();
				
				now = LocalDateTime.now();
				System.out.println("@" + dtf.format(now) + " " + Thread.currentThread().getName() + " visited:\t" + visited);
				
				System.out.println("ELEVATOR STATE: " + stateHistory);
			}
		}
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public PriorityQueue<Event> getTasksEvent() {
		return tasksEvent;
	}

	public void setTasksEvent(PriorityQueue<Event> tasksEvent) {
		this.tasksEvent = tasksEvent;
	}
}

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Elevator implements Runnable {

	//private Scheduler scheduler;
	//private PriorityQueue<Event> pendingTasks;
	Event completedTask;
	Event pendingTask;
	private ElevatorState state;
	private int currentFloor;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public Elevator() {
		//scheduler = s;
		pendingTask = null;
		completedTask = null;
		currentFloor = 0;
		
		state = ElevatorState.DOORS_OPEN;
	}
	
	public synchronized void recieveFLoorRequest(Event request) {
		// stops elevator from doing more than 1 task, this can be changed later
		
		while (pendingTask != null) {
			try {
				if (Thread.currentThread().getName() == "Scheduler") 
					wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		
		try {
			if (Thread.currentThread().getName() == "Scheduler") 
				Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.notifyAll();
		
		state = state.next(Transition.CLOSE_DOORS);
		
		pendingTask = request;
		
	}
	
	public synchronized Event goToNextFloor() {		
		completedTask = pendingTask;
		
		currentFloor = completedTask.getDestination();
		
		pendingTask = null;
		
		this.notifyAll();
		
		//simulate travel with state transitions
		state = state.next(Transition.THROTTLE_UP); //accelerating
		state = state.next(Transition.THROTTLE_BACK); //cruising
		state = state.next(Transition.BRAKE); //decelerating
				
		return completedTask;
	}
	
	public synchronized Event reportCompletedTask() {
		while (completedTask == null) {
			try {
				if (Thread.currentThread().getName() == "Scheduler") 
					wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		
		try {
			if (Thread.currentThread().getName() == "Scheduler") 
				Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.notifyAll();
		
		state = state.next(Transition.OPEN_DOORS);
		
		Event visited = completedTask;
		completedTask = null;
		
		return visited;
	}
	
	public void log() {
		// not sure why but this "un-freezes" the thread
		try {
			//if (Thread.currentThread().getName() == "Elevator")
				Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("ELEVATOR = "+pendingTask + " : " + completedTask);
		System.out.println("ELEVATOR STATE " + state);
	}
	
	@Override
	public void run() {		
		while (true) {
			log();
			
			// perform any available tasks
			if (pendingTask != null) {
				Event visited = this.goToNextFloor();
				
				LocalDateTime now = LocalDateTime.now();
				System.out.println("@" + dtf.format(now) + ": " + Thread.currentThread().getName() + " visited:\t\t" + visited);
			}
		}
	}

	public ElevatorState getState() {
		return state;
	}
 
	public void setState(ElevatorState state) {
		this.state = state;
	}
}

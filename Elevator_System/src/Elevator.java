import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.PriorityQueue;
import java.util.ArrayList;

public class Elevator implements Runnable {

	//private Scheduler scheduler;
	//private PriorityQueue<Event> pendingTasks;
	Event completedTask;
	Event pendingTask;
	private int currentFloor;
	
	private ElevatorState state;
	ArrayList<ElevatorState> stateHistory;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public Elevator() {
		//scheduler = s;
		pendingTask = null;
		completedTask = null;
		currentFloor = 0;
		
		state = ElevatorState.getInitialState();
		stateHistory = new ArrayList<>();
		
		//add initial state
		stateHistory.add(state);
		System.out.println("ELEVATOR STATE: " + state);
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
		
		/*try {
			if (Thread.currentThread().getName() == "Scheduler") 
				Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		
		changeState(Transition.CLOSE_DOORS);
		
		this.notifyAll();
		
		pendingTask = request;
		
	}
	
	public synchronized Event goToNextFloor() {		
		completedTask = pendingTask;
		
		currentFloor = completedTask.getDestination();
		
		pendingTask = null;
		
		//simulate travel with state transitions
		changeState(Transition.THROTTLE_UP);
		changeState(Transition.THROTTLE_BACK);
		changeState(Transition.BRAKE);
		changeState(Transition.STOPPING);
		
		this.notifyAll();
				
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
		
		changeState(Transition.OPEN_DOORS);
		
		Event visited = completedTask;
		completedTask = null;
		
		this.notifyAll();
		
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
		//System.out.println("ELEVATOR HISTOR: " + stateHistory);
		//System.out.println("ELEVATOR STATE: " + state);
	}
	
	public void changeState(Transition t) {
		state = state.next(t);
		stateHistory.add(state);
		
		System.out.println("ELEVATOR STATE: " + state);
		
		/*try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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
	
	public Event getPendingTask() {
		return pendingTask;
	}
	
	
	public Event getCompletedTask() {
		return completedTask;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}
}

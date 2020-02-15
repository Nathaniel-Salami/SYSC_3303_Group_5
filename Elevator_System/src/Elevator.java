import java.time.LocalDateTime; //real time
import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.PriorityQueue;

/**
 * {@summary The elevator subsystem notifies the scheduler that an elevator has
 * reached a floor. Once an elevator has been told to move, the elevator
 * subsystem is informed so that it can send out messages back to the scheduler.}
 */

public class Elevator implements Runnable {

	// private Scheduler scheduler;
	// private PriorityQueue<Event> pendingTasks;
	Event completedTask;
	Event pendingTask; // pending event for the elevator
	private ElevatorState state; // state machine for the elevator subsystem
	private int currentFloor;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public Elevator() {
		// scheduler = s;
		pendingTask = null;
		completedTask = null;
		currentFloor = 0;

		// initial state
		state = ElevatorState.DOORS_OPEN;
	}

	/*
	 * Helper function: Receives events from the scheduler
	 */
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

		this.notifyAll(); // the elevator subsystem will notify the scheduler when an elevator has reached
							// a floor

		state = state.next(Transition.CLOSE_DOORS);

		pendingTask = request;

	}

	/*
	 * Helper function: Simulates travel with state transitions
	 */
	public synchronized Event goToNextFloor() {
		completedTask = pendingTask;

		currentFloor = completedTask.getDestination();

		pendingTask = null;

		this.notifyAll();

		// simulate travel with state transitions
		state = state.next(Transition.THROTTLE_UP); // accelerating
		System.out.println("ELEVATOR STATE " + state);
		state = state.next(Transition.THROTTLE_BACK); // cruising
		System.out.println("ELEVATOR STATE " + state);
		state = state.next(Transition.BRAKE); // decelerating
		System.out.println("ELEVATOR STATE " + state);

		return completedTask;
	}

	/*
	 * Helper function: Notifies the scheduler when an elevator has reached a floor
	 */
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

		this.notifyAll(); // the elevator notifies the scheduler when it has reached a floor

		state = state.next(Transition.OPEN_DOORS);

		Event visited = completedTask;
		completedTask = null;

		return visited;
	}

	/*
	 * Helper function: Logs elevator states to the console
	 */
	public void log() {
		// not sure why but this "un-freezes" the thread
		try {
			// if (Thread.currentThread().getName() == "Elevator")
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("ELEVATOR = "+pendingTask + " : " + completedTask);
		System.out.println("ELEVATOR STATE " + state);
	}

	/*
	 * Run
	 */
	@Override
	public void run() {
		while (true) {
			log();

			// perform any available tasks
			if (pendingTask != null) {
				Event visited = this.goToNextFloor();

				LocalDateTime now = LocalDateTime.now(); // real time
				System.out.println(
						"@" + dtf.format(now) + ": " + Thread.currentThread().getName() + " visited:\t\t" + visited);
			}
		}
	}

	/*
	 * Get & set methods for class attributes
	 */
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

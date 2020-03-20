package Elevator;

import Utility.Transition;

public enum ElevatorState {
	//initial state for the elevator
	DOORS_OPEN {
		@Override
		public ElevatorState next(Transition transition) {
			return DOORS_CLOSED;
		}
	},
	DOORS_CLOSED {
		@Override
		public ElevatorState next(Transition transition) {
			return (transition == Transition.THROTTLE_UP) ? MOVING : DOORS_OPEN;
		}
	},
	MOVING{
		@Override
		public ElevatorState next(Transition transiion) {
			try {
				Thread.sleep(6030);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return STOPPED;
		}
	},
	STOPPED {
		@Override
		public ElevatorState next(Transition transition) {
			return DOORS_OPEN;
		}
	};

	public abstract ElevatorState next(Transition transition);
	public static ElevatorState getInitialState() {
		return DOORS_OPEN;
	}
}

	

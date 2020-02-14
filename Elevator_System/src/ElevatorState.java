  
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
			return (transition == Transition.THROTTLE_UP) ? ACCELERATING : DOORS_OPEN;
		}
	},
	ACCELERATING {
		@Override
		public ElevatorState next(Transition transition) {
				return CRUISING;
		}
	},
	CRUISING {
		@Override
		public ElevatorState next(Transition transition) {
			return DECELERATING;
		}
	},
	DECELERATING {
		@Override
		public ElevatorState next(Transition transition) {
			return STOPPED;
		}
	},
	STOPPED {
		@Override
		public ElevatorState next(Transition transition) {
			return DOORS_OPEN;
		}
	},
	INVALID {
		@Override
		public ElevatorState next(Transition transition) {
			return DOORS_OPEN;
		}
	};

	public abstract ElevatorState next(Transition transition);
}
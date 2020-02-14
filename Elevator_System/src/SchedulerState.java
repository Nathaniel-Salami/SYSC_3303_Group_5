public enum SchedulerState {
	//initial state for the elevator
	DOORS_OPEN {
		@Override
		public SchedulerState next(Transition transition) {
			return DOORS_CLOSED;
		}
	},
	DOORS_CLOSED {
		@Override
		public SchedulerState next(Transition transition) {
			return (transition == Transition.THROTTLE_UP) ? ACCELERATING : DOORS_OPEN;
		}
	},
	ACCELERATING {
		@Override
		public SchedulerState next(Transition transition) {
				return CRUISING;
		}
	},
	CRUISING {
		@Override
		public SchedulerState next(Transition transition) {
			return DECELERATING;
		}
	},
	DECELERATING {
		@Override
		public SchedulerState next(Transition transition) {
			return STOPPED;
		}
	},
	STOPPED {
		@Override
		public SchedulerState next(Transition transition) {
			return DOORS_OPEN;
		}
	};

	public abstract SchedulerState next(Transition transition);
}

	

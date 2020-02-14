
public enum SchedulerState {
	//initial state for the scheduler
	START {
		@Override
		public SchedulerState next(Transition transition) {
			return RECIEVE_FLOOR_REQUEST;
		}
	},
	RECIEVE_FLOOR_REQUEST {
		@Override
		public SchedulerState next(Transition transition) {
			return SEND_FLOOR_REQUEST;
		}
	},
	SEND_FLOOR_REQUEST {
		@Override
		public SchedulerState next(Transition transition) {
			return RECIEVE_ELEVATOR_REPORT;
		}
	},
	RECIEVE_ELEVATOR_REPORT {
		@Override
		public SchedulerState next(Transition transition) {
			return SEND_ELEVATOR_REPORT;
		}
	},
	SEND_ELEVATOR_REPORT {
		@Override
		public SchedulerState next(Transition transition) {
			return START;
		}
	};

	public abstract SchedulerState next(Transition transition);

}

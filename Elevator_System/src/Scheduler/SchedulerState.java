package Scheduler;

import Utility.Transition;

public enum SchedulerState {
	//initial state for the scheduler
	WAITING_FOR_REQUEST {
		@Override
		public SchedulerState next(Transition transition) {
			return SELECT_ELEVATOR;
		}
	},
	SELECT_ELEVATOR {
		@Override
		public SchedulerState next(Transition transition) {
			return 	WAITING_FOR_REPORT;
		}
	},
	WAITING_FOR_REPORT {
		@Override
		public SchedulerState next(Transition transition) {
			return SEND_ELEVATOR_REPORT;
		}
	},
	SEND_ELEVATOR_REPORT {
		@Override
		public SchedulerState next(Transition transition) {
			return WAITING_FOR_REQUEST;
		}
	};

	public abstract SchedulerState next(Transition transition);
	public static SchedulerState getInitialState() {
		return WAITING_FOR_REQUEST;
	}

}
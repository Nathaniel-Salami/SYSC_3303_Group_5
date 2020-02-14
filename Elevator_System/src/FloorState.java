public enum FloorState {
	//initial state for the elevator
	PRESS_DIRECTION {
		@Override
		public FloorState next(Transition transition) {
			return LOAD;
		}
	},
	LOAD {
		@Override
		public FloorState next(Transition transition) {
			return TRAVEL;
		}
	},
	TRAVEL {
		@Override
		public FloorState next(Transition transition) {
				return UNLOAD;
		}
	},
	UNLOAD {
		@Override
		public FloorState next(Transition transition) {
			return DECELERATING;
		}
	};

	public abstract FloorState next(Transition transition);
}

	

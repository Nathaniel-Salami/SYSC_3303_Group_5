public enum FloorState {
	//initial state for the floor
	START {
		@Override
		public FloorState next(Transition transition) {
			return MAKE_REQUEST;
		}
	},
	MAKE_REQUEST {
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
			return START;
		}
	};

	public abstract FloorState next(Transition transition);
}
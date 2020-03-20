package Utility;
public enum Transition {
	//elevator transitions
	CLOSE_DOORS,
	OPEN_DOORS,
	THROTTLE_UP,
	THROTTLE_BACK,
	BRAKE,
	STOPPING,
	
	//floor transitions
	PRESS_BUTTON,
	ENTER,
	WAIT,
	LEAVE,
	RESET,
	
	//scheduler transition
	SEND,
	RECEIVE
}

package Scheduler;

import java.net.InetAddress;

import Utility.Direction;

public class ElevatorRepresentation {

	protected int floor;
	protected Direction dir;
	protected int port;
	protected InetAddress address;
	protected boolean inUse = false;
	
	public ElevatorRepresentation(InetAddress address, int port,int floor, Direction dir) {
		this.address = address;
		this.port = port;
		this.floor = floor;
		this.dir = dir;
	}
	
	@Override
	public String toString() {
		return address + " " + port + " Current Floor:" + floor + " Direction:"+dir;
	}
	
	public void copy(ElevatorRepresentation el) {
		this.floor = el.floor;
		this.dir = el.dir;
		this.inUse = el.inUse;
	}
	
	@Override
	public boolean equals(Object o) { 
		if(o == this) return true;
		 
		if(o instanceof ElevatorRepresentation) {
			if(((ElevatorRepresentation) o).port == this.port) {
				for(int i = 0; i < this.address.getAddress().length; i++) {
					if(((ElevatorRepresentation) o).address.getAddress()[i] != this.address.getAddress()[i])
						return false;
				}
				return true;
			}
		}
		 
		return false; 
	}
}

package Scheduler;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import Utility.Event;

public class Elevators {
	
	protected ArrayList<SimpleEntry<ElevatorRepresentation, ArrayList<Event>>> prioMap;
	
	public Elevators() {
		
	}
	
	private synchronized boolean contains(ElevatorRepresentation el) {
		for(int i = 0; i < prioMap.size(); i++)
			if(prioMap.get(i).getKey().equals(el))
				return true;
		
		return false;
	}
	
	
	public synchronized void registerElevator(ElevatorRepresentation el) {
		if(contains(el)) return;
		prioMap.add(new SimpleEntry<>(el,new ArrayList<Event>()));
	}
	
}

package Scheduler;

import java.util.AbstractMap.SimpleEntry;
import java.net.InetAddress;
import java.util.ArrayList;

import Utility.Event;

public class RestructuredSchedulder {
	
	Requests requests;
	protected ArrayList<SimpleEntry<ElevatorRepresentation, ArrayList<Event>>> prioMap;
	protected InetAddress floor;
	
	public RestructuredSchedulder() {
		requests = new Requests();
		prioMap = new ArrayList<>();
	}
	
	public synchronized void registerElevator(ElevatorRepresentation el) {
	
		// Very Inefficient, but I really don't want to spend an hour trying to build a good hash function
		boolean add = true;
		
		for(int i = 0; i < prioMap.size();i++) {
			if(prioMap.get(i).getKey() == el)
				add = false;
		}
		
		if(add)
			prioMap.add(new SimpleEntry<>(el,new ArrayList<Event>()));
		
	}
	
	public synchronized int getElevatorIndex(ElevatorRepresentation rep) {
		for(int i = 0; i < prioMap.size();i++) 
			if(prioMap.get(i).getKey().equals(rep))
				return i;
		
		return -1;
	}
	
	public synchronized void addRequest(ElevatorRepresentation rep, Event event) {
		
		for(int i = 0; i < prioMap.size();i++) 
			if(prioMap.get(i).getKey().equals(rep))
				prioMap.get(i).getValue().add(event);
		
	}
	
	public synchronized void removeRequest(ElevatorRepresentation rep, Event event) {
		
		for(int i = 0; i < prioMap.size();i++) 
			if(prioMap.get(i).getKey().equals(rep))
				prioMap.get(i).getValue().remove(event);
		
	}
	
	public synchronized ArrayList<Event> getRequests(ElevatorRepresentation rep){
		for(int i = 0; i < prioMap.size();i++) 
			if(prioMap.get(i).getKey().equals(rep))
				return prioMap.get(i).getValue();
		
		return null;
	}
	
}

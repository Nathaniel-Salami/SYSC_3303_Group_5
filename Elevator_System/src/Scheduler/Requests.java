package Scheduler;

import java.util.ArrayList;
import java.util.LinkedList;

import Utility.Event;

public class Requests {

	private ArrayList<Event> pendingRequests;
	private LinkedList<Event> finishedRequests;
	
	private boolean empty = true;
	
	public Requests() {
		pendingRequests = new ArrayList<Event>();
		finishedRequests = new LinkedList<Event>();
	}
	
	public synchronized void addPendingRequest(Event event) {
		pendingRequests.add(event);
		empty = false;
		
		notifyAll();
	}
	
	public synchronized Event getPrendingRequest(int i) {
		
		while(empty) {
			try {
				wait();
			} catch (Exception e) {}
		}
		
		Event request = pendingRequests.get(i);
		empty = (pendingRequests.isEmpty());
		
		notifyAll();
		return request;
		
	}
	
	public synchronized void removePrendingRequest(Event event) {
		while(empty) {
			try {
				wait();
			}catch(Exception e) {e.printStackTrace();}
		}
		pendingRequests.remove(event);
		notifyAll();
	}
	
	public synchronized void addFinishedRequest(Event event) {
		finishedRequests.addFirst(event);
		notifyAll();
	}
	
	public synchronized Event getFinishedRequest() {
		
		while(finishedRequests.isEmpty()) {
			try {
				wait();
			}catch (Exception e) {}
		}
		
		Event e = finishedRequests.removeLast();
		notifyAll();
		return e;
	}
	
	public synchronized int sizePending() {
		return pendingRequests.size();
	}
	
	public synchronized int sizeFinished() {
		return finishedRequests.size();
	}
	
	
	
}



public class Event implements Comparable<Event> {
	
	private Time time;
	private int floor;
	private int destination;
	private Direction direction;
	
	public Event(String data) {
		
		String[] tokens = data.split(" ");
		
		if(tokens.length > 3) {
			setTime(new Time(tokens[0]));
			setFloor(Integer.parseInt(tokens[1]));
			getDirection(tokens[2]);
			setDestination(Integer.parseInt(tokens[3]));
		}
		
		//System.out.println("added: " + this);
	}
	
	private void getDirection(String dir) {
		if(dir.toLowerCase().equals("up"))
			direction = Direction.Up;
		else
			direction = Direction.Down;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	@Override
	public String toString() {
		return String.format("%02d:%02d:%02d:%02d %02d %s %02d",time.getHour(),time.getMinute(),time.getSecond(),time.getMilisecond(),floor,direction.toString(),destination);
	}

	@Override
	public int compareTo(Event e) {
		return toString().compareTo(e.toString());

	}
	
}

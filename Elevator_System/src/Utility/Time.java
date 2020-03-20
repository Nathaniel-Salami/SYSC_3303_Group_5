package Utility;


public class Time {
	
	private int hour,minute,second,milisecond;
	
	public Time(String time) {
		String increments[] = time.split(":");
		
		setHour(Integer.parseInt(increments[0]));
		setMinute(Integer.parseInt(increments[1]));
		setSecond(Integer.parseInt(increments[2]));
		setMilisecond(Integer.parseInt(increments[3]));
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getMilisecond() {
		return milisecond;
	}

	public void setMilisecond(int milisecond) {
		this.milisecond = milisecond;
	}
	
	
}

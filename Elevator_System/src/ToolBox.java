import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ToolBox {
	
	private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private final static int TIME = 600;
	
	public static void sleep(int t) {
		try {
			Thread.sleep(t);
		} 
		catch (InterruptedException e) {}
	}
	
	public static void sleep() {
		try {
			Thread.sleep(TIME);
		} 
		catch (InterruptedException e) {}
	}
	
	public static String getNow() {
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

}

package Utility;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import Elevator.ElevatorState;
import Scheduler.ElevatorRepresentation;

public class Helper {	
	
	static int TIME = 500;
	
	public static void printSend(String entity, DatagramPacket dp, boolean isRPCReply, String destEntity) {
		if (isRPCReply) {
			System.out.println(entity + ": Sending reply packet to " + destEntity);
		}
		else {
			System.out.println(entity + ": Sending packet to " + destEntity);
		}
		System.out.println("\tTimeStamp:        " + nowTime());
		System.out.println("\tTo host:          " + dp.getAddress());
		System.out.println("\tDestination port: " + dp.getPort());
		System.out.println("\tLength:           " + dp.getLength());
		System.out.println("\tContaining: ");
		System.out.println("\t\tString: " + new String(dp.getData(), 0, dp.getLength()));
		System.out.println("\t\tBytes:  " + Arrays.toString(trim(dp.getData())));
		System.out.println();
	}
	
	public static void printReceive(String entity, DatagramPacket dp, boolean isRPCReply, String destEntity) {
		if (isRPCReply) {
			System.out.println(entity + ": Reply packet received from " + destEntity);
		}
		else {
			System.out.println(entity + ": Packet received from " + destEntity);
		}
		System.out.println("\tTimeStamp:  " + nowTime());
		System.out.println("\tFrom host:  " + dp.getAddress());
		System.out.println("\tHost port:  " + dp.getPort());
		System.out.println("\tLength:     " + dp.getLength());
		System.out.println("\tContaining: ");
		System.out.println("\t\tString: " + new String(dp.getData(), 0, dp.getLength()));
		System.out.println("\t\tBytes:  " + Arrays.toString(trim(dp.getData())));
		System.out.println();
	}
	
	public static void sleep(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e ) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void sleep() {
		Helper.sleep(TIME);
	}
	
	public static String nowTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now); 
	}
	 
	
	//remove excess 0's from data (leaves one 0 to match requirement)
	public static byte[] trim(byte[] data) {
		int i = data.length - 1;
		
	    while ((data[i] == 0) && (data.length != 4)) {
	        --i;
	    }
	    
	    byte[] temp = Arrays.copyOf(data, i + 2);
	    		
	    //return temp;
	    return data;
	}
	
	public static ElevatorRepresentation extractInformation(DatagramPacket request) {
		byte data[] = request.getData();
		ByteBuffer wrapper = ByteBuffer.wrap(data, 0, 4);
		int floor = wrapper.getInt();
		Direction dir = (data[4] == 0)?Direction.Up:(data[4] == 1)?Direction.Down:Direction.Static; 
		
		return new ElevatorRepresentation(request.getAddress(), request.getPort(), floor, dir);
	}
	
	public static byte[] constuctMessage(boolean stop, Direction dir) {
		byte msg[] = new byte[2];
		
		switch(dir) {
		case Up:
			msg[1] = 0;
			break;
		case Down:
			msg[1] = 1;
			break;
		case Static:
			msg[1] = 2;
			break;
		}
		
		msg[0] = (byte) ((stop)?1:0);
		
		return msg;
	}
}

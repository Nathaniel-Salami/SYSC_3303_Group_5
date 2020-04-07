package Floor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import Utility.Event;

public class ReceiverThread implements Runnable {

	private DatagramSocket socket;
	private DatagramPacket packet;
	
	private Floor floorSystem;
	
	public ReceiverThread(Floor floor) {
		floorSystem = floor;
		try {
			socket = new DatagramSocket(43);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public void run() {
		
		while(true) {
			packet = new DatagramPacket(new byte[100], 100);
			try {
				socket.receive(packet);
			}catch (Exception e) {
				// TODO: handle exception
			}
			Event ev =  new Event(new String(packet.getData()).trim());
			floorSystem.completeRequest(ev);
		}
		
	}

}

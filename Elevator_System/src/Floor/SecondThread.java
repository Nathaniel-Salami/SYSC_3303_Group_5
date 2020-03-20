package Floor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import Utility.Event;
import Utility.Helper;

public class SecondThread implements Runnable {

	DatagramSocket socket;
	DatagramPacket packet;
	
	public SecondThread() {
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
			System.out.printf("(%s)Packer Received: %s \n",Helper.nowTime(),ev);
		}
		
	}

}

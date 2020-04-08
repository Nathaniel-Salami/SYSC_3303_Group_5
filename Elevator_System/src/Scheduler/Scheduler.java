package Scheduler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * {@summary The Scheduler is only being used as a communication channel from
 * the Floor thread to the Elevator thread and back again.}
 */


public class Scheduler {

	public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
       
        int lowest, highest;
        while(true) {
            System.out.print("Ënter Lowest Floor: ");
            int num = -1;
        	try {
				String line = reader.readLine();
				num  = Integer.parseInt(line);
			} catch (Exception e) {
				System.out.println("Invalid Input");
			}
        	
        	lowest = num;
        	break;
        	
        }
        
        while(true) {
            System.out.print("Ënter Highest Floor: ");
            int num = -1;
        	try {
				String line = reader.readLine();
				num  = Integer.parseInt(line);
			} catch (Exception e) {
				System.out.println("Invalid Input");
			}
        	if(lowest < num) {
        		highest = num;
        		break;
        	}
        	else 
        		System.out.println("Invalid Highest Floor");
        }
        
		RestructuredSchedulder scheduler = new RestructuredSchedulder(lowest,highest);
		Thread t1 = new Thread(new RegistrationThread(scheduler),"Registration Thread");
		t1.start();
		Thread t2 = new Thread(new RequestThread(scheduler),"Request Thread");
		t2.start();
		Thread t3 = new Thread(new SchedulingThread(scheduler), "Scheduling Thread");
		t3.start();
		Thread t4 = new Thread(new ElevatorContolThread(scheduler), "Elevator Control Thread");
		t4.start();
		Thread t5 = new Thread(new ArrivalThread(scheduler), "Floor Response Thread");
		t5.start();

		
	}

}

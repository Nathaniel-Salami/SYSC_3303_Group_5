# Iteration 2 - SYSC 3303 Real Time Concurrent Systems - Winter 2020

Purpose: The purpose of this software is to cover the intuition behind real time embedded systems through the implementation of an elevator system.

Developers & Development Date: Group 5 - February 15, 2020
  Nathaniel Salami
  Teodor Kirilov
  Lila Mcilveen
  Alexander D'Alessandro
  Dominic Kocjan

The learning purpose:
	State machines for real-time embedded systems
	Concurrent threads in Java (Elevator subsytem, Floor subsystem, Scheduler subsystem)
  	Java's approach to thread synchronization: synchronized, wait(), notify() and notifyAll()
  	Handling file input through a data structure

Software organization: 
	There are 15 .java files consisting of:
	- Elevator.java		
   	- ElevatorDemo.java
	- ElevatorState.java
	- ElevatorTest.java
	- Floor.java
	- FloorState.java
    	- FloorTest.java
    	- Scheduler.java
	- SchedulerState.java
    	- SchedulerTest.java
	- ToolBox.java
	- Transition.java
	
    	- Event.java
    	- Time.java
    	- Direction.java
    
	There is 1 .txt file:
	- floor-commands.txt
	
Set up instructions
	- Open Eclipse and import the project
	- Right click on ElevatorDemo.java and select Run As >> Java Application

	All text input will be loaded, and dynamically allocated into a data structure.
	
Limitations:
	- All three threads run within the same program, based on iteration specifications.


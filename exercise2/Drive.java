package behavPIII;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/**
 * The lowest priority behaviour, keeps the robot driving forward
 * @author E1
 *
 */
public class Drive implements Behavior {


	private DifferentialPilot pilot;
	private boolean suppressedDrive;
	
	/**
	 * the constructor receiving and declaring the variables
	 * @param pilot the differential pilot used to move the robot
	 */
	public Drive(DifferentialPilot pilot) {
		super();
		this.pilot = pilot;
		this.suppressedDrive = false;
	}

	/**
	 * the drive method will always take control if able so that it keeps driving
	 */
	@Override
	public boolean takeControl() {
		//if nothing else is happening drive!
		return true;
	}

	/**
	 * the action method that runs while this behaviour takes control
	 * simply moves the robot forward until suppressed
	 */
	@Override
	public void action() {
		pilot.forward();//main action is drive forward
		while(!suppressedDrive)
		{//when it is suppressed by another action the thread.yield looks for what
			// wants to be run
			Thread.yield();
		}
		pilot.stop();
		suppressedDrive = false;
		//resets the flag
	}

	/**
	 * the suppress method for the behaviour implementation
	 */
	@Override
	public void suppress() {
		suppressedDrive = true;

	}

}

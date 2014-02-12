package behavPIII;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

/**
 * This behavious takes precedence over Drive but is no prioritised over TakeTurn
 * it keeps the robot on the line by adjusting it if one sensor hits the line
 * @author E1
 *
 */
public class Adjust implements Behavior {

	private DifferentialPilot pilot;
	private LightSensor left;
	private LightSensor right;
	private boolean side;
	private boolean suppressedAdjust;
	
	private static final int ADJUST = 10;
	
	/**
	 * The constructor receives and sets the variables
	 * @param p The differential pilot for the movement of the robot
	 * @param left the left light sensor
	 * @param right the right light sensor
	 */
	public Adjust(DifferentialPilot p, LightSensor left, LightSensor right) 
	{
		super();
		this.pilot = p;
		this.left = left;
		this.right = right;
		left.setFloodlight(true);
		right.setFloodlight(true);
		suppressedAdjust = false;
	}

	/**
	 * sets the side boolean for which direction the robot needs to be adjusted in
	 * and then returns true for the behaviour to take control if one sensor detects black
	 */
	@Override
	public boolean takeControl() {
		if((left.readNormalizedValue()<400) && !(right.readNormalizedValue()<400))
		{//if only the left sensor is on black then take control
			side = false;
			return true;
		}
		if((right.readNormalizedValue()<400) && !(left.readNormalizedValue()<400))
		{//if only the right sensor is on black then take control
			side = true;
			return true;
		}
		
		return false;
	}

	/**
	 * here the adjustment is made, depending on which sensor goes over the line the robot
	 * rotates to correct itself to follow the line again
	 */
	@Override
	public void action() {
		if (side) {// If right hand side
			pilot.rotate(ADJUST);
		} 
		else {// If left hand side
			pilot.rotate(-ADJUST);
		}
		while(!suppressedAdjust && pilot.isMoving())
		{//also runs while moving as the drive does not suppress this action
			Thread.yield();
		}
		suppressedAdjust = false;
	}

	/**
	 * the suppress method for the behavious implementation
	 */
	@Override
	public void suppress() {
		suppressedAdjust = true;

	}

}

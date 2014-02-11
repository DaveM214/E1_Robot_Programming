package exercise2;

import java.util.ArrayList;
import java.util.Random;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;

//TODO implement function that makes it check if a wall is in front of it and then turn round if it is

/**
 * 
 * Class that extends the previous LineDancing due to the fact that the same
 * underlying behaviour is present and that overall function of the two is
 * similar. Class facilitates the navigation of a grid of black lines on a light
 * surface. Listeners are used to both correct the course of the robot when
 * following the lines of the grid and for detecting junctions. When a junction
 * is detected
 * 
 * @author E1
 * 
 */
public class GridNavigate extends LineDancing {

	// Constants for inputting direction more easily
	private static final int UP = 0;
	private static final int RIGHT = 1;
	private static final int DOWN = 2;
	private static final int LEFT = 3;
	private ArrayList<Integer> directions = new ArrayList<Integer>();
	private boolean repeatPattern;
	private int currentMovement = 0;
	private boolean randomise = false;
	private boolean junctionReached = false;

	/**
	 * Constructor, creates an instance of this class
	 * 
	 * @param robot
	 *            The robot
	 * @param repeat
	 *            Set the boolean field of whether or not the robot should
	 *            repeat the path programmed into it
	 */
	public GridNavigate(CastorBotLineFollow robot, boolean repeat) {
		super(robot);
		repeatPattern = repeat;
	}

	/**
	 * Override of method from the super class. Performs the same functions as
	 * method in the super class in that it keeps a robot following a line but
	 * can now also detect junctions and pass the fact that it has found a
	 * junction onto another method which allows it to navigate them.
	 * 
	 * @param side
	 *            The side that needs adjusting according to the listener. RIGHT
	 *            is TRUE, FALSE is LEFT
	 */
	public void adjustPosition(boolean side) {

		if (leftListener.adjusting() && rightListener.adjusting()) {
			System.out.println("junction!");
			junctionReached();
		} 
		
		else {
			if (side) {// If right hand side
				robot.getPilot().rotate(-ROTATION_CONSTANT);
			} else {// If left hand side
				robot.getPilot().rotate(ROTATION_CONSTANT);
			}
		}

	}

	/**
	 * Override of the method in the super class due to increased functionality
	 * required as it must now check that it is also not at a junction.
	 * Implements the desired behaviour of the program
	 */
	@Override
	public void run() {
		robot.getRight().setFloodlight(true);
		robot.getLeft().setFloodlight(true);
		robot.getPilot().setTravelSpeed(10);

		while(true)
		{
			while(!(leftListener.adjusting() || rightListener.adjusting()))
			{
				robot.getPilot().travel(TRAVELMM);
				Thread.yield();
			}
		}
	}

	/**
	 * Method called when the adjustment method reaches what it detects as a
	 * junction. Follows the next direction in the arrayList of directions. If
	 * there are no more directions left in the list it will either go back to
	 * the first if the repeat flag is true or it will do random movements.
	 * Repetition is useful for creating patterns.
	 */
	private void junctionReached() {

		int turn = 0;
		junctionReached = true;

		if (currentMovement >= directions.size()) {
			if (repeatPattern) {
				currentMovement = 0;
			}

			else {
				randomise = true;
			}

		}

		if (randomise) {
			turn = (int) (Math.random() * 5);
		} else {
			turn = directions.get(currentMovement);
		}

		currentMovement++;
		robot.getPilot().rotate(90 * turn);
		junctionReached = false;

	}
	
	public void addDirections(int i)
	{
		if(i > 3)
		{
			return;//If invalid number do nothing
		}
		else
		{
			directions.add(i);
		}
	}

	/**
	 * Main method, called on execution in order to run the program via the run
	 * method of the class
	 * 
	 * @param args
	 *            main method arguments
	 */
	public static void main(String[] args) {
		Configuration config = new WHEATLEYCONFIG();
		CastorBotLineFollow robot = new CastorBotLineFollow(config,
				SensorPort.S4, SensorPort.S2);
		Button.waitForAnyPress();
		GridNavigate demo = new GridNavigate(robot, true);
		demo.addDirections(LEFT);
	}

}

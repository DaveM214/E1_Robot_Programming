package exercise2;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import lejos.nxt.LightSensor;

/**
 * Class that allows a robot using the layout of sensors defined in the
 * CastorBotLineFollow class to follow a single dark coloured line on a white
 * background
 * 
 * @author Team E1
 * 
 */
public class LineDancing {

	protected LightSensorListener leftListener;
	protected LightSensorListener rightListener;
	protected CastorBotLineFollow robot;
	protected final static int ROTATION_CONSTANT = 15;
	protected final static int TRAVELMM = 30;

	/**
	 * Takes in a CastorBotLineFollow which is a CastorBot with two light
	 * sensors on it as a parameter
	 * 
	 * @param robot
	 *            The CastorBorLineFollow entity being used
	 */
	public LineDancing(CastorBotLineFollow robot) {

		// Listeners are declared now because they are specific to the task.
		this.robot = robot;
		leftListener = new LightSensorListener(LightSensorListener.LEFT, this);
		rightListener = new LightSensorListener(LightSensorListener.RIGHT, this);
		robot.getLeftPort().addSensorPortListener(leftListener);
		robot.getRightPort().addSensorPortListener(rightListener);
	}

	/**
	 * Run method that executes the main task of the program. Makes robot follow
	 * a line by travelling forward a set amount and adjsuting the course of the
	 * robot as direct by the listener which checks that the robot is staying on
	 * the line
	 */
	public void run() {

		robot.getRight().setFloodlight(true);
		robot.getLeft().setFloodlight(true);
		robot.getPilot().setTravelSpeed(robot.getPilot().getMaxTravelSpeed());
		while (true) {
			while (!(leftListener.adjusting() || rightListener.adjusting())) {
				robot.getPilot().travel(TRAVELMM, true);
				Thread.yield();
			}

		}

	}

	/**
	 * 
	 * @param side
	 *            the side the light sensor is on TRUE is RIGHT. FALSE is LEFT
	 */
	public void adjustPosition(boolean side) {
		if (side) {// If right hand side
			robot.getPilot().rotate(-ROTATION_CONSTANT);
		} else {// If left hand side
			robot.getPilot().rotate(ROTATION_CONSTANT);
		}
	}

	/**
	 * @param args The launch arguments
	 */
	public static void main(String[] args) {

		Configuration config = new WHEATLEYCONFIG();
		CastorBotLineFollow robot = new CastorBotLineFollow(config,
				SensorPort.S4, SensorPort.S2);
		Button.waitForAnyPress();
		LineDancing demo = new LineDancing(robot);
		demo.run();
	}
}
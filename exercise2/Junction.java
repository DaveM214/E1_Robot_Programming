package behavPIII;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * This is the main class for the grid navigation, here we create the arbitrator and add
 * the behaviours
 * @author E1
 *
 */
public class Junction {
	
	DifferentialPilot pilot;
	LightSensor left;
	LightSensor right;
	
	/**
	 * The constructor receives and declares the variables
	 * @param pilot the differential pilot for the robot movement
	 * @param left the left light sensor
	 * @param right the right light sensor
	 */
	public Junction(DifferentialPilot pilot, LightSensor left, LightSensor right) {
		super();
		this.pilot = pilot;
		this.left = left;
		this.right = right;
	}

	/**
	 * the run method is where the arbitrator is created, behaviours added and the arbitrator
	 * is started
	 */
	public void run()
	{
		pilot.setTravelSpeed(10);
		Behavior[] behaviours = {new Drive(pilot), new Adjust(pilot, left, right),
											new TakeTurn(pilot, left, right)};
		Arbitrator arby = new Arbitrator(behaviours);
		arby.start();
		//the arbitrator controls which behaviour to do
	}
	
	/**
	 * The main method in which demo is called and sent the differential pilot.
	 * @param args
	 */
	public static void main(String[] args) {
		Button.waitForAnyPress();
		Junction demo = new Junction(new DifferentialPilot(5.6, 12.9, Motor.C,
				Motor.B), new LightSensor(SensorPort.S2), new LightSensor(SensorPort.S4));
		demo.run();
	}

}

package org.usfirst.frc.team4121.robot;

import org.usfirst.frc.team4121.robot.commands.AngleMotorCommand;
import org.usfirst.frc.team4121.robot.commands.AngleMotorReverseCommand;
import org.usfirst.frc.team4121.robot.commands.ClosedArmsCommand;
import org.usfirst.frc.team4121.robot.commands.EjectCubeCommandGroup;
import org.usfirst.frc.team4121.robot.commands.ElevatorToHomeCommand;
import org.usfirst.frc.team4121.robot.commands.ElevatorToSwitchCommand;
import org.usfirst.frc.team4121.robot.commands.OpenArmsCommand;
import org.usfirst.frc.team4121.robot.commands.ShiftGearCommand;
import org.usfirst.frc.team4121.robot.commands.AngleMotorStopCommand;
import org.usfirst.frc.team4121.robot.commands.SwitchDriveCommand;
import org.usfirst.frc.team4121.robot.commands.TakeInCubeCommandGroup;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


/**
 * This is the main class that initializes and tells what buttons to do what.
 * 
 * @author Jonas Muhlenkamp
 */
public class OI {
	
	//General Declarations of Objects
	public XboxController xbox;
	public ADXRS450_Gyro MainGyro;
	public DigitalInput elevatorTopLimit, elevatorBottomLimit;
	public Encoder rightEncoder, leftEncoder;
	public Button shiftGear, switchDrive;
	public Button elevatorHome, elevatorSwitch, angleUp, angleDown;
	public Button takeInCube, spinWheelsOut, openGrabber, closeGrabber;
	
	
	public OI() {		
		
		//Initialize gyro
		MainGyro = new ADXRS450_Gyro();

		//Initialize elevator limit switches
		elevatorTopLimit = new DigitalInput(0);
		elevatorBottomLimit = new DigitalInput(1);
			
		//Define xbox controller
		xbox = new XboxController(0);

		//Xbox controller buttons
		elevatorHome = new JoystickButton(xbox, 1); //a button
		elevatorSwitch = new JoystickButton(xbox, 2); //b button
	
		angleDown = new JoystickButton(xbox, 3);//x button
		angleUp = new JoystickButton(xbox, 4);//y button
		
		openGrabber = new JoystickButton (xbox, 8); //start button
		closeGrabber = new JoystickButton(xbox, 7); //back button
		takeInCube = new JoystickButton(xbox, 5); //top left trigger button
		spinWheelsOut = new JoystickButton(xbox, 6); //top right trigger button
		
		shiftGear = new JoystickButton(xbox, 9); //left joystick button
		switchDrive = new JoystickButton(xbox, 10); //right joystick button
		
		//define button commands
		shiftGear.whenPressed(new ShiftGearCommand());
		switchDrive.whenPressed(new SwitchDriveCommand());
		
		angleUp.whileHeld(new AngleMotorCommand());
		angleUp.whenReleased(new AngleMotorStopCommand());
		angleDown.whileHeld(new AngleMotorReverseCommand());
		angleDown.whenReleased(new AngleMotorStopCommand());
		
		elevatorSwitch.whenPressed(new ElevatorToSwitchCommand());
		elevatorHome.whenPressed(new ElevatorToHomeCommand());

		takeInCube.whenPressed(new TakeInCubeCommandGroup());
		spinWheelsOut.whenPressed(new EjectCubeCommandGroup());
		openGrabber.whenPressed(new OpenArmsCommand());
		closeGrabber.whenPressed(new ClosedArmsCommand());
		
	}
}

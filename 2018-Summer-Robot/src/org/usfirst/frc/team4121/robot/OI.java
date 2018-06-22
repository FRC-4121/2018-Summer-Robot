package org.usfirst.frc.team4121.robot;

import org.usfirst.frc.team4121.robot.commands.AbortAutoDriveToCube;
import org.usfirst.frc.team4121.robot.commands.AngleMotorCommand;
import org.usfirst.frc.team4121.robot.commands.AngleMotorReverseCommand;
import org.usfirst.frc.team4121.robot.commands.AutoAngleMotorDownCommand;
import org.usfirst.frc.team4121.robot.commands.AutoPickUpCubeCommandGroup;
import org.usfirst.frc.team4121.robot.commands.ClosedArmsCommand;
import org.usfirst.frc.team4121.robot.commands.EjectCubeCommandGroup;
import org.usfirst.frc.team4121.robot.commands.ElevatorToHomeCommand;
import org.usfirst.frc.team4121.robot.commands.ElevatorToPyramid;
import org.usfirst.frc.team4121.robot.commands.ElevatorToSwitchCommand;
import org.usfirst.frc.team4121.robot.commands.OpenArmsCommand;
import org.usfirst.frc.team4121.robot.commands.ShiftDownCommand;
import org.usfirst.frc.team4121.robot.commands.ShiftUpCommand;
import org.usfirst.frc.team4121.robot.commands.StopAngleMotorCommand;
import org.usfirst.frc.team4121.robot.commands.StopClimbCommand;
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
	public Encoder rightEncoder, leftEncoder;
	public Button shiftUp, shiftDown, switchDrive;
	public Button elevatorHome, elevatorSwitch, elevatorPyramid, endAngleMotorUp, endAngleMotorDown;
	public Button takeInCube, spinWheelsOut, openGrabber, closeGrabber;
	
	public OI() {		
		
		//Initialize gyro
		MainGyro = new ADXRS450_Gyro();
		
		//Define controllers , values can be moved on smart dashboard;
		xbox = new XboxController(2);
		
		shiftDown = new JoystickButton(xbox, 9); //left joystick button
		shiftUp = new JoystickButton(xbox, 10); //right joystick button

		//switchDrive = new JoystickButton(xbox, 4); //left trigger
		//endAngleMotorUp = new JoystickButton(xbox, 6);//right trigger
		//endAngleMotorDown = new JoystickButton(xbox, 7);
			
		//Xbox controller buttons
		elevatorHome = new JoystickButton(xbox, 1); //a button
		elevatorSwitch = new JoystickButton(xbox, 2); //b button
		elevatorPyramid = new JoystickButton(xbox, 3); //x button
		//takeInCube = new JoystickButton(xbox, 5); //top left trigger
		//spinWheelsOut = new JoystickButton(xbox, 6); //top right trigger
		openGrabber = new JoystickButton (xbox, 5); //back button
		closeGrabber = new JoystickButton(xbox, 6); //start button
		
		//define button commands
		shiftUp.whenActive(new ShiftUpCommand());
		shiftDown.whenActive(new ShiftDownCommand());
		switchDrive.whenPressed(new SwitchDriveCommand());
		
		endAngleMotorUp.whileHeld(new AngleMotorCommand());
		endAngleMotorUp.whenReleased(new StopAngleMotorCommand());
		endAngleMotorDown.whileHeld(new AngleMotorReverseCommand());
		endAngleMotorDown.whenReleased(new StopAngleMotorCommand());
		
		elevatorSwitch.whenPressed(new ElevatorToSwitchCommand());
		elevatorHome.whenPressed(new ElevatorToHomeCommand());
		elevatorPyramid.whenPressed(new ElevatorToPyramid());
		
		takeInCube.whenPressed(new TakeInCubeCommandGroup());
		spinWheelsOut.whenPressed(new EjectCubeCommandGroup());
		openGrabber.whenPressed(new OpenArmsCommand());
		closeGrabber.whenPressed(new ClosedArmsCommand());
		
	}
}

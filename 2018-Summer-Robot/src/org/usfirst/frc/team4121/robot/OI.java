package org.usfirst.frc.team4121.robot;

import org.usfirst.frc.team4121.robot.commands.AbortAutoDriveToCube;
import org.usfirst.frc.team4121.robot.commands.AngleMotorCommand;
import org.usfirst.frc.team4121.robot.commands.AngleMotorReverseCommand;
import org.usfirst.frc.team4121.robot.commands.AutoAngleMotorDownCommand;
import org.usfirst.frc.team4121.robot.commands.AutoPickUpCubeCommandGroup;
import org.usfirst.frc.team4121.robot.commands.BeginningMatchCommandGroup;
import org.usfirst.frc.team4121.robot.commands.ClimbCommand;
import org.usfirst.frc.team4121.robot.commands.ClimbReverseCommand;
import org.usfirst.frc.team4121.robot.commands.ClosedArmsCommand;
import org.usfirst.frc.team4121.robot.commands.EjectCubeCommandGroup;
import org.usfirst.frc.team4121.robot.commands.ElevatorToHomeCommand;
import org.usfirst.frc.team4121.robot.commands.ElevatorToPyramid;
import org.usfirst.frc.team4121.robot.commands.ElevatorToScaleCommand;
import org.usfirst.frc.team4121.robot.commands.ElevatorToSwitchCommand;
import org.usfirst.frc.team4121.robot.commands.OpenArmsCommand;
import org.usfirst.frc.team4121.robot.commands.ShiftDownCommand;
import org.usfirst.frc.team4121.robot.commands.ShiftUpCommand;
import org.usfirst.frc.team4121.robot.commands.StopAngleMotorCommand;
import org.usfirst.frc.team4121.robot.commands.StopClimbCommand;
import org.usfirst.frc.team4121.robot.commands.SwitchDriveCommand;
import org.usfirst.frc.team4121.robot.commands.TakeInCubeCommandGroup;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


/**
 * This is the main class that initializes and tells what buttons to do what.
 * 
 * @author Saliva Crustyman
 */
public class OI {
	
	//General Declarations of Objects
	public Joystick leftJoy, rightJoy;
	public XboxController xbox;
	public DigitalInput limitSwitchClimbBottomStop,limitSwitchClimbUpperStop;
	//public ADXRS450_Gyro MainGyro;
	public Encoder rightEncoder, leftEncoder;
	public Button climb, reverseClimb, servo, shiftUp, shiftDown, switchDrive;
	public Button elevatorHome, elevatorSwitch, elevatorScale, bumpUp, bumpDown, elevatorPyramid,autoDriveToCube, abortAutoDriveToCube, endAngleMotorUp, endAngleMotorDown;
	public Button takeInCube, spinWheelsOut, takeInCubeJoy, spinWheelsOutJoy, autoTesterEndAngleMotor;
	public Button openGrabber, closeGrabber, openGrabberJoy, closeGrabberJoy, testBeginMatch;
	public Button openServo, closeServo, beginMatch;
	
	public OI() {
	
		//Limit Switch Initializations
		limitSwitchClimbBottomStop = new DigitalInput(0);  
		limitSwitchClimbUpperStop = new DigitalInput(1);		
		
		//Initialize gyro
		//MainGyro = new ADXRS450_Gyro();
		
		//Define controllers , values can be moved on smart dashboard
		leftJoy = new Joystick(0);
		rightJoy = new Joystick(1);
		xbox = new XboxController(2);
		
		//Left joystick buttons
		shiftDown = new JoystickButton(leftJoy, 4); //left trigger
		shiftUp = new JoystickButton(leftJoy, 5); //top right trigger
		openGrabberJoy = new JoystickButton (leftJoy, 3); //center trigger  
		closeGrabberJoy = new JoystickButton(leftJoy, 2); //lower center trigger 
		abortAutoDriveToCube = new JoystickButton(leftJoy, 1); //trigger button 
		testBeginMatch = new JoystickButton(leftJoy, 10); 
		
		
		//Right joystick buttons
		//openServo = new JoystickButton(rightJoy, 10); 
		//closeServo = new JoystickButton(rightJoy, 11);
		//beginMatch = new JoystickButton(rightJoy, 7);
		takeInCubeJoy = new JoystickButton(rightJoy, 3); //center trigger
		spinWheelsOutJoy = new JoystickButton(rightJoy, 2); //lower center trigger
		switchDrive = new JoystickButton(rightJoy, 4); //left trigger
		autoDriveToCube = new JoystickButton(rightJoy, 1); //trigger button
		endAngleMotorUp = new JoystickButton(rightJoy, 6);//right trigger
		endAngleMotorDown = new JoystickButton(rightJoy, 7);
		//autoTesterEndAngleMotor = new JoystickButton(rightJoy, 7); //will be removed
			
		//Xbox controller buttons
		elevatorHome = new JoystickButton(xbox, 1); //a button
		elevatorSwitch = new JoystickButton(xbox, 2); //b button
		elevatorPyramid = new JoystickButton(xbox, 3); //x button
		elevatorScale = new JoystickButton (xbox, 4); //y button
		takeInCube = new JoystickButton(xbox, 5); //top left trigger
		spinWheelsOut = new JoystickButton(xbox, 6); //top right trigger
		openGrabber = new JoystickButton (xbox, 7); //back button
		closeGrabber = new JoystickButton(xbox, 8); //start button
		//bumpUp = new JoystickButton(xbox, 9); //left mini joystick
		//bumpDown = new JoystickButton(xbox, 10); //right mini joystick
		climb = new JoystickButton(xbox, 9);
		reverseClimb = new JoystickButton(xbox, 10);
		
		//define left joystick button commands
		shiftUp.whenActive(new ShiftUpCommand());
		shiftDown.whenActive(new ShiftDownCommand());
		openGrabberJoy.whenPressed(new OpenArmsCommand());
		closeGrabberJoy.whenPressed(new ClosedArmsCommand());
		abortAutoDriveToCube.whenPressed(new AbortAutoDriveToCube());
		testBeginMatch.whenPressed(new AutoAngleMotorDownCommand());
		
		//define right joystick button commands
		//beginMatch.whenPressed(new BeginningMatchCommandGroup());
		takeInCubeJoy.whenPressed(new TakeInCubeCommandGroup());
		spinWheelsOutJoy.whenPressed(new EjectCubeCommandGroup());
		switchDrive.whenPressed(new SwitchDriveCommand());
		autoDriveToCube.whenPressed(new AutoPickUpCubeCommandGroup());
		endAngleMotorUp.whileHeld(new AngleMotorCommand());
		endAngleMotorUp.whenReleased(new StopAngleMotorCommand());
		endAngleMotorDown.whileHeld(new AngleMotorReverseCommand());
		endAngleMotorDown.whenReleased(new StopAngleMotorCommand());
		//autoTesterEndAngleMotor.whenReleased(new AutoAngleMotorDownCommand());
		//openServo.whenPressed(new OpenServoCommand());
		//closeServo.whenPressed(new CloseServo());
		
		//define xbox button commands
		climb.whileHeld(new ClimbCommand());
		climb.whenReleased(new StopClimbCommand());
		reverseClimb.whileHeld(new ClimbReverseCommand());
		reverseClimb.whenReleased(new StopClimbCommand());
		elevatorSwitch.whenPressed(new ElevatorToSwitchCommand());
		elevatorScale.whenPressed(new ElevatorToScaleCommand());
		elevatorHome.whenPressed(new ElevatorToHomeCommand());
		elevatorPyramid.whenPressed(new ElevatorToPyramid());
		takeInCube.whenPressed(new TakeInCubeCommandGroup());
		spinWheelsOut.whenPressed(new EjectCubeCommandGroup());
		openGrabber.whenPressed(new OpenArmsCommand());
		closeGrabber.whenPressed(new ClosedArmsCommand());
		
		//bumpUp.whenPressed(new BumpElevatorUpCommand());
		//bumpDown.whenPressed(new BumpElevatorDownCommand());
		
	}
}

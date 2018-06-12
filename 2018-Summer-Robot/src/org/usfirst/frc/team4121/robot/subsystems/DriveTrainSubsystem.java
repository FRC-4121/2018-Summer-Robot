package org.usfirst.frc.team4121.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.*;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;
import org.usfirst.frc.team4121.robot.commands.DriveWithJoysticksCommand;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;

/**         
 *	DriveTrain subsystem	
 *	                                                                    
 *	@author Saliva Crustyman 
 */
public class DriveTrainSubsystem extends Subsystem {
	
	double leftDirection, rightDirection;
	
	//Initializing all WPI_TalonSRXs using CAN                                                
	WPI_TalonSRX leftMotorMaster = new WPI_TalonSRX(RobotMap.LEFT_MOTOR_MASTER);
	WPI_TalonSRX leftMotorSlave = new WPI_TalonSRX(RobotMap.LEFT_MOTOR_SLAVE);
	SpeedControllerGroup leftMotorGroup = new SpeedControllerGroup(leftMotorMaster, leftMotorSlave);
	
	WPI_TalonSRX rightMotorMaster = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR_MASTER);
	WPI_TalonSRX rightMotorSlave = new WPI_TalonSRX(RobotMap.RIGHT_MOTOR_SLAVE);
	SpeedControllerGroup rightMotorGroup = new SpeedControllerGroup(rightMotorMaster, rightMotorSlave);
	
	//Creating a differential drive from left  and right speed groups
	DifferentialDrive drive = new DifferentialDrive(leftMotorGroup, rightMotorGroup);
	
	//Set up encoders on master controllers
	boolean encodersConfig = initEncoders();
	 
	
	//Setting the default command to DriveWithJoysticksCommands
	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoysticksCommand());
	}
	
	
	//Initialize left and right encoders
	public boolean initEncoders() {
		
		//left motor
		leftMotorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.kPIDLoopIdx, RobotMap.kTimeoutMs);
		leftMotorMaster.setSensorPhase(!RobotMap.kSensorPhase);
		leftMotorMaster.setInverted(RobotMap.kMotorInvert);
		int absolutePositionLeft = leftMotorMaster.getSensorCollection().getPulseWidthPosition();
		/* mask out overflows, keep bottom 12 bits */
		absolutePositionLeft &= 0xFFF;
		if (!RobotMap.kSensorPhase)
			absolutePositionLeft *= -1;
		if (RobotMap.kMotorInvert) //need to mess with this
			absolutePositionLeft *= -1;
		leftMotorMaster.setSelectedSensorPosition(absolutePositionLeft, RobotMap.kPIDLoopIdx, RobotMap.kTimeoutMs);
		
		
		//right motor
		rightMotorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.kPIDLoopIdx, RobotMap.kTimeoutMs);
		rightMotorMaster.setSensorPhase(RobotMap.kSensorPhase); //might need to change kSensorPhaseinRobotMap
		rightMotorMaster.setInverted(RobotMap.kMotorInvert);
		int absolutePositionRight = rightMotorMaster.getSensorCollection().getPulseWidthPosition();

		absolutePositionRight &= 0xFFF;
		if (RobotMap.kSensorPhase)
		absolutePositionRight *= -1;
		if (RobotMap.kMotorInvert) //need to mess with this
		absolutePositionRight *= -1;
		rightMotorMaster.setSelectedSensorPosition(absolutePositionRight, RobotMap.kPIDLoopIdx, RobotMap.kTimeoutMs);

		//Return True
		return true;
	}
	
	
	//Drive method that creates two tank drives with the left and right joysticks
	public void drive() {
//		
//		double targetPositionRotationsLeft =  RobotMap.kTimeoutMs * 4096; //need to do math in here
//		leftMotorFront.set(ControlMode.Position, targetPositionRotationsLeft);
//		double targetPositionRotationsRight =  RobotMap.kTimeoutMs * 4096; //need to do math in here
//		rightMotorRear.set(ControlMode.Position, targetPositionRotationsRight);
//		
		//controls
		if(RobotMap.DIRECTION_MULTIPLIER==1)
		{
			drive.tankDrive(Robot.oi.leftJoy.getY()*RobotMap.DIRECTION_MULTIPLIER, Robot.oi.rightJoy.getY()*RobotMap.DIRECTION_MULTIPLIER);
			//drive.tankDrive(Robot.oi.xbox.getY(Hand.kLeft)*RobotMap.DIRECTION_MULTIPLIER, Robot.oi.xbox.getY(Hand.kRight)*RobotMap.DIRECTION_MULTIPLIER);
			//SmartDashboard.putString("Direction multiplier: ", Integer.toString(RobotMap.DIRECTION_MULTIPLIER));			
			//SmartDashboard.putString("Left Joy Position: ", Double.toString(Robot.oi.leftJoy.getY()));
			//SmartDashboard.putString("Right Joy Position: ", Double.toString(Robot.oi.rightJoy.getY()));
		}
		else
		{
			drive.tankDrive(Robot.oi.rightJoy.getY()*RobotMap.DIRECTION_MULTIPLIER, Robot.oi.leftJoy.getY()*RobotMap.DIRECTION_MULTIPLIER);
			//SmartDashboard.putString("Direction multiplier: ", Integer.toString(RobotMap.DIRECTION_MULTIPLIER));			
			//SmartDashboard.putString("Left Joy Position: ", Double.toString(Robot.oi.leftJoy.getY()));
			//SmartDashboard.putString("Right Joy Position: ", Double.toString(Robot.oi.rightJoy.getY()));
			// drive.tankDrive(Robot.oi.xbox.getY(Hand.kRight)*RobotMap.DIRECTION_MULTIPLIER, Robot.oi.xbox.getY(Hand.kLeft)*RobotMap.DIRECTION_MULTIPLIER); 
		}
		
		
//		SmartDashboard.putNumber("Left Sensor position", leftMotorMaster.getSelectedSensorPosition(RobotMap.kPIDLoopIdx));
//		SmartDashboard.putNumber("Right Sensor position", rightMotorMaster.getSelectedSensorPosition(RobotMap.kPIDLoopIdx));
		SmartDashboard.putNumber("Left Output", leftMotorMaster.getMotorOutputPercent());
		SmartDashboard.putNumber("Right Output", rightMotorMaster.getMotorOutputPercent());
		
		drive.setSafetyEnabled(false);
		
		drive.setMaxOutput(0.8);
	}
	
	
	//Auto drive that inputs two doubles for the speeds of the motors
	public void autoDrive(double leftMotorSpeed, double rightMotorSpeed) {
				
//		pidOutput = new PIDOutput() {
//    		
//    		@Override
//    		public void pidWrite(double d) {
//    			drive.tankDrive(direction*leftMotorSpeed - d, direction*rightMotorSpeed + d);
//    		}
//    	};
//    	
//    	pid = new PIDController(0.01, 0.0, 0.0, Robot.oi.MainGyro, pidOutput);    	
//    	pid.setAbsoluteTolerance(RobotMap.ANGLE_TOLERANCE);
//    //	pid.setContinuous();
//    	pid.setSetpoint(angle);
//    	pid.enable();
		//other stuff
		
		drive.setSafetyEnabled(false);
		
		drive.setMaxOutput(0.8);
		
		drive.tankDrive(leftMotorSpeed, rightMotorSpeed*.95);
		
	}
	
    public boolean autoDistance() {
    	
//    	boolean thereYet = false;
// 
//    	//Check elapsed time
//    	if(autoStopTime<=timer.get())
//    	{
//    		
//    		//Too much time has elapsed.  Stop this command.
//    		pid.disable();
//    		thereYet = true;
//    		
//    	}
//    	else
//    	{
//    		
//    	
//    		//Robot.distanceTraveled = (Robot.oi.leftEncoder.getDistance() + Robot.oi.rightEncoder.getDistance()) / 2.0;
//    		//
//    		int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
//    		int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
//    		Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;
//    		
//    		SmartDashboard.putString("Left encoder start: ", Integer.toString(leftEncoderStart));
//    		SmartDashboard.putString("Right encoder start: ", Integer.toString(rightEncoderStart));    		
//    		SmartDashboard.putString("Left encoder position: ", Integer.toString(Robot.driveTrain.getLeftEncoderPosition()));
//    		SmartDashboard.putString("Right encoder position: ", Integer.toString(Robot.driveTrain.getRightEncoderPosition()));
//    		SmartDashboard.putString("Total rotations left: ", Integer.toString(totalRotationsLeft));
//    		SmartDashboard.putString("Total rotations right: ", Integer.toString(totalRotationsRight));
//			SmartDashboard.putString("Distance traveled: ", Double.toString(Robot.distanceTraveled));			
//    		
//			if (autoDistance <= Robot.distanceTraveled)
//    		{
//    			
//    			//Robot has reached its destination.  Stop this command
//    			pid.disable();
//    			thereYet = true;
//    			drive.tankDrive(0,0);
//    			
//    		}
//    	
//    	}
//    	
//    	return thereYet;
    	return true;
    }
	
	//A method that stops all motors
	public void autoStop() {
		
		drive.tankDrive(0, 0);
	}
	
	
	//Return the left encoder position
	public int getLeftEncoderPosition() {
		return leftMotorMaster.getSelectedSensorPosition(RobotMap.kPIDLoopIdx);
	}
	
	
	//Return the right encoder position
	public int getRightEncoderPosition() {
		return rightMotorMaster.getSelectedSensorPosition(RobotMap.kPIDLoopIdx);
	}
	
	
	//Reset encoder position to zero
//	public void resetEncoders() {
//		leftEncoderStart = leftMotorMaster.getSelectedSensorPosition(RobotMap.kPIDLoopIdx);
//		rightEncoderStart = rightMotorMaster.getSelectedSensorPosition(RobotMap.kPIDLoopIdx);
//	}

}

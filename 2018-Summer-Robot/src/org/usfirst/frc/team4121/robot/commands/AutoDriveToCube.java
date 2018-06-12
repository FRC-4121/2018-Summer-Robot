package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;
import org.usfirst.frc.team4121.robot.extraClasses.PIDControl;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoDriveToCube extends Command {

	double stopTime;
	double startTime;
	double cubeCorrection = 0;
	double speedMultiplier;
	double cubeHeight;

	public Timer timer = new Timer();


	public AutoDriveToCube(double timeout) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.driveTrain);
		requires(Robot.end);
		stopTime = timeout;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.start();
		startTime= timer.get();
		
//		cubeHeight = Robot.cubeHeight.getDouble(0);
//		
//		if(cubeHeight == 10.0) //set the height of the elevator based on the cubeHeight from the camera
//			
//			Robot.elevator.goToHome();
//			
//		else 
//			
//			Robot.elevator.runToPyramid();
		
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		if(Robot.cubePercentWidth.getDouble(0)<20)
		{
			speedMultiplier = -1; //might be + on comp bot
		}
		else
		{
			speedMultiplier = -.75; //might be + on comp bot
		}

		if(Math.abs(Robot.cubeOffset.getDouble(0)/160)> .04)
		{
			cubeCorrection = RobotMap.kP_Cube*Robot.cubeOffset.getDouble(0)/160;
		}
		else
		{
			cubeCorrection = 0;
		}

		SmartDashboard.putNumber("Cube Correction", cubeCorrection);
		Robot.driveTrain.autoDrive(speedMultiplier*RobotMap.AUTO_DRIVE_SPEED - cubeCorrection, speedMultiplier*RobotMap.AUTO_DRIVE_SPEED + cubeCorrection); //flip signs on comp bot


	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {

		boolean thereYet = false;
		if(!RobotMap.ABORT_CUBE)
		{
			//Check elapsed time
			if(Robot.cubePercentWidth.getDouble(0) > 80) //when cube takes up majority of screen you are done need to change value
			{

				//Too much time has elapsed.  Stop this command.
				thereYet = true;

			}
			else if (stopTime<=timer.get()-startTime)
			{

				//Too much time has elapsed.  Stop this command.
				thereYet = true;

			}
			else if(Robot.cubePercentWidth.getDouble(0)== 0 && 1<=timer.get()-startTime)
			{
				thereYet = true;
			}
			SmartDashboard.putBoolean("There Yet", thereYet);
		}
		else
		{
			thereYet = true;
		}
		
		if (thereYet) {
			Robot.driveTrain.autoDrive(0, 0);
		}
		
		
		return thereYet;



	}

	// Called once after isFinished returns true
	protected void end() {
		if(!RobotMap.ABORT_CUBE)
		{
			Robot.end.autoTakeInCube();
		}
		
		if(RobotMap.ABORT_CUBE)
		{
			RobotMap.ABORT_CUBE = false;
		}
		

	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}

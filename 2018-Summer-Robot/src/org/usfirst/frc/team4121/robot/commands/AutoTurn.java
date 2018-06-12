package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;
import org.usfirst.frc.team4121.robot.extraClasses.PIDControl;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoTurn extends Command {

	double targetAngle;
	double startTime;
	double stopTime;
	double angleError;
	double angleCorrection;
	double motorOutput;

	PIDControl pidControl;

	private Timer timer = new Timer();



	//Class constructor
	public AutoTurn(double angle, double time) { //change in smartdashboard

		targetAngle = angle;
		stopTime = time;

		requires(Robot.driveTrain);

		//set up PID controller
		pidControl = new PIDControl(RobotMap.kP_Turn, RobotMap.kI_Turn, RobotMap.kD_Turn);

	}


	// Called just before this Command runs the first time
	protected void initialize() {

		timer.start();
		startTime = timer.get();
		angleError = 0;
		angleCorrection = 0;

	}


	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		angleCorrection = pidControl.Run(Robot.driveAngle.getDouble(0), targetAngle);
		motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;
		Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

	}


	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {

		//Declare return flag
		boolean thereYet = false;

		//Check elapsed time
		if(stopTime<=timer.get()-startTime)
		{

			//Too much time has elapsed.  Stop this command
			thereYet = true;

		}
		else
		{

			angleError = Robot.driveAngle.getDouble(0) - targetAngle;
			if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE)
			{
				thereYet = true;

			}

		}


		//Return the flag
		return thereYet;

	}


	// Called once after isFinished returns true
	protected void end() {

		Robot.driveTrain.autoStop(); //maybe don't need depends on robot

	}


	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {

		Robot.driveTrain.autoStop(); //maybe don't need depends on robot
		
	}
	
}

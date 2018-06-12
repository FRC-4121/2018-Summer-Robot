package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class EjectCubeCommand extends Command {

	private double ejectSpeed;
	private double startTime;
	private double stopTime = 0.5;
	private Timer wheelTimer = new Timer();


	public EjectCubeCommand(double speed) {

		//Require end effector subsystem
		requires(Robot.end);

		ejectSpeed = speed;

	}


	// Called just before this Command runs the first time
	protected void initialize() {

		wheelTimer.start();
		startTime = wheelTimer.get();

	}


	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		Robot.end.endeffector(ejectSpeed);

	}


	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {

		boolean stopYet = false;

		if(stopTime <= (wheelTimer.get() - startTime))
		{
			stopYet = true;
		}

		return stopYet;

	}


	// Called once after isFinished returns true
	protected void end() {

		Robot.end.endeffector(0);

	}


	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {

		Robot.end.endeffector(0);
	
	}
	
}

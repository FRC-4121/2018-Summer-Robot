package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class toScaleAutoCommand extends Command {

	private Timer liftTimer;
	private double startTime;
	private char robotSide;

	public toScaleAutoCommand(char side) {

		requires(Robot.elevator);
		//liftTimer = new Timer();
		robotSide = side;


	}

	// Called just before this Command runs the first time
	protected void initialize() {

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		if (robotSide == 'N' || robotSide == RobotMap.AUTO_SCALE_POSITION) {

			Robot.elevator.runToScale();

		}

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return true;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
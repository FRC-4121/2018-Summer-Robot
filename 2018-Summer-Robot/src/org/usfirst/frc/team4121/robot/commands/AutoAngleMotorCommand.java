package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoAngleMotorCommand extends Command {
	
	double time;
	double startTime;
	Timer timer = new Timer();
	
	public AutoAngleMotorCommand() {
		requires(Robot.end);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		startTime = timer.get();
    	time = 2;//test value
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	
		Robot.end.angleMotor(RobotMap.ANGLE_END_EFFECTOR_SPEED);
		
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (timer.get() - startTime <= time) 

			return true;

		else 
			return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}

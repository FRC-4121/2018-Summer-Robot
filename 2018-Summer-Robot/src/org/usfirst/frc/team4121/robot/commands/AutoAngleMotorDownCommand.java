package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoAngleMotorDownCommand extends Command {
	
	double time;
	double startTime;
	Timer timer = new Timer();
	
	public AutoAngleMotorDownCommand() {
		requires(Robot.end);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.start();
		startTime = timer.get();//I want to use a limit switch here instead of timer.
    	time = 1;//test value
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	
		Robot.end.angleMotor(RobotMap.DOWN_ANGLE_END_EFFECTOR_SPEED);
		
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (timer.get() - startTime >= time) 

			return true;

		else 
			return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		
		Robot.end.angleMotor(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}

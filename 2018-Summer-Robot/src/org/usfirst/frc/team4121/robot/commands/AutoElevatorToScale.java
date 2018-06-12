package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoElevatorToScale extends Command {

	private Timer liftTimer;
	private double startTime;
	private double stopTime;
	
    public AutoElevatorToScale(double time) {
    	
    	requires(Robot.elevator);
    	
    	stopTime = time;

    }

    
    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	liftTimer = new Timer();
    	liftTimer.start();
    	startTime = liftTimer.get();
    	
    }

    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	Robot.elevator.runToScale();

    }

    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	boolean thereYet = false;
    	
    	if (stopTime <= liftTimer.get() - startTime) {
    		thereYet = true;
    	}
    	
        return thereYet;
    }

    
    // Called once after isFinished returns true
    protected void end() {
    }

    
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

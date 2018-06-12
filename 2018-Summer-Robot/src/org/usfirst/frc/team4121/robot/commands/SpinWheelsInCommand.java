package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SpinWheelsInCommand extends Command {
	
	private double startTime;
	private double stopTime = 1.5;
	private Timer wheelTimer = new Timer();

    public SpinWheelsInCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.end);
    	
    }

    
    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	wheelTimer.start();
    	startTime = wheelTimer.get();
    	
    }

    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	Robot.end.endeffector(.85);
    	
    }

    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	boolean stopYet = false;
    	
     	if(stopTime <= (wheelTimer.get() - startTime)) //change time, once reaches five seconds command should stop
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
    }
}

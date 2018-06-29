package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShiftGearCommand extends Command {

    public ShiftGearCommand() {
       
    	requires(Robot.shifter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	if(Robot.shifter.shifterSolenoid.get() == DoubleSolenoid.Value.kForward) {
    		
    		Robot.shifter.shiftUp();
    	
    	} else if(Robot.shifter.shifterSolenoid.get() == DoubleSolenoid.Value.kReverse) {
    		
    		Robot.shifter.shiftDown();
    	} 
    	
    	//Robot.shifter.gearPosition();
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

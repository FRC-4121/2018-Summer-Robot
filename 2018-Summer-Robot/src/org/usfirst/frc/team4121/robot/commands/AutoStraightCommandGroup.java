package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoStraightCommandGroup extends CommandGroup {

    public AutoStraightCommandGroup() {
       
    	
    	addSequential(new ShiftDownCommand());
    	addSequential(new AutoDrive(100, -1, 0, 15)); 
    	addSequential(new BeginningMatchCommandGroup());
    	
    }
    
}

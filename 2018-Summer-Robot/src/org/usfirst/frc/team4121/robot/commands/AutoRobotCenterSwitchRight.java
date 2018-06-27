package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRobotCenterSwitchRight extends CommandGroup {

    public AutoRobotCenterSwitchRight() {
      
    	addSequential(new ShiftDownCommand());
    	addSequential(new BeginningMatchCommandGroup());
    	addSequential(new AutoDrive(40, -1, 0, 3)); 
    	addSequential(new AutoTurn (90, 1));   	
    	addSequential(new AutoDrive(55, -1, 90, 3));
    	addSequential(new AutoTurn (0, 1));   
    	addSequential(new ElevatorToSwitchCommand());
    	addSequential(new AutoDrive(70, -1, 0, 3));
    	addSequential (new EjectCubeCommand(-.58));
    	
    }
}

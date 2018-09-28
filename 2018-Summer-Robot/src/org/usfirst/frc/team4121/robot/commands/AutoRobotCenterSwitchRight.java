package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRobotCenterSwitchRight extends CommandGroup {

    public AutoRobotCenterSwitchRight() {
      
    	addSequential(new BeginningMatchCommandGroup());
    	addSequential(new AutoDrive(15, -1, 0, 3)); 
    	addSequential(new AutoTurn (90, 1)); 
    	addSequential(new ElevatorToSwitchCommand());
    	addSequential(new AutoDrive(10, -1, 90, 3));
    	addSequential(new AutoTurn (0, 1));   
    	addSequential(new AutoDrive(20, -1, 0, 3));
    	addSequential (new EjectCubeCommand(-.58));
    	
    }
}

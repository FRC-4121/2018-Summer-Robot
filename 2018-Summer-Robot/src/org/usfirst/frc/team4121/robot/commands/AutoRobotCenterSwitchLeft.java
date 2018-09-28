package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRobotCenterSwitchLeft extends CommandGroup {

    public AutoRobotCenterSwitchLeft() {
    	
    	addSequential(new BeginningMatchCommandGroup());
    	addSequential(new AutoDrive(15, -1, 0, 3)); 
    	addSequential(new AutoTurn (-90, 1)); 
    	addSequential(new ElevatorToSwitchCommand());
    	addSequential(new AutoDrive(40, -1, -90, 4));
    	addSequential(new AutoTurn (0, 1));   
    	addSequential(new AutoDrive(12, -1, 0, 3));
    	addSequential (new EjectCubeCommand(-.58));
    }
}

package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRobotLeftSwitchLeft1Cube extends CommandGroup {

    public AutoRobotLeftSwitchLeft1Cube() {

    	addSequential(new BeginningMatchCommandGroup());
    	addSequential(new ElevatorToSwitchCommand());
    	addSequential(new AutoDrive(95, -1, 0, 7)); //drive to switch used to
    	addSequential(new AutoTurn(45, 1.5)); //used to be 98
    	addSequential(new EjectCubeCommand(-1.5));
    
    }
}

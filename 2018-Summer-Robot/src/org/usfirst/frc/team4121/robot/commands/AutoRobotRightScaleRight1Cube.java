package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRobotRightScaleRight1Cube extends CommandGroup {

    public AutoRobotRightScaleRight1Cube() {

    	addSequential(new BeginningMatchCommandGroup());
    	addSequential(new ElevatorToSwitchCommand());
    	addSequential(new AutoDrive(150, -1, 0, 10)); 
    	addSequential(new ElevatorToScaleCommand());
    	addSequential(new AutoDrive(100, -1, 0, 10));
    	addSequential(new AutoTurn(-45, 1.5));
    	addSequential(new EjectCubeCommand(-0.75));
    
    }
}

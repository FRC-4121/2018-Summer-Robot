package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRobotRightScaleRight2Cubes extends CommandGroup {

    public AutoRobotRightScaleRight2Cubes() {
    	
    	addSequential(new BeginningMatchCommandGroup());
    	addSequential(new ShiftDownCommand());
    	addSequential(new ElevatorToSwitchCommand());
    	addSequential(new AutoDrive(150, -1, 0, 5)); 
    	addSequential(new ElevatorToScaleCommand());
    	addSequential(new AutoDrive(100, -1, 0, 5));
    	addSequential(new AutoTurn(-45, 1.5));
    	addSequential(new EjectCubeCommand(-0.75));
    	//addSequential(new AutoDrive(10, 1, 135, 3));
    	addSequential(new AutoElevatorToHome(3.0));
    	addSequential(new AutoTurn(-145, 1.5));
    	addSequential(new AutoPickUpCubeCommandGroup());
    	addSequential(new AutoDrive(10, 1, 0, 2)); 
    	
    }
}

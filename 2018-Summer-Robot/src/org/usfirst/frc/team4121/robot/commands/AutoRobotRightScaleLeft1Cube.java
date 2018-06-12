package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRobotRightScaleLeft1Cube extends CommandGroup {

    public AutoRobotRightScaleLeft1Cube() {

    	addSequential(new ShiftUpCommand());
    	addSequential(new BeginningMatchCommandGroup());  	
    	addSequential(new AutoDrive(197, -1, 0, 10)); //drive to switch  
    	addSequential(new ShiftDownCommand());
    	addSequential(new AutoTurn (-90, 1));   	
    	addSequential(new AutoDrive(107, -1, -90, 10));
    	addSequential(new ElevatorToSwitchCommand());   	
    	addSequential(new AutoDrive(80, -1, -90, 10));   	
    	addSequential(new AutoTurn (0, 1));   	
    	addSequential(new AutoDrive(8, -1, 0, 5));    	
    //	addSequential(new AutoElevatorToScale(4.0));
    	addSequential(new ElevatorToScaleCommand());
    	addSequential(new AutoTurn (40, 2.0)); 
    	addSequential(new EjectCubeCommand(-0.65));
    }
}

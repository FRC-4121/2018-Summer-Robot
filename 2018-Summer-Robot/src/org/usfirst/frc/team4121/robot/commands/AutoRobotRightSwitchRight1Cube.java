package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoRobotRightSwitchRight1Cube extends CommandGroup {

    public AutoRobotRightSwitchRight1Cube() {

    	addSequential(new BeginningMatchCommandGroup());
    	addSequential(new ElevatorToSwitchCommand());
    	addSequential(new AutoDrive(60, -1, 0, 7)); //drive to switch
    	addSequential(new AutoTurn(-45, 1.5));
    	addSequential(new EjectCubeCommandGroup());
    
    }
}

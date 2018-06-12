package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TakeInCubeCommandGroup extends CommandGroup {

    public TakeInCubeCommandGroup() {
       
    	addSequential(new ClosedArmsCommand());
    	addSequential(new SpinWheelsInCommand());
    	
    
    	
       //addSequential(new ElevatorToSwitchCommand());

    }
}

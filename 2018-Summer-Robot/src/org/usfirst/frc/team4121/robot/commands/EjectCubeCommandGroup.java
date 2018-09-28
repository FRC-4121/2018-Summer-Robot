package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class EjectCubeCommandGroup extends CommandGroup {

    public EjectCubeCommandGroup() {

    	addSequential(new SpinWheelsOutCommand());
    	addSequential(new OpenArmsCommand());
    }
}

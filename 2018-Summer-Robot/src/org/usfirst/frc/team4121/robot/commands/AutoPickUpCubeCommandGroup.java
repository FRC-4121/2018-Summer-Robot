package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoPickUpCubeCommandGroup extends CommandGroup {

	public AutoPickUpCubeCommandGroup() {

		addSequential(new OpenArmsCommand());
		addSequential(new AutoDriveToCube(20));
		
	}
}

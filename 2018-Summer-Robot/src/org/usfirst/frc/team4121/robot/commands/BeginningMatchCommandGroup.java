package org.usfirst.frc.team4121.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**This command group will make sure the arms are grabbing the cube
 * at the beginning of the match and also opens the servos.
 */

public class BeginningMatchCommandGroup extends CommandGroup {

    public BeginningMatchCommandGroup() {

    	addSequential(new ClosedArmsCommand());
    	
        addSequential(new AutoAngleMotorDownCommand());
        
    }
}

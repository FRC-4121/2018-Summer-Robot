package org.usfirst.frc.team4121.robot.subsystems;

import org.usfirst.frc.team4121.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *	Shifter subsystem
 */
public class ShifterSubsystem extends Subsystem {
	
	//Initializes the compressor
	Compressor compressor = new Compressor(RobotMap.COMPRESSOR);
	
	//initializes the double solenoid
	public DoubleSolenoid shifterSolenoid = new DoubleSolenoid(2,3);
	
    public void initDefaultCommand() {
    }
    
    //Changes the solenoid to kReverse or slow mode
    public void shiftUp() {
    	shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
    	SmartDashboard.putString("Shift Up", "true");
    }
    
    //Changes the solenoid to kForward or fast mode
    public void shiftDown() {
    	shifterSolenoid.set(DoubleSolenoid.Value.kForward);
    	SmartDashboard.putString("Shift Down", "true");
    }
    
    //Makes the gear position default to kForward or fast
    //Unused right now
    public void defaultGearPosition() {
    	shifterSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    
    //Returns what gear position we are in (used in the Smart Dashboard)
    public String gearPosition() {
    	if(shifterSolenoid.get() == DoubleSolenoid.Value.kForward) {
    		return "Fast";
    	}
    	else if(shifterSolenoid.get() == DoubleSolenoid.Value.kReverse) {
    		return "Slow";
    	}
    	return "Neither";
    }
}


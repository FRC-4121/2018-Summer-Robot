package org.usfirst.frc.team4121.robot.subsystems;

import org.usfirst.frc.team4121.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class EndEffector extends Subsystem {

	public WPI_TalonSRX endmotor1 = new WPI_TalonSRX(RobotMap.ENDMOTOR1);
	public WPI_TalonSRX endmotor2 = new WPI_TalonSRX(RobotMap.ENDMOTOR2);
	public WPI_TalonSRX anglemotor = new WPI_TalonSRX(RobotMap.ANGLEMOTOR);
	public Servo leftServo = new Servo(8);
	public Servo rightServo = new Servo(9);
	
	private Timer wheelTimer = new Timer();
	private double startTime;
	
	
	
	//solenoid setup
	Compressor compressor = new Compressor(RobotMap.COMPRESSOR); //change this value
	public DoubleSolenoid armSolenoid = new DoubleSolenoid(0,1);
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    //sets wheels spinning
    public void endeffector(double endspeed) {
    	endmotor1.set(-endspeed);
    	endmotor2.set(endspeed);//for competition this should be negative
    	
    }
    
    public void angleMotor(double turnspeed) {
    	
    	anglemotor.set(turnspeed);
    	
    }
    
    //for auto take in cube command
    public void autoTakeInCube()
    {
    	wheelTimer.start();
    	startTime= wheelTimer.get();
    	
    	closeArms();
    	while(wheelTimer.get()-startTime < 1.5)
    	{
    		endeffector(.75);
    	}
    	endeffector(0);
    	
    	
    }
    
    //stops wheels with limit switch 
    public void stopWithLimitSwitch(){
    	endmotor1.set(.5);
    	endmotor2.set(.5);

    }
    public void openArms()
    {
    	armSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
    public void closeArms()
    {
    	//if(!Robot.oi.limitSwitchEnd.get())
    	{
        	armSolenoid.set(DoubleSolenoid.Value.kForward);
    	}
    	
    }
    
    public void openServos() {
    	leftServo.set(.5);//.5
    	rightServo.set(.5);
    
    }
    
    public void closeServos() {
    	leftServo.set(0);
    	rightServo.set(.9);
    }
  
}


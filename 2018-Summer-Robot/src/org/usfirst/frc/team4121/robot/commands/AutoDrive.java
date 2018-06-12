package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;
import org.usfirst.frc.team4121.robot.extraClasses.PIDControl;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDrive extends Command {
	
	double targetDistance; 
	double direction; //-1 = Reverse, +1 = Forward (reverse is for gear forward is for shooting)
	double targetAngle;  //drive angle
	double stopTime;  //timeout time
	double angleCorrection, angleError;
	double startTime;
	
	PIDControl pidControl;

	public Timer timer = new Timer();
	
	public int leftEncoderStart;
	public int rightEncoderStart;
	
	//Class constructor
    public AutoDrive(double dis, double dir, double ang, double time) { //intakes distance, direction, angle, and stop time
    	
    	requires(Robot.driveTrain);
    	
    	//Set local variables
    	targetDistance = dis;
    	direction = dir;
    	targetAngle = ang;
    	stopTime = time;
    	    	
    	//Set up PID control
    	pidControl = new PIDControl(RobotMap.kP_Straight, RobotMap.kI_Straight, RobotMap.kD_Straight);
    	
    	//pidOutput = new PIDOutput() {
//	
//    		@Override
//    		public void pidWrite(double d) {
//    			Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED - d, direction*RobotMap.AUTO_DRIVE_SPEED + d);
//    		}
//    	};
//    	
//    	pid = new PIDController(0.045, 0.0, 0.0, Robot.oi.MainGyro, pidOutput);    	
//    	pid.setAbsoluteTolerance(RobotMap.ANGLE_TOLERANCE);
//    	pid.setContinuous();
//    	pid.setSetpoint(angle);
    	
    }

    
    // Called just before this Command runs the first time
    protected void initialize() {
    	
        Robot.distanceTraveled = 0.0;
        timer.start();
        startTime= timer.get();
        leftEncoderStart = Robot.driveTrain.getLeftEncoderPosition();
        rightEncoderStart = Robot.driveTrain.getRightEncoderPosition();
        angleCorrection = 0;
        angleError = 0;
        
    }

    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
//    	angleCorrection = pidControl.Run(Robot.oi.MainGyro.getAngle(), targetAngle);
    	angleError = Robot.driveAngle.getDouble(0)-targetAngle;
    	angleCorrection = RobotMap.kP_Straight*angleError;
    	Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);    	    	
		SmartDashboard.putString("Angle Correction", Double.toString(angleCorrection));
		SmartDashboard.putString("Angle Error", Double.toString(angleError));

    }

    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	boolean thereYet = false;
 
    	//Check elapsed time
    	if(stopTime<=timer.get()-startTime)
    	{
    		
    		//Too much time has elapsed.  Stop this command.
    		thereYet = true;
    		
    	}
    	else
    	{
    		
    	
    		//Robot.distanceTraveled = (Robot.oi.leftEncoder.getDistance() + Robot.oi.rightEncoder.getDistance()) / 2.0;
    		//
    		int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
    		int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
    		Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;
    		
//    		SmartDashboard.putString("Left encoder start: ", Integer.toString(leftEncoderStart));
//    		SmartDashboard.putString("Right encoder start: ", Integer.toString(rightEncoderStart));    		
//    		SmartDashboard.putString("Left encoder position: ", Integer.toString(Robot.driveTrain.getLeftEncoderPosition()));
//    		SmartDashboard.putString("Right encoder position: ", Integer.toString(Robot.driveTrain.getRightEncoderPosition()));
//    		SmartDashboard.putString("Total rotations left: ", Integer.toString(totalRotationsLeft));
//    		SmartDashboard.putString("Total rotations right: ", Integer.toString(totalRotationsRight));
//			SmartDashboard.putString("Distance traveled: ", Double.toString(Robot.distanceTraveled));	
//			SmartDashboard.putString("Drive Angle:", Double.toString(Robot.oi.MainGyro.getAngle()));
//			SmartDashboard.putString("Angle Error", Double.toString(angleError));

    		
			if (targetDistance <= Robot.distanceTraveled)
    		{
    			
    			//Robot has reached its destination.  Stop this command
    			thereYet = true;
    			
    		}
    	
    	}
    	
    	return thereYet;

    	

    }

    
    // Called once after isFinished returns true
    protected void end() {
    	
    	Robot.driveTrain.autoStop(); //maybe don't need depends on robot
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    
    protected void interrupted() {
    	
    	//Robot.driveTrain.pid.disable();
    	
    }
    
}

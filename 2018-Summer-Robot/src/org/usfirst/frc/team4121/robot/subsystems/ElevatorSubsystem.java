package org.usfirst.frc.team4121.robot.subsystems;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ElevatorSubsystem extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.


//	public WPI_TalonSRX m_motor = new WPI_TalonSRX (RobotMap.ELEVATOR_MOTOR_MASTER);
//	public WPI_TalonSRX m_motor2_follower = new WPI_TalonSRX (RobotMap.ELEVATOR_MOTOR_SLAVE);  old code
	

	public TalonSRX m_motor = new TalonSRX (RobotMap.ELEVATOR_MOTOR_MASTER);
	public TalonSRX m_motor2_follower = new TalonSRX (RobotMap.ELEVATOR_MOTOR_SLAVE);

	public double targetPos = 0;
	public double oldTargetPos;
	public double inchesPerRev;
	public int encoderPulsesPerOutputRev; // number of motor encoders pulses per encoder output revolution
	
	public double cruiseVelocityUp ;
	public double cruiseVelocityDn ;
	public double accelUp ;
	public double accelDn ;
	public double targetPosSwitch ;
	public double targetPosScale ;
	public double targetPosPyramid;
	public double bumpUp ;
	public double bumpDn;


	private boolean initMotors = initElevatorControls();


	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
	}

	
	public boolean initElevatorControls()
	{
		
		//elevator code
		// * inches per rev = Drum Dia * PI * motor-drum sprocket ratio
		// */
		inchesPerRev = RobotMap.kWinchDrumDia * 3.1415 * RobotMap.kMotorSprocketTeeth /
				RobotMap.kDrumShaftSprocketTeeth;
		inchesPerRev = inchesPerRev / 2.0;
		encoderPulsesPerOutputRev = RobotMap.kEncoderPPR * RobotMap.kEncoderRatio;

		
		/* first choose the sensor */
		m_motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.kTimeoutMs);
		m_motor.setSensorPhase(true); //change back to true  for comp. bot

		
		/* Set relevant frame periods to be at least as fast as periodic rate*/
		m_motor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, RobotMap.kTimeoutMs);
		m_motor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, RobotMap.kTimeoutMs);

		
		/* set the peak, nominal outputs */
		m_motor.configNominalOutputForward(0, RobotMap.kTimeoutMs);
		m_motor.configNominalOutputReverse(0, RobotMap.kTimeoutMs);
		m_motor.configPeakOutputForward(1, RobotMap.kTimeoutMs);
		m_motor.configPeakOutputReverse(-1, RobotMap.kTimeoutMs);

		
		m_motor2_follower.configNominalOutputForward(0, RobotMap.kTimeoutMs);
		m_motor2_follower.configNominalOutputReverse(0, RobotMap.kTimeoutMs);
		m_motor2_follower.configPeakOutputForward(1, RobotMap.kTimeoutMs);
		m_motor2_follower.configPeakOutputReverse(-1, RobotMap.kTimeoutMs);

		
		/* set closed loop gains in slot0 */
		/* these will need to be tuned once the final masses are known */
		m_motor.config_kF(RobotMap.kPIDLoopIdx, RobotMap.kf, RobotMap.kTimeoutMs);
		m_motor.config_kP(RobotMap.kPIDLoopIdx, RobotMap.kp, RobotMap.kTimeoutMs);
		m_motor.config_kI(RobotMap.kPIDLoopIdx, RobotMap.ki, RobotMap.kTimeoutMs);
		m_motor.config_kD(RobotMap.kPIDLoopIdx, RobotMap.kd, RobotMap.kTimeoutMs);

		
		/* set acceleration and vcruise velocity - see documentation */
		/* velocity and acceleration has to be in encoder units
		 * rev/s = inches per sec / inches per revolution
		 * velocity is encoder pules per 100ms = rev/s*PPR*GearRatio/10 ;
		 */
//		m_motor.configMotionCruiseVelocity((int) RobotMap.kCruiseSpeedUp / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);
//		m_motor.configMotionAcceleration((int) RobotMap.kAccelerationUp / (int) inchesPerRev * encoderPulsesPerOutputRev / 10, RobotMap.kTimeoutMs);

		
		/* set motor2 follower */
		m_motor2_follower.set(ControlMode.Follower, RobotMap.ELEVATOR_MOTOR_MASTER);
		
		
		/*
		 *  Set motion magic values for scale and switch
		 */
		 
		/* set acceleration and vcruise velocity - see documentation */
		/* velocity and acceleration has to be in encoder units
		/* rev/s = inches per sec / inches per revolution
		/* velocity is encoder pules per 100ms = rev/s*PPR*GearRatio/10 ;
		*/		
		cruiseVelocityUp = RobotMap.kCruiseSpeedUp/inchesPerRev*encoderPulsesPerOutputRev/10 ;
		cruiseVelocityDn = RobotMap.kCruiseSpeedDown/inchesPerRev*encoderPulsesPerOutputRev/10 ;
		accelUp = RobotMap.kAccelerationUp/inchesPerRev*encoderPulsesPerOutputRev/10 ;
		accelDn = RobotMap.kAccelerationDown/inchesPerRev*encoderPulsesPerOutputRev/10 ;
		targetPosSwitch = RobotMap.dPosSwitch/inchesPerRev*4096/RobotMap.dFudgeFactor;
		targetPosScale = RobotMap.dPosScale/inchesPerRev*4096/RobotMap.dFudgeFactor ;
		targetPosPyramid = RobotMap.dPosPyramid/inchesPerRev*4096/RobotMap.dFudgeFactor;
		bumpUp = RobotMap.dPosBumpUp/inchesPerRev*4096/RobotMap.dFudgeFactor ;
		bumpDn = RobotMap.dPosBumpDown/inchesPerRev*4096/RobotMap.dFudgeFactor ;
		

		
		/* zero the sensor */
		m_motor.setSelectedSensorPosition(0, RobotMap.kPIDLoopIdx, RobotMap.kTimeoutMs);

		
		/* set current limit */
		m_motor.configPeakCurrentLimit(RobotMap.kPeakMotorCurrent, 100) ;
		m_motor.configContinuousCurrentLimit(RobotMap.kMaxMotorCurrent, 100) ;
		m_motor.configPeakCurrentDuration(100, 0 ) ;
		m_motor.enableCurrentLimit(true);
		m_motor2_follower.configPeakCurrentLimit(RobotMap.kPeakMotorCurrent, 100) ;
		m_motor2_follower.configContinuousCurrentLimit(RobotMap.kMaxMotorCurrent, 100) ;
		m_motor2_follower.configPeakCurrentDuration(100, 0 ) ;
		m_motor2_follower.enableCurrentLimit(true);

		
		//Return true
		return true;
		
	}

	
	public void runToScale() // run to scale height
	{
		/* go to scale height */
		oldTargetPos = targetPos;
		targetPos = targetPosScale;
		if (targetPos > oldTargetPos) {
			m_motor.configMotionCruiseVelocity((int) cruiseVelocityUp , RobotMap.kTimeoutMs);
			m_motor.configMotionAcceleration((int) accelUp, RobotMap.kTimeoutMs);
			m_motor.set(ControlMode.MotionMagic, targetPos);
		} 
		else {
			m_motor.configMotionCruiseVelocity((int) cruiseVelocityDn, RobotMap.kTimeoutMs);
			m_motor.configMotionAcceleration((int) accelDn, RobotMap.kTimeoutMs);
			m_motor.set(ControlMode.MotionMagic, targetPos);
		}
	}



	public void runToSwitch() // run to switch height
	{
		oldTargetPos = targetPos;
		targetPos = targetPosSwitch;
		if (targetPos > oldTargetPos) {
			m_motor.configMotionCruiseVelocity((int) cruiseVelocityUp, RobotMap.kTimeoutMs);
			m_motor.configMotionAcceleration((int) accelUp, RobotMap.kTimeoutMs);
			m_motor.set(ControlMode.MotionMagic, targetPos);
		} 
		else {
			m_motor.configMotionCruiseVelocity((int) cruiseVelocityDn, RobotMap.kTimeoutMs);
			m_motor.configMotionAcceleration((int) accelDn, RobotMap.kTimeoutMs);
			m_motor.set(ControlMode.MotionMagic, targetPos);
		}
		
	}
	
/* new functions here - bump up and bump down */

	public void bumpUp() // bump up bump up distance
	{
		targetPos += bumpUp ;
		if(targetPos > targetPosScale) //if trying to go outside of max height range, will just run to scale
		{
			targetPos = targetPosScale;
		}
		
		m_motor.configMotionCruiseVelocity((int) cruiseVelocityUp, RobotMap.kTimeoutMs);
		m_motor.configMotionAcceleration((int) accelUp, RobotMap.kTimeoutMs);
		m_motor.set(ControlMode.MotionMagic, targetPos);
		
	}

	
	public void bumpDown() // bump down height
	{
		targetPos += bumpDn ;
		if(targetPos < 0) //change 12 to match bumpDown value
		{
			targetPos = 0;
		}
		
		m_motor.configMotionCruiseVelocity((int) cruiseVelocityDn, RobotMap.kTimeoutMs);
		m_motor.configMotionAcceleration((int) accelDn, RobotMap.kTimeoutMs);
		m_motor.set(ControlMode.MotionMagic, targetPos);
		
	}

	
	public void goToHome() // go to home
	{
		targetPos = 0;
		m_motor.configMotionCruiseVelocity((int) cruiseVelocityDn, RobotMap.kTimeoutMs);
		m_motor.configMotionAcceleration((int) accelDn, RobotMap.kTimeoutMs);
		m_motor.set(ControlMode.MotionMagic, targetPos);
	}
	
	public void runToPyramid()
	{
		oldTargetPos = targetPos;
		targetPos = targetPosPyramid;
		if (targetPos > oldTargetPos) {
			m_motor.configMotionCruiseVelocity((int) cruiseVelocityUp , RobotMap.kTimeoutMs);
			m_motor.configMotionAcceleration((int) accelUp, RobotMap.kTimeoutMs);
			m_motor.set(ControlMode.MotionMagic, targetPos);
		} 
		else {
			m_motor.configMotionCruiseVelocity((int) cruiseVelocityDn, RobotMap.kTimeoutMs);
			m_motor.configMotionAcceleration((int) accelDn, RobotMap.kTimeoutMs);
			m_motor.set(ControlMode.MotionMagic, targetPos);
		}
	}



}


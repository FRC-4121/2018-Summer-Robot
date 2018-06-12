package org.usfirst.frc.team4121.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 * @author Hen "traitor" Bayden thanks ben for showing up 
 */
public class RobotMap {

	//Motor Controller Constants - Practice Robot
//	public static final int LEFT_MOTOR_MASTER = 0;
//	public static final int LEFT_MOTOR_SLAVE = 15;
//	public static final int RIGHT_MOTOR_MASTER = 4;
//	public static final int RIGHT_MOTOR_SLAVE = 3;
//	public static final int CLIMBER1 = 2;
//	public static final int ENDMOTOR1 = 8;
//	public static final int ENDMOTOR2 = 1;
//	public static final int ELEVATOR_MOTOR_MASTER = 6;
//	public static final int ELEVATOR_MOTOR_SLAVE = 7;
//	public static final int ANGLEMOTOR = 9;

//	Motor Controller Constants - Competition Robots
	public static final int LEFT_MOTOR_MASTER = 7;
	public static final int LEFT_MOTOR_SLAVE = 15;
	public static final int RIGHT_MOTOR_MASTER = 4;
	public static final int RIGHT_MOTOR_SLAVE = 1;
	public static final int CLIMBER1 = 8;
	public static final int ENDMOTOR1 = 6;
	public static final int ENDMOTOR2 = 3; 
	public static final int ELEVATOR_MOTOR_MASTER = 2;
	public static final int ELEVATOR_MOTOR_SLAVE = 0;
	public static final int ANGLEMOTOR = 9; //need to change value

	//Motor Speeds
	public static final double END_EFFECTOR_SPEED = .5;
	public static final double ANGLE_END_EFFECTOR_SPEED = -.65;//this will angle the arm up because the polarity of the Talon is reversed.  check on competition bot.
	public static final double DOWN_ANGLE_END_EFFECTOR_SPEED = .5; //this angles down (see above). may need to change for competition bot.
	public static final double DRIVE_SPEED = 0.8;
	public static double AUTO_DRIVE_SPEED = .95;
	public static double AUTO_TURN_SPEED = 0.3;
	public static double CLIMBER_SPEED = -1.0; //should be negative
	public static double CLIMBER_REVERSE_SPEED = 1.0; //should be positive

	//Miscellaneous
	public static int DIRECTION_MULTIPLIER = 1;
	public static final int COMPRESSOR = 0;
	public static double STRAIGHT_ANGLE_TOLERANCE = .01;
	public static double TURN_ANGLE_TOLERANCE = .001;
	public static char AUTO_SWITCH_POSITION;
	public static char AUTO_SCALE_POSITION;
	public static String AUTO_SWITCH_AND_SCALE;
	public static boolean ABORT_CUBE= false;

	//PID values
	public static double kP_Straight = 0.03;
	public static double kP_Cube = .2;
	public static double kP_Turn = 0.06;
	public static double kI_Straight = 0.0;
	public static double kI_Turn = 0.0;
	public static double kD_Straight = 0.0;
	public static double kD_Turn = 0.0;

	//Camera Image Values
	public static final int IMG_WIDTH = 160;
	public static final int IMG_HEIGHT = 120;

	//encoder constants
	public static boolean kSensorPhase = false;
	public static boolean kMotorInvert = false;
	public static final int kPIDLoopIdx = 0;
	public static final int kTimeoutMs = 10;
	public static int LEFT_STARTING_POSITION = 0;
	public static int RIGHT_STARTING_POSITION = 0;
	public static int DRIVE_GEAR_RATIO = 5;

	//Elevator target positions
	public static final double dPosSwitch = 30 ;
	public static final double dPosScale = 74;
	public static final double dPosPyramid = 10;
	public static final double dPosBumpUp = 2 ;
	public static final double dPosBumpDown = -2 ;
	public static final double dFudgeFactor = 1.135 ;  // actual distance/programmed distance

	//Elevator drive ratios
	public static final int kMotorGearRatio = 5 ;
	public static final int kEncoderRatio = 1 ; //ratio of encoder revs to output revs
	public static final int kMotorSprocketTeeth = 12 ;
	public static final int kDrumShaftSprocketTeeth = 12 ;
	public static final double kWinchDrumDia = 0.5 ;

	//Motor encoder info
	public static final int kEncoderPPR = 4096 ;  // encoder pulses per revolution

	//PID constants
	public static final double kf = 0.5 ;
	public static final double kp = 0.25 ;
	public static final double ki = 0 ;
	public static final double kd = 7 ;

	/* 
	 * Motion magic motion parameters
	 * Test values only - for real try to increase 4x by competition 
	 */

	public static final double kCruiseSpeed = 50.0 ; // inches per sec
	public static final double kAcceleration = 30.0 ; // inches/s^2

	public static final double kCruiseSpeedUp = 40.0 ; // inches per sec  
	public static final double kAccelerationUp = 40.0 ; // inches/s^2
	public static final double kCruiseSpeedDown = 20.0 ; // inches per sec
	public static final double kAccelerationDown = 10.0 ; // inches/s^2

	/* 
	 * speed and acceleration down need to be set to keep tension on the cable
	 * g = 386.4 in/s^2 
	 */
	
	//motor IDS
	public static final int kMotorPort = 0;
	public static final int kMotor2Port = 1 ;
	public static final int kMaxMotorCurrent = 35 ;
	public static final int kPeakMotorCurrent = 55 ;

}

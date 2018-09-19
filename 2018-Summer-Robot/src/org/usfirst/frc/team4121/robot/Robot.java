package org.usfirst.frc.team4121.robot;

import org.usfirst.frc.team4121.robot.commands.AutoRobotCenterSwitchLeft;
import org.usfirst.frc.team4121.robot.commands.AutoRobotCenterSwitchRight;
import org.usfirst.frc.team4121.robot.commands.AutoRobotLeftSwitchLeft1Cube;
import org.usfirst.frc.team4121.robot.commands.AutoRobotRightSwitchRight1Cube;
import org.usfirst.frc.team4121.robot.commands.AutoStraightCommandGroup;
import org.usfirst.frc.team4121.robot.subsystems.DriveTrainSubsystem;
import org.usfirst.frc.team4121.robot.subsystems.ElevatorSubsystem;
import org.usfirst.frc.team4121.robot.subsystems.EndEffector;
import org.usfirst.frc.team4121.robot.subsystems.ShifterSubsystem;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	//Camera Stuff
	public static UsbCamera cam;
	public static CameraServer camServer;

	//Subsystems
	public static DriveTrainSubsystem driveTrain;
	public static ShifterSubsystem shifter;
	public static EndEffector end;
	public static ElevatorSubsystem elevator;

	//Sensors and inputs
	public static OI oi;

	//SmartDashboard chooser
	//private SendableChooser<Command> chooser;

	//Commands
	private Command autonomousCommand;

	//encoder math values
	public static double distanceTraveled;
	public static double angleTraveled;
	public static double leftDistance;
	public static double rightDistance;

	//2018 Game specific variables
	public static String gameData = null;
	public Timer timer = new Timer();
	public double startTime;
	public double stopTime;
	private boolean autoCommandStarted = false;
	public static String myTarget;
	public static String mySide;


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		//Initialize subsystems		
		driveTrain = new DriveTrainSubsystem();
		shifter = new ShifterSubsystem();
		end = new EndEffector();
		elevator = new ElevatorSubsystem();
		oi = new OI();


		//Initialize dashboard choosers
		//(not using due to randomness of auto this year)
		//chooser = new SendableChooser<>();
		//chooser.addObject("Do nothing", new AutoStopCommand());
		//SmartDashboard.putData("Auto Mode:", chooser);

		//Configure the camera for the robot
		camServer = CameraServer.getInstance();		
		Robot.cam = new UsbCamera("cam0", 0);		
		Robot.camServer.addCamera(Robot.cam);		
		Robot.cam.setResolution(RobotMap.IMG_WIDTH, RobotMap.IMG_HEIGHT);
		Robot.cam.setBrightness(10);		
		Robot.camServer.startAutomaticCapture(Robot.cam);
		
		//Initialize variables
		distanceTraveled = 0.0;
		angleTraveled = 0.0;
		mySide = "";
		myTarget = "";

		//Initialize Smartdashboard entries
		SmartDashboard.putString("Target", myTarget);
		SmartDashboard.putString("Side", mySide);
		
		//Calibrate the main gyro (this may not be the correct method)
		Robot.oi.MainGyro.calibrate();

	}

	public void disabled() {

		while(isDisabled()) {

		}

	}


	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {


	}


	@Override
	public void disabledPeriodic() {

		//Start scheduler
		Scheduler.getInstance().run();

	}


	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */	
	@Override
	public void autonomousInit() {

		//System.out.println("Auto started");
		
		//Set auto flags
		autoCommandStarted = false;

		//Calibrate the main gyro (this may not be the correct method)
		Robot.oi.MainGyro.calibrate();

		//Reset encoders
		RobotMap.LEFT_STARTING_POSITION = Robot.driveTrain.getLeftEncoderPosition();
		RobotMap.RIGHT_STARTING_POSITION = Robot.driveTrain.getRightEncoderPosition();

		//Get game related data from SmartDashboard
		//mySide = SmartDashboard.getString("Side", "LEFT");
		//myTarget = SmartDashboard.getString("Target", "SWITCH");

		//Get selected autonomous command (again, not using due to auto setup)
		//autonomousCommand = chooser.getSelected();

		//Start game data timer
		timer.start();
		//System.out.println("Game data timer started");
		startTime = timer.get();

	}


	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {

		//in case the game data is not properly set, we give the field 10 seconds, then default to AutoStraightCommandGroup
		stopTime = 10;

		if(stopTime <= timer.get() - startTime && gameData == null) { //timer check code

			//drive forward if it's been 10 seconds w/o game data
			if (!autoCommandStarted) {
				autonomousCommand = new AutoStraightCommandGroup();
				autonomousCommand.start();
				autoCommandStarted = true;
			}

		} else { //if timer hasn't stopped yet, grab the data

			String testGameData = DriverStation.getInstance().getGameSpecificMessage();

			//if the data contains useful stuff, use it!
			if(testGameData != null && !testGameData.isEmpty()) {

				//get individual strings
				gameData = testGameData;
				RobotMap.AUTO_SWITCH_POSITION = gameData.charAt(0);

				autonomousCommand = new AutoStraightCommandGroup();
				//System.out.println("Auto command initialized");
				
				//determine which command to run
				if (mySide.toUpperCase().equals("LEFT"))
				{
					if (RobotMap.AUTO_SWITCH_POSITION == 'L')
					{
						autonomousCommand = new AutoRobotLeftSwitchLeft1Cube();
					}
					else
					{
						autonomousCommand = new AutoStraightCommandGroup();							
					}

				}
				else if (mySide.toUpperCase().equals("CENTER"))
				{

					if(RobotMap.AUTO_SWITCH_POSITION == 'L') 
					{
						autonomousCommand = new AutoRobotCenterSwitchLeft();
					}
					else
					{
						autonomousCommand = new AutoRobotCenterSwitchRight();
					}

				}
				else
				{
					if (RobotMap.AUTO_SWITCH_POSITION == 'L')
					{
						autonomousCommand = new AutoStraightCommandGroup();														
					}
					else
					{
						autonomousCommand = new AutoRobotRightSwitchRight1Cube();
					}

				}

			}

		}

		//Start the selected command
		if(!autoCommandStarted && gameData != null) 
		{
			autonomousCommand.start();
			autoCommandStarted = true;

		}

		//Start autonomous scheduler
		Scheduler.getInstance().run();

		//Put key values on smart dashboard
		SmartDashboard.putString("Gear Position: ", shifter.gearPosition());
	}


	@Override
	public void teleopInit() {

		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null){
			autonomousCommand.cancel();
		}
		Scheduler.getInstance().removeAll();

	}


	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {

		//Start scheduler
		Scheduler.getInstance().run();

		//Put key values on the SmartDashboard
		SmartDashboard.putString("Gear Postion: ", shifter.gearPosition());
		SmartDashboard.putString("Drive Direction:", Integer.toString(RobotMap.DIRECTION_MULTIPLIER));

		SmartDashboard.putNumber("Drive Angle: ", Robot.oi.MainGyro.getAngle());
		//SmartDashboard.putNumber("Master Current", Robot.elevator.m_motor.getOutputCurrent());
		//SmartDashboard.putNumber("Slave Current", Robot.elevator.m_motor2_follower.getOutputCurrent());
		//SmartDashboard.putNumber("Master Output", Robot.elevator.m_motor.getMotorOutputPercent());
		//SmartDashboard.putNumber("Slave Output", Robot.elevator.m_motor2_follower.getMotorOutputPercent());
	}


	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {	

	}
}

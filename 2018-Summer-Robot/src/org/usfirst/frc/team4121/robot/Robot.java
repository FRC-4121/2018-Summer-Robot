package org.usfirst.frc.team4121.robot;

import org.usfirst.frc.team4121.robot.commands.AutoRightSideNoTurnCommandGroup;
import org.usfirst.frc.team4121.robot.commands.AutoRobotLeftScaleLeft1Cube;
import org.usfirst.frc.team4121.robot.commands.AutoRobotLeftScaleLeft2Cubes;
import org.usfirst.frc.team4121.robot.commands.AutoRobotLeftScaleRight1Cube;
import org.usfirst.frc.team4121.robot.commands.AutoRobotLeftSwitchLeft1Cube;
import org.usfirst.frc.team4121.robot.commands.AutoRobotRightScaleLeft1Cube;
import org.usfirst.frc.team4121.robot.commands.AutoRobotRightScaleRight1Cube;
import org.usfirst.frc.team4121.robot.commands.AutoRobotRightScaleRight2Cubes;
import org.usfirst.frc.team4121.robot.commands.AutoRobotRightSwitchRight1Cube;
import org.usfirst.frc.team4121.robot.commands.AutoStopCommand;
import org.usfirst.frc.team4121.robot.commands.AutoStraightCommandGroup;
import org.usfirst.frc.team4121.robot.subsystems.ClimberSubsystem;
import org.usfirst.frc.team4121.robot.subsystems.DriveTrainSubsystem;
import org.usfirst.frc.team4121.robot.subsystems.ElevatorSubsystem;
import org.usfirst.frc.team4121.robot.subsystems.EndEffector;
import org.usfirst.frc.team4121.robot.subsystems.ShifterSubsystem;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * 
 * @author Saliva Crustyman
 */
public class Robot extends IterativeRobot {

	//Camera Stuff
	public static UsbCamera cam;
	public static CameraServer server;


	//Network tables
	public static NetworkTableInstance dataTableInstance;
	public static NetworkTable visionTable;
	public static NetworkTable navxTable;
	public static NetworkTableEntry robotStop;
	public static NetworkTableEntry cubeHeight;
	public static NetworkTableEntry cubeAngle;
	public static NetworkTableEntry cubeDistance;
	public static NetworkTableEntry cubeOffset;
	public static NetworkTableEntry cubePercentWidth;
	public static NetworkTableEntry driveAngle;
	public static NetworkTableEntry yVelocity;
	public static NetworkTableEntry xVelocity;
	public static NetworkTableEntry yDisplacement;
	public static NetworkTableEntry xDisplacement;
	public static NetworkTableEntry zeroGyro;
	public static NetworkTableEntry writeVideo;


	//Subsystems
	public static DriveTrainSubsystem driveTrain;
	public static ShifterSubsystem shifter;
	public static ClimberSubsystem climber;
	public static EndEffector end;
	public static ElevatorSubsystem elevator;


	//Sensors and inputs
	public static OI oi;


	//SmartDashboard chooser
	private SendableChooser<Command> chooser;


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
	public static double numberOfCubes;


	//Time
	public Date currentDate;
	public SimpleDateFormat fullDateFormat;
	public SimpleDateFormat fullTimeFormat;


	//Logging
	//FileWriter logger;



	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		//Initialize NetworkTables
		dataTableInstance = NetworkTableInstance.getDefault();
		visionTable = dataTableInstance.getTable("vision");
		navxTable = dataTableInstance.getTable("navx");


		//Initialize NetworkTable entries
		robotStop = visionTable.getEntry("RobotStop");
		cubeAngle = visionTable.getEntry("CubeAngle");
		cubeDistance = visionTable.getEntry("CubeDistance");
		cubeOffset = visionTable.getEntry("CubeOffset");
		cubePercentWidth = visionTable.getEntry("CubePercentWidth");
		writeVideo = visionTable.getEntry("WriteVideo");
		cubeHeight = visionTable.getEntry("cubeHeight");
		driveAngle = navxTable.getEntry("DriveAngle");
		yVelocity = navxTable.getEntry("YVelocity");
		xVelocity = navxTable.getEntry("XVelocity");
		yDisplacement = navxTable.getEntry("YDisplacement");
		xDisplacement = navxTable.getEntry("XDisplacement");
		zeroGyro = navxTable.getEntry("ZeroGyro");


		//Initialize NetworkTAble values
		robotStop.setDouble(0.0);


		//Initialize subsystems		
		driveTrain = new DriveTrainSubsystem();
		shifter = new ShifterSubsystem();
		climber = new ClimberSubsystem();
		end = new EndEffector();
		elevator = new ElevatorSubsystem();
		oi = new OI();


		//Initialize dashboard choosers
		//!!Update this to reflect any new auto code!!
//		chooser = new SendableChooser<>();
//		chooser.addObject("Do nothing", new AutoStopCommand());
//		chooser.addDefault("Straight", new AutoStraightCommandGroup());
//		chooser.addObject("Left Switch", new AutoRobotLeftSwitchLeft1Cube());
//		chooser.addObject("Right Switch", new AutoRobotRightSwitchRight1Cube());
//		chooser.addObject("Right Straight", new AutoRightSideNoTurnCommandGroup());
//		chooser.addObject("Left Scale", new AutoRobotLeftScaleLeft1Cube());
//		chooser.addObject("Right Scale", new AutoRobotRightScaleRight1Cube());
//		chooser.addObject("Rightside Opposite Scale", new AutoRobotRightScaleLeft1Cube());
//		chooser.addObject("Leftside Opposite Scale", new AutoRobotLeftScaleRight1Cube());
//		SmartDashboard.putData("Auto Mode:", chooser);

		//Initialize variables
		distanceTraveled = 0.0;
		angleTraveled = 0.0;
		mySide = "";
		myTarget = "";
		numberOfCubes = 1.0;

		//Initialize Smartdashboard entries
		SmartDashboard.putString("Target", myTarget);
		SmartDashboard.putString("Side", mySide);
		SmartDashboard.putNumber("Cubes", numberOfCubes);

		//Initialize date variables
		currentDate = new Date();
		fullDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		fullTimeFormat = new SimpleDateFormat("hh:mm:ss");


		//Initialize logging file
		//		String logFilename = "C:\\Users\\team4\\Logs\\RobotLog" + fullDateFormat.format(currentDate) + ".txt";
		//		try
		//		{
		//			logger = new FileWriter(logFilename, true);
		//		}
		//		catch (IOException e)
		//		{
		//			
		//		}

	}


	void Disabled() {

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

		//Close log file
		//		try
		//		{
		//			logger.close();
		//		}
		//		catch (IOException e)
		//		{
		//			System.out.println(e);
		//		}

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

		//Set auto flags
		autoCommandStarted = false;


		//Calibrate the main gyro
		zeroGyro.setDouble(1.0);


		//Reset encoders
		RobotMap.LEFT_STARTING_POSITION = Robot.driveTrain.getLeftEncoderPosition();
		RobotMap.RIGHT_STARTING_POSITION = Robot.driveTrain.getRightEncoderPosition();


		//Get game related data from SmartDashboard
		mySide = SmartDashboard.getString("Side", "LEFT");
		myTarget = SmartDashboard.getString("Target", "SCALE");
		numberOfCubes = SmartDashboard.getNumber("Cubes", 1.0);

		//		currentDate = new Date();
		//		String message = fullTimeFormat.format(currentDate) + "==>  Robot Start Side: " + mySide;
		//		String message2 = fullTimeFormat.format(currentDate) + "==>  Auto Target: " + myTarget;
		//		String message3 = fullTimeFormat.format(currentDate) + "==>  Number of Cubes: " + numberOfCubes;
		//		try
		//		{
		//			logger.write(message);
		//			logger.write(message2);
		//			logger.write(message3);
		//		}
		//		catch (IOException e)
		//		{
		//
		//		}

		//Get selected autonomous command
		//autonomousCommand = chooser.getSelected();


		//Start game data timer
		timer.start();
		startTime = timer.get();

	}


	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {

		//in case the game data is not properly set, we give the field 10 seconds, then default to AutoStraightCommandGroup
		stopTime = 10;

		SmartDashboard.putString("Test Target", myTarget);
		SmartDashboard.putString("Test Side", mySide);
		SmartDashboard.putNumber("Test Number Of Cubes", numberOfCubes);

		if(stopTime <= timer.get() - startTime && gameData == null) { //timer check code

			//drive forward if it's been 10 seconds w/o game data
			if (!autoCommandStarted) {
				autonomousCommand = new AutoStraightCommandGroup();
				autonomousCommand.start();
				autoCommandStarted = true;
			}

			//Log message
			//			currentDate = new Date();
			//			String message = fullTimeFormat.format(currentDate) + "==>  Game data not received wthin 10 seconds.  Default command used.";
			//			try
			//			{
			//				logger.write(message);
			//			}
			//			catch (IOException e)
			//			{
			//
			//			}

		} else { //if timer hasn't stopped yet, grab the data

			String testGameData = DriverStation.getInstance().getGameSpecificMessage();

			//if the data contains useful stuff, use it!
			if(testGameData != null && !testGameData.isEmpty()) {

				//get individual strings
				gameData = testGameData;
				RobotMap.AUTO_SWITCH_POSITION = gameData.charAt(0);
				RobotMap.AUTO_SCALE_POSITION = gameData.charAt(1);
				RobotMap.AUTO_SWITCH_AND_SCALE = gameData.substring(2);


				//				//Log message
				//				currentDate = new Date();
				//				String message = fullTimeFormat.format(currentDate) + "==>  Game data received.  Data = " + gameData;
				//				try
				//				{
				//					logger.write(message);
				//				}
				//				catch (IOException e)
				//				{
				//
				//				}


				//determine which command to run
				if (mySide.toUpperCase().equals("LEFT"))
				{

					if (myTarget.toUpperCase().equals("SWITCH"))
					{

						if (RobotMap.AUTO_SWITCH_POSITION == 'L')
						{

							if (numberOfCubes == 1.0)
							{
								autonomousCommand = new AutoRobotLeftSwitchLeft1Cube();
							}
							else
							{
								autonomousCommand = new AutoRobotLeftSwitchLeft1Cube();
							}

						}
						else
						{
							autonomousCommand = new AutoStraightCommandGroup();							
						}

					}
					else
					{

						if (RobotMap.AUTO_SCALE_POSITION == 'L')
						{

							if (numberOfCubes == 1.0)
							{
								autonomousCommand = new AutoRobotLeftScaleLeft1Cube();
							}
							else
							{
								autonomousCommand = new AutoRobotLeftScaleLeft2Cubes();
							}

						}
						else
						{

							if (numberOfCubes == 1.0)
							{
								autonomousCommand = new AutoRobotLeftScaleRight1Cube();
							}
							else
							{
								autonomousCommand = new AutoRobotLeftScaleRight1Cube();
							}

						}

					}

				}
				else
				{

					if (myTarget.toUpperCase().equals("SWITCH"))
					{

						if (RobotMap.AUTO_SWITCH_POSITION == 'L')
						{
							autonomousCommand = new AutoStraightCommandGroup();														
						}
						else
						{

							if (numberOfCubes == 1.0)
							{
								autonomousCommand = new AutoRobotRightSwitchRight1Cube();
							}
							else
							{
								autonomousCommand = new AutoRobotRightSwitchRight1Cube();
							}

						}

					}
					else
					{

						if (RobotMap.AUTO_SCALE_POSITION == 'L')
						{

							if (numberOfCubes == 1.0)
							{
								autonomousCommand = new AutoRobotRightScaleLeft1Cube();
							}
							else
							{
								autonomousCommand = new AutoRobotRightScaleLeft1Cube();
							}

						}
						else
						{

							if (numberOfCubes == 1.0)
							{
								autonomousCommand = new AutoRobotRightScaleRight1Cube();
							}
							else
							{
								autonomousCommand = new AutoRobotRightScaleRight2Cubes();
							}

						}

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

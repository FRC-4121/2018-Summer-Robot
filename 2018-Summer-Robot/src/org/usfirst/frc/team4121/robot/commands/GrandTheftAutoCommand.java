package org.usfirst.frc.team4121.robot.commands;

import org.usfirst.frc.team4121.robot.Robot;
import org.usfirst.frc.team4121.robot.RobotMap;
import org.usfirst.frc.team4121.robot.extraClasses.PIDControl;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GrandTheftAutoCommand extends Command {

	public char robotStartSide;

	double targetDistance; 
	double direction; //-1 = Reverse, +1 = Forward (reverse is for gear forward is for shooting)
	double targetAngle;  //drive angle
	double stopTime;  //timeout time
	double angleCorrection, angleError;
	double motorOutput;
	double startTime;
	double cubeCorrection = 0;
	double speedMultiplier;
	
	//test values for distances through the 'pathway'
	double testCloseSwitchFromPathDist = 24;
	double testOppositeSwitchFromPathDist = 160;

	PIDControl pidControlDrive;
	PIDControl pidControlTurn;//can I make two PIDs?

	public Timer timer = new Timer();

	public int leftEncoderStart;
	public int rightEncoderStart;

	//These boolean flags are in charge of what step of our we are own (like addSequential) 
	//flags for L:LLX, R:RRX, L:RLX, and R:LRX cases
	boolean drivingToScale = true;
	
	boolean scaleProcedure = false;
	boolean liftCubeToScale = false;
	boolean angleGrabberUp = false;
	boolean ejectCube = false;
	boolean backUp = false;
	boolean angleGrabberDown = false;
	boolean lowerElevToGround = false;
	
	boolean turnAround = false;
	boolean grabSecondCube = false;
	boolean turnToScale = false;
	boolean secondTurnAround = false;
	boolean toScaleFromCube = false;
	
	//flags for L:RRX and R:LLX
	boolean drivingToPathway = true; 
	boolean turnIntoPath = false; 
	boolean driveToOtherSide = false; 
	boolean turnToSwitch = false; 
	boolean liftToSwitch = false;
	//ejectCube 
	//lowerElevToGround
	//maybe do this, time depending
//	Pick up cube #2
//	Turn around
//	Lift cube #2 to scale height
//	(Angle motor here??)
//	(Drive forward??)
//	Eject cube #2 into scale]
	
	//flags for L:LRX and R:RLX
	//drivingToPathway
	//turnIntoPath
	boolean driveToCloseSwitch = false;
//	turnToSwitch
//	liftToSwitch
//	ejectCube
//	[Talk to strategy and determine if this is necessary:
//	Lower to ground height
//	Pick up cube #2
//	Lift to switch height
//	Eject cube #2 into switch]
	
	
	public GrandTheftAutoCommand(char side) {
		
		//we basically require everything
		requires(Robot.driveTrain);
		requires(Robot.elevator);
		requires(Robot.end);

		robotStartSide = side;

		//set up the PIDs
		pidControlDrive = new PIDControl(RobotMap.kP_Straight, RobotMap.kI_Straight, RobotMap.kD_Straight);
		pidControlTurn = new PIDControl(RobotMap.kP_Turn, RobotMap.kI_Turn, RobotMap.kD_Turn);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		
		timer.start();
		startTime = timer.get();
		
		Robot.distanceTraveled = 0.0;
		leftEncoderStart = Robot.driveTrain.getLeftEncoderPosition();
		rightEncoderStart = Robot.driveTrain.getRightEncoderPosition();
		
		angleCorrection = 0;
		angleError = 0;
		
		//general beginning procedure
		Robot.end.closeArms();
		Robot.end.openServos();

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		if(robotStartSide == 'L') { //run this on the left side

			switch (RobotMap.AUTO_SWITCH_AND_SCALE) {

			//both of these scenarios have the same actions (double cube in the scale)
			case "LL": 
			case "RL":

				//Drive to Scale
				if(drivingToScale) {

					//set variables for driving
					targetDistance = 260; //distance to the scale
					direction = 1;
					targetAngle = 0;

					//Drive action
					angleError = Robot.driveAngle.getDouble(0) - targetAngle;
					angleCorrection = RobotMap.kP_Straight * angleError;
					Robot.driveTrain.autoDrive(direction * RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 10;
					
					if(stopTime <= timer.get()-startTime) {

						//Too much time has elapsed. Stop doing stuff, as an error has likely occurred. 
						drivingToScale = false;

					} else { //if time hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							drivingToScale = false;
							scaleProcedure = true;
							liftCubeToScale = true;
							Robot.distanceTraveled = 0; //reset the distance traveled
							angleCorrection = 0;
							angleError = 0;

						}

					}

				}
				
				//we may need to pivot a little bit here before lifting so that we get the cube in the scale
				
				if(scaleProcedure) { //like a command group, we lift, angle, eject, back up, angle, and lower.
					
					//Lift cube to scale height
					if(liftCubeToScale) {

						Robot.elevator.runToScale();

						if(Robot.elevator.targetPos <= Robot.elevator.oldTargetPos) { //this may stop the elevator too soon.  better ways to do this????

							liftCubeToScale = false;
							angleGrabberUp = true;

							startTime = timer.get(); //reset the timer for the next step

						}

					}

					//Angle motor appropriately.. this code may still need some work
					if(angleGrabberUp) {

						stopTime = 2; //time to angle motor up

						Robot.end.angleMotor(RobotMap.ANGLE_END_EFFECTOR_SPEED); //angle the motor up

						if (stopTime <= timer.get()-startTime) { //stop when we hit the time defined above

							angleGrabberUp = false;
							ejectCube = true;
							Robot.end.angleMotor(0);
							
							startTime = timer.get(); //reset the timer for the next step

						}

					}

					//Eject
					if(ejectCube) {

						stopTime = 1; //time to eject cube

						Robot.end.endeffector(-1.0);

						if(stopTime <= timer.get()-startTime) {

							ejectCube = false;
							backUp = true;
							
							Robot.end.endeffector(0);

							startTime = timer.get(); //same drill as usual

						}

					}

					//Back up??
					if(backUp) {

						//set variables for driving
						targetDistance = 10; //we don't need to back up very far
						direction = -1;
						targetAngle = 0;

						//Drive action
						angleError = Robot.driveAngle.getDouble(0)-targetAngle;
						angleCorrection = RobotMap.kP_Straight*angleError;
						Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

						//Check elapsed time
						stopTime = 2;
						if(stopTime <= timer.get()-startTime) {

							//We've probably gone far enough
							backUp = false;
							angleGrabberDown = true;
							Robot.distanceTraveled = 0;

						} else { //if timer hasn't run out, check how far we've gone

							int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
							int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
							Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

							if (targetDistance <= Robot.distanceTraveled) {

								//Robot has reached its destination.  Stop this command and switch to the next part of the command
								backUp = false;
								angleGrabberDown = true;
								Robot.distanceTraveled = 0;
								angleCorrection = 0;
								angleError = 0;
								
							}

						}

					}

					//reverse angle motor
					if(angleGrabberDown) { //should have a limit switch for this!!

						stopTime = 2; //time to angle motor up

						Robot.end.angleMotor(-RobotMap.ANGLE_END_EFFECTOR_SPEED); //angle the motor down

						if (stopTime <= timer.get()-startTime) { //stop when we hit the time defined above

							angleGrabberUp = false;
							ejectCube = true;
							
							Robot.end.angleMotor(0);
							
							
							

						}

					}

					//Lower elevator
					if(lowerElevToGround) {

						Robot.elevator.goToHome();

						if(Robot.elevator.targetPos <= 0) { //when the elevator hits the ground, we're done. <= in case roundoff error happens

							lowerElevToGround = false;
							scaleProcedure = false;
							turnAround = true;

							startTime = timer.get(); //reset the timer for the next step

						}

					}
				
				}
				
				//Turn around
				if(turnAround) {

					targetAngle = 135;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						turnAround = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							turnAround = false;
							grabSecondCube = true;
							angleCorrection = 0;
							angleError = 0;

							startTime = timer.get();

						}

					}

				}

				//Grab new cube
				if(grabSecondCube) {
									
					if(Robot.cubePercentWidth.getDouble(0)<20)
					{
						speedMultiplier = -1; //might be + on comp bot
					}
					else
					{
						speedMultiplier = -.75; //might be + on comp bot
					}

					if(Math.abs(Robot.cubeOffset.getDouble(0)/160)> .04)
					{
						cubeCorrection = RobotMap.kP_Cube*Robot.cubeOffset.getDouble(0)/160;
					}
					else
					{
						cubeCorrection = 0;
					}

					SmartDashboard.putNumber("Cube Correction", cubeCorrection);
					Robot.driveTrain.autoDrive(speedMultiplier*RobotMap.AUTO_DRIVE_SPEED - cubeCorrection, speedMultiplier*RobotMap.AUTO_DRIVE_SPEED + cubeCorrection); //flip signs on comp bot

					if(!RobotMap.ABORT_CUBE)
					{
						//Check elapsed time
						stopTime = 15; //obviously way too much time, but it's for error control, not execution.
						if(Robot.cubePercentWidth.getDouble(0) > 85) //when cube takes up majority of screen you are done need to change value
						{

							//Too much time has elapsed.  Stop this command.
							grabSecondCube = false;
							secondTurnAround = true;

							startTime = timer.get();

						}
						else if (stopTime<=timer.get()-startTime)
						{

							//Too much time has elapsed.  Stop this command.
							grabSecondCube = false;
							secondTurnAround = true;

							startTime = timer.get();

						}
						else if(Robot.cubePercentWidth.getDouble(0)== 0 && 1<=timer.get()-startTime)
						{
							grabSecondCube = false;
							secondTurnAround = true;

							startTime = timer.get();
						}

					}
					else
					{
						grabSecondCube = false;
						secondTurnAround = true;

						startTime = timer.get();
					}

				}

				//Turn around again
				if(secondTurnAround) {

					targetAngle = -25;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						secondTurnAround = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							secondTurnAround = false;
							toScaleFromCube = true;
							angleCorrection = 0;
							angleError = 0;

						}

					}

				}

				//Drive forward to the scale
				if(toScaleFromCube) {

					//set variables for driving
					targetDistance = 40; //conservative distance
					direction = 1;
					targetAngle = 0;

					//Drive action
					angleError = Robot.driveAngle.getDouble(0)-targetAngle;
					angleCorrection = RobotMap.kP_Straight*angleError;
					Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 5;
					if(stopTime <= timer.get()-startTime) {

						toScaleFromCube = false;

					} else { //if timer hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							toScaleFromCube = false;
							scaleProcedure = true; //we re-run the scale procedures above
							Robot.distanceTraveled = 0;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}

				}

			case "RR":
				
				if(drivingToPathway) {
					
					//set variables for driving
					targetDistance = 240; //we don't need to back up very far
					direction = 1;
					targetAngle = 0;

					//Drive action
					angleError = Robot.driveAngle.getDouble(0)-targetAngle;
					angleCorrection = RobotMap.kP_Straight*angleError;
					Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 10;
					if(stopTime <= timer.get()-startTime) {

						//Too much time has elapsed - stop!
						drivingToPathway = false;
						
					} else { //if timer hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							drivingToPathway = false;
							turnIntoPath = true;
							Robot.distanceTraveled = 0;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
				}
				
				if(turnIntoPath) {
					
					targetAngle = 90;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						turnIntoPath = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							turnIntoPath = false;
							driveToOtherSide = true;
							
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
				}
				
				if(driveToOtherSide) {
					
					//set variables for driving
					targetDistance = testOppositeSwitchFromPathDist; //should be about the right distance, test it though
					direction = 1;
					targetAngle = 90; //we are driving to the right from the gyro's perspective

					//Drive action
					angleError = Robot.driveAngle.getDouble(0)-targetAngle;
					angleCorrection = RobotMap.kP_Straight*angleError;
					Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 10;
					if(stopTime <= timer.get()-startTime) {

						//Too much time has elapsed - stop!
						drivingToPathway = false;
						
					} else { //if timer hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							drivingToPathway = false;
							turnIntoPath = true;
							
							Robot.distanceTraveled = 0;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
				
				}
				
				if(liftToSwitch) {
					
					Robot.elevator.runToSwitch();
					
					if(Robot.elevator.targetPos <= Robot.elevator.oldTargetPos) {
						
						liftToSwitch = false;
						turnToSwitch = true;
						
						startTime = timer.get();
						
					}
					
				}
				
				if(turnToSwitch) {
					
					targetAngle = 180;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						turnIntoPath = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							turnToSwitch = false;
							ejectCube = true;
							
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
				}
				
				if(ejectCube) {
					
					stopTime = 1; //time to eject cube

					Robot.end.endeffector(-1.0);

					if(stopTime <= timer.get()-startTime) {

						ejectCube = false;
						backUp = true;

						startTime = timer.get(); //same drill as usual

					}
					
				}
				
//				??Back up here??
//				[Time dependent:
//				Lower to ground height
//				Pick up cube #2
//				Turn around
//				Lift cube #2 to scale height
//				(Angle motor here??)
//				(Drive forward??)
//				Eject cube #2 into scale]

			

			case "LR":	

				if(drivingToPathway) {
					
					//set variables for driving
					targetDistance = 240; //we don't need to back up very far
					direction = 1;
					targetAngle = 0;

					//Drive action
					angleError = Robot.driveAngle.getDouble(0)-targetAngle;
					angleCorrection = RobotMap.kP_Straight*angleError;
					Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 10;
					if(stopTime <= timer.get()-startTime) {

						//Too much time has elapsed - stop!
						drivingToPathway = false;
						
					} else { //if timer hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							drivingToPathway = false;
							turnIntoPath = true;
							Robot.distanceTraveled = 0;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
				}
				
				if(turnIntoPath) {
					
					targetAngle = 90;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						turnIntoPath = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							turnIntoPath = false;
							driveToCloseSwitch = true;
							angleCorrection = 0;
							angleError = 0;
						}

					}
					
				}
				
				if(driveToCloseSwitch) {
					
					//set variables for driving
					targetDistance = testCloseSwitchFromPathDist; //TEST VALUE!!!!
					direction = 1;
					targetAngle = 90; //we are driving to the right from the gyro's perspective

					//Drive action
					angleError = Robot.driveAngle.getDouble(0)-targetAngle;
					angleCorrection = RobotMap.kP_Straight*angleError;
					Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 10;
					if(stopTime <= timer.get()-startTime) {

						//Too much time has elapsed - stop!
						drivingToPathway = false;
						
					} else { //if timer hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							drivingToPathway = false;
							turnIntoPath = true;
							Robot.distanceTraveled = 0;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}	
					
				}
				
				if(liftToSwitch) {
					
					Robot.elevator.runToSwitch();
					
					if(Robot.elevator.targetPos <= Robot.elevator.oldTargetPos) {
						
						liftToSwitch = false;
						turnToSwitch = true;
						
						startTime = timer.get();
						
					}
					
				}
				
				if(turnToSwitch) {
					
					targetAngle = 180;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						turnIntoPath = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							turnToSwitch = false;
							ejectCube = true;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
				}
				
				if(ejectCube) {
					
					stopTime = 1; //time to eject cube

					Robot.end.endeffector(-1.0);

					if(stopTime <= timer.get()-startTime) {

						ejectCube = false;
						backUp = true;
						Robot.end.endeffector(0);

						startTime = timer.get(); //same drill as usual

					}
					
				}
				
				//[Talk to strategy and determine if this is necessary:
//				Lower to ground height
//				Pick up cube #2
//				Lift to switch height
//				Eject cube #2 into switch]

			}

		} else { //run this on the right side

			switch (RobotMap.AUTO_SWITCH_AND_SCALE) {

			case "RR": //we may be able to rejigger the L:LL and R:RR cases to just trip (another) flag and run code outside the switch
			case "LR": //both of these scenarios involve the exact same actions
				
				//Drive to Scale
				if(drivingToScale) {

					//set variables for driving
					targetDistance = 260; //distance to the scale
					direction = 1;
					targetAngle = 0;

					//Drive action
					angleError = Robot.driveAngle.getDouble(0) - targetAngle;
					angleCorrection = RobotMap.kP_Straight * angleError;
					Robot.driveTrain.autoDrive(direction * RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 10;
					
					if(stopTime <= timer.get()-startTime) {

						//Too much time has elapsed. Stop doing stuff, as an error has likely occurred. 
						drivingToScale = false;

					} else { //if time hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							drivingToScale = false;
							scaleProcedure = true;
							liftCubeToScale = true;
							Robot.distanceTraveled = 0; //reset the distance traveled
							angleCorrection = 0;
							angleError = 0;
							
						}

					}

				}
				
				//we may need to pivot a little bit here before lifting so that we hit the scale
				
				if(scaleProcedure) { //like a command group, we lift, angle, eject, back up, angle, and lower.
					
					//Lift cube to scale height
					if(liftCubeToScale) {

						Robot.elevator.runToScale();

						if(Robot.elevator.targetPos <= Robot.elevator.oldTargetPos) { //this may stop the elevator too soon.  better ways to do this????

							liftCubeToScale = false;
							angleGrabberUp = true;

							startTime = timer.get(); //reset the timer for the next step

						}

					}

					//Angle motor appropriately.. this code may still need some work
					if(angleGrabberUp) {

						stopTime = 2; //time to angle motor up

						Robot.end.angleMotor(RobotMap.ANGLE_END_EFFECTOR_SPEED); //angle the motor up

						if (stopTime <= timer.get()-startTime) { //stop when we hit the time defined above

							angleGrabberUp = false;
							ejectCube = true;
							
							Robot.end.angleMotor(0);

							startTime = timer.get(); //reset the timer for the next step

						}

					}

					//Eject
					if(ejectCube) {

						stopTime = 1; //time to eject cube

						Robot.end.endeffector(-1.0);

						if(stopTime <= timer.get()-startTime) {

							ejectCube = false;
							backUp = true;
							
							Robot.end.endeffector(0);

							startTime = timer.get(); //same drill as usual

						}

					}

					//Back up??
					if(backUp) {

						//set variables for driving
						targetDistance = 10; //we don't need to back up very far
						direction = -1;
						targetAngle = 0;

						//Drive action
						angleError = Robot.driveAngle.getDouble(0)-targetAngle;
						angleCorrection = RobotMap.kP_Straight*angleError;
						Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

						//Check elapsed time
						stopTime = 2;
						if(stopTime <= timer.get()-startTime) {

							//We've probably gone far enough
							backUp = false;
							angleGrabberDown = true;
							Robot.distanceTraveled = 0;

						} else { //if timer hasn't run out, check how far we've gone

							int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
							int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
							Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

							if (targetDistance <= Robot.distanceTraveled) {

								//Robot has reached its destination.  Stop this command and switch to the next part of the command
								backUp = false;
								angleGrabberDown = true;
								Robot.distanceTraveled = 0;
								angleCorrection = 0;
								angleError = 0;
							}

						}

					}

					//reverse angle motor
					if(angleGrabberDown) { //should have a limit switch for this!!

						stopTime = 0; //time to angle motor up

						Robot.end.angleMotor(-RobotMap.ANGLE_END_EFFECTOR_SPEED); //angle the motor down

						if (stopTime <= timer.get()-startTime) { //stop when we hit the time defined above
							
							Robot.end.angleMotor(0);
							angleGrabberUp = false;
							ejectCube = true;

						}

					}

					//Lower elevator
					if(lowerElevToGround) {

						Robot.elevator.goToHome();

						if(Robot.elevator.targetPos <= 0) { //when the elevator hits the ground, we're done. <= in case roundoff error happens

							lowerElevToGround = false;
							scaleProcedure = false;
							turnAround = true;

							startTime = timer.get(); //reset the timer for the next step

						}

					}
				
				}
				
				//Turn around
				if(turnAround) {

					targetAngle = 180;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						turnAround = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							turnAround = false;
							grabSecondCube = true;
							angleCorrection = 0;
							angleError = 0;
							startTime = timer.get();

						}

					}

				}

				//Grab new cube
				if(grabSecondCube) {
									
					if(Robot.cubePercentWidth.getDouble(0)<20)
					{
						speedMultiplier = -1; //might be + on comp bot
					}
					else
					{
						speedMultiplier = -.75; //might be + on comp bot
					}

					if(Math.abs(Robot.cubeOffset.getDouble(0)/160)> .04)
					{
						cubeCorrection = RobotMap.kP_Cube*Robot.cubeOffset.getDouble(0)/160;
					}
					else
					{
						cubeCorrection = 0;
					}

					//SmartDashboard.putNumber("Cube Correction", cubeCorrection);
					Robot.driveTrain.autoDrive(speedMultiplier*RobotMap.AUTO_DRIVE_SPEED - cubeCorrection, speedMultiplier*RobotMap.AUTO_DRIVE_SPEED + cubeCorrection); //flip signs on comp bot

					if(!RobotMap.ABORT_CUBE)
					{
						//Check elapsed time
						stopTime = 15; //obviously way too much time, but it's for error control, not execution.
						if(Robot.cubePercentWidth.getDouble(0) > 85) //when cube takes up majority of screen you are done need to change value
						{

							//Too much time has elapsed.  Stop this command.
							grabSecondCube = false;
							secondTurnAround = true;

							startTime = timer.get();

						}
						else if (stopTime<=timer.get()-startTime)
						{

							//Too much time has elapsed.  Stop this command.
							grabSecondCube = false;
							secondTurnAround = true;

							startTime = timer.get();

						}
						else if(Robot.cubePercentWidth.getDouble(0)== 0 && 1<=timer.get()-startTime)
						{
							grabSecondCube = false;
							secondTurnAround = true;

							startTime = timer.get();
						}

					}
					else
					{
						grabSecondCube = false;
						secondTurnAround = true;

						startTime = timer.get();
					}

				}

				//Turn around again
				if(secondTurnAround) {

					targetAngle = 0;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						secondTurnAround = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							secondTurnAround = false;
							toScaleFromCube = true;
							angleCorrection = 0;
							angleError = 0;

						}

					}

				}

				//Drive forward a little bit
				if(toScaleFromCube) {

					//set variables for driving
					targetDistance = 20; //probably about this distance
					direction = 1;
					targetAngle = 0;

					//Drive action
					angleError = Robot.driveAngle.getDouble(0)-targetAngle;
					angleCorrection = RobotMap.kP_Straight*angleError;
					Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 5;
					if(stopTime <= timer.get()-startTime) {

						toScaleFromCube = false;

					} else { //if timer hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							toScaleFromCube = false;
							scaleProcedure = true; //we re-run the scale procedures above
							Robot.distanceTraveled = 0;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}

				}


			case "LL":

				if(drivingToPathway) {
					
					//set variables for driving
					targetDistance = 240; //we don't need to back up very far
					direction = 1;
					targetAngle = 0;

					//Drive action
					angleError = Robot.driveAngle.getDouble(0)-targetAngle;
					angleCorrection = RobotMap.kP_Straight*angleError;
					Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 10;
					if(stopTime <= timer.get()-startTime) {

						//Too much time has elapsed - stop!
						drivingToPathway = false;
						
					} else { //if timer hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							drivingToPathway = false;
							turnIntoPath = true;
							Robot.distanceTraveled = 0;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
					
				}
				
				if(turnIntoPath) {
					
					targetAngle = -90;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						turnIntoPath = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							turnIntoPath = false;
							liftToSwitch = true;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
				}
				
				if(liftToSwitch) {
					
					Robot.elevator.runToSwitch();
					
					if(Robot.elevator.targetPos <= Robot.elevator.oldTargetPos) {
						
						liftToSwitch = false;
						turnToSwitch = true;
						
						startTime = timer.get();
						
					}
					
				}
				
				if(turnToSwitch) {
					
					targetAngle = 180;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						turnIntoPath = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							turnToSwitch = false;
							ejectCube = true;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
				}
				
				if(ejectCube) {
					
					stopTime = 1; //time to eject cube

					Robot.end.endeffector(-1.0);

					if(stopTime <= timer.get()-startTime) {

						Robot.end.endeffector(0);
						ejectCube = false;
						backUp = true;

						startTime = timer.get(); //same drill as usual

					}
					
				}
//				??Back up here??
//				[Time dependent:
//				Lower to ground height
//				Pick up cube #2
//				Turn around
//				Lift cube #2 to scale height
//				(Angle motor here??)
//				(Drive forward??)
//				Eject cube #2 into scale]

			case "RL":

				if(drivingToPathway) {
					
					//set variables for driving
					targetDistance = 240; //we don't need to back up very far
					direction = 1;
					targetAngle = 0;

					//Drive action
					angleError = Robot.driveAngle.getDouble(0)-targetAngle;
					angleCorrection = RobotMap.kP_Straight*angleError;
					Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 10;
					if(stopTime <= timer.get()-startTime) {

						//Too much time has elapsed - stop!
						drivingToPathway = false;
						
					} else { //if timer hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							drivingToPathway = false;
							turnIntoPath = true;
							Robot.distanceTraveled = 0;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
				}
				
				if(turnIntoPath) {
					
					targetAngle = -90;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						turnIntoPath = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							turnIntoPath = false;
							driveToCloseSwitch = true;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
				}
				
				if(driveToCloseSwitch) {
					
					//set variables for driving
					targetDistance = testCloseSwitchFromPathDist; //TEST VALUE!!!!
					direction = 1;
					targetAngle = -90; //we are driving to the right from the gyro's perspective

					//Drive action
					angleError = Robot.driveAngle.getDouble(0)-targetAngle;
					angleCorrection = RobotMap.kP_Straight*angleError;
					Robot.driveTrain.autoDrive(direction*RobotMap.AUTO_DRIVE_SPEED + angleCorrection, direction*RobotMap.AUTO_DRIVE_SPEED - angleCorrection);

					//Check elapsed time
					stopTime = 10;
					if(stopTime <= timer.get()-startTime) {

						//Too much time has elapsed - stop!
						drivingToPathway = false;
						
					} else { //if timer hasn't run out, check how far we've gone

						int totalRotationsRight = Math.abs((Robot.driveTrain.getRightEncoderPosition() - rightEncoderStart)) / 4096;
						int totalRotationsLeft = Math.abs((Robot.driveTrain.getLeftEncoderPosition() - leftEncoderStart)) / 4096;
						Robot.distanceTraveled = (6*Math.PI*((totalRotationsRight+totalRotationsLeft)/2))/ RobotMap.DRIVE_GEAR_RATIO;

						if (targetDistance <= Robot.distanceTraveled) {

							//Robot has reached its destination.  Stop this command and switch to the next part of the command
							drivingToPathway = false;
							turnIntoPath = true;
							Robot.distanceTraveled = 0;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}	
					
				}
				
				if(liftToSwitch) {
					
					Robot.elevator.runToSwitch();
					
					if(Robot.elevator.targetPos <= Robot.elevator.oldTargetPos){ //might stop the elevator too soon
						
						liftToSwitch = false;
						turnToSwitch = true;
						
						startTime = timer.get();
						
					}
					
				}
				
				if(turnToSwitch) {
					
					targetAngle = 180;

					angleCorrection = pidControlTurn.Run(Robot.driveAngle.getDouble(0), targetAngle);

					motorOutput = angleCorrection * RobotMap.AUTO_TURN_SPEED;

					Robot.driveTrain.autoDrive(motorOutput, -motorOutput);  

					//Check elapsed time
					stopTime = 5;
					if(stopTime<=timer.get()-startTime) {

						//Too much time has elapsed.  Stop this command
						turnIntoPath = false;

					} else {

						angleError = Robot.driveAngle.getDouble(0) - targetAngle;
						if (Math.abs(angleError) <= RobotMap.TURN_ANGLE_TOLERANCE) { //check the angle error against our tolerance

							turnToSwitch = false;
							ejectCube = true;
							angleCorrection = 0;
							angleError = 0;
							
						}

					}
					
				}
				
				//might need to drive forward a bit here
				
				if(ejectCube) {
					
					stopTime = 1; //time to eject cube

					Robot.end.endeffector(-1.0);

					if(stopTime <= timer.get()-startTime) {

						Robot.end.endeffector(0);
						ejectCube = false;
						backUp = true;

						startTime = timer.get(); //same drill as usual

					}
					
				}

			}

		}

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {

		stopTime = 15;
		startTime = timer.get();

		if(stopTime <= timer.get()-startTime)

			return true;

		else {

			return false;

		}
		
	}

	// Called once after isFinished returns true
	protected void end() {
		
		//make sure everything has stopped moving
		Robot.end.angleMotor(0);
		Robot.end.endeffector(0);
		Robot.driveTrain.autoStop();
		
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}

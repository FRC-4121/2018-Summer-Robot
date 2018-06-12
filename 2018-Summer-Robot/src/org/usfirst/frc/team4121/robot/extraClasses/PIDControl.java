package org.usfirst.frc.team4121.robot.extraClasses;

/*
*  This class implements a basic PID control algorithm	
*/
public class PIDControl {
	
	//Declare variables
	private double kP, kI, kD = 1;
	private double targetError, previousError;
	private double errorSum, errorChange;
	private double timeStep = 0.02;  //update time of robot code, 20 ms
	private double correctionFactor;
	
	
	//Default constructor
	public PIDControl(double gainFactor, double integralFactor, double derivativeFactor) {
		
		kP = gainFactor;
		kI = integralFactor;
		kD = derivativeFactor;
		
		Reset();
		
	}
	
	
	//Runs PID control calculations
	public double Run(double sensorReading, double targetValue) {
		
		//Reset correction factor
		correctionFactor = 0;
		
		//Calculate current error
		targetError = sensorReading - targetValue;
		
		//Calculate new correction
		errorSum += (targetError * timeStep);
		errorChange = (targetError - previousError) / timeStep;
		correctionFactor = kP*targetError + kI*errorSum + kD*errorChange;
		
		//Set previous error
		previousError = targetError;
		
		//Return correction factor
		return correctionFactor;
		
	}
	
	
	public void Reset() {
		
		errorSum = 0;
		errorChange = 0;
		targetError = 0;
		previousError = 0;
		
	}
	
}
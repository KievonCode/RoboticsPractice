package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj.TimedRobot;


public class Robot extends TimedRobot {

  private final int LEFT_MOTOR_ID = 13;
  private final int RIGHT_MOTOR_ID = 11;

  private CANSparkMax leftMotor;
  private CANSparkMax rightMotor;

  private XboxController driverController;

  private DifferentialDrive robotDrive;

  private Timer timer;


  @Override
  public void robotInit() {

      timer = new Timer();
      // Code to initialize robot components
      this.leftMotor = new CANSparkMax(LEFT_MOTOR_ID, MotorType.kBrushless);
      this.rightMotor = new CANSparkMax(RIGHT_MOTOR_ID, MotorType.kBrushless);

          // Reset motor controllers to factory defaults
      this.leftMotor.restoreFactoryDefaults();
      this.rightMotor.restoreFactoryDefaults();
      
      // Set current limits for the motors to prevent overloading
      this.leftMotor.setSmartCurrentLimit(60, 20);
      this.rightMotor.setSmartCurrentLimit(60, 20);

      this.leftMotor.setInverted(true);
      this.rightMotor.setInverted(false);

      // Set motor modes (for braking, etc.)
      this.leftMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
      this.rightMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);

      this.robotDrive = new DifferentialDrive(this.leftMotor, this.rightMotor);

      this.robotDrive.setMaxOutput(0.25); // Limit max speed for safety
      this.robotDrive.setSafetyEnabled(true); // Enable motor safety on the drive object
      this.robotDrive.setExpiration(0.1);
    
      this.drivercontroller = new XboxController(0);
    

  }

  @Override
  public void teleopPeriodic() {
      // Code for periodic tasks during teleop
      robotDrive.arcadeDrive(-driverController.getLeftY(), -driverController.getRightX());
      if (driverController.getLeftBumper()) {
        // switch to brakes when left bumper is pressed
        this.leftMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        this.rightMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

      } else {
        // release brake (coast) when left bumper is not pressed
        this.leftMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
        this.rightMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
      }

  }

  @Override
  public void disabledInit() {
      // Code to run once when robot is disabled
      this.leftMotor.setIdleMode(IdleMode.kBrake);
      this.rightMotor.setIdleMode(IdleMode.kBrake);

      timer.reset();
      timer.start();
  }

  @Override
  public void disabledPeriodic() {
      // Code for periodic tasks during Disabled mode
      if (timer.hasElapsed(seconds:3.0)) {
        this.leftMotor.setIdleMode(IdleMode.kCoast);
        this.rightMotor.setIdleMode(IdleMode.kCoast);
        timer.stop();
      }
  }
}

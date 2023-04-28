// Copyright (c) Brandon Pacewic
// SPDX-License-Identifier: MIT WITH FIRST-exception

package org.firstinspires.ftc.teamcode.common.hardware

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.GrabberSubsystem
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.LiftSubsystem
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.CRPivotSubsystem
import org.firstinspires.ftc.teamcode.common.drive.drivetrain.Drivetrain
import org.firstinspires.ftc.teamcode.common.drive.drivetrain.MecanumDrivetrain
import org.firstinspires.ftc.teamcode.common.drive.geometry.Pose
import org.firstinspires.ftc.teamcode.common.drive.localization.Localizer
import org.firstinspires.ftc.teamcode.common.drive.localization.TwoWheelLocalizer

/**
 * Robot controller class wrapping all hardware and subsystems.
 */
class Robot(
    hardwareMap: HardwareMap, 
    private val opMode: OpMode = OpMode.TELEOP
) {
    enum class OpMode {
        TELEOP,
        AUTO,
        TESTING
    }

    var drive: Drivetrain
    var grabber: GrabberSubsystem
    var lift: LiftSubsystem
    var pivot: CRPivotSubsystem

    // Encoders and localizer are only instantiated in autonomous as it
    // costs unnecessary time in teleop as the localizer is not used during
    // driver control.
    var horizontalEncoder: Encoder? = null
    var lateralEncoder: Encoder? = null
    var localizer: Localizer? = null

    @Volatile private var imuAngle: Double = 0.0

    init {
        drive = MecanumDrivetrain(hardwareMap)
        grabber = GrabberSubsystem(hardwareMap)
        lift = LiftSubsystem(hardwareMap, opMode)
        pivot = CRPivotSubsystem(hardwareMap)

        if (opMode == OpMode.AUTO) {
            // TODO: Update encoder names.
            horizontalEncoder = 
                Encoder(hardwareMap.get(DcMotorEx::class.java, "motorFrontRight"))
            lateralEncoder = 
                Encoder(hardwareMap.get(DcMotorEx::class.java, "motorBackRight"))

            // TODO: Reverse encoder directions if necessary.

            localizer = TwoWheelLocalizer(
                { horizontalEncoder!!.getCurrentPosition() },
                Pose(0.0, -4.0, 0.0),
                { lateralEncoder!!.getCurrentPosition() },
                Pose(4.0, -4.0, Math.toRadians(90.0)),
                { getHeading() }
            )
        }
    }

    /**
     * Starts the IMU thread.
     */
    fun startImuThread(opMode: LinearOpMode, hardwareMap: HardwareMap) {
        val imuThread = Thread {
            val imu = hardwareMap.get(IMU::class.java, "imu")
            val parameters = IMU.Parameters(RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
            ))
            imu.initialize(parameters)

            while (!opMode.isStopRequested && opMode.opModeIsActive()) {
                synchronized(imu) {
                    imuAngle = -imu.robotYawPitchRollAngles.getYaw(AngleUnit.RADIANS)
                }
            }
        }
        imuThread.start()
    }

    /**
     * Gets the current heading angle of the robot.
     *
     * This function is not synchronized as it does not modify the volatile imuAngle.
     */
    fun getHeading(): Double {
        return imuAngle
    }
}

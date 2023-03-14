package org.firstinspires.ftc.teamcode.opmode.teleop

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

import org.firstinspires.ftc.teamcode.common.control.OpModeEx
import org.firstinspires.ftc.teamcode.common.drive.drivetrain.TankDrivetrain
import org.firstinspires.ftc.teamcode.common.drive.geometry.Pose

@Disabled
@TeleOp(name = "Tank Drive")
class TankDrive : OpModeEx() {
    lateinit var drive: TankDrivetrain
    var motorPowerFactor = 0.3

    override fun initialize() {
        drive = TankDrivetrain(hardwareMap)
    }

    override fun run() {
        if (gamepad1.x) {
            motorPowerFactor = 0.3
        } else if (gamepad1.y) {
            motorPowerFactor = 1.0
        }

        drive.setDrivePower(Pose(
            -gamepad1.left_stick_y.toDouble() * motorPowerFactor,
            -gamepad1.right_stick_y.toDouble() * motorPowerFactor,
        ))
    }
}

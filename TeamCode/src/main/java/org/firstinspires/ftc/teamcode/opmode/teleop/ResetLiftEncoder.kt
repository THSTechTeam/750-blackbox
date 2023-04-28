package org.firstinspires.ftc.teamcode.opmode.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor

import org.firstinspires.ftc.teamcode.common.hardware.Robot

/**
 * Run this opmode to reset the lift encoder of the PowerPlay robot.
 */
@TeleOp(name = "Reset Lift Encoder")
class ResetLiftEncoder : LinearOpMode() {
    override fun runOpMode() {
        val robot = Robot(hardwareMap, Robot.OpMode.TESTING)

        telemetry.addLine("Ready to reset lift encoder.")
        telemetry.addLine("Start the opmode to reset the lift encoder.")
        telemetry.update()

        waitForStart()

        robot.lift.lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }
}

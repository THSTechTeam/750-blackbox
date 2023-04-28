package org.firstinspires.ftc.teamcode.opmode.teleop

import com.arcrobotics.ftclib.command.CommandScheduler
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor

import org.firstinspires.ftc.teamcode.common.commandbase.commands.GrabberStateCommand
import org.firstinspires.ftc.teamcode.common.commandbase.commands.LiftPositionCommand
import org.firstinspires.ftc.teamcode.common.control.OpModeEx
import org.firstinspires.ftc.teamcode.common.drive.geometry.Pose
import org.firstinspires.ftc.teamcode.common.hardware.Robot

import kotlin.math.abs

@TeleOp(name = "Power Play")
class PowerPlay : OpModeEx() {
    companion object {
        const val baseDriveSpeed = 0.8
        const val driveSpeedFactor = 0.6

        const val manualLiftIncrement = 60

        const val manualPivotIncrement = 0.35
        const val pivotSpeed = 0.7
    }

    lateinit var robot: Robot

    override fun initialize() {
        CommandScheduler.getInstance().reset()
        
        robot = Robot(hardwareMap)
    }

    override fun run() {
        /**
         * Drive config, uses gamepad1.
         */
        val speedFactor = baseDriveSpeed - (driveSpeedFactor * gamepad1.right_trigger.toDouble())
        telemetry.addData("Speed Factor", speedFactor)

        robot.drive.setDrivePower(
            Pose(
                -gamepad1.left_stick_y.toDouble(),
                gamepad1.left_stick_x.toDouble(),
                gamepad1.right_stick_x.toDouble()
            ).times(speedFactor)
        )

        /**
         * Arm / Lift config, uses gamepad2.
         */

        // Preset lift positions.
        if (gamepad2.a) {
            schedule(LiftPositionCommand(robot.lift, LiftPositionCommand.Position.GROUND))
        } else if (gamepad2.x) {
            schedule(LiftPositionCommand(robot.lift, LiftPositionCommand.Position.LOW))
        } else if (gamepad2.y) {
            schedule(LiftPositionCommand(robot.lift, LiftPositionCommand.Position.MEDIUM))
        } else if (gamepad2.b) {
            schedule(LiftPositionCommand(robot.lift, LiftPositionCommand.Position.HIGH))
        } else if (gamepad2.dpad_up) {
            schedule(LiftPositionCommand(robot.lift, LiftPositionCommand.Position.CONE_STACK_5))
        } else if (gamepad2.dpad_left) {
            schedule(LiftPositionCommand(robot.lift, LiftPositionCommand.Position.CONE_STACK_4))
        } else if (gamepad2.dpad_down) {
            schedule(LiftPositionCommand(robot.lift, LiftPositionCommand.Position.CONE_STACK_3))
        }

        // Manual lift control.
        if (abs(gamepad2.left_stick_y) > 0.1) {
            robot.lift.targetPosition = (robot.lift.targetPosition - (gamepad2.left_stick_y * manualLiftIncrement)).toInt()
        }

        // Resets the lift encoder
        if (gamepad2.dpad_right && !robot.lift.isBusy) {
            robot.lift.lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            robot.lift.targetPosition = 0
        }

        // Pivot control.
        if (abs(gamepad2.right_stick_x) > 0.1) {
            robot.pivot.pivot.setPower(gamepad2.right_stick_x * pivotSpeed)
        } else {
            robot.pivot.pivot.setPower(0.0)
        }

        // Grabber control.
        if (gamepad2.right_trigger > 0.5) {
            schedule(GrabberStateCommand(robot.grabber, GrabberStateCommand.State.CLOSE))
        } else if (robot.grabber.state == GrabberStateCommand.State.CLOSE) {
            schedule(GrabberStateCommand(robot.grabber, GrabberStateCommand.State.OPEN))
        }

        CommandScheduler.getInstance().run()
        robot.lift.update()
        telemetry.update()
    }
}

package org.firstinspires.ftc.teamcode.common.commandbase.subsystem

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

import org.firstinspires.ftc.teamcode.common.hardware.Robot.OpMode
import org.firstinspires.ftc.teamcode.common.control.PIDController

import kotlin.math.abs

class LiftSubsystem(
    hardwareMap: HardwareMap, 
    opMode: OpMode
) : SubsystemBase() {
    companion object {
        const val maxStartingDifference = 50
    }

    var controller = PIDController(0.003, 0.0, 0.001)
    var lift: DcMotorEx
    var targetPosition = 0

    var isBusy = false

    init {
        lift = hardwareMap.get(DcMotorEx::class.java, "motorLift")
        lift.direction = DcMotorSimple.Direction.REVERSE
 
        val motorConfigurationType = lift.motorType.clone()
        motorConfigurationType.achieveableMaxRPMFraction = 1.0
        lift.motorType = motorConfigurationType
        lift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        if (abs(lift.currentPosition) > maxStartingDifference && !opMode == OpMode.TESTING) {
            throw RuntimeException("Lift encoder is not zeroed. Run the ResetLiftEncoder OpMode.")
        }
    }

    fun update() {
        lift.power = controller.calculate(lift.currentPosition.toDouble(), targetPosition.toDouble())
    }
}

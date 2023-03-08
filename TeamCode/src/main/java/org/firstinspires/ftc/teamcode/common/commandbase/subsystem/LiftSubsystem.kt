package org.firstinspires.ftc.teamcode.common.commandbase.subsystem

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

import org.firstinspires.ftc.teamcode.common.hardware.Robot.OpMode
import org.firstinspires.ftc.teamcode.common.control.PIDController

class LiftSubsystem(
    hardwareMap: HardwareMap, 
    opMode: OpMode
) : SubsystemBase() {
    var controller = PIDController(0.003, 0.0, 0.001)
    var lift: DcMotorEx
    var targetPosition: Int? = null
    
    init {
        lift = hardwareMap.get(DcMotorEx::class.java, "motorLift")
        lift.direction = DcMotorSimple.Direction.REVERSE

        val motorConfigurationType = lift.motorType.clone()
        motorConfigurationType.achieveableMaxRPMFraction = 1.0
        lift.motorType = motorConfigurationType
        lift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        if (opMode == OpMode.AUTO) {
            lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        }
    }

    fun update() {
        if (targetPosition == null) {
            return
        }

        lift.power = controller.calculate(lift.currentPosition.toDouble(), targetPosition!!.toDouble())
    }
}

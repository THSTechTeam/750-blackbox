package org.firstinspires.ftc.teamcode.common.commandbase.subsystem

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.HardwareMap

import org.firstinspires.ftc.teamcode.common.hardware.CRServoWrapper

class CRPivotSubsystem(hardwareMap: HardwareMap) : SubsystemBase() {
    var pivot: CRServoWrapper

    init {
        pivot = CRServoWrapper(hardwareMap.get(com.qualcomm.robotcore.hardware.CRServo::class.java, "servoPivot"))
        pivot.setDirection(CRServoWrapper.Direction.REVERSE)
    }
}

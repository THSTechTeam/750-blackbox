package org.firstinspires.ftc.teamcode.common.commandbase.subsystem

import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.HardwareMap

import org.firstinspires.ftc.teamcode.common.commandbase.commands.GrabberStateCommand
import org.firstinspires.ftc.teamcode.common.hardware.CRServoWrapper

class GrabberSubsystem(hardwareMap: HardwareMap) : SubsystemBase() {
    var grabber: CRServoWrapper
    var state = GrabberStateCommand.State.OPEN

    init {
        grabber = CRServoWrapper(hardwareMap.get(CRServo::class.java, "servoGrabber"))
        grabber.setDirection(CRServoWrapper.Direction.REVERSE)
    }
}

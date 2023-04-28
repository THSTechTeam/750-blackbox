package org.firstinspires.ftc.teamcode.common.commandbase.commands

import com.arcrobotics.ftclib.command.CommandBase
import com.qualcomm.robotcore.util.ElapsedTime

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.GrabberSubsystem

class GrabberStateCommand(
    var grabberSubsystem: GrabberSubsystem,
    var state: State
) : CommandBase() {
    enum class State(val power: Double) {
        OPEN(0.7),
        CLOSE(0.0)
    }

    var timer: ElapsedTime? = null
    val timeUntilFinished = 0.25

    override fun execute() {
        if (timer != null) {
            return
        }

        timer = ElapsedTime()
        grabberSubsystem.state = state
        grabberSubsystem.grabber.setPower(state.power)
    }

    override fun isFinished(): Boolean {
        return if (timer == null) {
            false
        } else {
            timer!!.seconds() >= timeUntilFinished
        }
    }
}

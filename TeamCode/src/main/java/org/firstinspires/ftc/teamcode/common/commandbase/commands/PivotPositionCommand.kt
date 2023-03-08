package org.firstinspires.ftc.teamcode.common.commandbase.commands

import com.arcrobotics.ftclib.command.CommandBase
import com.qualcomm.robotcore.util.ElapsedTime

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.PivotSubsystem

import kotlin.math.abs

class PivotPositionCommand : CommandBase {
    enum class Position(val tick: Double) {
        FORWARD(0.0),
        COUNTER90(0.5),
        COUNTER180(1.0)
    }

    var PivotSubsystem: PivotSubsystem
    var position = Position.FORWARD.tick
    val allowedError = 0.05

    var timer: ElapsedTime? = null
    val timeOut = 5.0

    constructor(PivotSubsystem: PivotSubsystem, position: Position) {
        this.PivotSubsystem = PivotSubsystem
        this.position = position.tick
    }

    constructor(PivotSubsystem: PivotSubsystem, position: Double) {
        this.PivotSubsystem = PivotSubsystem
        this.position = position
    }

    override fun execute() {
        if (timer != null) {
            return
        }

        timer = ElapsedTime()
        PivotSubsystem.pivot.position = position
    }

    override fun isFinished(): Boolean {
        return if (timer == null) {
            false
        } else {
            abs(PivotSubsystem.pivot.position - position) <= allowedError || timer!!.seconds() >= timeOut
        }
    }
}

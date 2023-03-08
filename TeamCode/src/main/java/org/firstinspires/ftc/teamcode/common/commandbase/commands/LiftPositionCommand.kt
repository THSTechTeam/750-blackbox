package org.firstinspires.ftc.teamcode.common.commandbase.commands

import com.arcrobotics.ftclib.command.CommandBase
import com.qualcomm.robotcore.util.ElapsedTime

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.LiftSubsystem

import kotlin.math.abs

class LiftPositionCommand : CommandBase {
    companion object {
        private const val startStackPosition = 700
        private const val stackConeOffset = 150
    }

    enum class Position(val tick: Int) {
        HIGH(4300),
        LOWER_THROUGH_HIGH(4000),
        MEDIUM(2980),
        LOW(1900),
        CONE_STACK_5(startStackPosition),
        CONE_STACK_4(startStackPosition - stackConeOffset),
        CONE_STACK_3(startStackPosition - stackConeOffset * 2),
        CONE_STACK_2(startStackPosition - stackConeOffset * 3),
        GROUND(100),
        ZERO(0)
    }

    val allowedError = 50

    var liftSubsystem: LiftSubsystem
    var position = Position.ZERO.tick

    var timer: ElapsedTime? = null
    val timeOut = 10.0

    constructor(liftSubsystem: LiftSubsystem, position: Position) {
        this.liftSubsystem = liftSubsystem
        this.position = position.tick
    }

    constructor(liftSubsystem: LiftSubsystem, position: Int) {
        this.liftSubsystem = liftSubsystem
        this.position = position
    }

    override fun execute() {
        if (timer != null) {
            return
        }

        timer = ElapsedTime()
        liftSubsystem.targetPosition = position
    }

    override fun isFinished(): Boolean {
        return if (timer == null) {
            false
        } else {
            abs(liftSubsystem.lift.currentPosition - position) <= allowedError || timer!!.seconds() >= timeOut
        }
    }
}

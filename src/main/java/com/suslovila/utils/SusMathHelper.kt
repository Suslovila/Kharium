package com.suslovila.utils

import org.lwjgl.opengl.GL11
import java.util.concurrent.ThreadLocalRandom

object SusMathHelper {
    fun glTranslateRandomWithEqualD(factor: Double) = GL11.glTranslated(nextDouble(factor), nextDouble(factor), nextDouble(factor))
    fun nextDouble(n1: Double, n2: Double) = ThreadLocalRandom.current().nextDouble(n1, n2)

    fun nextDouble(n1: Double): Double =
        if (Math.abs(n1) == 0.0) 0.0 else ThreadLocalRandom.current().nextDouble(-n1, n1)
    fun wrapDegrees(p_14178_: Float): Float {
        var f = p_14178_ % 360.0f
        if (f >= 180.0f) {
            f -= 360.0f
        }
        if (f < -180.0f) {
            f += 360.0f
        }
        return f
    }
    fun randomSign() = if (SusUtils.random.nextBoolean()) 1 else -1

}
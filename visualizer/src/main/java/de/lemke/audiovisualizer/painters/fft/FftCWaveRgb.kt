package de.lemke.audiovisualizer.painters.fft

import android.graphics.*
import de.lemke.audiovisualizer.painters.Painter
import de.lemke.audiovisualizer.painters.Painter.Companion.Direction
import de.lemke.audiovisualizer.painters.Painter.Companion.Direction.UP_OUT
import de.lemke.audiovisualizer.painters.Painter.Companion.Interpolator
import de.lemke.audiovisualizer.painters.Painter.Companion.Interpolator.SPLINE
import de.lemke.audiovisualizer.utils.VisualizerHelper

class FftCWaveRgb(
    flags: Int = Paint.ANTI_ALIAS_FLAG,
    var color: List<Int> = listOf(Color.RED, Color.GREEN, Color.BLUE),
    startHz: Int = 0,
    endHz: Int = 2000,
    num: Int = 128,
    interpolator: Interpolator = SPLINE,
    direction: Direction = UP_OUT,
    mirror: Boolean = false,
    power: Boolean = true,
    radiusR: Float = .4f,
    ampR: Float = .6f,
    var rot: Float = 10f,
) : Painter() {

    override var paint: Paint = Paint()
    private val wave = FftCWave(Paint(flags).apply {
        style = Paint.Style.FILL;xfermode = PorterDuffXfermode(PorterDuff.Mode.ADD)
    }, startHz, endHz, num, interpolator, direction, mirror, power, radiusR, ampR)

    override fun calc(helper: VisualizerHelper) {
        wave.calc(helper)
    }

    override fun draw(canvas: Canvas, helper: VisualizerHelper) {
        wave.paint.color = color[0]
        wave.draw(canvas, helper)
        rotateHelper(canvas, rot, .5f, .5f) {
            wave.paint.color = color[1]
            wave.draw(canvas, helper)
        }
        rotateHelper(canvas, rot * 2, .5f, .5f) {
            wave.paint.color = color[2]
            wave.draw(canvas, helper)
        }
    }
}
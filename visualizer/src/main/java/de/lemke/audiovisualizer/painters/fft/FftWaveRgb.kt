package de.lemke.audiovisualizer.painters.fft

import android.graphics.*
import de.lemke.audiovisualizer.painters.Painter
import de.lemke.audiovisualizer.painters.Painter.Companion.Direction
import de.lemke.audiovisualizer.painters.Painter.Companion.Direction.UP_OUT
import de.lemke.audiovisualizer.painters.Painter.Companion.Interpolator
import de.lemke.audiovisualizer.painters.Painter.Companion.Interpolator.SPLINE
import de.lemke.audiovisualizer.utils.VisualizerHelper

class FftWaveRgb(
    flags: Int = Paint.ANTI_ALIAS_FLAG,
    var color: List<Int> = listOf(Color.RED, Color.GREEN, Color.BLUE),
    startHz: Int = 0,
    endHz: Int = 2000,
    num: Int = 128,
    interpolator: Interpolator = SPLINE,
    direction: Direction = UP_OUT,
    mirror: Boolean = false,
    power: Boolean = false,
    ampR: Float = 1f,
) : Painter() {

    override var paint: Paint = Paint()
    private val wave = FftWave(Paint(flags).apply {
        style = Paint.Style.FILL;xfermode = PorterDuffXfermode(PorterDuff.Mode.ADD)
    }, startHz, endHz, num, interpolator, direction, mirror, power, ampR)

    override fun calc(helper: VisualizerHelper) {
        wave.calc(helper)
    }

    override fun draw(canvas: Canvas, helper: VisualizerHelper) {
        canvas.save()
        canvas.scale(1.2f, 1f, canvas.width / 2f, canvas.height.toFloat())
        drawHelper(canvas, UP_OUT, -.03f, 0f) {
            wave.paint.color = color[0]
            wave.draw(canvas, helper)
        }
        drawHelper(canvas, UP_OUT, 0f, 0f) {
            wave.paint.color = color[1]
            wave.draw(canvas, helper)
        }
        drawHelper(canvas, UP_OUT, .03f, 0f) {
            wave.paint.color = color[2]
            wave.draw(canvas, helper)
        }
        canvas.restore()
    }
}
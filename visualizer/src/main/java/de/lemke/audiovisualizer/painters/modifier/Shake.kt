package de.lemke.audiovisualizer.painters.modifier

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import de.lemke.audiovisualizer.painters.Painter
import de.lemke.audiovisualizer.painters.Painter.Companion.Direction.UP_OUT
import de.lemke.audiovisualizer.utils.VisualizerHelper

class Shake(
    vararg val painters: Painter,
    var animX: ValueAnimator = ValueAnimator.ofFloat(0f, .01f, 0f, -.01f, 0f).apply {
        duration = 16000;repeatCount = ValueAnimator.INFINITE
    },
    var animY: ValueAnimator = ValueAnimator.ofFloat(0f, .01f, 0f, -.01f, 0f).apply {
        duration = 8000;repeatCount = ValueAnimator.INFINITE
    }
) : Painter() {

    override var paint = Paint()

    init {
        animX.start()
        animY.start()
    }

    override fun calc(helper: VisualizerHelper) {
        painters.forEach { painter ->
            painter.calc(helper)
        }

    }

    override fun draw(canvas: Canvas, helper: VisualizerHelper) {
        canvas.save()
        drawHelper(canvas, UP_OUT, animX.animatedValue as Float, animY.animatedValue as Float) {
            painters.forEach { painter ->
                painter.paint.apply { colorFilter = paint.colorFilter;xfermode = paint.xfermode }
                painter.draw(canvas, helper)
            }
        }
        canvas.restore()
    }
}
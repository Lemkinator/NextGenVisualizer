package de.lemke.audiovisualizer.painters.modifier

import android.graphics.Canvas
import android.graphics.Paint
import de.lemke.audiovisualizer.painters.Painter
import de.lemke.audiovisualizer.utils.VisualizerHelper

class Move(
    vararg val painters: Painter,
    var xRelative: Float = 0f,
    var yRelative: Float = 0f,
) : Painter() {

    override var paint = Paint()

    override fun calc(helper: VisualizerHelper) {
        painters.forEach { painter ->
            painter.calc(helper)
        }
    }

    override fun draw(canvas: Canvas, helper: VisualizerHelper) {
        canvas.save()
        canvas.translate(canvas.width * xRelative, canvas.height * yRelative)
        painters.forEach { painter ->
            painter.paint.apply { colorFilter = paint.colorFilter;xfermode = paint.xfermode }
            painter.draw(canvas, helper)
        }
        canvas.restore()
    }
}
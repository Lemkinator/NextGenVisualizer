package de.lemke.audiovisualizer.painters.misc

import android.graphics.*
import de.lemke.audiovisualizer.painters.Painter
import de.lemke.audiovisualizer.painters.Painter.Companion.Direction.UP_OUT
import de.lemke.audiovisualizer.utils.VisualizerHelper
import kotlin.math.min

class Icon(
    var bitmap: Bitmap,
    var radiusR: Float = .3f,
    override var paint: Paint = Paint(),
) : Painter() {

    private val matrix = Matrix()

    companion object {
        fun getCircledBitmap(bitmap: Bitmap): Bitmap {
            val tmpBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val tmpCanvas = Canvas(tmpBitmap)
            val tmpPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            val tmpRect = Rect(0, 0, bitmap.width, bitmap.height)
            tmpCanvas.drawARGB(0, 0, 0, 0)
            tmpCanvas.drawCircle(bitmap.width / 2f, bitmap.height / 2f, bitmap.width / 2f, tmpPaint)
            tmpPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            tmpCanvas.drawBitmap(bitmap, tmpRect, tmpRect, tmpPaint)
            return tmpBitmap
        }
    }

    override fun calc(helper: VisualizerHelper) {}

    override fun draw(canvas: Canvas, helper: VisualizerHelper) {
        val shortest = min(canvas.width, canvas.height)
        bitmap.apply bitmap@{
            val radius = shortest * radiusR
            matrix.apply {
                postScale(radius / this@bitmap.width, radius / this@bitmap.width)
                postTranslate(-radius / 2f, -radius / 2f)
            }
            drawHelper(canvas, UP_OUT, .5f, .5f) {
                canvas.drawBitmap(this, matrix, paint)
            }
            matrix.reset()
        }
    }
}

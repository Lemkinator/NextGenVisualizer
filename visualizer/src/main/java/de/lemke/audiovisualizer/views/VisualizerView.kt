package de.lemke.audiovisualizer.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import de.lemke.audiovisualizer.painters.Painter
import de.lemke.audiovisualizer.painters.misc.SimpleText
import de.lemke.audiovisualizer.painters.modifier.Compose
import de.lemke.audiovisualizer.utils.FrameManager
import de.lemke.audiovisualizer.utils.VisualizerHelper

class VisualizerView : View {

    private val frameManager = FrameManager()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val simpleText: SimpleText = SimpleText().apply { paint.textSize = 12 * resources.displayMetrics.density } //12dp
    private lateinit var painter: Painter
    private lateinit var visualizerHelper: VisualizerHelper

    private val anim = true
    private val fps = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setup(visualizerHelper: VisualizerHelper, painter: Painter) {
        this.visualizerHelper = visualizerHelper
        this.painter = Compose(painter, simpleText)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (this::painter.isInitialized && this::visualizerHelper.isInitialized) {
            setLayerType(LAYER_TYPE_HARDWARE, paint)
            canvas.apply {
                simpleText.text = if (fps) "FPS: ${frameManager.fps()}" else ""
                painter.calc(visualizerHelper)
                painter.draw(canvas, visualizerHelper)
            }
            frameManager.tick()
            if (anim) invalidate()
        }
    }
}
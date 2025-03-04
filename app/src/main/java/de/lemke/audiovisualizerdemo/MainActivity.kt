package de.lemke.audiovisualizerdemo

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.BitmapFactory
import android.graphics.Paint.Cap.ROUND
import android.graphics.Paint.Style.FILL
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import de.lemke.audiovisualizer.painters.Painter.Companion.Direction.BOTH
import de.lemke.audiovisualizer.painters.Painter.Companion.Direction.DOWN_IN
import de.lemke.audiovisualizer.painters.fft.*
import de.lemke.audiovisualizer.painters.misc.Gradient
import de.lemke.audiovisualizer.painters.misc.Gradient.Companion.LINEAR_HORIZONTAL
import de.lemke.audiovisualizer.painters.misc.Gradient.Companion.LINEAR_VERTICAL
import de.lemke.audiovisualizer.painters.misc.Gradient.Companion.LINEAR_VERTICAL_MIRROR
import de.lemke.audiovisualizer.painters.misc.Gradient.Companion.RADIAL
import de.lemke.audiovisualizer.painters.misc.Gradient.Companion.SWEEP
import de.lemke.audiovisualizer.painters.misc.Icon
import de.lemke.audiovisualizer.painters.modifier.*
import de.lemke.audiovisualizer.painters.waveform.WfmAnalog
import de.lemke.audiovisualizer.utils.Preset
import de.lemke.audiovisualizer.utils.Preset.Companion.PresetType.ICON
import de.lemke.audiovisualizer.utils.Preset.Companion.PresetType.LIVE_BG
import de.lemke.audiovisualizer.utils.Preset.Companion.PresetType.WAVE_RGB_ICON
import de.lemke.audiovisualizer.utils.VisualizerHelper
import de.lemke.audiovisualizerdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "VisualizerDemo"
    }

    private lateinit var binding: ActivityMainBinding
    private val microphonePermissionCallback: ActivityResultCallback<Boolean> =
        ActivityResultCallback { granted -> if (granted) permissionGranted() else permissionNotGranted() }
    private val activityResultLauncher = registerForActivityResult(RequestPermission(), microphonePermissionCallback)
    private lateinit var helper: VisualizerHelper
    private var current = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestMicrophonePermission()
    }

    fun requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) == PERMISSION_GRANTED) microphonePermissionCallback.onActivityResult(true)
        else activityResultLauncher.launch(RECORD_AUDIO)
    }

    private fun permissionGranted() {
        Log.d(TAG, "Microphone permission granted")
        initVisualizer()
    }

    private fun permissionNotGranted() {
        Log.d(TAG, "Microphone permission not granted")
    }

    private fun initVisualizer() {
        val circleBitmap = Icon.getCircledBitmap(BitmapFactory.decodeResource(resources, R.drawable.chino512))
        helper = VisualizerHelper(0)
        val list = listOf(
            // Basic components
            Compose(
                Move(WfmAnalog(), yRelative = -.3f),
                Move(FftBar(), yRelative = -.1f),
                Move(FftLine(), yRelative = .1f),
                Move(FftWave(), yRelative = .3f),
                Move(FftWaveRgb(), yRelative = .5f)
            ),
            Compose(
                Move(FftBar(direction = DOWN_IN), yRelative = -.3f),
                Move(FftLine(direction = DOWN_IN), yRelative = -.1f),
                Move(FftWave(direction = DOWN_IN), yRelative = .1f),
                Move(FftWaveRgb(direction = DOWN_IN), yRelative = .3f)
            ),
            Compose(
                Move(FftBar(direction = BOTH), yRelative = -.3f),
                Move(FftLine(direction = BOTH), yRelative = -.1f),
                Move(FftWave(direction = BOTH), yRelative = .1f),
                Move(FftWaveRgb(direction = BOTH), yRelative = .3f)
            ),
            // Basic components (Circle)
            Compose(
                Move(FftCLine(), xRelative = -.3f),
                FftCWave(),
                Move(FftCWaveRgb(), xRelative = .3f)
            ),
            Compose(
                Move(FftCLine(direction = DOWN_IN), xRelative = -.3f),
                FftCWave(direction = DOWN_IN),
                Move(FftCWaveRgb(direction = DOWN_IN), xRelative = .3f)
            ),
            Compose(
                Move(FftCLine(direction = BOTH), xRelative = -.3f),
                FftCWave(direction = BOTH),
                Move(FftCWaveRgb(direction = BOTH), xRelative = .3f)
            ),
            //Blend
            Blend(FftLine().apply { paint.strokeWidth = 8f; paint.strokeCap = ROUND }, Gradient(LINEAR_HORIZONTAL)),
            Blend(FftLine().apply { paint.strokeWidth = 8f; paint.strokeCap = ROUND }, Gradient(LINEAR_VERTICAL, hsv = true)),
            Blend(FftLine().apply { paint.strokeWidth = 8f; paint.strokeCap = ROUND }, Gradient(LINEAR_VERTICAL_MIRROR, hsv = true)),
            Blend(FftCLine().apply { paint.strokeWidth = 8f; paint.strokeCap = ROUND }, Gradient(RADIAL)),
            Blend(FftCBar(direction = BOTH, gapX = 8f).apply { paint.style = FILL }, Gradient(SWEEP, hsv = true)),
            // Composition
            Glitch(Beat(Preset.getPresetWithBitmap(ICON, circleBitmap))),
            Compose(
                WfmAnalog().apply { paint.alpha = 150 },
                Shake(Preset.getPresetWithBitmap(WAVE_RGB_ICON, circleBitmap)).apply { animX.duration = 1000; animY.duration = 2000 }),
            Compose(
                Preset.getPresetWithBitmap(LIVE_BG, BitmapFactory.decodeResource(resources, R.drawable.background)),
                FftCLine().apply { paint.strokeWidth = 8f;paint.strokeCap = ROUND }
            )
        )
        binding.visualizerView.setup(helper, list[current])
        binding.next.setOnClickListener {
            Log.d(TAG, "Next")
            current = (current + 1) % list.size
            binding.visualizerView.setup(helper, list[current])
        }
        binding.previous.setOnClickListener {
            Log.d(TAG, "Previous")
            current = (current - 1 + list.size) % list.size
            binding.visualizerView.setup(helper, list[current])
        }
    }

    override fun onDestroy() {
        helper.release()
        super.onDestroy()
    }
}

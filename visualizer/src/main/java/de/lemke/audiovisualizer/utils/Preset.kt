package de.lemke.audiovisualizer.utils

import android.graphics.*
import de.lemke.audiovisualizer.painters.*
import de.lemke.audiovisualizer.painters.fft.*
import de.lemke.audiovisualizer.painters.misc.*
import de.lemke.audiovisualizer.painters.modifier.*
import de.lemke.audiovisualizer.utils.Preset.Companion.PresetType.DEBUG
import de.lemke.audiovisualizer.utils.Preset.Companion.PresetType.ICON
import de.lemke.audiovisualizer.utils.Preset.Companion.PresetType.LIVE_BG
import de.lemke.audiovisualizer.utils.Preset.Companion.PresetType.WAVE_RGB_ICON

class Preset {
    companion object {
        enum class PresetType {
            ICON, WAVE_RGB_ICON, LIVE_BG, DEBUG
        }

        fun getPreset(type: PresetType): Painter {
            return when (type) {
                DEBUG -> FftBar()
                else -> FftBar()
            }
        }

        fun getPresetWithBitmap(type: PresetType, bitmap: Bitmap): Painter {
            return when (type) {
                ICON -> Compose(Rotate(FftCLine()), Icon(Icon.getCircledBitmap(bitmap)))
                WAVE_RGB_ICON -> Compose(Rotate(FftCWaveRgb()), Icon(Icon.getCircledBitmap(bitmap)))
                LIVE_BG -> Scale(Shake(Background(bitmap)), scaleX = 1.02f, scaleY = 1.02f)
                DEBUG -> Icon(bitmap)
                else -> Icon(bitmap)
            }
        }
    }
}
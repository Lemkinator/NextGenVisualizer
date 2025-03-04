package de.lemke.audiovisualizer.utils

import android.media.audiofx.Visualizer
import kotlin.math.hypot

class VisualizerHelper(sessionId: Int) {

    private val visualizer: Visualizer = Visualizer(sessionId)
    private val fftBuff: ByteArray
    private val fftMF: FloatArray
    private val fftM: DoubleArray
    private val waveBuff: ByteArray

    init {
        visualizer.captureSize = Visualizer.getCaptureSizeRange()[1]
        fftBuff = ByteArray(visualizer.captureSize)
        waveBuff = ByteArray(visualizer.captureSize)
        fftMF = FloatArray(fftBuff.size / 2 - 1)
        fftM = DoubleArray(fftBuff.size / 2 - 1)
        visualizer.enabled = true
    }

    fun getFft(): ByteArray {
        if (visualizer.enabled) visualizer.getFft(fftBuff)
        return fftBuff
    }

    fun getWave(): ByteArray {
        if (visualizer.enabled) visualizer.getWaveForm(waveBuff)
        return waveBuff
    }

    fun getFftMagnitude(): DoubleArray {
        getFft()
        for (k in 0 until fftMF.size) {
            val i = (k + 1) * 2
            fftM[k] = hypot(fftBuff[i].toDouble(), fftBuff[i + 1].toDouble())
        }
        return fftM
    }

    /**
     * Get Fft values from startHz to endHz
     */
    fun getFftMagnitudeRange(startHz: Int, endHz: Int): DoubleArray {
        val sIndex = hzToFftIndex(startHz)
        val eIndex = hzToFftIndex(endHz)
        return getFftMagnitude().copyOfRange(sIndex, eIndex)
    }

    /**
     * Equation from documentation, kth frequency = k*Fs/(n/2)
     */
    fun hzToFftIndex(hz: Int): Int {
        return (hz * 1024 / (44100 * 2)).coerceAtLeast(0).coerceAtMost(255)
    }

    /**
     * Release visualizer when not using anymore
     */
    fun release() {
        visualizer.enabled = false
        visualizer.release()
    }

}
package com.norm.mysynthesizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.math.exp
import kotlin.math.ln

class WavetableSynthesizerViewModel : ViewModel() {
    var wavetableSynthesizer: WavetableSynthesizer? = null
        set(value) {
            field = value
            applayParameters()
        }
    private val _frequency = MutableLiveData(300f)
    val frequency: LiveData<Float>
        get() {
            return _frequency
        }

    fun setFrequencySliderPosition(frequencySliderPositions: Float) {
        val frequencyInHz = frequencyInHzSliderPositions(frequencySliderPositions)
        _frequency.value = frequencyInHz
        viewModelScope.launch {
            wavetableSynthesizer?.setFrequency(frequencyInHz)
        }
    }

    private val frequencyRange = 40f..3000f
    private fun frequencyInHzSliderPositions(sliderPositions: Float): Float {
        val rangePosition = linerToExponential(sliderPositions)
        return valueFromRangePosition(frequencyRange, rangePosition)
    }

    fun sliderPositionFromFrequencyInHz(frequencyInHz: Float): Float {
        val rangePosition = rangePositionFromValue(frequencyRange, frequencyInHz)
        return exponentialToLiner(rangePosition)
    }

    companion object LinerToExponentialConverter {
        private const val MINIMUM_VALUE = 0.001f

        fun linerToExponential(value: Float): Float {
            assert(value in 0f..1f)

            if (value < MINIMUM_VALUE) {
                return 0f
            }

            return exp(ln(MINIMUM_VALUE) - ln(MINIMUM_VALUE) * value)
        }

        fun valueFromRangePosition(range: ClosedFloatingPointRange<Float>, rangePositions: Float) =
            range.start + (range.endInclusive - range.start) * rangePositions

        fun rangePositionFromValue(range: ClosedFloatingPointRange<Float>, value: Float): Float {
            assert(value in range)

            return (value - range.start) / (range.endInclusive - range.start)
        }

        fun exponentialToLiner(rangePosition: Float): Float {
            assert(rangePosition in 0f..1f)

            if (rangePosition < MINIMUM_VALUE) {
                return rangePosition
            }

            return (ln(rangePosition) - ln(MINIMUM_VALUE) / (-ln(MINIMUM_VALUE)))
        }
    }

    private val _volume = MutableLiveData(-24f)
    val volume: LiveData<Float>
        get() {
            return _volume
        }

    val volumeRange = (-60f)..0f

    fun setVolume(volumeInDb: Float) {
        _volume.value = volumeInDb
        viewModelScope.launch {
            wavetableSynthesizer?.setVolume(volumeInDb)
        }
    }

    private var wavetable = Wavetable.SINE
    fun setWavetable(newWavetable: Wavetable) {
        wavetable = newWavetable
        viewModelScope.launch {
            wavetableSynthesizer?.setWavetable(newWavetable)
        }
    }

    private val _playButtonLabel = MutableLiveData(R.string.play)

    val playButtonLabel: LiveData<Int>
        get() {
            return _playButtonLabel
        }

    fun playClicked() {
        viewModelScope.launch {
            if (wavetableSynthesizer?.isPlaying() == true) {
                wavetableSynthesizer?.stop()
            } else {
                wavetableSynthesizer?.play()
            }
            updatePlayButtonLabel()
        }
    }

    private fun updatePlayButtonLabel() {
        viewModelScope.launch {
            if (wavetableSynthesizer?.isPlaying() == true) {
                _playButtonLabel.value = R.string.stop
            } else {
                _playButtonLabel.value = R.string.play
            }
        }
    }

    fun applayParameters() {
        viewModelScope.launch {
            wavetableSynthesizer?.setFrequency(frequency.value!!)
            wavetableSynthesizer?.setWavetable(wavetable)
            updatePlayButtonLabel()
        }
    }
}
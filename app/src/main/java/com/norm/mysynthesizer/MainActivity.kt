package com.norm.mysynthesizer

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.norm.mysynthesizer.ui.theme.MySynthesizerTheme

class MainActivity : ComponentActivity() {
    private val synthesizerViewModel: WavetableSynthesizerViewModel by viewModels()
    private val synthesizer = LoggingWavetableSynthesizer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        synthesizerViewModel.wavetableSynthesizer = synthesizer

        setContent {
            MySynthesizerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SynthesizerApp(Modifier, synthesizerViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        synthesizerViewModel.applayParameters()
    }
}

@Composable
fun SynthesizerApp(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Synthesizer App"
        )
        WavetableSelectionPanel(modifier, synthesizerViewModel)
        ControlPanel(modifier, synthesizerViewModel)
    }
}

@Composable
fun WavetableSelectionPanel(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.wavetable),
            )
            WavetableSelectionsButtons(modifier, synthesizerViewModel)
        }

    }
}

@Composable
fun WavetableSelectionsButtons(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (wavetable in Wavetable.values()) {
            WavetableButton(
                modifier = modifier,
                onClick = {
                    synthesizerViewModel.setWavetable(wavetable)
                },
                label = stringResource(wavetable.toResourceString())
            )
        }
    }
}

@Composable
fun WavetableButton(
    modifier: Modifier,
    onClick: () -> Unit,
    label: String
) {
    Button(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(
            text = label
        )
    }
}

@Composable
fun ControlPanel(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PitchControl(modifier, synthesizerViewModel)
            PlayControl(modifier, synthesizerViewModel)
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VolumeControl(modifier, synthesizerViewModel)
        }
    }
}

@Composable
fun PitchControl(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    val frequency = synthesizerViewModel.frequency.observeAsState()

    val sliderPosition = rememberSaveable {
        mutableStateOf(
            synthesizerViewModel.sliderPositionFromFrequencyInHz(frequency.value!!)
        )
    }


    PitchControlContent(
        modifier,
        pitchControlLabel = stringResource(R.string.frequency),
        value = sliderPosition.value,
        onValueChange = {
            sliderPosition.value = it
            synthesizerViewModel.setFrequencySliderPosition(it)
        },
        valueRange = 0F..1F,
        frequencyValueLabel = stringResource(R.string.frequency_value, frequency.value!!)
    )
}

@Composable
fun PitchControlContent(
    modifier: Modifier,
    pitchControlLabel: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    frequencyValueLabel: String
) {
    Text(text = pitchControlLabel)
    Slider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange
    )
    Text(text = frequencyValueLabel)
}

@Composable
fun PlayControl(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    val playButtonLabel = synthesizerViewModel.playButtonLabel.observeAsState()
    Button(
        modifier = modifier,
        onClick = {
            synthesizerViewModel.playClicked()
        }
    ) {
        Text(text = stringResource(playButtonLabel.value!!))
    }
}

@Composable
fun VolumeControl(
    modifier: Modifier,
    synthesizerViewModel: WavetableSynthesizerViewModel
) {
    val volume = synthesizerViewModel.volume.observeAsState()

    VolumeControlContent(
        modifier = modifier,
        value = volume.value!!,
        onValueChange = {
            synthesizerViewModel.setVolume(it)
        },
        valueRange = synthesizerViewModel.volumeRange
    )
}

@Composable
fun VolumeControlContent(
    modifier: Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val sliderHeight = screenHeight / 4

    Icon(imageVector = Icons.Filled.VolumeUp, contentDescription = null)
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .width(sliderHeight.dp)
            .rotate(270F),
        valueRange = valueRange
    )
    Icon(imageVector = Icons.Filled.VolumeMute, contentDescription = null)
}
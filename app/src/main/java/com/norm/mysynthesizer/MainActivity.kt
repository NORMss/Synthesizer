package com.norm.mysynthesizer

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.norm.mysynthesizer.ui.theme.MySynthesizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContent {
            MySynthesizerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SynthesizerApp(Modifier)
                }
            }
        }
    }
}

@Composable
fun SynthesizerApp(
    modifier: Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Synthesizer App"
        )
        WavetableSelectionPanel(modifier)
        ControlPanel(modifier)
    }
}

@Composable
fun WavetableSelectionPanel(
    modifier: Modifier
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
            WavetableSelectionsButtons(modifier)
        }

    }
}

@Composable
fun WavetableSelectionsButtons(
    modifier: Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (wavetable in arrayOf("Sine", "Triangle", "Square", "Saw")) {
            WavetableButton(
                modifier = modifier,
                onClick = {
                },
                label = wavetable
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
    modifier: Modifier
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
            PitchControl(modifier)
            PlayControl(modifier)
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VolumeControl(modifier)
        }
    }
}

@Composable
fun PitchControl(modifier: Modifier) {
    var frequency by rememberSaveable {
        mutableStateOf(300F)
    }
    PitchControlContent(
        modifier,
        pitchControlLabel = stringResource(R.string.frequency),
        value = frequency,
        onValueChange = {
            frequency = it
        },
        valueRange = 40F..3000F,
        frequencyValueLabel = stringResource(R.string.frequency_value, frequency)
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
fun PlayControl(modifier: Modifier) {
    Button(
        modifier = modifier,
        onClick = {

        }
    ) {
        Text(text = stringResource(R.string.play))
    }
}

@Composable
fun VolumeControl(
    modifier: Modifier
) {
    var volume by rememberSaveable() {
        mutableStateOf(-10F)
    }

    VolumeControlContent(
        modifier = modifier,
        value = volume,
        onValueChange = {
            volume = it
        },
        valueRange = -60F..0F
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
        valueRange = -60F..0F
    )
    Icon(imageVector = Icons.Filled.VolumeMute, contentDescription = null)
}
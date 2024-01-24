package com.norm.mysynthesizer

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NativeWavetableSynthesizer : WavetableSynthesizer, DefaultLifecycleObserver {
    private var synthesizerHandle: Long = 0
    private val synthesizerMutex = Object()
    private external fun create(): Long
    private external fun delete(synthesizerHandle: Long)
    private external fun play(synthesizerHandle: Long)
    private external fun stop(synthesizerHandle: Long)
    private external fun isPlaying(synthesizerHandle: Long): Boolean
    private external fun setFrequency(synthesizerHandle: Long, frequencyInHz: Float)
    private external fun setVolume(synthesizerHandle: Long, volumeInDb: Float)
    private external fun setWavetable(synthesizerHandle: Long, wavetable: Int)

    companion object {
        init {
            System.loadLibrary("mysynthesizer")
        }
    }


    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        synchronized(synthesizerMutex) {
            createNativeHandlerIfNotExists()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        synchronized(synthesizerMutex) {
            if (synthesizerHandle == 0L) {
                return
            }

            delete(synthesizerHandle)
            synthesizerHandle = 0L
        }
    }

    private fun createNativeHandlerIfNotExists() {
        if (synthesizerHandle != 0L) {
            return
        }

        synthesizerHandle = create()
    }

    override suspend fun play() = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandlerIfNotExists()
            play(synthesizerHandle)
        }
    }

    override suspend fun stop() = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandlerIfNotExists()
            stop(synthesizerHandle)
        }
    }

    override suspend fun isPlaying(): Boolean = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandlerIfNotExists()
            return@withContext isPlaying(synthesizerHandle)
        }
    }

    override suspend fun setFrequency(frequencyInHz: Float) = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandlerIfNotExists()
            setFrequency(synthesizerHandle, frequencyInHz)
        }
    }

    override suspend fun setVolume(volumeInDb: Float) = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandlerIfNotExists()
            setVolume(synthesizerHandle, volumeInDb)
        }
    }

    override suspend fun setWavetable(wavetable: Wavetable) = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            createNativeHandlerIfNotExists()
            setWavetable(synthesizerHandle, wavetable.ordinal)
        }
    }

}
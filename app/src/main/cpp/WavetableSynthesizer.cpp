#include "Log.h"
#include "WavetableSynthesizer.h"
#include "OboeAudioPlayer.h"
#include "WavetableOscillator.h"

namespace wavetablesynthesizer {
    WavetableSynthesizer::WavetableSynthesizer()
            : _oscillator{std::make_shared<A4Oscillator>(sampleRate)},
              _audioPlayer{std::make_unique<OboeAudioPlayer>(_oscillator, sampleRate)} {}


    WavetableSynthesizer::~WavetableSynthesizer() = default;


    void WavetableSynthesizer::play() {
        LOGD("play() called.");
        const auto result = _audioPlayer->play();
        if (result == 0) {
            _isPlaying = true;
        } else {
            LOGD("Could not start playback.");
        }

    }

    void WavetableSynthesizer::stop() {
        LOGD("stop() called.");
        _audioPlayer->stop();
        _isPlaying = false;
    }

    bool WavetableSynthesizer::isPlaying() const {
        LOGD("isPlaying() called.");
        return _isPlaying;
    }

    bool WavetableSynthesizer::setFrequency(float frequencyInHz) const {
        LOGD("setFrequency() called with %.2f Hz argument.", frequencyInHz);
        return _isPlaying;
    }

    void WavetableSynthesizer::setVolume(float volumeInDb) {
        LOGD("setVolume() called with %.2f dB argument.", volumeInDb);
    }

    void WavetableSynthesizer::setWavetable(Wavetable wavetable) {
        LOGD("setWavetable() called with %.d argument.", static_cast<int>(wavetable));
    }
}
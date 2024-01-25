#pragma once

#include <memory>

namespace wavetablesynthesizer {
    enum class Wavetable {
        SINE, TRIANGLE, SQUARE, SAW
    };

    class AudioSource;

    class AudioPlayer;

    constexpr auto sampleRate = 48000;

    class WavetableSynthesizer {
    public:
        WavetableSynthesizer();

        ~WavetableSynthesizer();

        void play();

        void stop();

        bool isPlaying() const;

        bool setFrequency(float frequencyInHz) const;

        static void setVolume(float volumeInDb);

        static void setWavetable(Wavetable wavetable);

    private:
        bool _isPlaying = false;
        std::shared_ptr<AudioSource> _oscillator;
        std::unique_ptr<AudioPlayer> _audioPlayer;
    };
}
#pragma once

namespace wavetablesynthesizer {
    enum class Wavetable {
        SINE, TRIANGLE, SQUARE, SAW
    };

    class WavetableSynthesizer {
    public:
        void play();

        void stop();

        bool isPlaying() const;

        bool setFrequency(float frequencyInHz) const;

        static void setVolume(float volumeInDb);

        static void setWavetable(Wavetable wavetable);

    private:
        bool _isPlaying = false;
    };
}
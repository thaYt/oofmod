package me.thayt.oofmod.managers;

import me.thayt.oofmod.utils.Chat;

import javax.sound.sampled.*;
import javax.sound.sampled.Line.Info;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class SoundManager {
    private ArrayList<SourceDataLine> sounds = new ArrayList<>();
    private boolean stop = false;

    public void playSound(File file, float volume) {
        try {
            stop = false;
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final Info info = new Info(SourceDataLine.class);

            try (final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                if (line != null) {
                    line.open(outFormat);
                    line.start();
                    sounds.add(line);
                    if (!is16Bit(file)) {
                        // mp3
                        stream(AudioSystem.getAudioInputStream(outFormat, in), line);
                    } else {
                        int bitsPerSample = outFormat.getSampleSizeInBits();
                        float volumeFactor = (float) Math.pow(2, bitsPerSample - 16) * (volume / 100f);
                        // wav
                        stream(AudioSystem.getAudioInputStream(outFormat, in), line, volumeFactor);
                    }
                    sounds.remove(line);
                    line.drain();
                    line.stop();
                }
            }

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            Chat.sendFormattedChatMessage("&cAn error occurred while trying to play a sound!");
            Chat.sendFormattedChatMessage(e.getMessage());
        }
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine line)
            throws IOException {
        final byte[] buffer = new byte[65536];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            if (stop)
                return;
            line.write(buffer, 0, n);
        }
    }

    // ridiculously overcomplicated code to make sure literally fucking *anything*
    // works & no i don't understand it
    private void stream(AudioInputStream in, SourceDataLine line, float volumeFactor)
            throws IOException {
        final byte[] buffer = new byte[65536];
        final int sampleSizeInBytes = in.getFormat().getSampleSizeInBits() / 8;
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            if (stop)
                return;
            for (int i = 0; i < n; i += sampleSizeInBytes) {
                short sample = 0;
                if (sampleSizeInBytes == 2) {
                    // 16-bit sample
                    if (in.getFormat().isBigEndian()) {
                        sample = (short) ((buffer[i] & 0xff) << 8 | (buffer[i + 1] & 0xff));
                    } else {
                        sample = (short) ((buffer[i + 1] & 0xff) << 8 | (buffer[i] & 0xff));
                    }
                } else if (sampleSizeInBytes == 1) {
                    // 8-bit sample
                    sample = (short) (buffer[i] & 0xff);
                }
                sample = (short) (sample * volumeFactor);
                if (sampleSizeInBytes == 2) {
                    // 16-bit sample
                    if (in.getFormat().isBigEndian()) {
                        buffer[i] = (byte) ((sample >> 8) & 0xff);
                        buffer[i + 1] = (byte) (sample & 0xff);
                    } else {
                        buffer[i] = (byte) (sample & 0xff);
                        buffer[i + 1] = (byte) ((sample >> 8) & 0xff);
                    }
                } else if (sampleSizeInBytes == 1) {
                    // 8-bit sample
                    buffer[i] = (byte) (sample & 0xff);
                }
            }
            line.write(buffer, 0, n);
        }
    }

    public boolean is16Bit(File file) throws IOException, UnsupportedAudioFileException {
        AudioInputStream in = AudioSystem.getAudioInputStream(file);
        AudioFormat format = in.getFormat();
        in.close();
        return format.getSampleSizeInBits() == 16;
    }

    /**
     * @return time in seconds
     */
    public double getDuration(File file) {
        if (file.getName().endsWith(".mp3")) {
            try {
                return new Mp3File(file).getLengthInSeconds();
            } catch (IOException | UnsupportedTagException | InvalidDataException e) {
                Chat.sendFormattedChatMessage("&ccould not get duration of " +
                        file.getName());
                e.printStackTrace();
                Chat.sendFormattedChatMessage(e.getMessage());
            }
        } else if (file.getName().endsWith(".wav")) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                AudioFormat format = audioInputStream.getFormat();
                long frames = audioInputStream.getFrameLength();
                audioInputStream.close();
                return (frames + 0.0) / format.getFrameRate();
            } catch (UnsupportedAudioFileException | IOException e) {
                Chat.sendFormattedChatMessage("&ccould not get duration of " + file.getName());
                Chat.sendFormattedChatMessage(e.getMessage());
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void stopAll() {
        stop = true;
        new Thread(() -> {
            while (stop)
                if (sounds.size() == 0)
                    stop = false;
        }).start();
    }
}

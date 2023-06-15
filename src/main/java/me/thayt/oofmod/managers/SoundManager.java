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
                    // volume modification
                    // doesn't work, at least on windows
                    FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(volume / 100);
                    line.start();
                    sounds.add(line);
                    stream(AudioSystem.getAudioInputStream(outFormat, in), line);
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

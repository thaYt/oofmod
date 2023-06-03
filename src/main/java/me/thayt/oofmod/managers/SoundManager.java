package me.thayt.oofmod.managers;

import me.thayt.oofmod.utils.Chat;

import javax.sound.sampled.*;
import javax.sound.sampled.Line.Info;
import java.io.File;
import java.io.IOException;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class SoundManager {
    // yes i stole this code from stackoverflow. i'm not an audio expert okay i don't want to think
    // also volume idk
    public static void playSound(File file, float volume) {
        try (final AudioInputStream in = AudioSystem.getAudioInputStream(file)) {
            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final Info info = new Info(SourceDataLine.class);

            try (final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                if (line != null) {
                    line.open(outFormat);
                    line.start();
                    stream(AudioSystem.getAudioInputStream(outFormat, in), line);
                    line.drain();
                    line.stop();
                }
            }

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            Chat.sendFormattedChatMessage("&cAn error occurred while trying to play a sound!");
            e.printStackTrace();
            Chat.sendFormattedChatMessage(e.getMessage());
        }
    }

    private static AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private static void stream(AudioInputStream in, SourceDataLine line)
            throws IOException {
        final byte[] buffer = new byte[65536];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }

    /**
     * @return time in seconds
     */
    public static double getDuration(File file) {
//        if (file.getName().endsWith(".wav")) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            return (frames + 0.0) / format.getFrameRate();
        } catch (Exception e) {
            Chat.sendFormattedChatMessage("&ccould not get duration of " + file.getName());
            e.printStackTrace();
            Chat.sendFormattedChatMessage(e.getMessage());
        }
//        } else if (file.getName().endsWith(".mp3")) {
//            try {
//                return new Mp3File(file).getLengthInSeconds();
//            } catch (IOException | UnsupportedTagException | InvalidDataException e) {
//                Chat.sendFormattedChatMessage("&ccould not get duration of " + file.getName());
//                e.printStackTrace();
//                Chat.sendFormattedChatMessage(e.getMessage());
//            }
//        }
        return 0;
    }
}

package me.thayt.oofmod.managers;

import me.thayt.oofmod.utils.Chat;

import javax.sound.sampled.*;
import javax.sound.sampled.Line.Info;
import java.io.File;
import java.io.IOException;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public class SoundManager {
    public static void playSound(File file, float volume) {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
//            if (file.getName().endsWith(".mp3")) {
//                // this is probably a really shit way of doing it
//                InputStream stream = new ByteArrayInputStream(Converter.convertFrom(Files.readAllBytes(file.toPath())).toByteArray());
//                in = AudioSystem.getAudioInputStream(stream);
//            }
            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final Info info = new Info(SourceDataLine.class);

            try (final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                if (line != null) {
                    line.open(outFormat);
                    // volume modification
                    FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(volume / 100);
                    line.start();
                    stream(AudioSystem.getAudioInputStream(outFormat, in), line);
                    line.drain();
                    line.stop();
                }
            }

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            Chat.sendFormattedChatMessage("&cAn error occurred while trying to play a sound!");
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
        try {
//            if (file.getName().endsWith(".mp3")) {
//                // this is probably a really shit way of doing it
//                InputStream stream = new ByteArrayInputStream(Converter.convertFrom(Files.readAllBytes(file.toPath())).toByteArray());
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream);
//                AudioFormat format = audioInputStream.getFormat();
//                long frames = audioInputStream.getFrameLength();
//                return (frames + 0.0) / format.getFrameRate();
//            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            return (frames + 0.0) / format.getFrameRate();
        } catch (UnsupportedAudioFileException | IOException e) {
            Chat.sendFormattedChatMessage("&ccould not get duration of " + file.getName());
            Chat.sendFormattedChatMessage(e.getMessage());
            e.printStackTrace();
        }
//        try {
//            return new Mp3File(file).getLengthInSeconds();
//        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
//            Chat.sendFormattedChatMessage("&ccould not get duration of " + file.getName());
//            e.printStackTrace();
//            Chat.sendFormattedChatMessage(e.getMessage());
//        }
        return 0;
    }
}

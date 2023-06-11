package me.thayt.oofmod.managers;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

public class StorageManager {

    private static final String configName = "/config.txt";
    private final File configFile;
    private String activeSound;
    private float volume;
    private boolean bedBreakToggle;
    private boolean deathToggle;
    private boolean killToggle;

    // somebody please make this dogshit config better
    public StorageManager() {
        getConfigFolder();
        getSoundFolder();
        configFile = new File(getConfigFolder() + configName);
        if (configFile.exists()) {
            parseConfig();
        } else {
            initConfig();
            activeSound = "";
            setVolume(100);
        }
    }

    private void initConfig() {
        try {
            Files.write(configFile.toPath(), this.getClass().getClassLoader().getResourceAsStream("defaultConfig.txt").readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseConfig() {
        try {
            String cfg = new String(Files.readAllBytes(configFile.toPath()));
            // if you have a , in your directory i feel bad for you
            for (int i = 0; i < cfg.split(",").length; i++) {
                String line = cfg.split(",")[i].strip();
                switch (i) {
                    case 0 -> activeSound = line;
                    case 1 -> volume = Float.parseFloat(line);
                    case 2 -> bedBreakToggle = Boolean.parseBoolean(line);
                    case 3 -> deathToggle = Boolean.parseBoolean(line);
                    case 4 -> killToggle = Boolean.parseBoolean(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<File> getAllSounds() {
        File dir = getSoundFolder().toFile();
        return FileUtils.listFiles(dir, new String[]{"mp3", "wav"}, false);
//        return FileUtils.listFiles(dir, new String[]{"wav"}, false);
    }

    private Path getConfigFolder() {
        File dir = Paths.get(System.getProperty("user.home"), ".weave", "oofmod").toFile();
        if (dir.exists() && !dir.isDirectory()) dir.delete();
        if (!dir.exists()) dir.mkdirs();
        return dir.toPath();
    }

    public Path getSoundFolder() {
        File dir = Paths.get(System.getProperty("user.home"), ".weave", "oofmod", "sounds").toFile();
        if (dir.exists() && !dir.isDirectory()) dir.delete();
        if (!dir.exists()) dir.mkdirs();
        return dir.toPath();
    }

    public void writeDefaultSound() {
        try {
            Files.write(Paths.get(getSoundFolder() + "/oof.wav"), this.getClass().getClassLoader().getResourceAsStream("oof.wav").readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setActiveSound(String loc) {
        activeSound = loc;
        try {
            String cfg = new String(Files.readAllBytes(configFile.toPath()));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cfg.split(",").length; i++) {
                String line = cfg.split(",")[i].strip();
                if (i == 0) {
                    sb.append(loc);
                } else {
                    sb.append(line);
                }
                if (i != 4) sb.append(",");
            }
            deleteConfig();
            Files.writeString(configFile.toPath(), sb.toString(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setVolume(float vol) {
        volume = vol;
        try {
            String cfg = new String(Files.readAllBytes(configFile.toPath()));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cfg.split(",").length; i++) {
                String line = cfg.split(",")[i].strip();
                if (i == 1) {
                    sb.append(vol);
                } else {
                    sb.append(line);
                }
                if (i != 4) sb.append(",");
            }
            deleteConfig();
            Files.writeString(configFile.toPath(), sb.toString(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setBedBreakToggle(Boolean b) {
        bedBreakToggle = b;
        try {
            String cfg = new String(Files.readAllBytes(configFile.toPath()));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cfg.split(",").length; i++) {
                String line = cfg.split(",")[i].strip();
                if (i == 2) {
                    sb.append(b);
                } else {
                    sb.append(line);
                }
                if (i != 4) sb.append(",");
            }
            deleteConfig();
            Files.writeString(configFile.toPath(), sb.toString(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDeathToggle(Boolean b) {
        deathToggle = b;
        try {
            String cfg = new String(Files.readAllBytes(configFile.toPath()));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cfg.split(",").length; i++) {
                String line = cfg.split(",")[i].strip();
                if (i == 3) {
                    sb.append(b);
                } else {
                    sb.append(line);
                }
                if (i != 4) sb.append(",");
            }
            deleteConfig();
            Files.writeString(configFile.toPath(), sb.toString(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setKillToggle(Boolean b) {
        killToggle = b;
        try {
            String cfg = new String(Files.readAllBytes(configFile.toPath()));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cfg.split(",").length; i++) {
                String line = cfg.split(",")[i].strip();
                if (i == 4) {
                    sb.append(b);
                } else {
                    sb.append(line);
                }
                if (i != 4) sb.append(",");
            }
            deleteConfig();
            Files.writeString(configFile.toPath(), sb.toString(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteConfig() {
        configFile.delete();
    }

    public String getActiveSound() {
        if (!new File(activeSound).exists()) activeSound = "";
        return activeSound;
    }

    public float getVolume() {
        return volume;
    }

    public boolean isBedBreakToggle() {
        return bedBreakToggle;
    }

    public boolean isDeathToggle() {
        return deathToggle;
    }

    public boolean isKillToggle() {
        return killToggle;
    }
}

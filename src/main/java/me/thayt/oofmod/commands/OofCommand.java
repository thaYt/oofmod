package me.thayt.oofmod.commands;

import me.thayt.oofmod.OofMod;
import me.thayt.oofmod.managers.SoundManager;
import me.thayt.oofmod.utils.Chat;
import net.weavemc.loader.api.command.Command;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;

public class OofCommand extends Command {
    public OofCommand() {
        super("oofmod", "oof");
    }

    @Override
    public void handle(String[] args) {
        String oof = "&5&l[&d&lOOF&5&l]&7";
        if (args.length == 0) {
            Chat.sendFormattedChatMessage(oof + " help:");
            Chat.sendFormattedChatMessage("    &d&l/oof default, d&7: puts default oof sound in sounds folder");
            Chat.sendFormattedChatMessage("    &d&l/oof sounds, s&7: lists and selects sounds");
            Chat.sendFormattedChatMessage("    &d&l/oof toggle, t&7: toggles messages that will trigger the sound effect");
            Chat.sendFormattedChatMessage("    &d&l/oof volume, v&7: gets/sets the volume");
            Chat.sendFormattedChatMessage("    &d&l/oof help, other&7: displays this help message");
            return;
        }
        switch (args[0]) {
            case "test" -> {
                if (OofMod.storageManager.getActiveSound().isEmpty()) return;
                new Thread(() -> SoundManager.playSound(new File(OofMod.storageManager.getActiveSound()), OofMod.storageManager.getVolume())).start();
            }
            case "default", "d" -> {
                OofMod.storageManager.writeDefaultSound();
                Chat.sendFormattedChatMessage(oof + " wrote default oof sound to sounds folder");
            }
            case "sounds", "s" -> {
                if (args.length < 2) {
                    Chat.sendFormattedChatMessage(oof + " current sounds: ");
                    OofMod.storageManager.getAllSounds().forEach(file -> {
                        try {
                            Chat.sendFormattedChatMessage(file.getName() + " - " + new DecimalFormat("0.00").format(SoundManager.getDuration(file)));
                        } catch (Exception e) {
                            Chat.sendFormattedChatMessage(file.getName());
                            Chat.sendFormattedChatMessage(e.getMessage());
                        }
                    });
                    return;
                }

                // over-engineered. i know.
                String soundName = String.join(" ", Arrays.asList(args).subList(1, args.length));
                File matchingSound = OofMod.storageManager.getAllSounds().stream()
                        .filter(file -> file.getName().equals(soundName))
                        .findFirst()
                        .orElse(null);

                if (matchingSound != null) {
                    OofMod.storageManager.setActiveSound(matchingSound.getPath());
                    Chat.sendFormattedChatMessage(oof + " setting active sound to: " + matchingSound.getName());
                } else {
                    Chat.sendFormattedChatMessage(oof + " could not find file: " + Arrays.asList(args).subList(1, args.length));
                }
            }
            case "volume", "v" -> {
                if (args.length < 2) {
                    Chat.sendFormattedChatMessage(oof + " volume currently set to " + OofMod.storageManager.getVolume());
                    Chat.sendFormattedChatMessage("    &dplease enter an argument after to set the volume");
                    return;
                }
                if (Float.parseFloat(args[1]) > 500.0) {
                    Chat.sendFormattedChatMessage(oof + " volume must be less than 500%");
                    return;
                }
                try {
                    OofMod.storageManager.setVolume(Float.parseFloat(args[1]));
                    Chat.sendFormattedChatMessage(oof + " set volume to " + args[1]);
                } catch (Exception e) {
                    Chat.sendFormattedChatMessage("error setting volume: " + e);
                }
            }
            case "toggle", "t" -> {
                if (args.length < 2) {
                    Chat.sendFormattedChatMessage(oof + " please send an argument after 'toggle'. Valid arguments:");
                    Chat.sendFormattedChatMessage("    &d&lbeds, bed, b&7 - beds");
                    Chat.sendFormattedChatMessage("    &d&ldeaths, death, d&7 - deaths");
                    Chat.sendFormattedChatMessage("    &d&lkills, kill, k&7 - kills");
                    return;
                }
                switch (args[1]) {
                    case "beds", "bed", "b" -> {
                        OofMod.storageManager.setBedBreakToggle(!OofMod.storageManager.isBedBreakToggle());
                        Chat.sendFormattedChatMessage(oof + " set Beds to " + OofMod.storageManager.isBedBreakToggle());
                    }
                    case "deaths", "death", "d" -> {
                        OofMod.storageManager.setDeathToggle(!OofMod.storageManager.isDeathToggle());
                        Chat.sendFormattedChatMessage(oof + " set Deaths to " + OofMod.storageManager.isDeathToggle());
                    }
                    case "kills", "kill", "k" -> {
                        OofMod.storageManager.setKillToggle(!OofMod.storageManager.isKillToggle());
                        Chat.sendFormattedChatMessage(oof + " set Kills to " + OofMod.storageManager.isKillToggle());
                    }
                    default -> {
                        Chat.sendFormattedChatMessage(oof + " please send a valid argument after 'toggle'. Valid arguments:");
                        Chat.sendFormattedChatMessage("    &d&lbeds, bed, b&7 - beds");
                        Chat.sendFormattedChatMessage("    &d&ldeaths, death, d&7 - deaths");
                        Chat.sendFormattedChatMessage("    &d&lkills, kill, k&7 - kills");
                    }
                }
            }
            default -> {
                Chat.sendFormattedChatMessage(oof + " help:");
                Chat.sendFormattedChatMessage("    &d&l/oof default, d&7: puts default oof sound in sounds folder");
                Chat.sendFormattedChatMessage("    &d&l/oof sounds, s&7: lists and selects sounds");
                Chat.sendFormattedChatMessage("    &d&l/oof toggle, t&7: toggles messages that will trigger the sound effect");
                Chat.sendFormattedChatMessage("    &d&l/oof volume, v&7: gets/sets the volume");
                Chat.sendFormattedChatMessage("    &d&l/oof help, other&7: displays this help message");
            }
        }
    }
}

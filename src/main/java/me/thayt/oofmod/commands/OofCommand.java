package me.thayt.oofmod.commands;

import me.thayt.oofmod.OofMod;
import me.thayt.oofmod.managers.SoundManager;
import me.thayt.oofmod.utils.Chat;
import net.weavemc.loader.api.command.Command;

import java.io.File;
import java.util.Arrays;

public class OofCommand extends Command {
    public OofCommand() {
        super("oofmod", "oof");
    }

    @Override
    public void handle(String[] args) {
        if (args.length == 0) {
            Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&r&7 help:");
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
                Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 wrote default oof sound to sounds folder");
            }
            case "sounds", "s" -> {
                if (args.length < 2) {
                    Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 current sounds: ");
                    OofMod.storageManager.getAllSounds().forEach(file -> {
                        try {
                            Chat.sendFormattedChatMessage(file.getName() + " - " + String.format(String.valueOf(SoundManager.getDuration(file)), "%.2f") + "s");
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
                    Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 setting active sound to: " + matchingSound.getName());
                } else {
                    Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 could not find file: " + Arrays.asList(args).subList(1, args.length));
                }
            }
            case "volume", "v" -> {
                if (args.length < 2) {
                    Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 volume currently set to " + OofMod.storageManager.getVolume());
                    Chat.sendFormattedChatMessage("    &dplease enter an argument after to set the volume");
                    return;
                }
                try {
                    OofMod.storageManager.setVolume(Float.parseFloat(args[1]));
                    Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 set volume to " + args[1]);
                } catch (Exception e) {
                    Chat.sendFormattedChatMessage("error setting volume: " + e);
                }
            }
            case "toggle", "t" -> {
                if (args.length < 2) {
                    Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 please send an argument after 'toggle'. Valid arguments:");
                    Chat.sendFormattedChatMessage("    &d&lbeds, bed, b&7 - beds");
                    Chat.sendFormattedChatMessage("    &d&ldeaths, death, d&7 - deaths");
                    Chat.sendFormattedChatMessage("    &d&lkills, kill, k&7 - kills");
                    return;
                }
                switch (args[1]) {
                    case "beds", "bed", "b" -> {
                        OofMod.storageManager.setBedBreakToggle(!OofMod.storageManager.isBedBreakToggle());
                        Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 set Beds to " + OofMod.storageManager.isBedBreakToggle());
                    }
                    case "deaths", "death", "d" -> {
                        OofMod.storageManager.setDeathToggle(!OofMod.storageManager.isDeathToggle());
                        Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 set Deaths to " + OofMod.storageManager.isDeathToggle());
                    }
                    case "kills", "kill", "k" -> {
                        OofMod.storageManager.setKillToggle(!OofMod.storageManager.isKillToggle());
                        Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 set Kills to " + OofMod.storageManager.isKillToggle());
                    }
                    default -> {
                        Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&7 please send a valid argument after 'toggle'. Valid arguments:");
                        Chat.sendFormattedChatMessage("    &d&lbeds, bed, b&7 - beds");
                        Chat.sendFormattedChatMessage("    &d&ldeaths, death, d&7 - deaths");
                        Chat.sendFormattedChatMessage("    &d&lkills, kill, k&7 - kills");
                    }
                }
            }
            default -> {
                Chat.sendFormattedChatMessage("&5&l[&d&lOOF&5&l]&r&7 help:");
                Chat.sendFormattedChatMessage("    &d&l/oof default, d&7: puts default oof sound in sounds folder");
                Chat.sendFormattedChatMessage("    &d&l/oof sounds, s&7: lists and selects sounds");
                Chat.sendFormattedChatMessage("    &d&l/oof toggle, t&7: toggles messages that will trigger the sound effect");
                Chat.sendFormattedChatMessage("    &d&l/oof volume, v&7: gets/sets the volume");
                Chat.sendFormattedChatMessage("    &d&l/oof help, other&7: displays this help message");
            }
        }
    }
}

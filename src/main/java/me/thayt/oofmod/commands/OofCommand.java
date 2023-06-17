package me.thayt.oofmod.commands;

import me.thayt.oofmod.OofMod;
import me.thayt.oofmod.utils.Chat;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.weavemc.loader.api.command.Command;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;

import static me.thayt.oofmod.OofMod.soundManager;

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
            Chat.sendFormattedChatMessage(
                    "    &d&l/oof toggle, t&7: toggles messages that will trigger the sound effect");
            Chat.sendFormattedChatMessage("    &d&l/oof volume, v&7: gets/sets the volume");
            Chat.sendFormattedChatMessage("    &d&l/oof stop&7: stops all sounds");
            Chat.sendFormattedChatMessage("    &d&l/oof test&7: plays a sound manually");
            Chat.sendFormattedChatMessage("    &d&l/oof help, other&7: displays this help message");
            return;
        }
        switch (args[0]) {
            case "stop" -> {
                soundManager.stopAll();
                Chat.sendFormattedChatMessage(oof + " stopped all sounds");
            }
            case "test" -> {
                if (args.length < 2) {
                    Chat.sendFormattedChatMessage(oof + " usage:");
                    Chat.sendFormattedChatMessage("    &d&l/oof test <file>");
                    return;
                }
                // over-engineered. i know.
                String soundName = String.join(" ", Arrays.asList(args).subList(1, args.length));
                File matchingSound = OofMod.storageManager.getAllSounds().stream()
                        .filter(file -> file.getName().equals(soundName))
                        .findFirst()
                        .orElse(null);

                if (matchingSound == null)
                    return;
                Chat.sendFormattedChatMessage(String.format("&7Playing %s", matchingSound.getName()));
                new Thread(() -> OofMod.soundManager.playSound(matchingSound, OofMod.storageManager.getVolume()))
                        .start();
                OofMod.storageManager.setActiveSound(matchingSound.getPath());
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
                            String text = "&7" + file.getName() + " -&d "
                                    + new DecimalFormat("0.00").format(soundManager.getDuration(file)) + "s";
                            if (!soundManager.is16Bit(file))
                                text += "&c - will not support volume";
                            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    "/oof test " + file.getName());
                            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ChatComponentText("/oof test " + file.getName()));
                            Chat.sendRaw(new ChatComponentText(Chat.color(text)).setChatStyle(
                                    new ChatStyle().setChatClickEvent(clickEvent).setChatHoverEvent(hoverEvent)));
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
                    Chat.sendFormattedChatMessage(
                            oof + " could not find file: " + Arrays.asList(args).subList(1, args.length));
                }
            }
            case "volume", "v" -> {
                if (args.length < 2) {
                    Chat.sendFormattedChatMessage(
                            oof + " volume currently set to " + OofMod.storageManager.getVolume());
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
                    Chat.sendFormattedChatMessage(
                            oof + " please send a valid argument after 'toggle'. Valid arguments:");
                    Chat.sendFormattedChatMessage("    &d&lbeds, bed, b&7 - beds");
                    Chat.sendFormattedChatMessage("    &d&ldeaths, death, d&7 - deaths");
                    Chat.sendFormattedChatMessage("    &d&lkills, kill, k&7 - kills");
                    Chat.sendFormattedChatMessage("    &d&lall, a&7 - toggles all");
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
                    case "all", "a" -> {
                        OofMod.storageManager.setBedBreakToggle(!OofMod.storageManager.isBedBreakToggle());
                        OofMod.storageManager.setDeathToggle(!OofMod.storageManager.isDeathToggle());
                        OofMod.storageManager.setKillToggle(!OofMod.storageManager.isKillToggle());
                        Chat.sendFormattedChatMessage(oof + " set Beds to " + OofMod.storageManager.isBedBreakToggle());
                        Chat.sendFormattedChatMessage(oof + " set Deaths to " + OofMod.storageManager.isDeathToggle());
                        Chat.sendFormattedChatMessage(oof + " set Kills to " + OofMod.storageManager.isKillToggle());
                    }
                    default -> {
                        Chat.sendFormattedChatMessage(
                                oof + " please send a valid argument after 'toggle'. Valid arguments:");
                        Chat.sendFormattedChatMessage("    &d&lbeds, bed, b&7 - beds");
                        Chat.sendFormattedChatMessage("    &d&ldeaths, death, d&7 - deaths");
                        Chat.sendFormattedChatMessage("    &d&lkills, kill, k&7 - kills");
                        Chat.sendFormattedChatMessage("    &d&lall, a&7 - toggles all");
                    }
                }
            }
            default -> {
                Chat.sendFormattedChatMessage(oof + " help:");
                Chat.sendFormattedChatMessage("    &d&l/oof default, d&7: puts default oof sound in sounds folder");
                Chat.sendFormattedChatMessage("    &d&l/oof sounds, s&7: lists and selects sounds");
                Chat.sendFormattedChatMessage(
                        "    &d&l/oof toggle, t&7: toggles messages that will trigger the sound effect");
                Chat.sendFormattedChatMessage("    &d&l/oof volume, v&7: gets/sets the volume");
                Chat.sendFormattedChatMessage("    &d&l/oof stop&7: stops all sounds");
                Chat.sendFormattedChatMessage("    &d&l/oof test&7: plays a sound manually");
                Chat.sendFormattedChatMessage("    &d&l/oof help, other&7: displays this help message");
            }
        }
    }
}

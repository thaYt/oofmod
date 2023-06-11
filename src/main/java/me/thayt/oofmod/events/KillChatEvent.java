package me.thayt.oofmod.events;

import me.thayt.oofmod.OofMod;
import me.thayt.oofmod.managers.SoundManager;
import me.thayt.oofmod.utils.Chat;
import me.thayt.oofmod.utils.Event;
import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.ChatReceivedEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.io.File;
import java.util.Objects;
import java.util.regex.Matcher;

public class KillChatEvent extends Event {
    @SubscribeEvent
    public void onMessage(ChatReceivedEvent event) {
        if (!OofMod.storageManager.isKillToggle()) return;
        if (OofMod.storageManager.getActiveSound().isEmpty()) return;
        String line = Chat.cleanMessage(event.getMessage().getUnformattedText());
        if (line.split(" ").length == 0) return;
        Matcher killMessageMatcher = Chat.killPattern.matcher(line);
        Matcher bedBreakMatcher = Chat.bedPattern.matcher(line);
        if (killMessageMatcher.find() && !bedBreakMatcher.find() && Objects.equals(killMessageMatcher.group("killer"), Minecraft.getMinecraft().thePlayer.getName()))
            new Thread(() -> SoundManager.playSound(new File(OofMod.storageManager.getActiveSound()), OofMod.storageManager.getVolume())).start();
    }
}


package me.thayt.oofmod.events;

import me.thayt.oofmod.OofMod;
import me.thayt.oofmod.utils.Chat;
import me.thayt.oofmod.utils.Event;
import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.ChatReceivedEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.io.File;
import java.util.Objects;
import java.util.regex.Matcher;

public class DeathChatEvent extends Event {
    @SubscribeEvent
    public void onMessage(ChatReceivedEvent event) {
        if (!OofMod.storageManager.isDeathToggle()) return;
        if (OofMod.storageManager.getActiveSound().isEmpty()) return;
        String line = Chat.cleanMessage(event.getMessage().getUnformattedText());
        if (line.split(" ").length == 0) return;
        Matcher killMessageMatcher = Chat.killPattern.matcher(line);
        if (killMessageMatcher.find() && Objects.equals(killMessageMatcher.group("username"), Minecraft.getMinecraft().thePlayer.getName()))
            new Thread(() -> OofMod.soundManager.playSound(new File(OofMod.storageManager.getActiveSound()), OofMod.storageManager.getVolume())).start();
    }
}

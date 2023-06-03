package me.thayt.oofmod.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.regex.Pattern;

public class Chat {
    public static final Pattern bedPattern = Pattern.compile("(?<colour>\\w{3,7}) Bed.+ (by|of|to|for|with) (?<username>\\w{1,16})", Pattern.CASE_INSENSITIVE);
    public static final Pattern killPattern = Pattern.compile("(?<username>\\w{1,16}).+ (by|of|to|for|with|the) (?:(?<killer>\\w{1,16}))", Pattern.CASE_INSENSITIVE);

    // got this from someone else. don't blame me lmao
    public static String color(String message) {
        return message
                .replace("&0", EnumChatFormatting.BLACK.toString())
                .replace("&1", EnumChatFormatting.DARK_BLUE.toString())
                .replace("&2", EnumChatFormatting.DARK_GREEN.toString())
                .replace("&3", EnumChatFormatting.DARK_AQUA.toString())
                .replace("&4", EnumChatFormatting.DARK_RED.toString())
                .replace("&5", EnumChatFormatting.DARK_PURPLE.toString())
                .replace("&6", EnumChatFormatting.GOLD.toString())
                .replace("&7", EnumChatFormatting.GRAY.toString())
                .replace("&8", EnumChatFormatting.DARK_GRAY.toString())
                .replace("&9", EnumChatFormatting.BLUE.toString())
                .replace("&a", EnumChatFormatting.GREEN.toString())
                .replace("&b", EnumChatFormatting.AQUA.toString())
                .replace("&c", EnumChatFormatting.RED.toString())
                .replace("&d", EnumChatFormatting.LIGHT_PURPLE.toString())
                .replace("&e", EnumChatFormatting.YELLOW.toString())
                .replace("&f", EnumChatFormatting.WHITE.toString())
                .replace("&k", EnumChatFormatting.OBFUSCATED.toString())
                .replace("&l", EnumChatFormatting.BOLD.toString())
                .replace("&m", EnumChatFormatting.STRIKETHROUGH.toString())
                .replace("&n", EnumChatFormatting.UNDERLINE.toString())
                .replace("&o", EnumChatFormatting.ITALIC.toString())
                .replace("&r", EnumChatFormatting.RESET.toString());
    }

    public static void sendFormattedChatMessage(String str) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(color(str)));
    }

    public static String cleanMessage(String message) {
        return message.replaceAll("§.", "");
    }
}

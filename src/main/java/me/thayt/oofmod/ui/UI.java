// unused, waiting for someone to come out with a better gui library
package me.thayt.oofmod.ui;

public class UI {
}
/*

import me.thayt.oofmod.OofMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UI extends GuiScreen {
    private final ArrayList<GuiButton> buttons = new ArrayList<>();
    private final ArrayList<GuiButton> sounds = new ArrayList<>();

    @Override
    public void initGui() {
        super.initGui();
        buttons.add(new GuiButton(0, width / 2 - 30, height - 30, "Exit"));
        buttons.add(new GuiButton(0, 10, height - 30, "Open Sound Folder"));
        buttons.add(new GuiButton(0, 10, 10, "Get Default Sound"));
    }

    @Override
    public void drawScreen(int i, int i1, float v) {
        super.drawDefaultBackground();

        this.fontRendererObj.drawString("Sound List", width / 2 - (this.fontRendererObj.getStringWidth("Sound List") / 2), 10, -1);

        int currentY = 70;

        if (OofMod.storageManager.getSounds().isEmpty())
            this.fontRendererObj.drawString("No sounds in folder", width / 2 - (this.fontRendererObj.getStringWidth("No sounds in folder") / 2), currentY, -1);

        for (File file : OofMod.storageManager.getSounds()) {
            String name = file.getPath() + " • " + OofMod.soundManager.getDuration(file);
            if (name.equals(OofMod.storageManager.getActiveSound()))
                this.fontRendererObj.drawString(name, width / 2 - (this.fontRendererObj.getStringWidth(name) / 2), currentY + 5, -1);
            GuiButton button = new GuiButton(0, width / 2 - 50, currentY + 5, name);
            button.setWidth(100);
            button.drawButton(this.mc, i, i1);
            sounds.add(button);
            currentY += 30;
        }
        buttons.get(0).setWidth(60);
        buttons.get(0).drawButton(this.mc, i, i1);

        buttons.get(1).setWidth(120);
        buttons.get(1).drawButton(this.mc, i, i1);

        buttons.get(2).setWidth(120);
        buttons.get(2).drawButton(this.mc, i, i1);
    }

    @Override
    public void mouseReleased(int mx, int my, int button) {
        if (buttons.get(0).isMouseOver()) {
            this.mc.displayGuiScreen(null);
        } else if (buttons.get(1).isMouseOver()) {
            try {
                Desktop.getDesktop().open(new File(OofMod.storageManager.getSoundsFolder().toUri()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (buttons.get(2).isMouseOver()) {
            OofMod.storageManager.writeDefaultSound();
        }

        for (GuiButton sound : sounds) {
            if (sound.isMouseOver()) {
                OofMod.storageManager.setActiveSound(sound.displayString.split(" • ")[0]);
            }
        }

        super.mouseReleased(mx, my, button);
    }
}
 */
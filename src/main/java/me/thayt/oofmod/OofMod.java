package me.thayt.oofmod;

import me.thayt.oofmod.commands.OofCommand;
import me.thayt.oofmod.events.BedChatEvent;
import me.thayt.oofmod.events.DeathChatEvent;
import me.thayt.oofmod.events.KillChatEvent;
import me.thayt.oofmod.managers.EventManager;
import me.thayt.oofmod.managers.SoundManager;
import me.thayt.oofmod.managers.StorageManager;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.command.CommandBus;

public class OofMod implements ModInitializer {
    public static StorageManager storageManager;
    public static SoundManager soundManager;

    @Override
    public void preInit() {
        System.out.println("initializing oofmod");

        EventManager eventManager = new EventManager();
        storageManager = new StorageManager();
        soundManager = new SoundManager();

        CommandBus.register(new OofCommand());

        eventManager.register(new KillChatEvent());
        eventManager.register(new DeathChatEvent());
        eventManager.register(new BedChatEvent());

        System.out.println("done oofmod");
    }
}
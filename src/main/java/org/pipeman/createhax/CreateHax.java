package org.pipeman.createhax;

import mod.MrRedRhino.createhax.BuildConfig;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.pipeman.createhax.hax.FlyHack;
import org.pipeman.createhax.hax.SuperSponge;
import org.pipeman.createhax.hax.lumberhack.LumberHack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BuildConfig.MODID)
public class CreateHax {

    public static IEventBus modEventBus;

    public static final KeyBinding flyHackHotkey = new KeyBinding("Flight-hack", 66, "Hax");
    public static boolean flyHackOn = false;
    public static float flyHackSpeed = 1;

    public static final KeyBinding lumberHackHotkey = new KeyBinding("Lumberhack", 66, "Hax");
    public static boolean lumberHackOn = false;

    public static final KeyBinding superSpongeHotkey = new KeyBinding("Super-Sponge", 66, "Hax");
    public static boolean superSpongeOn = false;
    public static int superSpongeBlocksPerTick = 4;


    public CreateHax() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new FlyHack());
        ClientRegistry.registerKeyBinding(flyHackHotkey);

        MinecraftForge.EVENT_BUS.register(new LumberHack());
        ClientRegistry.registerKeyBinding(lumberHackHotkey);

        MinecraftForge.EVENT_BUS.register(new SuperSponge());
        ClientRegistry.registerKeyBinding(superSpongeHotkey);

//        MinecraftForge.EVENT_BUS.register(new AntiAimbot());
    }
}

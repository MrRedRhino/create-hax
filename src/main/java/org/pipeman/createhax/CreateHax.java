package org.pipeman.createhax;

import mod.MrRedRhino.createhax.BuildConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.pipeman.createhax.lumberhack.LumberHack;
import org.pipeman.createhax.register.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.NonNullLazyValue;
import org.pipeman.createhax.register.config.ModConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DatagenModLoader;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BuildConfig.MODID)
public class CreateHax {

    public static IEventBus modEventBus;
    public static final NonNullLazyValue<CreateRegistrate> registrate = CreateRegistrate.lazy(BuildConfig.MODID);

    public static final KeyBinding flyHackHotkey = new KeyBinding("Contraption flight", 66, "Hax");
    public static boolean flyHackOn = false;
    public static final float flyHackSpeed = 1;

    public static final KeyBinding lumberHackHotkey = new KeyBinding("Lumberhack", 66, "Hax");
    public static boolean lumberHackOn = false;


    public CreateHax() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        CreateRegistrate r = registrate.get();
//        ModItems.register(r);
//        ModBlocks.register(r);
//        ModEntities.register(r);
//        ModTiles.register(r);
//        if (DatagenModLoader.isRunningDataGen()) {
//            modEventBus.addListener((GatherDataEvent g) -> ModPonder.generateLang(r, g));
//        }
//        modEventBus.addListener((FMLClientSetupEvent e) -> ModPonder.register());
//        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
//                () -> ModPartials::load);
//        modEventBus.addListener(ModConfigs::onLoad);
//        modEventBus.addListener(ModConfigs::onReload);
//        ModConfigs.register();

        MinecraftForge.EVENT_BUS.register(new AutoPotatoCanon());
        ClientRegistry.registerKeyBinding(flyHackHotkey);

        MinecraftForge.EVENT_BUS.register(new LumberHack());
        ClientRegistry.registerKeyBinding(lumberHackHotkey);

//        MinecraftForge.EVENT_BUS.register(new AutoSleep());
    }
}

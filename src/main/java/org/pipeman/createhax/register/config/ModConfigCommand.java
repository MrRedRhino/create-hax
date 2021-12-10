package org.pipeman.createhax.register.config;

import com.simibubi.create.foundation.config.ui.BaseConfigScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import mod.MrRedRhino.createhax.BuildConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.command.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ModConfigCommand {
    public static class ModConfigScreen extends BaseConfigScreen {
        public ModConfigScreen(Screen parent, @Nonnull String modID) {
            super(parent, modID);
        }
    }

    public static ModConfigScreen createScreen(Screen parent) {
        return (ModConfigScreen) new ModConfigScreen(parent, BuildConfig.MODID)
                .withTitles("Client Settings", "World Generation Settings", "Gameplay Settings")
                .withSpecs(null, null, ModConfigs.SERVER.specification);
    }
    public static final String configCommandName = BuildConfig.MODID;

    @SubscribeEvent
    public static void commands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(configCommandName).then(Commands.literal("config").executes((ctx) -> {
            ScreenOpener.open(createScreen(null));
            return 1;
        })));
    }
}

package org.pipeman.createhax;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AutoPotatoCanon {
    public void shoot(PlayerEntity player, Hand hand, ItemStack itemStack) {

    }

    @SubscribeEvent
    public void KeyInput(InputEvent.KeyInputEvent event) {
    }
}

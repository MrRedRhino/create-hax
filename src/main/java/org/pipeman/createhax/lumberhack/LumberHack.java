package org.pipeman.createhax.lumberhack;

import com.simibubi.create.AllBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.pipeman.createhax.CreateHax;

import java.util.HashMap;
import java.util.Map;

public class LumberHack {
    static Map<BlockPos, BreakPosition> blocksToBreak = new HashMap<>();

    Minecraft MC = Minecraft.getInstance();

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        if (CreateHax.lumberHackHotkey.isDown()) {
            CreateHax.lumberHackOn = !CreateHax.lumberHackOn;
            if (!CreateHax.lumberHackOn) {
                blocksToBreak.clear();
            }
        }
    }

    @SubscribeEvent
    public void interaction(PlayerInteractEvent.LeftClickBlock event) {
        BlockState block = event.getWorld().getBlockState(event.getPos());
        if (block.isToolEffective(ToolType.AXE) && MC.player.getMainHandItem().sameItem(AllBlocks.MECHANICAL_SAW.asStack())) {

            Util.switchToItemInHotbar(AllBlocks.MECHANICAL_SAW.asStack());

            // Sneak and place saw
            MC.getConnection().send(new CEntityActionPacket(MC.player.getEntity(), CEntityActionPacket.Action.PRESS_SHIFT_KEY));
            Util.sendItemUsePacket(event.getPos(), event.getFace());
            MC.getConnection().send(new CEntityActionPacket(MC.player.getEntity(), CEntityActionPacket.Action.RELEASE_SHIFT_KEY));

            // Place hand-crank
            Util.switchToItemInHotbar(AllBlocks.HAND_CRANK.asStack());
            Util.sendItemUsePacket(event.getPos().relative(event.getFace()), event.getFace());

            blocksToBreak.put(event.getPos(), new BreakPosition(event.getPos(), event.getFace()));
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        try {
            for (BreakPosition inst : blocksToBreak.values()) {
                try {
                    inst.tick();
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
    }
}

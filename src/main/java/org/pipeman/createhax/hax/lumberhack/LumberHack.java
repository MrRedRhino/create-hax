package org.pipeman.createhax.hax.lumberhack;

import com.simibubi.create.AllBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.pipeman.createhax.Util;
import org.pipeman.createhax.CreateHax;

import java.util.HashMap;
import java.util.Map;

public class LumberHack {
    static Map<BlockPos, BreakPosition> blocksToBreak = new HashMap<>();
    Minecraft MC = Minecraft.getInstance();
    private static boolean hotkeyDown = false;

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        if (CreateHax.lumberHackHotkey.isDown() && !hotkeyDown) {
            CreateHax.lumberHackOn = !CreateHax.lumberHackOn;
            Util.showToggleMessage("LumberHack", CreateHax.lumberHackOn);
            if (!CreateHax.lumberHackOn) blocksToBreak.clear();
        }
        hotkeyDown = CreateHax.lumberHackHotkey.isDown();
    }

    @SubscribeEvent
    public void interaction(PlayerInteractEvent.LeftClickBlock event) {
        BlockState block = event.getWorld().getBlockState(event.getPos());

        if (mayPlace(event.getPos(), block) && event.getFace() != null) {
            Util.switchToItemInHotbar(AllBlocks.MECHANICAL_SAW.asStack());

            // Sneak and place saw
            Util.sendSneakPacket(true);
            Util.sendItemUsePacket(event.getPos(), event.getFace());
            Util.sendSneakPacket(false);

            // Place hand-crank
            Util.switchToItemInHotbar(AllBlocks.HAND_CRANK.asStack());
            Util.sendItemUsePacket(event.getPos().relative(event.getFace()), event.getFace());

            blocksToBreak.put(event.getPos(), new BreakPosition(event.getPos(), event.getFace()));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        for (Map.Entry<BlockPos, BreakPosition> inst : blocksToBreak.entrySet()) {
            if (inst.getValue() == null) continue;
            inst.getValue().tick();
            if (inst.getValue().done) {
                inst.setValue(null);
            }
        }
    }

    boolean mayPlace(BlockPos pos, BlockState block) {
        if (!CreateHax.lumberHackOn
                || blocksToBreak.containsKey(pos)
                || !block.isToolEffective(ToolType.AXE)
                || MC.player == null
                || block.is(AllBlocks.MECHANICAL_SAW.getDefaultState().getBlock())
                || block.is(AllBlocks.HAND_CRANK.getDefaultState().getBlock()))
            return false;
        return MC.player.getMainHandItem().sameItem(AllBlocks.MECHANICAL_SAW.asStack());
    }
}

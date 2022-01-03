package org.pipeman.createhax.hax;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.pipeman.createhax.CreateHax;
import org.pipeman.createhax.Util;

import java.text.DecimalFormat;

public class SuperSponge {
    Minecraft MC = Minecraft.getInstance();
    int blocksThisTick = 0;
    private static boolean hotkeyDown = false;

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        if (CreateHax.superSpongeHotkey.isDown() && !hotkeyDown) {
            CreateHax.superSpongeOn = !CreateHax.superSpongeOn;
            Util.showToggleMessage("SuperSponge", CreateHax.superSpongeOn);
        }
        hotkeyDown = CreateHax.superSpongeHotkey.isDown();
    }

    @SubscribeEvent
    public void mouseScroll(InputEvent.MouseScrollEvent event) {
        if (hotkeyDown) {
            event.setCanceled(true);
            if (CreateHax.superSpongeBlocksPerTick > 0 || event.getScrollDelta() > 0)
                CreateHax.superSpongeBlocksPerTick += event.getScrollDelta();

            Util.sendActionbarMessage("Super sponge blocks/tick set to: §2" + CreateHax.superSpongeBlocksPerTick +
                    (CreateHax.superSpongeBlocksPerTick == 0 ? " (unlimited)" : ""));
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (MC.player == null || MC.level == null || !CreateHax.superSpongeOn) return;
        BlockPos playerPos = MC.player.blockPosition();

        for (int y = 4; y > -4; y--) {
            for (int x = -4; x < 4; x++) {
                for (int z = -4; z < 4; z++) {
                    if (isFluidSource(playerPos.offset(x, y, z), MC.level)) {
                        if (blocksThisTick <= CreateHax.superSpongeBlocksPerTick || CreateHax.superSpongeBlocksPerTick == 0) {
                            placeAndBreakCasing(playerPos.offset(x, y, z));
                            blocksThisTick++;
                        }
                    }
                    if (blocksThisTick >= CreateHax.superSpongeBlocksPerTick && CreateHax.superSpongeBlocksPerTick != 0) {
                        blocksThisTick = 0;
                        return;
                    }
                }
            }
        }
    }

    void placeAndBreakCasing(BlockPos pos) {
        if (MC.getConnection() == null || MC.player == null) return;

        Util.switchToItemInHotbar(AllBlocks.ANDESITE_CASING.asStack());
        Util.sendItemUsePacket(pos, Direction.UP);

        Util.switchToItemInHotbar(AllItems.WRENCH.asStack());
        Util.sendSneakPacket(true);
        Util.sendItemUsePacket(pos, Direction.UP);
        Util.sendSneakPacket(false);


    }

    boolean isFluidSource(BlockPos pos, World world) {
        if (world.getBlockState(pos).getBlock() instanceof FlowingFluidBlock) {
            return world.getFluidState(pos).isSource();
        }
        return false;
    }
}

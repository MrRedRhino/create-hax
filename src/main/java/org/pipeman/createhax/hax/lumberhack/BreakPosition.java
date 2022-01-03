package org.pipeman.createhax.hax.lumberhack;

import com.simibubi.create.AllItems;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import org.pipeman.createhax.Util;

public class BreakPosition {
    int handCrankCooldown;
    BlockPos blockToBreakPosition;
    Direction handCrankDirection;
    boolean done = false;

    BreakPosition(BlockPos pos, Direction dir) {
        blockToBreakPosition = pos;
        handCrankDirection = dir;
        handCrankCooldown = 0;
    }

    public void tick() {
        Minecraft MC = Minecraft.getInstance();
        if (blockToBreakPosition != null && handCrankDirection != null && handCrankCooldown == 0 && MC.player != null) {
            Util.sendItemUsePacket(blockToBreakPosition.relative(handCrankDirection, 2), Direction.UP);
            handCrankCooldown = 10;

            if (MC.player.level.getBlockState(blockToBreakPosition).is(Blocks.AIR)) {
                if (Util.switchToItemInHotbar(AllItems.WRENCH.asStack())) {
                    Util.sendSneakPacket(true);
                    Util.sendItemUsePacket(blockToBreakPosition.relative(handCrankDirection, 2), handCrankDirection);
                    Util.sendItemUsePacket(blockToBreakPosition.relative(handCrankDirection, 1), handCrankDirection);
                    Util.sendSneakPacket(false);
                }
                done = true;
            }
        }

        if (handCrankCooldown > 0) { handCrankCooldown--; }
    }
}

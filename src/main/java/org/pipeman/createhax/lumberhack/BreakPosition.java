package org.pipeman.createhax.lumberhack;

import com.simibubi.create.AllItems;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class BreakPosition {
    int handCrankCooldown;
    BlockPos blockToBreakPosition;
    Direction handCrankDirection;

    BreakPosition(BlockPos pos, Direction dir) {
        blockToBreakPosition = pos;
        handCrankDirection = dir;
        handCrankCooldown = 0;
    }

    public void tick() {
        Minecraft MC = Minecraft.getInstance();
        if (blockToBreakPosition != null && handCrankDirection != null && handCrankCooldown == 0) {
            Util.sendItemUsePacket(blockToBreakPosition.relative(handCrankDirection, 2), Direction.UP);
            handCrankCooldown = 10;

            if (MC.player.level.getBlockState(blockToBreakPosition).is(Blocks.AIR)) {
                if (Util.switchToItemInHotbar(AllItems.WRENCH.asStack())) {
                    MC.getConnection().send(new CHeldItemChangePacket(MC.player.inventory.findSlotMatchingItem(AllItems.WRENCH.asStack())));
                    MC.getConnection().send(new CEntityActionPacket(MC.player.getEntity(), CEntityActionPacket.Action.PRESS_SHIFT_KEY));
                    Util.sendItemUsePacket(blockToBreakPosition.relative(handCrankDirection, 2), handCrankDirection);
                    Util.sendItemUsePacket(blockToBreakPosition.relative(handCrankDirection, 1), handCrankDirection);
                    MC.getConnection().send(new CEntityActionPacket(MC.player.getEntity(), CEntityActionPacket.Action.RELEASE_SHIFT_KEY));
                }

                MC.gui.getChat().addMessage(ITextComponent.nullToEmpty("Finished breaking."));
                LumberHack.blocksToBreak.remove(blockToBreakPosition);
            }
        }

        if (handCrankCooldown > 0) { handCrankCooldown--; }
    }
}

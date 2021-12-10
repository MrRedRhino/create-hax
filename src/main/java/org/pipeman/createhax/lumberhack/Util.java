package org.pipeman.createhax.lumberhack;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class Util {
    static void sendItemUsePacket(BlockPos pos, Direction dir) {
        BlockRayTraceResult rayTraceResult = new BlockRayTraceResult(
                new Vector3d(0, 0, 0), dir, pos, false);
        Minecraft.getInstance().getConnection().send(new CPlayerTryUseItemOnBlockPacket(Hand.MAIN_HAND, rayTraceResult));
    }

    static boolean switchToItemInHotbar(ItemStack item) {
        PlayerInventory inv = Minecraft.getInstance().player.inventory;
        if (inv.findSlotMatchingItem(item) != -1) {
            Minecraft.getInstance().getConnection().send(new CHeldItemChangePacket(inv.findSlotMatchingItem(item)));
            return true;
        }

        return false;
    }
}

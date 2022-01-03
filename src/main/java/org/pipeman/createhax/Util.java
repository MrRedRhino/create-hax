package org.pipeman.createhax;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPickItemPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class Util {
    public static void sendItemUsePacket(BlockPos pos, Direction dir) {
        if (Minecraft.getInstance().getConnection() == null) return;
        BlockRayTraceResult rayTraceResult = new BlockRayTraceResult(
                new Vector3d(0, 0, 0), dir, pos, false);
        Minecraft.getInstance().getConnection().send(new CPlayerTryUseItemOnBlockPacket(Hand.MAIN_HAND, rayTraceResult));
    }


    public static void showToggleMessage(String module, boolean on) {
        String msg = "Toggled " + module + (on ? " §2ON" : " §cOFF");
        Minecraft.getInstance().gui.setOverlayMessage(ITextComponent.nullToEmpty(msg), false);
    }


    public static boolean switchToItemInHotbar(ItemStack item) {
        Minecraft MC = Minecraft.getInstance();
        if (MC.getConnection() == null || MC.gameMode == null || MC.player == null) return false;

        PlayerInventory inv = MC.player.inventory;
        int slotBefore = inv.selected;

        int i = inv.findSlotMatchingItem(item);
        if (i != -1) {
            if (PlayerInventory.isHotbarSlot(i)) {
                MC.getConnection().send(new CHeldItemChangePacket(i));
            } else {
                MC.getConnection().send(new CPickItemPacket(i));
            }
            inv.selected = slotBefore;
            return true;
        }
        return false;
    }


    public static void sendSneakPacket(boolean sneaking) {
        Minecraft MC = Minecraft.getInstance();
        if (MC.getConnection() == null || MC.player == null) return;
        if (sneaking) {
            MC.getConnection().send(new CEntityActionPacket(MC.player.getEntity(), CEntityActionPacket.Action.PRESS_SHIFT_KEY));
        } else {
            MC.getConnection().send(new CEntityActionPacket(MC.player.getEntity(), CEntityActionPacket.Action.RELEASE_SHIFT_KEY));
        }
    }


    public static void sendActionbarMessage(String message){
        ITextComponent text = new StringTextComponent(message);
        Minecraft.getInstance().gui.setOverlayMessage(text,false);
    }


    public static void sendChatMessage(String message){
        ITextComponent text = new StringTextComponent(message);
        Minecraft.getInstance().gui.getChat().addMessage(text);
    }
}

package org.pipeman.createhax;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AutoSleep {
    Vector3d posBeforeSleep;


    boolean canSleep(Minecraft MC) {
        long time = MC.player.level.getDayTime();
        return time > 12542 && time < 23459;
    }

    void sendItemUsePacket(BlockPos pos) {
        BlockRayTraceResult rayTraceResult = new BlockRayTraceResult(
                new Vector3d(0, 0, 0), Direction.UP, pos, false);
        Minecraft.getInstance().getConnection().send(new CPlayerTryUseItemOnBlockPacket(Hand.MAIN_HAND, rayTraceResult));
    }

    @SubscribeEvent
    public void onMovement(TickEvent.ClientTickEvent event) {
        Minecraft MC = Minecraft.getInstance();
        if (MC.player == null) { return; }
        BlockPos pos = MC.player.blockPosition();

        for (int y = -4; y < 4; y++) {
            for (int x = -4; x < 4; x++) {
                for (int z = -4; z < 4; z++) {
                    if (MC.player.level.getBlockState(pos.offset(x, y, z)).is(Blocks.WHITE_BED) && canSleep(MC) && !MC.player.isSleeping()) {
                        posBeforeSleep = MC.player.position();
                        MC.gui.getChat().addMessage(ITextComponent.nullToEmpty("Set posBeforeSleep to " + posBeforeSleep));
                        sendItemUsePacket(pos.offset(x, y, z));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void sleepEnd(PlayerWakeUpEvent event) {
        if (posBeforeSleep != null) {
            double x1 = posBeforeSleep.x();
            double y1 = posBeforeSleep.y();
            double z1 = posBeforeSleep.z();
            Minecraft.getInstance().getConnection().send(new CPlayerPacket.PositionPacket(x1, y1, z1, true));
            Minecraft.getInstance().gui.getChat().addMessage(ITextComponent.nullToEmpty("Teleported to " + posBeforeSleep));
        }
    }
}

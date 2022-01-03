package org.pipeman.createhax.hax;

import com.simibubi.create.content.contraptions.components.structureMovement.sync.ClientMotionPacket;
import com.simibubi.create.foundation.networking.AllPackets;
import mod.MrRedRhino.createhax.BuildConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.pipeman.createhax.CreateHax;
import org.pipeman.createhax.Util;

import java.text.DecimalFormat;

public class FlyHack {
    private static final Minecraft MC = Minecraft.getInstance();
    private static boolean hotkeyDown = false;

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        if (CreateHax.flyHackHotkey.isDown() && !hotkeyDown) {
            CreateHax.flyHackOn = !CreateHax.flyHackOn;
            Util.showToggleMessage("FlyHack", CreateHax.flyHackOn);
        }
        hotkeyDown = CreateHax.flyHackHotkey.isDown();
    }

    @SubscribeEvent
    public void mouseScroll(InputEvent.MouseScrollEvent event) {
        if (hotkeyDown) {
            event.setCanceled(true);
            if (CreateHax.flyHackSpeed > 0.1 || event.getScrollDelta() > 0)
                CreateHax.flyHackSpeed += event.getScrollDelta() / 10;

            DecimalFormat df = new DecimalFormat("#.##");
            Util.sendActionbarMessage("Fly speed set to: §2" + df.format(CreateHax.flyHackSpeed));
        }
    }

    @SubscribeEvent
    public void performFly(TickEvent.ClientTickEvent clientTickEvent) {
        if (MC.player == null || !CreateHax.flyHackOn) return;

        Vector3d movement = getJumpVec().multiply(2, 0.5, 2);
        AllPackets.channel.sendToServer(new ClientMotionPacket(movement, true, 0));
        MC.player.setDeltaMovement(movement);
    }

    private static Vector3d getJumpVec() {
        Vector3d movement = Vector3d.ZERO;
        if (MC.options.keyJump.isDown()) {
            movement = movement.add(0, 1, 0);
        }
        if (MC.options.keyShift.isDown() && movement.x == 0 && movement.z == 0) {
            movement = movement.add(0, -1, 0);
        }
        return movement.y < -.1 ? movement : movement.add(asVec3d());
    }

    private static Vector3d asVec3d() {
        if (MC.player == null)
            return Vector3d.ZERO;
        Vector2f vector2f = MC.player.input.getMoveVector();
        float f = MC.player.getSpeed();
        float f2 = f * vector2f.x;
        float f3 = f * vector2f.y;
        float f4 = MathHelper.sin(MC.player.yRot * ((float) Math.PI / 180F));
        float f5 = MathHelper.cos(MC.player.yRot * ((float) Math.PI / 180F));
        return new Vector3d(f2 * f5 - f3 * f4, 0, f3 * f5 + f2 * f4).scale(CreateHax.flyHackSpeed);
    }
}

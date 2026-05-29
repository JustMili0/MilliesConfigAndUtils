package net.justmili.libs.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class MathUtil {
    public static Random random = new Random();

    public static boolean chance(double chance) {
        // Ex.: 20% = 0.2
        if (chance > 1.0) chance = 1.0; // Limit to 100%
        return !(random.nextDouble() <= chance);
    }

    public static boolean isInView(ServerPlayer player, Entity target, int fov) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 targetCenter = target.getBoundingBox().getCenter();
        Vec3 toTarget = targetCenter.subtract(eyePos).normalize();
        double angle = Math.toDegrees(Math.acos(toTarget.dot(player.getLookAngle().normalize())));
        return angle < (fov / 2.0);
    }
    public static boolean isInView(ServerPlayer player, BlockPos target, int fov) {
        Vec3 targetCenter = Vec3.atCenterOf(target);
        Vec3 eyePos = player.getEyePosition();
        Vec3 toTarget = targetCenter.subtract(eyePos).normalize();
        double angle = Math.toDegrees(Math.acos(toTarget.dot(player.getLookAngle().normalize())));
        return angle < (fov / 2.0);
    }
}

package net.justmili.libs.utils;

import net.justmili.libs.data.MobData;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EntityUtil {
    // Player
    public static void applyEffect(ServerPlayer player, Holder<MobEffect> effect, int duration, int power) {
        player.addEffect(new MobEffectInstance(effect, duration, power, false, false, false));
    }

    public static void moveToValidRespawnPos(ServerPlayer player) {
        ServerPlayer.RespawnConfig respawnConfig = player.getRespawnConfig();
        ServerLevel respawnDim = player.level().getServer().overworld();

        if (respawnConfig != null) {
            ServerLevel targetLevel = player.level().getServer().getLevel(respawnConfig.respawnData().dimension());
            if (targetLevel != null) {
                TeleportTransition transition = player.findRespawnPositionAndUseSpawnBlock(false, TeleportTransition.DO_NOTHING);
                Vec3 position = transition.position();

                player.teleportTo(targetLevel, position.x, position.y+0.05, position.z, Set.of(), 180, 0, false);
                return;
            }
        }

        // Fallback to world spawn
        BlockPos spawnPos = respawnDim.getRespawnData().pos();
        player.teleportTo(respawnDim, spawnPos.getX()+0.5, spawnPos.getY(), spawnPos.getZ()+0.5, Set.of(), 180, 0, false);
    }

    public static boolean hasAdvancement(ServerPlayer player, Identifier namespacedAdvancementID) {
        AdvancementHolder holder = player.level().getServer().getAdvancements().get(namespacedAdvancementID);
        if (holder == null) return false;
        return player.getAdvancements().getOrStartProgress(holder).isDone();
    }
    public static void grantAdvancement(ServerPlayer player, Identifier namespacedAdvancementID) {
        AdvancementHolder holder = player.level().getServer().getAdvancements().get(namespacedAdvancementID);
        if (holder == null) return;
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(holder);
        if (!progress.isDone()) {
            for (String criteria : progress.getRemainingCriteria()) player.getAdvancements().award(holder, criteria);
        }
    }

    // Non-player
    // ----------

    // Generic
    public static <T extends Mob> List<T> getNearby(ServerPlayer player, Class<T> mob, double radius) {
        return player.level().getEntitiesOfClass(mob, player.getBoundingBox().inflate(radius));
    }
    // For a list of entities within an aea of a player
    public static <T extends Mob> void executeForNearby(ServerPlayer player, List<MobData> dataList, BiConsumer<T, MobData> action) {
        dataList.forEach(data ->
            getNearby(player, data.entityClass().asSubclass(Mob.class), data.range())
                .forEach(mob -> action.accept((T) mob, data))
        );
    }
    // For a single entity
    public static <T extends Mob> void executeForNearby(ServerPlayer player, Class<?> entityClass, double range, Consumer<T> action) {
        getNearby(player, entityClass.asSubclass(Mob.class), range)
            .forEach(mob -> action.accept((T) mob));
    }

    public static <T extends Mob> void executeForNearby(ServerPlayer player, Class<?> entityClass, double range, double speed, BiConsumer<T, Double> action) {
        getNearby(player, entityClass.asSubclass(Mob.class), range)
            .forEach(mob -> action.accept((T) mob, speed));
    }
}
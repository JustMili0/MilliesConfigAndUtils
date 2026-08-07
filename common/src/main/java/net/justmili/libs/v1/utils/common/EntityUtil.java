package net.justmili.libs.v1.utils.common;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EntityUtil {
    // Player
    public static void applyEffect(ServerPlayer player, MobEffect effects, int duration, int power) {
        player.addEffect(new MobEffectInstance(effects, duration, power, false, false, false));
    }

    public static void moveToValidRespawnPos(ServerPlayer player) {
        var respawnPos = player.getRespawnPosition();
        var respawnDim = player.getRespawnDimension();
        if (respawnPos == null) return;

        var targetLevel = player.server.getLevel(respawnDim);
        if (targetLevel == null) return;

        var maybeSpot = Player.findRespawnPositionAndUseSpawnBlock(targetLevel, respawnPos, 0, player.isRespawnForced(), false);
        if (maybeSpot.isPresent()) {
            var spot = maybeSpot.get();
            player.teleportTo(targetLevel, spot.x, spot.y + 0.05, spot.z, 180, 0);
            return;
        }
        double fallbackX = respawnPos.getX() + 0.5;
        double fallbackY = respawnPos.getY() + 0.05;
        double fallbackZ = respawnPos.getZ() + 0.5;
        player.teleportTo(targetLevel, fallbackX, fallbackY, fallbackZ, 180, 0);
    }

    public static boolean hasAdvancement(ServerPlayer player, ResourceLocation id) {
        var advancement = getAdvancement(player, id);
        if (advancement == null) return false;

        return getOrStartProgress(player, id).isDone();
    }

    public static void grantAdvancement(ServerPlayer player, ResourceLocation id) {
        var advancement = getAdvancement(player, id);
        if (advancement == null) return;

        var progress = getOrStartProgress(player, id);
        if (!progress.isDone()) for (String criteria : progress.getRemainingCriteria())
            player.getAdvancements().award(advancement, criteria);
    }

    public static void revokeAdvancement(ServerPlayer player, ResourceLocation id) {
        var advancement = getAdvancement(player, id);
        if (advancement == null) return;

        var progress = getOrStartProgress(player, id);
        if (progress.isDone()) for (String criteria : progress.getCompletedCriteria())
            player.getAdvancements().revoke(advancement, criteria);
    }

    private static Advancement getAdvancement(ServerPlayer player, ResourceLocation id) {
        return player.server.getAdvancements().getAdvancement(id);
    }

    private static AdvancementProgress getOrStartProgress(ServerPlayer player, ResourceLocation id) {
        return player.getAdvancements().getOrStartProgress(getAdvancement(player, id));
    }

    public static void consumeHeldWithResult(Player player, InteractionHand hand, Item result) {
        var stack = player.getItemInHand(hand);
        var item = new ItemStack(result);

        stack.shrink(1);
        if (stack.isEmpty()) {
            player.setItemInHand(hand, item);
        } else if (!player.getInventory().add(item)) {
            player.drop(item, false);
        }
    }

    // Non-player
    public static void applyEffect(LivingEntity entity, MobEffect effects, int duration, int power) {
        entity.addEffect(new MobEffectInstance(effects, duration, power, false, false, false));
    }

    // Generic
    public record MobData(Class<?> entityClass, double range, double runSpeed) { }

    public static <T extends Mob> List<T> getNearby(ServerPlayer player, Class<T> mob, double radius) {
        return player.level().getEntitiesOfClass(mob, player.getBoundingBox().inflate(radius));
    }

    // For a list of entities within an area of a player
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
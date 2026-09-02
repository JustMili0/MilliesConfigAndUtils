package net.justmili.api.events.server;

import net.justmili.api.events.base.CompoundEventResult;
import net.justmili.api.events.base.Event;
import net.justmili.api.events.base.EventResult;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerEvents {
    private PlayerEvents() {}

    public static final Event<Respawn> RESPAWN = Event.create(Respawn.class, callbacks -> (oldPlayer, newPlayer, alive) -> {
        for (Respawn event : callbacks) event.onRespawn(oldPlayer, newPlayer, alive);
    });
    public static final Event<Clone> CLONE = Event.create(Clone.class, callbacks -> (oldPlayer, newPlayer, alive) -> {
        for (Clone event : callbacks) event.onClone(oldPlayer, newPlayer, alive);
    });

    public static final Event<CraftItem> CRAFT_ITEM = Event.create(CraftItem.class, callbacks -> (player, itemCrafted, container) -> {
        for (CraftItem event : callbacks) event.onCraftItem(player, itemCrafted, container);
    });
    public static final Event<SmeltItem> SMELT_ITEM = Event.create(SmeltItem.class, callbacks -> (player, itemSmelted) -> {
       for (SmeltItem event : callbacks) event.onSmeltItem(player, itemSmelted);
    });

    @FunctionalInterface
    public interface Respawn {
        void onRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive);
    }

    @FunctionalInterface
    public interface Clone {
        void onClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive);
    }

    @FunctionalInterface
    public interface CraftItem {
        void onCraftItem(Player player, @NotNull ItemStack itemCrafted, Container container);
    }

    @FunctionalInterface
    public interface SmeltItem {
        void onSmeltItem(Player player, @NotNull ItemStack itemSmelted);
    }

    @FunctionalInterface
    public interface DropItem {
        EventResult onDropItem(Player player, ItemEntity item);
    }

    @FunctionalInterface
    public interface CanPickupItem {
        EventResult canPickupItem(Player player, ItemEntity item, ItemStack stack);
    }

    @FunctionalInterface
    public interface PickupItemPre {
        void onStartPickupItem(Player player, ItemEntity item, ItemStack stack);
    }

    @FunctionalInterface
    public interface PickupItemPost {
        void onEndPickupItem();
    }

    @FunctionalInterface
    public interface ChangeDimension {
        void onChangedDimension(ServerPlayer player, ResourceKey<Level> fromLevel, ResourceKey<Level> toLevel);
    }

    @FunctionalInterface
    public interface OpenMenu {
        void onOpenMenu(Player player, AbstractContainerMenu menu);
    }

    @FunctionalInterface
    public interface CloseMenu {
        void onCloseMenu(Player player, AbstractContainerMenu menu);
    }

    @FunctionalInterface
    public interface FillBucket {
        CompoundEventResult<ItemStack> onFillBucket();
    }

    @FunctionalInterface
    public interface AttackEntity {
        EventResult onAttackedEntity(Player player, Level level, Entity target, InteractionHand hand, @Nullable EntityHitResult hitResult);
    }

    @FunctionalInterface
    public interface KillOtherEntityPost {
        EventResult onEndKillOtherEntity();
    }
}

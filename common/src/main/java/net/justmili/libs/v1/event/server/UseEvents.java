package net.justmili.libs.v1.event.server;

import net.justmili.libs.v1.event.base.Event;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class UseEvents {
    private UseEvents() {}

    public static final Event<ItemPre> ITEM_PRE = Event.create(ItemPre.class, callbacks -> (level, player, hand) -> {
        for (ItemPre event : callbacks) {
            InteractionResult result = event.onUseItemPre(level, player, hand);
            if (result != InteractionResult.PASS) return result;
        }
        return InteractionResult.PASS;
    });

    public static final Event<ItemPost> ITEM_POST = Event.create(ItemPost.class, callbacks -> (level, player, hand, stack, result) -> {
        for (ItemPost event : callbacks) event.onUseItemPost(level, player, hand, stack, result);
    });

    public static final Event<BlockPost> BLOCK_POST = Event.create(BlockPost.class, callbacks -> (level, player, hand, pos, state, result) -> {
        for (BlockPost event : callbacks) event.onUseBlockPost(level, player, hand, pos, state, result);
    });

    public static final Event<EntityPost> ENTITY_POST = Event.create(EntityPost.class, callbacks -> (level, player, hand, entity, result) -> {
        for (EntityPost event : callbacks) event.onUseEntityPost(level, player, hand, entity, result);
    });

    @FunctionalInterface
    public interface ItemPre {
        InteractionResult onUseItemPre(Level level, Player player, InteractionHand hand);
    }
    @FunctionalInterface
    public interface ItemPost {
        void onUseItemPost(Level level, Player player, InteractionHand hand, ItemStack stack, InteractionResult result);
    }
    @FunctionalInterface
    public interface BlockPost {
        void onUseBlockPost(Level level, Player player, InteractionHand hand, BlockPos pos, BlockState state, InteractionResult result);
    }
    @FunctionalInterface
    public interface EntityPost {
        void onUseEntityPost(Level level, Player player, InteractionHand hand, Entity entity, InteractionResult result);
    }
}
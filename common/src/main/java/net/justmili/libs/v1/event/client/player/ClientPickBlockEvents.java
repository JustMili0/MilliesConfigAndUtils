/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Modified by JustMili for Core Libraries:
 * combined ClientPickBlockGatherCallback and ClientPickBlockApplyCallback into ClientPickBlockEvents
 * with static fields GATHER and APPLY; logic unchanged.
 */

package net.justmili.libs.v1.event.client.player;

import net.justmili.libs.v1.event.base.Event;
import net.justmili.libs.v1.event.base.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public final class ClientPickBlockEvents {
    private ClientPickBlockEvents() { }

    /**
     * This event is emitted at the beginning of the block picking process in
     * order to find any applicable ItemStack. The first non-empty ItemStack
     * will be returned, overriding vanilla behavior.
     *
     * <p>Note that this is called any time the pick key is pressed, even if there is no target block.
     * The {@link HitResult} could be a {@link BlockHitResult} or an {@link EntityHitResult}.
     * If the hit missed, it will be a {@link BlockHitResult} with {@linkplain BlockHitResult#getType() type}
     * {@link BlockHitResult.Type#MISS}, so make sure to check for that.
     */
    public static final Event<Gather> GATHER = EventFactory.createArrayBacked(Gather.class,
        (listeners) -> (player, result) -> {
            for (Gather event : listeners) {
                ItemStack stack = event.gather(player, result);

                if (stack != ItemStack.EMPTY && !stack.isEmpty()) {
                    return stack;
                }
            }

            return ItemStack.EMPTY;
        }
    );

    /**
     * This event is emitted during the block-picking process. It can be used to
     * modify the returned ItemStack, as well as nullify it - returning an empty
     * ItemStack will cause the event to leave, and no block to be picked.
     */
    public static final Event<Apply> APPLY = EventFactory.createArrayBacked(Apply.class,
        (listeners) -> (player, result, itemStack) -> {
            ItemStack stack = itemStack;

            for (Apply event : listeners) {
                stack = event.apply(player, result, stack);

                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }

            return stack;
        }
    );

    @FunctionalInterface
    public interface Gather {
        ItemStack gather(Player player, HitResult result);
    }

    @FunctionalInterface
    public interface Apply {
        ItemStack apply(Player player, HitResult result, ItemStack stack);
    }
}
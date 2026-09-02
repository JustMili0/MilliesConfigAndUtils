package net.justmili.api.events.bridge.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.justmili.api.events.server.UseEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

public final class ServerEventsBridge {
    private ServerEventsBridge() {}

    public static void init() {
        // Server Lifecycle
        ServerLifecycleEvents.SERVER_STARTING.register(server -> STARTING.invoker().onServerStarting(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> STOPPING.invoker().onServerStopping(server));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> STOPPED.invoker().onServerStopped(server));

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) ->
                DATAPACK_RELOAD_PRE.invoker().onDatapackReloadPre(server, resourceManager));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) ->
                DATAPACK_RELOAD_POST.invoker().onDatapackReloadPost(server, resourceManager, success));
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) ->
            DATAPACK_SYNC_CONTENTS.invoker().onDatapackSyncContents(player, joined));

        // ServerConnectionEvents
        // TODO: add

        // Server Ticks
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            SERVER_PRE.invoker().onStartTick(server); // Tick server
            for (var player : server.getPlayerList().getPlayers()) PLAYER_PRE.invoker().onStartTick(player); // Tick players
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            SERVER_POST.invoker().onEndTick(server); // Tick server
            for (var player : server.getPlayerList().getPlayers()) PLAYER_POST.invoker().onEndTick(player); // Tick players
        });
        ServerTickEvents.START_WORLD_TICK.register(level -> {
            LEVEL_PRE.invoker().onStartTick(level); // Tick level
            for (var entity : level.getAllEntities()) ENTITY_PRE.invoker().onStartTick(entity); // Tick entities
        });
        ServerTickEvents.END_WORLD_TICK.register(level -> {
            LEVEL_POST.invoker().onEndTick(level); // Tick level
            for (var entity : level.getAllEntities()) ENTITY_POST.invoker().onEndTick(entity); // Tick entities
        });

        // ServerLevelEvents
        // TODO: add

        // BlockEntityEvents
        // TODO: add

        // EntityEvents
        // TODO: add

        // LivingEntityEvents
        // TODO: add

        // PlayerEvents
        // TODO: add

        // PlayerAdvancementEvents
        // TODO: add

        // EntitySleepEvents
        // TODO: add

        // ChatEvents
        // TODO: add

        // Use Events
        UseItemCallback.EVENT.register((player, world, hand) -> {
            var stack = player.getItemInHand(hand);
            UseEvents.ITEM_POST.invoker().onUseItemPost(world, player, hand, stack, InteractionResult.PASS);
            return InteractionResultHolder.pass(stack);
        });
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            var pos = hitResult.getBlockPos();
            var state = world.getBlockState(pos);
            UseEvents.BLOCK_POST.invoker().onUseBlockPost(world, player, hand, pos, state, InteractionResult.PASS);
            return InteractionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            UseEvents.ENTITY_POST.invoker().onUseEntityPost(world, player, hand, entity, InteractionResult.PASS);
            return InteractionResult.PASS;
        });
    }
}
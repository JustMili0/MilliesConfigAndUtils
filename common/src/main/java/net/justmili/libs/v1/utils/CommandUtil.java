package net.justmili.libs.v1.utils;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class CommandUtil {
    // Replace "new" permission system with the good ol' numbers
//    public static boolean hasPerms(CommandSourceStack source, int level) {
//        return source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.byId(level)));
//    }

    // Command success/fail response
    public static void sendOk(CommandSourceStack source, Component message, boolean allowLogging) {
        source.sendSuccess(() -> message, allowLogging);
    }
    public static void sendOk(CommandSourceStack source, String message, boolean allowLogging) {
        sendOk(source, Component.literal(message), allowLogging);
    }

    public static void sendFail(CommandSourceStack source, Component message) {
        source.sendFailure(message);
    }
    public static void sendFail(CommandSourceStack source, String message) {
        sendFail(source, Component.literal(message));
    }

    // Broadcast
    public static void broadcastPlayer(ServerPlayer player, Component message, boolean bypassHiddenChat) {
        player.sendSystemMessage(message, bypassHiddenChat);
    }
    public static void broadcastPlayer(ServerPlayer player, String message, boolean bypassHiddenChat) {
        broadcastPlayer(player, Component.literal(message), bypassHiddenChat);
    }

    public static void broadcastServer(MinecraftServer server, Component message, boolean bypassHiddenChat) {
        server.getPlayerList().broadcastSystemMessage(message, bypassHiddenChat);
    }
    public static void broadcastServer(MinecraftServer server, String message, boolean bypassHiddenChat) {
        broadcastServer(server, Component.literal(message), bypassHiddenChat);
    }

    // Other
    public static void executeAsPlayer(MinecraftServer server, ServerPlayer player, String command) {
        if (player != null && server != null) server.getCommands().performPrefixedCommand(player.createCommandSourceStack().withSuppressedOutput(), command);
    }
    public static void executeAsServer(MinecraftServer server, String command) {
        if (server != null) server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
    }
}

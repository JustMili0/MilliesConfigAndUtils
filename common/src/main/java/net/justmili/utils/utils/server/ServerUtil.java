package net.justmili.utils.utils.server;

import com.mojang.authlib.GameProfile;
import net.justmili.api.events.server.ServerLifecycleEvents;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.WorldData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ServerUtil {
    private static MinecraftServer server;
    private static IntegratedServer integrated;
    private static DedicatedServer dedicated;

    /// Server is automatically assigned by CoreLibs common class at server startup
    public static void setServer() {
        ServerLifecycleEvents.STARTING.register(instance -> {
            server = instance;
            integrated = instance instanceof IntegratedServer? (IntegratedServer) instance : null;
            dedicated = instance instanceof DedicatedServer? (DedicatedServer) instance : null;
        });
    }

    public static MinecraftServer server() {
        return server;
    }

    public static IntegratedServer integrated() {
        return integrated;
    }

    public static DedicatedServer dedicated() {
        return dedicated;
    }

    public static boolean isIntegrated() {
        return integrated != null;
    }

    public static boolean isDedicated() {
        return dedicated != null;
    }

    // Players
    public static PlayerList getPlayerList() {
        return server.getPlayerList();
    }

    public static int getMaxPlayers() {
        return getPlayerList().getMaxPlayers();
    }

    public static int getPlayerCount() {
        return getPlayerList().getPlayerCount();
    }

    public static List<ServerPlayer> getPlayers() {
        return getPlayerList().getPlayers();
    }

    public static ServerPlayer getPlayer(UUID uuid) {
        return getPlayerList().getPlayer(uuid);
    }

    public static ServerPlayer getPlayer(String username) {
        return getPlayerList().getPlayerByName(username);
    }

    public static String getPlayerName(UUID uuid, boolean tryGetOfflinePlayer) {
        var player = getPlayerList().getPlayer(uuid);
        if (player != null) return player.getName().getString();
        // 1.21.9+
        //if (tryGetOfflinePlayer) return server.services().profileResolver().fetchById(uuid).map(GameProfile::name).orElse("");
        return "";
    }

    public static String getPlayerName(String username, boolean tryGetOfflinePlayer) {
        var player = getPlayerList().getPlayerByName(username);
        if (player != null) return player.getName().getString();
        // 1.21.9+
        //if (tryGetOfflinePlayer) return server.services().profileResolver().fetchByName(username).map(GameProfile::name).orElse("");
        return "";
    }

    public static void opPlayer(ServerPlayer player) {
        getPlayerList().op(player.getGameProfile());
    }

    public static void opPlayer(GameProfile profile) {
        getPlayerList().op(profile);
    }

    public static void deopPlayer(ServerPlayer player) {
        getPlayerList().deop(player.getGameProfile());
    }

    public static void deopPlayer(GameProfile profile) {
        getPlayerList().deop(profile);
    }

    public static boolean isOp(ServerPlayer player) {
        return getPlayerList().isOp(player.getGameProfile());
    }

    public static boolean isOp(GameProfile profile) {
        return getPlayerList().isOp(profile);
    }

    public static void broadcast(Component message, boolean showAboveHotbar) {
        getPlayerList().broadcastSystemMessage(message, showAboveHotbar);
    }

    public static void kickUnwhitelisted() {
        server.kickUnlistedPlayers(server.createCommandSourceStack());
    }

    // Levels
    public static ServerLevel overworld() {
        return server.overworld();
    }

    public static ServerLevel getLevel(ResourceKey<Level> dimension) {
        return server.getLevel(dimension);
    }

    public static Iterable<ServerLevel> getLevels() {
        return server.getAllLevels();
    }

    public static GameRules gameRules() {
        return server.getGameRules();
    }

    public static WorldData worldData() {
        return server.getWorldData();
    }

    public static Difficulty getDifficulty() {
        return worldData().getDifficulty();
    }

    public static void setDifficulty(Difficulty difficulty, boolean force) {
        server.setDifficulty(difficulty, force);
    }

    public static GameType getDefaultGameType() {
        return server.getDefaultGameType();
    }

    public static void setDefaultGameType(GameType type) {
        server.setDefaultGameType(type);
    }

    public static boolean isHardcore() {
        return server.isHardcore();
    }

    // Registries / managers
    public static Commands commands() {
        return server.getCommands();
    }

    public static void runCommandAs(CommandSourceStack source, String command) {
        commands().performPrefixedCommand(source, command);
    }

    public static void runCommandAsServer(String command) {
        commands().performPrefixedCommand(server.createCommandSourceStack(), command);
    }

    public static ServerFunctionManager functions() {
        return server.getFunctions();
    }

    public static ServerAdvancementManager advancements() {
        return server.getAdvancements();
    }

    public static RecipeManager recipeManager() {
        return server.getRecipeManager();
    }

    public static StructureTemplateManager structureManager() {
        return server.getStructureManager();
    }

    public static ServerScoreboard scoreboard() {
        return server.getScoreboard();
    }

    public static CustomBossEvents bossEvents() {
        return server.getCustomBossEvents();
    }

    // Networking / identity
    public static ServerConnectionListener getConnection() {
        return server.getConnection();
    }

    public static int getPermissionLevel(ServerPlayer player) {
        return getPermissionLevel(player.getGameProfile());
    }

    public static int getPermissionLevel(GameProfile profile) {
        return server.getProfilePermissions(profile);
    }

    public static boolean isOnlineMode() {
        return server.usesAuthentication();
    }

    public static boolean isOfflineMode() {
        return !isOnlineMode();
    }

    // General state
    public static boolean isRunning() {
        return server.isRunning();
    }

    public static boolean isStopped() {
        return server.isStopped();
    }

    public static boolean isReady() {
        return server.isReady();
    }

    public static boolean isSaving() {
        return server.isCurrentlySaving();
    }

    public static int getTickCount() {
        return server.getTickCount();
    }

    public static float getAvgTickTime() {
        return server.getAverageTickTime();
    }

    public static String getMotd() {
        return server.getMotd();
    }

    public static void setMotd(String motd) {
        server.setMotd(motd);
    }

    public static int getPort() {
        return server.getPort();
    }

    public static void setPort(int port) {
        server.setPort(port);
    }

    public static boolean isPvpOn() {
        return server.isPvpAllowed();
    }

    public static void setPvp(boolean status) {
        server.setPvpAllowed(status);
    }

    public static boolean isFlightAllowed() {
        return server.isFlightAllowed();
    }

    public static void setFlightAllowed(boolean allowed) {
        server.setFlightAllowed(allowed);
    }

    public static boolean isSingleplayer() {
        return server.isSingleplayer();
    }

    public static int getPlayerIdleTimeout() {
        return server.getPlayerIdleTimeout();
    }

    public static void setPlayerIdleTimeout(int timeout) {
        server.setPlayerIdleTimeout(timeout);
    }

    public static boolean isWhitelistOn() {
        return server.isEnforceWhitelist();
    }

    public static void setWhitelist(boolean status) {
        server.setEnforceWhitelist(status);
    }

    public static int spawnProtectionRadius() {
        return server.getSpawnProtectionRadius();
    }

    public static int absoluteMaxWorldSize() {
        return server.getAbsoluteMaxWorldSize();
    }

    public static Optional<MinecraftServer.ServerResourcePackInfo> resourcePack() {
        return server.getServerResourcePack();
    }

    public static boolean resourcePackRequired() {
        return server.isResourcePackRequired();
    }

    public static String serverVersion() {
        return server.getServerVersion();
    }

    public static void saveEverything(boolean suppressLog, boolean flush, boolean forced) {
        server.saveEverything(suppressLog, flush, forced);
    }

    public static void saveChunks(boolean suppressLog, boolean flush, boolean forced) {
        server.saveAllChunks(suppressLog, flush, forced);
    }

    public static void pause() {
        server.halt(true);
    }

    public static void unpause() {
        server.halt(false);
    }

    // Integrated only
    public static boolean isOpenToLAN() {
        return integrated.isPublished();
    }

    public static boolean openToLAN(GameType gameMode, boolean cheats, int port) {
        return integrated.publishServer(gameMode, cheats, port);
    }

    // Dedicated only
    public static DedicatedServerProperties getProperties() {
        return dedicated.getProperties();
    }

    public static String serverIp() {
        return dedicated.getServerIp();
    }

    public static int serverPort() {
        return dedicated.getServerPort();
    }

    public static String runCommand(String command) {
        return dedicated.runCommand(command);
    }
}
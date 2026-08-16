package net.justmili.libs.v1.utils.server;

import com.mojang.authlib.GameProfile;
import net.justmili.libs.v1.event.server.ServerLifecycleEvents;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.commands.Commands;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.*;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.level.storage.loot.LootDataManager;

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
    public static PlayerList playerList() {
        return server.getPlayerList();
    }

    public static int maxPlayers() {
        return playerList().getMaxPlayers();
    }

    public static int playerCount() {
        return playerList().getPlayerCount();
    }

    public static List<ServerPlayer> players() {
        return playerList().getPlayers();
    }

    public static ServerPlayer player(UUID uuid) {
        return playerList().getPlayer(uuid);
    }

    public static ServerPlayer player(String username) {
        return playerList().getPlayerByName(username);
    }

    public static void opPlayer(ServerPlayer player) {
        playerList().op(player.getGameProfile());
    }

    public static void opPlayer(GameProfile profile) {
        playerList().op(profile);
    }

    public static void deopPlayer(ServerPlayer player) {
        playerList().deop(player.getGameProfile());
    }

    public static void deopPlayer(GameProfile profile) {
        playerList().deop(profile);
    }

    public static boolean isOp(ServerPlayer player) {
        return playerList().isOp(player.getGameProfile());
    }

    public static boolean isOp(GameProfile profile) {
        return playerList().isOp(profile);
    }

    public static void kickUnwhitelisted() {
        server.kickUnlistedPlayers(server.createCommandSourceStack());
    }

    // Levels
    public static ServerLevel overworld() {
        return server.overworld();
    }

    public static ServerLevel level(ResourceKey<Level> dimension) {
        return server.getLevel(dimension);
    }

    public static Iterable<ServerLevel> levels() {
        return server.getAllLevels();
    }

    public static GameRules gameRules() {
        return server.getGameRules();
    }

    public static WorldData worldData() {
        return server.getWorldData();
    }

    public static Difficulty difficulty() {
        return worldData().getDifficulty();
    }

    public static void setDifficulty(Difficulty difficulty, boolean force) {
        server.setDifficulty(difficulty, force);
    }

    public static GameType defaultGameType() {
        return server.getDefaultGameType();
    }

    public static void setDefaultGameType(GameType type) {
        server.setDefaultGameType(type);
    }

    public static boolean isHardcore() {
        return server.isHardcore();
    }

    public static boolean spawningMonsters() {
        return server.isSpawningMonsters();
    }

    public static boolean spawningAnimals() {
        return server.isSpawningAnimals();
    }

    public static boolean npcsEnabled() {
        return server.areNpcsEnabled();
    }

    // Registries / managers
    public static Commands commands() {
        return server.getCommands();
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

    public static LootDataManager lootData() {
        return server.getLootData();
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
    public static ServerConnectionListener connection() {
        return server.getConnection();
    }

    public static int permissionLevel(ServerPlayer player) {
        return server.getProfilePermissions(player.getGameProfile());
    }

    public static int permissionLevel(GameProfile profile) {
        return server.getProfilePermissions(profile);
    }

    public static boolean usesAuthentication() {
        return server.usesAuthentication();
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

    public static int tickCount() {
        return server.getTickCount();
    }

    public static float averageTickTime() {
        return server.getAverageTickTime();
    }

    public static String motd() {
        return server.getMotd();
    }

    public static void setMotd(String motd) {
        server.setMotd(motd);
    }

    public static int port() {
        return server.getPort();
    }

    public static void setPort(int port) {
        server.setPort(port);
    }

    public static boolean pvpAllowed() {
        return server.isPvpAllowed();
    }

    public static void setPvpAllowed(boolean allowed) {
        server.setPvpAllowed(allowed);
    }

    public static boolean flightAllowed() {
        return server.isFlightAllowed();
    }

    public static void setFlightAllowed(boolean allowed) {
        server.setFlightAllowed(allowed);
    }

    public static boolean isSingleplayer() {
        return server.isSingleplayer();
    }

    public static int playerIdleTimeout() {
        return server.getPlayerIdleTimeout();
    }

    public static void setPlayerIdleTimeout(int timeout) {
        server.setPlayerIdleTimeout(timeout);
    }

    public static boolean whitelistEnforced() {
        return server.isEnforceWhitelist();
    }

    public static void setEnforceWhitelist(boolean enforce) {
        server.setEnforceWhitelist(enforce);
    }

    public static int spawnProtectionRadius() {
        return server.getSpawnProtectionRadius();
    }

    public static int absoluteMaxWorldSize() {
        return server.getAbsoluteMaxWorldSize();
    }

    public static boolean enforcesSecureProfile() {
        return server.enforceSecureProfile();
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

    public static FrameTimer frameTimer() {
        return server.getFrameTimer();
    }

    public static ProfilerFiller profiler() {
        return server.getProfiler();
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

    public static void stop() {
        server.stopServer();
    }

    // Integrated only
    public static boolean isOpenToLAN() {
        return integrated.isPublished();
    }

    public static boolean openToLAN(GameType gameMode, boolean cheats, int port) {
        return integrated.publishServer(gameMode, cheats, port);
    }

    public static void setUUID(UUID uuid) {
        integrated.setUUID(uuid);
    }

    // Dedicated only
    public static DedicatedServerProperties properties() {
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

    public static long maxTickLength() {
        return dedicated.getMaxTickLength();
    }

    public static String levelIdName() {
        return dedicated.getLevelIdName();
    }
}
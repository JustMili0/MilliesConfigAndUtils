package net.justmili.libs.utils;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class FdaApiUtil {
    /// Uncomment with Minecraft 1.20.4+
//    //True/false and text
//    public static boolean getBoolValue(ServerPlayer player, AttachmentType<Boolean> variable) {
//        return getValue(player, variable, false);
//    }
//
//    public static void setBoolValue(ServerPlayer player, AttachmentType<Boolean> variable, boolean Bool) {
//        player.setAttached(variable, Bool);
//    }
//
//    public static String getStringValue(ServerPlayer player, AttachmentType<String> variable) {
//        return getValue(player, variable, "ValueReturnedNull");
//    }
//
//    public static void setStringValue(ServerPlayer player, AttachmentType<String> variable, String string) {
//        player.setAttached(variable, string);
//    }
//
//    //Numbers yay
//    public static int getIntValue(ServerPlayer player, AttachmentType<Integer> variable) {
//        return getValue(player, variable, -1);
//    }
//
//    public static void setIntValue(ServerPlayer player, AttachmentType<Integer> variable, int Int) {
//        player.setAttached(variable, Int);
//    }
//
//    public static double getDoubleValue(ServerPlayer player, AttachmentType<Double> variable) {
//        return getValue(player, variable, -1.0);
//    }
//
//    public static void setDoubleValue(ServerPlayer player, AttachmentType<Double> variable, double Double) {
//        player.setAttached(variable, Double);
//    }
//
//    public static float getFloatValue(ServerPlayer player, AttachmentType<Float> variable) {
//        return getValue(player, variable, -1.0f);
//    }
//
//    public static void setFloatValue(ServerPlayer player, AttachmentType<Float> variable, float Float) {
//        player.setAttached(variable, Float);
//    }
//
//    public static long getLongValue(ServerPlayer player, AttachmentType<Long> variable) {
//        return getValue(player, variable, -1L);
//    }
//
//    public static void setLongValue(ServerPlayer player, AttachmentType<Long> variable, long Long) {
//        player.setAttached(variable, Long);
//    }
//
//    //Helper
//    //Gets value of provided variable
//    public static <T> T getValue(ServerPlayer player, AttachmentType<T> variable, T defaultValue) {
//        if (variable == null) return defaultValue;
//        return player.getAttachedOrElse(variable, defaultValue);
//    }
//
//    //Creates values that will clear after a restart
//    public static <T> AttachmentType<T> createValue(String namespacedName, T defaultValue, Codec<T> codec) {
//        return AttachmentRegistry.create(namespacedName,
//            builder -> builder.initializer(() -> defaultValue)
//                .copyOnDeath()
//        );
//    }
//
//    //Creates values that will NOT clear after a restart
//    public static <T> AttachmentType<T> createPersistentValue(String namespacedName, T defaultValue, Codec<T> codec) {
//        return AttachmentRegistry.create(namespacedName,
//            builder -> builder.initializer(() -> defaultValue)
//                .copyOnDeath()
//                .persistent(codec)
//        );
//    }
}

package net.justmili.libs.v1.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class AttributeUtil {
    public static AttributeModifier newModifier(ResourceLocation id, double value, AttributeModifier.Operation operation) {
        return new AttributeModifier(id.toString(), value, operation);
    }
    public static AttributeInstance getAttribute(Player player, Attribute attribute) {
        return player.getAttribute(attribute);
    }
    public static void addTransient(AttributeInstance instance, AttributeModifier modifier) {
        if (instance == null) return;
        instance.addTransientModifier(modifier);
    }
    public static void addPermanent(AttributeInstance instance, AttributeModifier modifier) {
        if (instance == null) return;
        instance.addPermanentModifier(modifier);
    }
    public static void addOrUpdate(AttributeInstance instance, AttributeModifier modifier) {
        // Added addOrUpdateTransientModifier in 1.20.5
        if (instance == null) return;
        addTransient(instance, modifier);
    }
    public static void addOrReplace(AttributeInstance instance, AttributeModifier modifier) {
        // Added addOrReplacePermanentModifier in 1.20.5
        if (instance == null) return;
        instance.removeModifier(modifier);
        addPermanent(instance, modifier);
    }
}

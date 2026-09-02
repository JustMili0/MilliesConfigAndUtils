package net.justmili.utils.utils.client;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemRendererUtil {
    public enum ItemDisplay { FIRST_PERSON, THIRD_PERSON, HEAD, GUI, FIXED, ON_SHELF, ON_GROUND }
    public record Entry(Item item, ModelResourceLocation otherItemModel, List<ItemDisplay> displayAt) {}
    private static final Set<Entry> OVERRIDES = new HashSet<>();

    public static void renderAsOther(Item item, ModelResourceLocation otherItemModel, ItemDisplay... displayAt) {
        OVERRIDES.add(new Entry(item, otherItemModel, List.of(displayAt)));
    }

    public static ItemDisplay toItemDisplay(ItemDisplayContext context) {
        return switch (context) {
            case ItemDisplayContext.GUI -> ItemDisplay.GUI;
            case ItemDisplayContext.GROUND -> ItemDisplay.ON_GROUND;
            case ItemDisplayContext.FIXED -> ItemDisplay.FIXED;
            case ItemDisplayContext.ON_SHELF -> ItemDisplay.ON_SHELF;
            case ItemDisplayContext.HEAD -> ItemDisplay.HEAD;
            case ItemDisplayContext.FIRST_PERSON_LEFT_HAND, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND -> ItemDisplay.FIRST_PERSON;
            case ItemDisplayContext.THIRD_PERSON_LEFT_HAND, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND -> ItemDisplay.THIRD_PERSON;
            default -> null;
        };
    }

    public static Entry getOverrideItem(Item item) {
        for (Entry entry : OVERRIDES) {
            if (entry.item() == item) return entry;
        }
        return null;
    }
}
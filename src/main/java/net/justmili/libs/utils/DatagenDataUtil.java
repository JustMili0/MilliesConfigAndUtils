package net.justmili.libs.utils;

import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DatagenDataUtil {
    public static class Recipes {
        public static class Crafting {
            public static void shapeless(String modId, RecipeOutput writer, RecipeCategory category, Item output, int outCount, Item... inputs) {
                ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(category, output, outCount);
                for (Item input : inputs) builder.requires(input);
                String inputNames = Arrays.stream(inputs)
                    .map(RecipeProvider::getItemName)
                    .collect(Collectors.joining("_and_"));
                builder.unlockedBy(RecipeProvider.getHasName(inputs[0]), RecipeProvider.has(inputs[0]))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(output) + "_from_" + inputNames)));
            }
            public static void shapeless(String modId, RecipeOutput writer, RecipeCategory category, Item output, Item... inputs) {
                shapeless(writer, category, output, 1, inputs);
            }
            public static void shaped2x2(String modId, RecipeOutput writer, RecipeCategory category, Item material, Item output, int outCount) {
                ShapedRecipeBuilder.shaped(category, output, outCount)
                    .define('#', material)
                    .pattern("##")
                    .pattern("##")
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(output)));
            }
            public static void shaped3x3(String modId, RecipeOutput writer, RecipeCategory category, Item material, Item output, int outCount) {
                ShapedRecipeBuilder.shaped(category, output, outCount)
                    .define('#', material)
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(output)));
            }
        }
        public static class Building {
            public static void planks(String modId, RecipeOutput writer, Item log, Item planks) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
                    .requires(log)
                    .unlockedBy(RecipeProvider.getHasName(log), RecipeProvider.has(log))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(planks)));
            }
            public static void stairs(String modId, RecipeOutput writer, Item material, Item stairs) {
                RecipeProvider.stairBuilder(stairs, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(stairs)));
            }
            public static void slab(String modId, RecipeOutput writer, Item material, Item slab) {
                RecipeProvider.slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(slab)));
            }
            public static void fence(String modId, RecipeOutput writer, Item material, Item fence) {
                RecipeProvider.fenceBuilder(fence, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(fence)));
            }
            public static void fenceGate(String modId, RecipeOutput writer, Item material, Item fenceGate) {
                RecipeProvider.fenceGateBuilder(fenceGate, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(fenceGate)));
            }
            public static void door(String modId, RecipeOutput writer, Item material, Item door) {
                RecipeProvider.doorBuilder(door, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(door)));
            }
            public static void trapdoor(String modId, RecipeOutput writer, Item material, Item trapdoor) {
                RecipeProvider.trapdoorBuilder(trapdoor, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(trapdoor)));
            }
            public static void wall(String modId, RecipeOutput writer, Item material, Item wall) {
                RecipeProvider.wallBuilder(RecipeCategory.BUILDING_BLOCKS, wall, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(wall)));
            }
            public static void bars(String modId, RecipeOutput writer, Item material, Item bars) {
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, bars, 16)
                    .define('#', material)
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(bars)));
            }
        }
        public static class Redstone {
            public static void pressurePlate(String modId, RecipeOutput writer, Item material, Item pressurePlate) {
                RecipeProvider.pressurePlateBuilder(RecipeCategory.REDSTONE, pressurePlate, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(pressurePlate)));
            }
            public static void button(String modId, RecipeOutput writer, Item material, Item button) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button)
                    .requires(material)
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(button)));
            }
        }
        public static class Processing {
            public static void smelt(String modId, RecipeOutput writer, Item input, Item output, float exp, int cookingTime) {
                SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
                    .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(output) + "_from_" + RecipeProvider.getItemName(input) + "_smelting"));
            }
            public static void smelt(String modId, RecipeOutput writer, Item input, Item output, float exp) {
                smelt(writer, input, output, exp, 200);
            }
            public static void blast(String modId, RecipeOutput writer, Item input, Item output, float exp, int cookingTime) {
                SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
                    .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(output) + "_from_" + RecipeProvider.getItemName(input) + "_blasting"));
            }
            public static void blast(String modId, RecipeOutput writer, Item input, Item output, float exp) {
                blast(writer, input, output, exp, 100);
            }
            public static void smoke(String modId, RecipeOutput writer, Item input, Item output, float exp, int cookingTime) {
                SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
                    .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(output) + "_from_" + RecipeProvider.getItemName(input) + "_smoking"));
            }
            public static void smoke(String modId, RecipeOutput writer, Item input, Item output, float exp) {
                smoke(writer, input, output, exp, 100);
            }
            public static void cut(String modId, RecipeOutput writer, Item input, Item output, int resultCount) {
                SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, resultCount)
                    .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(output) + "_from_" + RecipeProvider.getItemName(input) + "_stonecutting"));
            }
            public static void cut(String modId, RecipeOutput writer, Item input, Item output) {
                cut(writer, input, output, 1);
            }
            public static void smithing(String modId, RecipeOutput writer, Item base, Item addition, Item template, RecipeCategory category, Item result) {
                SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(addition), category, result)
                    .unlocks(RecipeProvider.getHasName(addition), RecipeProvider.has(addition))
                    .save(writer, String.valueOf(Identifier.fromNamespaceAndPath(modId, RecipeProvider.getItemName(result) + "_from_" + RecipeProvider.getItemName(base) + "_smithing"));
            }
        }
    }
}
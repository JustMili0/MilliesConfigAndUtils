package net.justmili.libs.utils;

import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DatagenDataUtil {
    public static class Recipes {
        public static class Crafting {
            public static void shapeless(Consumer<FinishedRecipe> writer, RecipeCategory category, Item output, int outCount, Item... inputs) {
                ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(category, output, outCount);
                for (Item input : inputs) builder.requires(input);
                String inputNames = Arrays.stream(inputs)
                    .map(RecipeProvider::getItemName)
                    .collect(Collectors.joining("_and_"));
                builder.unlockedBy(RecipeProvider.getHasName(inputs[0]), RecipeProvider.has(inputs[0]))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(output) + "_from_" + inputNames));
            }
            public static void shapeless(Consumer<FinishedRecipe> writer, RecipeCategory category, Item output, Item... inputs) {
                shapeless(writer, category, output, 1, inputs);
            }
            public static void shaped2x2(Consumer<FinishedRecipe> writer, RecipeCategory category, Item material, Item output, int outCount) {
                ShapedRecipeBuilder.shaped(category, output, outCount)
                    .define('#', material)
                    .pattern("##")
                    .pattern("##")
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(output)));
            }
            public static void shaped3x3(Consumer<FinishedRecipe> writer, RecipeCategory category, Item material, Item output, int outCount) {
                ShapedRecipeBuilder.shaped(category, output, outCount)
                    .define('#', material)
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(output)));
            }
        }
        public static class Building {
            public static void planks(Consumer<FinishedRecipe> writer, Item log, Item planks) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
                    .requires(log)
                    .unlockedBy(RecipeProvider.getHasName(log), RecipeProvider.has(log))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(planks)));
            }
            public static void stairs(Consumer<FinishedRecipe> writer, Item material, Item stairs) {
                RecipeProvider.stairBuilder(stairs, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(stairs)));
            }
            public static void slab(Consumer<FinishedRecipe> writer, Item material, Item slab) {
                RecipeProvider.slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(slab)));
            }
            public static void fence(Consumer<FinishedRecipe> writer, Item material, Item fence) {
                RecipeProvider.fenceBuilder(fence, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(fence)));
            }
            public static void fenceGate(Consumer<FinishedRecipe> writer, Item material, Item fenceGate) {
                RecipeProvider.fenceGateBuilder(fenceGate, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(fenceGate)));
            }
            public static void door(Consumer<FinishedRecipe> writer, Item material, Item door) {
                RecipeProvider.doorBuilder(door, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(door)));
            }
            public static void trapdoor(Consumer<FinishedRecipe> writer, Item material, Item trapdoor) {
                RecipeProvider.trapdoorBuilder(trapdoor, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(trapdoor)));
            }
            public static void wall(Consumer<FinishedRecipe> writer, Item material, Item wall) {
                RecipeProvider.wallBuilder(RecipeCategory.BUILDING_BLOCKS, wall, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(wall)));
            }
            public static void bars(Consumer<FinishedRecipe> writer, Item material, Item bars) {
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, bars, 16)
                    .define('#', material)
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(bars)));
            }
        }
        public static class Redstone {
            public static void pressurePlate(Consumer<FinishedRecipe> writer, Item material, Item pressurePlate) {
                RecipeProvider.pressurePlateBuilder(RecipeCategory.REDSTONE, pressurePlate, Ingredient.of(material))
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(pressurePlate)));
            }
            public static void button(Consumer<FinishedRecipe> writer, Item material, Item button) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button)
                    .requires(material)
                    .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(button)));
            }
        }
        public static class Processing {
            public static void smelt(Consumer<FinishedRecipe> writer, Item input, Item output, float exp, int cookingTime) {
                SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
                    .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(output) + "_from_" + RecipeProvider.getItemName(input) + "_smelting"));
            }
            public static void smelt(Consumer<FinishedRecipe> writer, Item input, Item output, float exp) {
                smelt(writer, input, output, exp, 200);
            }
            public static void blast(Consumer<FinishedRecipe> writer, Item input, Item output, float exp, int cookingTime) {
                SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
                    .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(output) + "_from_" + RecipeProvider.getItemName(input) + "_blasting"));
            }
            public static void blast(Consumer<FinishedRecipe> writer, Item input, Item output, float exp) {
                blast(writer, input, output, exp, 100);
            }
            public static void smoke(Consumer<FinishedRecipe> writer, Item input, Item output, float exp, int cookingTime) {
                SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
                    .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(output) + "_from_" + RecipeProvider.getItemName(input) + "_smoking"));
            }
            public static void smoke(Consumer<FinishedRecipe> writer, Item input, Item output, float exp) {
                smoke(writer, input, output, exp, 100);
            }
            public static void cut(Consumer<FinishedRecipe> writer, Item input, Item output, int resultCount) {
                SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, resultCount)
                    .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(output) + "_from_" + RecipeProvider.getItemName(input) + "_stonecutting"));
            }
            public static void cut(Consumer<FinishedRecipe> writer, Item input, Item output) {
                cut(writer, input, output, 1);
            }
            public static void smithing(Consumer<FinishedRecipe> writer, Item base, Item addition, Item template, RecipeCategory category, Item result) {
                SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(addition), category, result)
                    .unlocks(RecipeProvider.getHasName(addition), RecipeProvider.has(addition))
                    .save(writer, LumynAPI.getHooked().asResource(RecipeProvider.getItemName(result) + "_from_" + RecipeProvider.getItemName(base) + "_smithing"));
            }
        }
    }
}
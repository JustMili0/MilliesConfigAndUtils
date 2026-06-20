package net.justmili.libs.v1.utils;

import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public record DatagenDataUtil(String modId, Consumer<FinishedRecipe> writer) {

    public void shapeless(RecipeCategory category, Item output, int outCount, Item... inputs) {
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(category, output, outCount);
        for (Item input : inputs) builder.requires(input);
        String inputNames = Arrays.stream(inputs)
            .map(RecipeProvider::getItemName)
            .collect(Collectors.joining("_and_"));
        builder.unlockedBy(RecipeProvider.getHasName(inputs[0]), RecipeProvider.has(inputs[0]))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(output)+"_from_"+inputNames));
    }

    public void shapeless(RecipeCategory category, Item output, Item... inputs) {
        shapeless(category, output, 1, inputs);
    }

    public void shaped2x2(RecipeCategory category, Item material, Item output, int outCount) {
        ShapedRecipeBuilder.shaped(category, output, outCount)
            .define('#', material)
            .pattern("##")
            .pattern("##")
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(output)));
    }

    public void shaped3x3(RecipeCategory category, Item material, Item output, int outCount) {
        ShapedRecipeBuilder.shaped(category, output, outCount)
            .define('#', material)
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(output)));
    }

    public void planks(Item log, Item planks) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
            .requires(log)
            .unlockedBy(RecipeProvider.getHasName(log), RecipeProvider.has(log))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(planks)));
    }

    public void stairs(Item material, Item stairs) {
        RecipeProvider.stairBuilder(stairs, Ingredient.of(material))
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(stairs)));
    }

    public void slab(Item material, Item slab) {
        RecipeProvider.slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(material))
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(slab)));
    }

    public void fence(Item material, Item fence) {
        RecipeProvider.fenceBuilder(fence, Ingredient.of(material))
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(fence)));
    }

    public void fenceGate(Item material, Item fenceGate) {
        RecipeProvider.fenceGateBuilder(fenceGate, Ingredient.of(material))
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(fenceGate)));
    }

    public void door(Item material, Item door) {
        RecipeProvider.doorBuilder(door, Ingredient.of(material))
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(door)));
    }

    public void trapdoor(Item material, Item trapdoor) {
        RecipeProvider.trapdoorBuilder(trapdoor, Ingredient.of(material))
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(trapdoor)));
    }

    public void wall(Item material, Item wall) {
        RecipeProvider.wallBuilder(RecipeCategory.BUILDING_BLOCKS, wall, Ingredient.of(material))
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(wall)));
    }

    public void bars(Item material, Item bars) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, bars, 16)
            .define('#', material)
            .pattern("###")
            .pattern("###")
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(bars)));
    }

    public void pressurePlate(Item material, Item pressurePlate) {
        RecipeProvider.pressurePlateBuilder(RecipeCategory.REDSTONE, pressurePlate, Ingredient.of(material))
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(pressurePlate)));
    }

    public void button(Item material, Item button) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button)
            .requires(material)
            .unlockedBy(RecipeProvider.getHasName(material), RecipeProvider.has(material))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(button)));
    }

    public void smelt(Item input, Item output, float exp, int cookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
            .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(output)+"_from_"+RecipeProvider.getItemName(input)+"_smelting"));
    }

    public void smelt(Item input, Item output, float exp) {
        smelt(input, output, exp, 200);
    }

    public void blast(Item input, Item output, float exp, int cookingTime) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
            .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(output)+"_from_"+RecipeProvider.getItemName(input)+"_blasting"));
    }

    public void blast(Item input, Item output, float exp) {
        blast(input, output, exp, 100);
    }

    public void smoke(Item input, Item output, float exp, int cookingTime) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
            .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(output)+"_from_"+RecipeProvider.getItemName(input)+"_smoking"));
    }

    public void smoke(Item input, Item output, float exp) {
        smoke(input, output, exp, 100);
    }

    public void cut(Item input, Item output, int resultCount) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, resultCount)
            .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(output)+"_from_"+RecipeProvider.getItemName(input)+"_stonecutting"));
    }

    public void cut(Item input, Item output) {
        cut(input, output, 1);
    }

    public void smithing(Item base, Item addition, Item template, RecipeCategory category, Item result) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(addition), category, result)
            .unlocks(RecipeProvider.getHasName(addition), RecipeProvider.has(addition))
            .save(writer, ResourceUtil.parse(modId, RecipeProvider.getItemName(result)+"_from_"+RecipeProvider.getItemName(base)+"_smithing"));
    }
}
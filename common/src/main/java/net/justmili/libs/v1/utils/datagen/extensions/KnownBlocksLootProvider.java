package net.justmili.libs.v1.utils.datagen.extensions;

import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public interface KnownBlocksLootProvider {
    Stream<Block> getKnownBlocks();
}

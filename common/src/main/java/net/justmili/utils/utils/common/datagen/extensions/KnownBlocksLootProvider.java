package net.justmili.utils.utils.common.datagen.extensions;

import net.minecraft.world.level.block.Block;

public interface KnownBlocksLootProvider {
    Iterable<Block> getKnownBlocks();
}

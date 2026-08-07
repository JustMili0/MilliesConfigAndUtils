package net.justmili.libs.v1.utils.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class LevelUtil {
    public static void setAir(Level level, BlockPos pos) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
    }

    public static void setAir(Level level, int x, int y, int z) {
        level.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
    }
}
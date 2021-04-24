package charcoalPit.block;

import charcoalPit.core.ModBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockBananaLeaves extends LeavesBlock {
	public BlockBananaLeaves(Properties properties) {
		super(properties);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if(!state.get(PERSISTENT)&&state.get(DISTANCE)==1){
			BlockPos pos2=pos.down();
			if(worldIn.getBlockState(pos2).getMaterial().isReplaceable()) {
				for (Direction dir : Direction.Plane.HORIZONTAL) {
					if (worldIn.getBlockState(pos2.offset(dir)).getBlock().isIn(BlockTags.JUNGLE_LOGS)) {
						worldIn.setBlockState(pos2, ModBlockRegistry.BanananaPod.getDefaultState().with(BlockBananaPod.HORIZONTAL_FACING,dir).with(BlockBananaPod.COUNT,random.nextInt(3)+1),3);
						break;
					}
				}
			}
		}
		super.randomTick(state, worldIn, pos, random);
	}
	
	@Override
	public boolean ticksRandomly(BlockState state) {
		return super.ticksRandomly(state)||(!state.get(PERSISTENT)&&state.get(DISTANCE)==1);
	}
}

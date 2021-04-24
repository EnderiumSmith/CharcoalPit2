package charcoalPit.block;

import charcoalPit.core.ModBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockCoconutLeaves extends LeavesBlock {
	
	public BlockCoconutLeaves(Properties prop){
		super(prop);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if(!state.get(PERSISTENT)&&state.get(DISTANCE)==1){
			BlockPos pos2=pos.down();
			if(worldIn.getBlockState(pos2).getMaterial().isReplaceable()) {
				for (Direction dir : Direction.Plane.HORIZONTAL) {
					if (worldIn.getBlockState(pos2.offset(dir)).getBlock().isIn(BlockTags.JUNGLE_LOGS)) {
						worldIn.setBlockState(pos2, ModBlockRegistry.CoconutPod.getDefaultState().with(BlockCocoPod.HORIZONTAL_FACING,dir).with(BlockCocoPod.DOUBLE,random.nextBoolean()),3);
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

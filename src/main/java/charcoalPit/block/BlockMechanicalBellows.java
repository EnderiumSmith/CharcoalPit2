package charcoalPit.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockMechanicalBellows extends BlockBellows {
	
	public BlockMechanicalBellows(){
		super(Material.ROCK);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		return ActionResultType.PASS;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if(!worldIn.isRemote){
			boolean flag=worldIn.isBlockPowered(pos);
			if(flag&&!state.get(PUSH)){
				worldIn.getPendingBlockTicks().scheduleTick(pos, this, 10);
			}
		}
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if(state.get(PUSH)) {
			if(!worldIn.isBlockPowered(pos)) {
				worldIn.setBlockState(pos, state.with(PUSH, false));
			}else{
				worldIn.getPendingBlockTicks().scheduleTick(pos, this, 1);
			}
		}else {
			if(worldIn.isBlockPowered(pos)){
				worldIn.setBlockState(pos, state.with(PUSH, true));
				blow(worldIn, pos, state);
				worldIn.getPendingBlockTicks().scheduleTick(pos, this, 20);
				worldIn.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1F, 1F);
			}
		}
	}
}

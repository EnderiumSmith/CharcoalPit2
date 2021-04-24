package charcoalPit.block;

import net.minecraft.block.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockCocoPod extends HorizontalBlock implements IGrowable {
	
	public static final IntegerProperty AGE= BlockStateProperties.AGE_0_2;
	public static final BooleanProperty DOUBLE=BooleanProperty.create("double");
	
	public static final VoxelShape NORTH1=Block.makeCuboidShape(5.0D, 5.0D, 1.0D, 11.0D, 12.0D, 7.0D);
	public static final VoxelShape SOUTH1=Block.makeCuboidShape(5.0D, 5.0D, 9.0D, 11.0D, 12.0D, 15.0D);
	public static final VoxelShape EAST1=Block.makeCuboidShape(9.0D, 5.0D, 5.0D, 15.0D, 12.0D, 11.0D);
	public static final VoxelShape WEST1=Block.makeCuboidShape(1.0D, 5.0D, 5.0D, 7.0D, 12.0D, 11.0D);
	
	public static final VoxelShape NORTH2= VoxelShapes.combine(NORTH1.withOffset(4D/16D,0D,0D),NORTH1.withOffset(-4D/16D,0D,0D), IBooleanFunction.OR);
	public static final VoxelShape SOUTH2= VoxelShapes.combine(SOUTH1.withOffset(4D/16D,0D,0D),SOUTH1.withOffset(-4D/16D,0D,0D), IBooleanFunction.OR);
	public static final VoxelShape EAST2= VoxelShapes.combine(EAST1.withOffset(0D,0D,4D/16D),EAST1.withOffset(0D,0D,-4D/16D), IBooleanFunction.OR);
	public static final VoxelShape WEST2= VoxelShapes.combine(WEST1.withOffset(0D,0D,4D/16D),WEST1.withOffset(0D,0D,-4D/16D), IBooleanFunction.OR);
	
	public BlockCocoPod(Properties prop){
		super(prop);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING,AGE,DOUBLE);
	}
	
	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(AGE)<2;
	}
	
	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return false;
	}
	
	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		worldIn.setBlockState(pos,state.with(AGE,state.get(AGE)+1),2);
	}
	
	@Override
	public boolean ticksRandomly(BlockState state) {
		return true;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (random.nextFloat()<0.375F) {
			int i = state.get(AGE);
			if (i < 2 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, worldIn.rand.nextInt(5) == 0)) {
				worldIn.setBlockState(pos, state.with(AGE, i + 1), 2);
				net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
			}
			if(i==2){
				worldIn.destroyBlock(pos,true);
			}
		}
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos.offset(state.get(HORIZONTAL_FACING))).getBlock();
		return block.isIn(BlockTags.JUNGLE_LOGS);
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == stateIn.get(HORIZONTAL_FACING) && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if(state.get(DOUBLE)){
			switch (state.get(HORIZONTAL_FACING)){
				case NORTH:return NORTH2;
				case SOUTH:return SOUTH2;
				case EAST:return EAST2;
				default:return WEST2;
			}
		}else{
			switch (state.get(HORIZONTAL_FACING)){
				case NORTH:return NORTH1;
				case SOUTH:return SOUTH1;
				case EAST:return EAST1;
				default:return WEST1;
			}
		}
	}
}

package charcoalPit.block;


import net.minecraft.block.*;
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

public class BlockBananaPod extends HorizontalBlock implements IGrowable {
	
	public static final IntegerProperty AGE= BlockStateProperties.AGE_0_2;
	public static final IntegerProperty COUNT=IntegerProperty.create("count",1,3);
	
	public static VoxelShape North1=Block.makeCuboidShape(6.0D, 0.0D, 1.0D, 10.0D, 12.0D, 5.0D);
	public static VoxelShape South1=Block.makeCuboidShape(6.0D, 0.0D, 11.0D, 10.0D, 12.0D, 15.0D);
	public static VoxelShape East1=Block.makeCuboidShape(11.0D, 0.0D, 6.0D, 15.0D, 12.0D, 10.0D);
	public static VoxelShape West1=Block.makeCuboidShape(1.0D, 0.0D, 6.0D, 5.0D, 12.0D, 10.0D);
	
	public static VoxelShape North2=VoxelShapes.combine(North1.withOffset(3D/16D,0D,0D),North1.withOffset(-3D/16D,0D,0D), IBooleanFunction.OR);
	public static VoxelShape South2=VoxelShapes.combine(South1.withOffset(3D/16D,0D,0D),South1.withOffset(-3D/16D,0D,0D), IBooleanFunction.OR);
	public static VoxelShape East2=VoxelShapes.combine(East1.withOffset(0D,0D,3D/16D),East1.withOffset(0D,0D,-3D/16D), IBooleanFunction.OR);
	public static VoxelShape West2=VoxelShapes.combine(West1.withOffset(0D,0D,3D/16D),West1.withOffset(0D,0D,-3D/16D), IBooleanFunction.OR);
	
	public static VoxelShape North3=VoxelShapes.combine(North1,VoxelShapes.combine(North1.withOffset(5D/16D,0D,0D),North1.withOffset(-5D/16D,0D,0D),IBooleanFunction.OR),IBooleanFunction.OR);
	public static VoxelShape South3=VoxelShapes.combine(South1,VoxelShapes.combine(South1.withOffset(5D/16D,0D,0D),South1.withOffset(-5D/16D,0D,0D),IBooleanFunction.OR),IBooleanFunction.OR);
	public static VoxelShape East3=VoxelShapes.combine(East1,VoxelShapes.combine(East1.withOffset(0D,0D,5D/16D),East1.withOffset(0D,0D,-5D/16D),IBooleanFunction.OR),IBooleanFunction.OR);
	public static VoxelShape West3=VoxelShapes.combine(West1,VoxelShapes.combine(West1.withOffset(0D,0D,5D/16D),West1.withOffset(0D,0D,-5D/16D),IBooleanFunction.OR),IBooleanFunction.OR);
	
	public BlockBananaPod(Properties p){
		super(p);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING,AGE,COUNT);
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
		switch (state.get(COUNT)){
			case 1:{
				switch (state.get(HORIZONTAL_FACING)){
					case NORTH:return North1;
					case SOUTH:return South1;
					case EAST:return  East1;
					default:return West1;
				}
			}
			case 2:{
				switch (state.get(HORIZONTAL_FACING)){
					case NORTH:return North2;
					case SOUTH:return South2;
					case EAST:return  East2;
					default:return West2;
				}
			}
			case 3:{
				switch (state.get(HORIZONTAL_FACING)){
					case NORTH:return North3;
					case SOUTH:return South3;
					case EAST:return  East3;
					default:return West3;
				}
			}
		}
		return VoxelShapes.empty();
	}
}

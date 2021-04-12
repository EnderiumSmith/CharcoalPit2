package charcoalPit.block;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockCorn extends CropsBlock {
	
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D)};
	
	
	public BlockCorn(Properties builder) {
		super(builder);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
	}
	
	@Override
	protected int getBonemealAgeIncrease(World worldIn) {
		return MathHelper.nextInt(worldIn.rand, 2, 5);
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return (getAge(state)<5||worldIn.getBlockState(pos.offset(Direction.UP)).isAir(worldIn,pos.offset(Direction.UP)))&&super.isValidPosition(state,worldIn,pos);
	}
	
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (worldIn.getLightSubtracted(pos, 0) >= 9) {
			int i = this.getAge(state);
			if (canGrow(worldIn,pos,state,worldIn.isRemote)) {
				float f = getGrowthChance(this, worldIn, pos);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
					worldIn.setBlockState(pos, this.withAge(i + 1), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
				}
			}
		}
		
	}
	
	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return super.canGrow(worldIn,pos,state,isClient)&&worldIn.getBlockState(pos.offset(Direction.UP)).isAir(worldIn,pos);
	}
	
	@Override
	public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity) {
		return true;
	}
	
	@Override
	protected IItemProvider getSeedsItem() {
		return ModItemRegistry.CornKernels;
	}
}

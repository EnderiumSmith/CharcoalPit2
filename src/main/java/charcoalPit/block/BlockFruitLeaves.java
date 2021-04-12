package charcoalPit.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.EntitySelectionContext;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Random;

public class BlockFruitLeaves extends Block {
	
	public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE_1_7;
	public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
	public static final IntegerProperty AGE=BlockStateProperties.AGE_0_7;
	
	public final IItemProvider fruit;
	public final float tick_chance;
	
	public BlockFruitLeaves(Properties properties,IItemProvider fruit,float tick_rate) {
		super(properties);
		this.fruit=fruit;
		this.tick_chance=tick_rate;
		this.setDefaultState(this.getStateContainer().getBaseState().with(DISTANCE,7).with(PERSISTENT,false).with(AGE,0));
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if(context instanceof EntitySelectionContext){
			if(((EntitySelectionContext)context).getEntity() instanceof ItemEntity){
				return VoxelShapes.empty();
			}
		}
		return super.getCollisionShape(state,worldIn,pos,context);
	}
	
	@Override
	public boolean ticksRandomly(BlockState state) {
		return true;
	}
	
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		worldIn.setBlockState(pos, updateDistance(state, worldIn, pos), 3);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!state.get(PERSISTENT) && state.get(DISTANCE) == 7) {
			spawnDrops(state, worldIn, pos);
			worldIn.removeBlock(pos, false);
		}
		if(random.nextFloat()<tick_chance){
			int stage=state.get(AGE);
			for(BlockPos mutable:BlockPos.Mutable.getAllInBoxMutable(pos.down(2).north(2).west(2),pos.up(2).south(2).east(2))){
				if(worldIn.getBlockState(mutable).getBlock()==this&&!worldIn.getBlockState(mutable).get(PERSISTENT)){
					int stage2=worldIn.getBlockState(mutable).get(AGE);
					if(stage2+1==stage||(stage==0&&stage2==7))
						return;
				}
			}
			if(stage<7)
				worldIn.setBlockState(pos,state.with(AGE,stage+1),2);
			else{
				worldIn.setBlockState(pos,state.with(AGE,0),2);
				InventoryHelper.spawnItemStack(worldIn,pos.getX(),pos.getY(),pos.getZ(),new ItemStack(fruit));
			}
		}
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(DISTANCE,PERSISTENT,AGE);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(state.get(AGE)==7){
			ItemHandlerHelper.giveItemToPlayer(player,new ItemStack(fruit),player.inventory.currentItem);
			worldIn.setBlockState(pos,state.with(AGE,0),2);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		int i = getDistance(facingState) + 1;
		if (i != 1 || stateIn.get(DISTANCE) != i) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}
		
		return stateIn;
	}
	
	private static BlockState updateDistance(BlockState state, IWorld worldIn, BlockPos pos) {
		int i = 7;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		
		for(Direction direction : Direction.values()) {
			blockpos$mutable.setAndMove(pos, direction);
			i = Math.min(i, getDistance(worldIn.getBlockState(blockpos$mutable)) + 1);
			if (i == 1) {
				break;
			}
		}
		
		return state.with(DISTANCE, Integer.valueOf(i));
	}
	
	private static int getDistance(BlockState neighbor) {
		if (BlockTags.LOGS.contains(neighbor.getBlock())) {
			return 0;
		} else {
			return neighbor.getBlock() instanceof BlockFruitLeaves ? neighbor.get(DISTANCE) : 7;
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (worldIn.isRainingAt(pos.up())) {
			if (rand.nextInt(15) == 1) {
				BlockPos blockpos = pos.down();
				BlockState blockstate = worldIn.getBlockState(blockpos);
				if (!blockstate.isSolid() || !blockstate.isSolidSide(worldIn, blockpos, Direction.UP)) {
					double d0 = (double)pos.getX() + rand.nextDouble();
					double d1 = (double)pos.getY() - 0.05D;
					double d2 = (double)pos.getZ() + rand.nextDouble();
					worldIn.addParticle(ParticleTypes.DRIPPING_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return updateDistance(this.getDefaultState().with(PERSISTENT, true), context.getWorld(), context.getPos());
	}
}

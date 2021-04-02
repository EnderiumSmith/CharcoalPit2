package charcoalPit.block;

import java.util.ArrayList;
import java.util.List;

import charcoalPit.core.MethodHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;

public class BlockBloomeryPile extends Block{
	
	public static final IntegerProperty LAYER=BlockStateProperties.LAYERS_1_8;
	
	protected static final VoxelShape[] SHAPES = new VoxelShape[]{VoxelShapes.empty(), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	public BlockBloomeryPile() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(5F, 6F).harvestTool(ToolType.PICKAXE));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[state.get(LAYER)];
	}
	
	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	      builder.add(LAYER);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		ArrayList<ItemStack> drops=new ArrayList<>();
		drops.add(new ItemStack(Items.IRON_ORE, Math.min(4, state.get(LAYER))));
		drops.add(new ItemStack(Items.CHARCOAL, Math.max(0, state.get(LAYER)-4)));
		return drops;
	}
	
	//@Override
	/*public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		int size=state.get(LAYER);
		if(player.getHeldItem(handIn).getItem().isIn(Tags.Items.ORES_IRON)) {
			if(worldIn.isRemote)
				return ActionResultType.SUCCESS;
			else {
				if(size<4) {
					worldIn.setBlockState(pos, state.with(LAYER, size+1));
					player.getHeldItem(handIn).shrink(1);
					worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
				}
				if(size==8) {
					BlockState state2=worldIn.getBlockState(pos.offset(Direction.DOWN));
					BlockState state3=worldIn.getBlockState(pos.offset(Direction.UP));
					if(state2.getBlock()!=this) {
						if(state3.getBlock()==this) {
							state3.getBlock().onBlockActivated(state3, worldIn, pos.offset(Direction.UP), player, handIn, hit);
						}else {
							if(state3.getMaterial().isReplaceable()&&MethodHelper.BloomeryIsValidPosition(worldIn, pos.offset(Direction.UP),false)) {
								worldIn.setBlockState(pos.offset(Direction.UP), state.with(LAYER, 1));
								player.getHeldItem(handIn).shrink(1);
								worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
							}
						}
					}
				}
				return ActionResultType.SUCCESS;
			}
		}
		if(player.getHeldItem(handIn).getItem()==Items.CHARCOAL||player.getHeldItem(handIn).getItem()==Items.COAL) {
			if(worldIn.isRemote)
				return ActionResultType.SUCCESS;
			else {
				if(size>3&&size<8) {
					worldIn.setBlockState(pos, state.with(LAYER, size+1));
					player.getHeldItem(handIn).shrink(1);
					worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
				}
				if(size==8) {
					BlockState state2=worldIn.getBlockState(pos.offset(Direction.UP));
					if(state2.getBlock()==this) {
						state2.getBlock().onBlockActivated(state2, worldIn, pos.offset(Direction.UP), player, handIn, hit);
					}
				}
			}
		}
		return ActionResultType.PASS;
	}*/
	

}

package charcoalPit.block;

import java.util.Random;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.recipe.BloomeryRecipe;
import charcoalPit.tile.TileBloomery2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class BlockBellows extends Block{
	
	public static final DirectionProperty FACING=DirectionalBlock.FACING;
	public static final BooleanProperty PUSH=BooleanProperty.create("push");
	
	public static final VoxelShape NORTH=VoxelShapes.create(0D, 0D, 0D, 1D, 1D, 6D/16D);
	public static final VoxelShape SOUTH=VoxelShapes.create(0D, 0D, 10D/16D, 1D, 1D, 1D);
	public static final VoxelShape WEST=VoxelShapes.create(0D, 0D, 0D, 6D/16D, 1D, 1D);
	public static final VoxelShape EAST=VoxelShapes.create(10D/16D, 0D, 0D, 1D, 1D, 1D);
	public static final VoxelShape UP=VoxelShapes.create(0D, 10D/16D, 0D, 1D, 1D, 1D);
	public static final VoxelShape DOWN=VoxelShapes.create(0D, 0D, 0D, 1D, 6D/16D, 1D);

	public BlockBellows() {
		super(Properties.create(Material.WOOD).hardnessAndResistance(2, 3).harvestTool(ToolType.AXE));
		setDefaultState(getDefaultState().with(PUSH, false));
	}
	
	public BlockBellows(Material m){
		super(Properties.create(m).hardnessAndResistance(2, 3).harvestTool(ToolType.AXE));
		setDefaultState(getDefaultState().with(PUSH, false));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if(!state.get(PUSH))
			return VoxelShapes.fullCube();
		switch(state.get(FACING)) {
		case DOWN:return DOWN;
		case EAST:return EAST;
		case NORTH:return NORTH;
		case SOUTH:return SOUTH;
		case UP:return UP;
		case WEST:return WEST;
		default:return VoxelShapes.fullCube();
		
		}
	}
	
	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return VoxelShapes.empty();
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(FACING, PUSH);
	}
	
	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context) {
	      return this.getDefaultState().with(FACING, context.getNearestLookingDirection());
	   }
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if(!state.get(PUSH)) {
			if(!worldIn.isRemote) {
				worldIn.getPendingBlockTicks().scheduleTick(pos, this, 10);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if(state.get(PUSH)) {
			worldIn.setBlockState(pos, state.with(PUSH, false));
		}else {
			worldIn.setBlockState(pos, state.with(PUSH, true));
			blow(worldIn, pos, state);
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, 20);
			worldIn.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1F, 1F);
		}
	}
	
	public void blow(World world, BlockPos pos, BlockState state) {
		BlockPos pos2=pos.offset(state.get(FACING));
		BlockState front=world.getBlockState(pos2);
		if(front.getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "tuyere_blocks")))) {
			int divs=0;
			for(Direction dir:Direction.Plane.HORIZONTAL) {
				BlockPos pos3=pos2.offset(dir);
				if(world.getBlockState(pos3).getBlock()==ModBlockRegistry.Bloomery&&
						((TileBloomery2)world.getTileEntity(pos3)).dummy==false)
					divs++;
			}
			if(divs>0) {
				for(Direction dir:Direction.Plane.HORIZONTAL) {
					BlockPos pos3=pos2.offset(dir);
					if(world.getBlockState(pos3).getBlock()==ModBlockRegistry.Bloomery&&
							((TileBloomery2)world.getTileEntity(pos3)).dummy==false){
						((TileBloomery2)world.getTileEntity(pos3)).blow(60/divs);
					}
				}
			}
		}
	}

}

package charcoalPit.block;

import java.util.Random;

import charcoalPit.CharcoalPit;
import charcoalPit.core.Config;
import charcoalPit.core.MethodHelper;
import charcoalPit.tile.TileBloomery2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

public class BlockBloomery extends Block{
	
	public static final IntegerProperty STAGE=IntegerProperty.create("stage", 1, 12);
	//1-8 layers,9 active,10 done,11 worked,12 cooled
	
	protected static final VoxelShape[] SHAPES = new VoxelShape[]{VoxelShapes.empty(), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), 
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	public BlockBloomery() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(5F, 6F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
	}
	
	@Override
	public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
		if(Config.EnableBloomeryPickRequirement.get()&&
				state.get(STAGE)>9&&
				player.getHeldItemMainhand().getItem().isIn(ItemTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "blacklist_pickaxes"))))
			return false;
		return super.canHarvestBlock(state, world, pos, player);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[Math.min(8, state.get(STAGE))];
	}
	
	@Override
	public boolean isTransparent(BlockState state) {
		return state.get(STAGE)<8;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(STAGE);
	}
	
	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		int i=state.get(STAGE);
		if(i==9)
			return 15;
		if(i==10||i==11)
			return 9;
		return 0;
	}
	
	@Override
	public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
		return state.get(STAGE)==9;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		((TileBloomery2)worldIn.getTileEntity(pos)).isValid=false;
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasTileEntity() && (!state.isIn(newState.getBlock()) || !newState.hasTileEntity())) {
			 ((TileBloomery2)worldIn.getTileEntity(pos)).dropInventory();
	         worldIn.removeTileEntity(pos);
	      }
	}
	
	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player,
			boolean willHarvest, FluidState fluid) {
		if(state.get(STAGE)==10) {
			getBlock().onBlockHarvested(world, pos, state, player);
			player.addExhaustion(0.01F);
			world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1F, 1F);
			TileBloomery2 tile=((TileBloomery2)world.getTileEntity(pos));
			tile.work();
			return false;
		}else
			return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
	}
	
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
	      if (!entityIn.isImmuneToFire() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn) && worldIn.getBlockState(pos).get(STAGE)>=10 
	    		  && worldIn.getBlockState(pos).get(STAGE)<12) {
	         entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
	      }

	      super.onEntityWalk(worldIn, pos, entityIn);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if(stateIn.get(STAGE)!=9)
			return;
		double centerX = pos.getX() + 0.5F;
		double centerY = pos.getY() + 2F;
		double centerZ = pos.getZ() + 0.5F;
		//double d3 = 0.2199999988079071D;
		//double d4 = 0.27000001072883606D;
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.1D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.15D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY-1, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.1D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY-1, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.15D, 0.0D);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileBloomery2();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
			int size=state.get(STAGE);
			TileBloomery2 tile=((TileBloomery2)worldIn.getTileEntity(pos));
			if(tile.getRecipe().input.test(player.getHeldItem(handIn))) {
				if(size<8) {
					for(int i=0;i<tile.ore.getSlots();i++) {
						if(tile.ore.getStackInSlot(i).isEmpty()) {
							player.setHeldItem(handIn, tile.ore.insertItem(i, player.getHeldItem(handIn), false));
							worldIn.setBlockState(pos, state.with(STAGE, size+1));
							worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
							return ActionResultType.SUCCESS;
						}
					}
				}else {
					BlockPos up=pos.offset(Direction.UP);
					if(worldIn.isAirBlock(up)&&MethodHelper.Bloomery2ValidPosition(worldIn, up, true, false)) {
						worldIn.setBlockState(up, state.with(STAGE, 1));
						TileBloomery2 dummy=((TileBloomery2)worldIn.getTileEntity(up));
						dummy.recipe=tile.recipe;
						dummy.dummy=true;
						player.setHeldItem(handIn, dummy.ore.insertItem(0, player.getHeldItem(handIn), false));
						worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
						return ActionResultType.SUCCESS;
					}
				}
			}else {
				if(player.getHeldItem(handIn).getItem().isIn(ItemTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "basic_fuels")))) {
					if(size<8) {
						for(int i=0;i<tile.fuel.getSlots();i++) {
							if(tile.fuel.getStackInSlot(i).isEmpty()) {
								player.setHeldItem(handIn, tile.fuel.insertItem(i, player.getHeldItem(handIn), false));
								worldIn.setBlockState(pos, state.with(STAGE, size+1));
								worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
								return ActionResultType.SUCCESS;
							}
						}
					}else {
						BlockPos up=pos.offset(Direction.UP);
						if(worldIn.isAirBlock(up)&&MethodHelper.Bloomery2ValidPosition(worldIn, up, true, false)) {
							worldIn.setBlockState(up, state.with(STAGE, 1));
							TileBloomery2 dummy=((TileBloomery2)worldIn.getTileEntity(up));
							dummy.recipe=tile.recipe;
							dummy.dummy=true;
							player.setHeldItem(handIn, dummy.fuel.insertItem(0, player.getHeldItem(handIn), false));
							worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
							return ActionResultType.SUCCESS;
						}
					}
				}
			}
			return ActionResultType.FAIL;
		}else {
			return ActionResultType.SUCCESS;
		}
	}
	
	

}

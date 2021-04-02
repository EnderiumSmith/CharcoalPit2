package charcoalPit.block;

import java.util.ArrayList;
import java.util.List;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.tile.TileBloom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockBloom extends Block{

	public static final IntegerProperty LAYER=BlockStateProperties.LAYERS_1_8;
	public static final BooleanProperty HOT=BooleanProperty.create("hot");
	public static final BooleanProperty FAIL=BooleanProperty.create("fail");
	public static final BooleanProperty DOUBLE=BooleanProperty.create("double");
	
	public BlockBloom() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(5, 6).harvestTool(ToolType.PICKAXE).setRequiresTool().sound(SoundType.ANVIL));
	}
	
	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return state.get(HOT)?15:0;
	}
	
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
	      if (!entityIn.isImmuneToFire() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn) && worldIn.getBlockState(pos).get(HOT)) {
	         entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
	      }

	      super.onEntityWalk(worldIn, pos, entityIn);
	   }
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	      builder.add(LAYER,HOT,FAIL,DOUBLE);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileBloom();
	}
	
	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player,
			boolean willHarvest, FluidState fluid) {
		if(state.get(LAYER)>1&&state.get(HOT)&&!state.get(FAIL)) {
			getBlock().onBlockHarvested(world, pos, state, player);
			world.setBlockState(pos, state.with(LAYER, state.get(LAYER)-1), 5);
			player.addExhaustion(0.01F);
			world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1F, 1F);
			return false;
		}else {
			if(state.get(HOT))
				world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1F, 1F);
			return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		}
		
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		if(state.get(FAIL)) {
			ArrayList<ItemStack> drops=new ArrayList<>();
			drops.add(new ItemStack(ModItemRegistry.BloomFail, state.get(DOUBLE)?8:4));
			return drops;
		}else {
			int i=state.get(DOUBLE)?8:4;
			int j=i;
			i-=state.get(LAYER);
			if(state.get(HOT))
				i++;
			ArrayList<ItemStack> drops=new ArrayList<>();
			drops.add(new ItemStack(Items.IRON_INGOT, i));
			drops.add(new ItemStack(ModItemRegistry.BloomCool, j-i));
			return drops;
		}
	}

}

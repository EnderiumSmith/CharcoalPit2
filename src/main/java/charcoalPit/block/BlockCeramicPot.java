package charcoalPit.block;

import java.util.Arrays;
import java.util.List;

import charcoalPit.core.MethodHelper;
import charcoalPit.gui.CeramicPotContainer;
import charcoalPit.tile.TileCeramicPot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

public class BlockCeramicPot extends Block{
	
	public static final VoxelShape POT=VoxelShapes.create(2D/16D, 0D, 2D/16D, 14D/16D, 1D, 14D/16D);

	public BlockCeramicPot(MaterialColor color) {
		super(Properties.create(Material.SHULKER, color).hardnessAndResistance(1.25F,4.2F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE));
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider() {
				
				@Override
				public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
					return new CeramicPotContainer(p_createMenu_1_, pos, p_createMenu_2_);
				}
				
				@Override
				public ITextComponent getDisplayName() {
					return new TranslationTextComponent("screen.charcoal_pit.ceramic_pot");
				}
			}, pos);;
		}
		return ActionResultType.SUCCESS;
	}
	
	/*@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileCeramicPot tile=((TileCeramicPot)worldIn.getTileEntity(pos));
		ItemStack stack=new ItemStack(this);
		stack.setTagInfo("inventory", tile.inventory.serializeNBT());
		ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, stack);
        itementity.setDefaultPickupDelay();
        worldIn.addEntity(itementity);
		super.onBlockHarvested(worldIn, pos, state, player);
	}*/
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		TileCeramicPot tile=((TileCeramicPot)builder.get(LootParameters.BLOCK_ENTITY));
		ItemStack stack=new ItemStack(this);
		stack.setTagInfo("inventory", tile.inventory.serializeNBT());
		return Arrays.asList(stack);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if(stack.hasTag()&&stack.getTag().contains("inventory")) {
			((TileCeramicPot)worldIn.getTileEntity(pos)).inventory.deserializeNBT(stack.getTag().getCompound("inventory"));
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		worldIn.updateComparatorOutputLevel(pos, state.getBlock());
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	@Override
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		if(stack.hasTag()&&stack.getTag().contains("inventory")) {
			CompoundNBT compoundnbt = stack.getTag().getCompound("inventory");
		    ItemStackHandler items=new ItemStackHandler(9);
		    items.deserializeNBT(compoundnbt);
		    for(int k=0;k<9;k++) {
		    	ItemStack itemstack=items.getStackInSlot(k);
		    	if(!itemstack.isEmpty()) {
		    		IFormattableTextComponent iformattabletextcomponent = itemstack.getDisplayName().copyRaw();
	                iformattabletextcomponent.appendString(" x").appendString(String.valueOf(itemstack.getCount()));
	                tooltip.add(iformattabletextcomponent);
		    	}
		    }
	    }
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return POT;
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return MethodHelper.calcRedstoneFromInventory(((TileCeramicPot)worldIn.getTileEntity(pos)).inventory);
	}
	
	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		TileCeramicPot tile=((TileCeramicPot)worldIn.getTileEntity(pos));
		ItemStack stack=new ItemStack(this);
		stack.setTagInfo("inventory", tile.inventory.serializeNBT());
		return stack;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileCeramicPot();
	}

}

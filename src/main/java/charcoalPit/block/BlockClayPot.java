package charcoalPit.block;

import java.util.Arrays;
import java.util.List;

import charcoalPit.core.MethodHelper;
import charcoalPit.gui.CeramicPotContainer;
import charcoalPit.gui.ClayPotContainer;
import charcoalPit.recipe.OreKilnRecipe;
import charcoalPit.tile.TileCeramicPot;
import charcoalPit.tile.TileClayPot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.Minecraft;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

public class BlockClayPot extends Block{
	
	public BlockClayPot() {
		super(Properties.create(Material.SHULKER, MaterialColor.LIGHT_GRAY).hardnessAndResistance(0.5F).sound(SoundType.GROUND).harvestTool(ToolType.SHOVEL));
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider() {
				
				@Override
				public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
					return new ClayPotContainer(p_createMenu_1_, pos, p_createMenu_2_);
				}
				
				@Override
				public ITextComponent getDisplayName() {
					return new TranslationTextComponent("screen.charcoal_pit.clay_pot");
				}
			}, pos);
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		TileClayPot tile=((TileClayPot)builder.get(LootParameters.BLOCK_ENTITY));
		ItemStack stack=new ItemStack(this);
		stack.setTagInfo("inventory", tile.inventory.serializeNBT());
		return Arrays.asList(stack);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if(stack.hasTag()&&stack.getTag().contains("inventory")) {
			((TileClayPot)worldIn.getTileEntity(pos)).inventory.deserializeNBT(stack.getTag().getCompound("inventory"));
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		worldIn.updateComparatorOutputLevel(pos, state.getBlock());
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(stack.hasTag()&&stack.getTag().contains("inventory")) {
			ItemStackHandler inv=new ItemStackHandler();
			inv.deserializeNBT(stack.getTag().getCompound("inventory"));
			if(OreKilnRecipe.oreKilnIsEmpty(inv)) {
				tooltip.add(new StringTextComponent("Empty"));
			}else {
				ItemStack out=OreKilnRecipe.OreKilnGetOutput(stack.getTag().getCompound("inventory"), Minecraft.getInstance().world);
				if(out.isEmpty()) {
					tooltip.add(new StringTextComponent(TextFormatting.DARK_RED+"Invalid"+" ("+OreKilnRecipe.oreKilnGetOreAmount(inv)+"/8)"));
				}else {
					ITextComponent tx=out.getDisplayName().copyRaw().append(new StringTextComponent(" x"+out.getCount()));
					tx.getStyle().applyFormatting(TextFormatting.GREEN);
					tooltip.add(tx);
					//tooltip.add(new StringTextComponent(TextFormatting.GREEN+"").append(out.getDisplayName()).append(new StringTextComponent(" x"+out.getCount())));
				}
			}
			int f=OreKilnRecipe.oreKilnGetFuelAvailable(inv);
			int n=OreKilnRecipe.oreKilnGetFuelRequired(inv);
			if(f==0) {
				if(n==0) {
					tooltip.add(new StringTextComponent("No Fuel"));
				}else {
					tooltip.add(new StringTextComponent(TextFormatting.DARK_RED+"No Fuel (0/"+n+")"));
				}
			}else {
				if(f<n) {
					tooltip.add(new StringTextComponent(TextFormatting.DARK_RED+"Fuel x"+f+" ("+f+"/"+n+")"));
				}else {
					if(f>n) {
						tooltip.add(new StringTextComponent(TextFormatting.YELLOW+"Fuel x"+f+" ("+f+"/"+n+")"));
					}else{
						tooltip.add(new StringTextComponent(TextFormatting.GREEN+"Fuel x"+f+" ("+f+"/"+n+")"));
					}
				}
			}
		}
	}
	
	@Override
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return BlockCeramicPot.POT;
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return MethodHelper.calcRedstoneFromInventory(((TileClayPot)worldIn.getTileEntity(pos)).inventory);
	}
	
	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		TileClayPot tile=((TileClayPot)worldIn.getTileEntity(pos));
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
		return new TileClayPot();
	}
	
}

package charcoalPit.block;

import java.util.Arrays;
import java.util.List;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.gui.BarrelContainer;
import charcoalPit.tile.TileBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class BlockBarrel extends Block implements IWaterLoggable {
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public static final VoxelShape BARREL=VoxelShapes.create(2D/16D, 0D, 2D/16D, 14D/16D, 1D, 14D/16D);

	public BlockBarrel() {
		super(Properties.create(Material.WOOD).hardnessAndResistance(2, 3).harvestTool(ToolType.AXE));
		setDefaultState(this.getDefaultState().with(WATERLOGGED,false));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(WATERLOGGED,fluidstate.getFluid()==Fluids.WATER);
		
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		return stateIn;
	}
	
	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return BARREL;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileBarrel();
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
			((TileBarrel)worldIn.getTileEntity(pos)).dropInventory();
			worldIn.removeTileEntity(pos);
	    }
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if(worldIn.isRemote)
			return ActionResultType.SUCCESS;
		if(player.getHeldItem(handIn).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
			if(FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, null))
					return ActionResultType.SUCCESS;
		}else if(player.getHeldItem(handIn).getItem()==Items.GLASS_BOTTLE){
			TileBarrel tile=((TileBarrel)worldIn.getTileEntity(pos));
			if(tile.tank.getFluid().getFluid()==ModFluidRegistry.AlcoholStill&&tile.tank.getFluidAmount()>=250){
				player.getHeldItem(handIn).shrink(1);
				ItemStack stack=new ItemStack(ModItemRegistry.AlcoholBottle);
				stack.setTag(tile.tank.getFluid().getTag().copy());
				tile.tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
				ItemHandlerHelper.giveItemToPlayer(player,stack);
				return ActionResultType.SUCCESS;
			}
			if(tile.tank.getFluid().getFluid()==ModFluidRegistry.VinegarStill&&tile.tank.getFluidAmount()>=250){
				player.getHeldItem(handIn).shrink(1);
				ItemStack stack=new ItemStack(ModItemRegistry.VinegarBottle);
				tile.tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
				ItemHandlerHelper.giveItemToPlayer(player,stack);
				return ActionResultType.SUCCESS;
			}
		}
		NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider() {
			
			@Override
			public Container createMenu(int arg0, PlayerInventory arg1, PlayerEntity arg2) {
				return new BarrelContainer(arg0, pos, arg1);
			}
			
			@Override
			public ITextComponent getDisplayName() {
				return new TranslationTextComponent("screen.charcoal_pit.barrel");
			}
		}, pos);
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		TileBarrel tile=((TileBarrel)builder.get(LootParameters.BLOCK_ENTITY));
		ItemStack stack=new ItemStack(this);
		if(tile.tank.getFluidAmount()>0)
			stack.setTagInfo("Fluid", tile.tank.writeToNBT(new CompoundNBT()));
		return Arrays.asList(stack);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if(stack.hasTag()&&stack.getTag().contains("Fluid")) {
			((TileBarrel)worldIn.getTileEntity(pos)).tank.readFromNBT(stack.getTag().getCompound("Fluid"));
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		if(stack.hasTag()&&stack.getTag().contains("Fluid")){
			FluidStack fluid=FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid"));
			tooltip.add(fluid.getDisplayName().copyRaw().append(new StringTextComponent(":"+fluid.getAmount())));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		int a=((TileBarrel)worldIn.getTileEntity(pos)).tank.getFluidAmount();
		a+=999;
		a/=1000;
		if(a>8)
			a--;
		return a;
	}



}

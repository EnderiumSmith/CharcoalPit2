package charcoalPit.tile;

import java.util.NoSuchElementException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.recipe.BarrelRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileBarrel extends TileEntity implements ITickableTileEntity{

	public FluidTank tank;
	public ItemStackHandler input, output;
	public int process,total;
	public boolean valid;
	public String recipeId;
	
	public TileBarrel() {
		super(ModTileRegistry.Barrel);
		tank=new FluidTank(16000, f->f.getFluid().getAttributes().getTemperature()<450 && !f.getFluid().getAttributes().isGaseous()) {
			@Override
			protected void onContentsChanged() {
				markDirty();
				valid=false;
			}
		};
		input=new ItemStackHandler(1) {
			@Override
			protected void onContentsChanged(int slot) {
				markDirty();
				valid=false;
			}
		};
		output=new ItemStackHandler(1) {
			@Override
			protected void onContentsChanged(int slot) {
				markDirty();
				valid=false;
			}
		};
		process=-1;
		valid=false;
		recipeId="null";
		total=0;
	}
	
	@Override
	public void tick() {
		if(!world.isRemote) {
				if(!valid) {
					valid=true;
					BarrelRecipe recipe=BarrelRecipe.getRecipe(input.getStackInSlot(0), tank.getFluid(), world);
					if(validateRecipe(recipe)>0) {
						process=recipe.time;
						total=process;
						recipeId=recipe.id.toString();
						this.markDirty();
					}else {
						process=-1;
						total=0;
						recipeId="null";
						this.markDirty();
					}
				}
				if(process>0) {
					process--;
					if(process%200==0)
						markDirty();
				}
				else if(process==0) {
					process--;
					total=0;
					BarrelRecipe recipe;
					try {
						recipe=(BarrelRecipe)world.getRecipeManager().getRecipe(new ResourceLocation(recipeId)).get();
					}catch(NoSuchElementException e) {
						recipeId="null";
						e.printStackTrace();
						return;
					}
					int rounds=validateRecipe(recipe);
					boolean has_output_item=(recipe.flags&0b100)==0b100;
					boolean has_output_fluid=(recipe.flags&0b1000)==0b1000;
					tank.drain(rounds*recipe.fluid_in.amount, FluidAction.EXECUTE);
					if(has_output_fluid) {
						tank.setFluid(FluidStack.EMPTY);
						tank.fill(new FluidStack(recipe.fluid_out.getFluid(), rounds*recipe.fluid_out.amount, recipe.fluid_out.nbt), FluidAction.EXECUTE);
					}
					ItemStack container=input.getStackInSlot(0).getContainerItem().copy();
					container.setCount(rounds*recipe.in_amount);
					input.extractItem(0, rounds*recipe.in_amount, false);
					container=input.insertItem(0,container,false);
					if(!container.isEmpty()){
						InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), container);
					}
					if(has_output_item) {
						output.insertItem(0, new ItemStack(recipe.item_out.getMatchingStacks()[0].getItem(), rounds*recipe.out_amount, recipe.nbt_out), false);
					}
				}else
					if(!input.getStackInSlot(0).isEmpty()) {
						if(input.getStackInSlot(0).getItem()==Items.GLASS_BOTTLE){
							if(tank.getFluid().getFluid()==ModFluidRegistry.AlcoholStill&&tank.getFluidAmount()>=250){
								ItemStack stack=new ItemStack(ModItemRegistry.AlcoholBottle);
								stack.setTag(tank.getFluid().getTag().copy());
								if(output.insertItem(0,stack,true)==ItemStack.EMPTY){
									output.insertItem(0,stack,false);
									tank.drain(250,FluidAction.EXECUTE);
									input.extractItem(0,1,false);
								}
							}
							if(tank.getFluid().getFluid()==ModFluidRegistry.VinegarStill&&tank.getFluidAmount()>=250){
								ItemStack stack=new ItemStack(ModItemRegistry.VinegarBottle);
								if(output.insertItem(0,stack,true)==ItemStack.EMPTY){
									output.insertItem(0,stack,false);
									tank.drain(250,FluidAction.EXECUTE);
									input.extractItem(0,1,false);
								}
							}
						}
						transferFluid();
					}
			
		}else {
			
		}
		
	}
	
	public int validateRecipe(BarrelRecipe recipe) {
		if(recipe==null)
			return 0;
		boolean void_excess_input_fluid=(recipe.flags&0b1)==0b1;
		boolean void_excess_output_item=(recipe.flags&0b10)==0b10;
		boolean has_output_item=(recipe.flags&0b100)==0b100;
		boolean has_output_fluid=(recipe.flags&0b1000)==0b1000;
		if(!recipe.fluid_in.test(tank.getFluid().getFluid()))
			return 0;
		if(!recipe.item_in.test(input.getStackInSlot(0)))
			return 0;
		//inputs valid;
		FluidTank sim_tank=new FluidTank(16000);
		FluidTank sim_fluid=new FluidTank(16000);
		sim_fluid.readFromNBT(tank.writeToNBT(new CompoundNBT()));
		if(has_output_fluid&&
				tank.getFluid().isFluidEqual(new FluidStack(recipe.fluid_out.getFluid(), recipe.fluid_out.amount, recipe.fluid_out.nbt))) {
			sim_tank.readFromNBT(tank.writeToNBT(new CompoundNBT()));
		}
		ItemStackHandler sim_in=new ItemStackHandler(1);
		sim_in.deserializeNBT(input.serializeNBT());
		ItemStackHandler sim_out=new ItemStackHandler(1);
		sim_out.deserializeNBT(output.serializeNBT());
		int rounds=0;
		boolean once_inserted_item=false;
		boolean ok;
		do {
			ok=true;
			if(sim_fluid.getFluidAmount()<recipe.fluid_in.amount)
				ok=false;
			else
				sim_fluid.drain(recipe.fluid_in.amount, FluidAction.EXECUTE);
			if(sim_in.getStackInSlot(0).isEmpty()||sim_in.getStackInSlot(0).getCount()<recipe.in_amount)
				ok=false;
			else 
				sim_in.extractItem(0, recipe.in_amount, false);
			if(has_output_fluid) {
				if(sim_tank.fill(new FluidStack(recipe.fluid_out.getFluid(), recipe.fluid_out.amount, recipe.fluid_out.nbt), FluidAction.EXECUTE)<recipe.fluid_out.amount)
					ok=false;
			}
			if(has_output_item) {
				if(sim_out.insertItem(0, new ItemStack(recipe.item_out.getMatchingStacks()[0].getItem(), recipe.out_amount, recipe.nbt_out), false)!=ItemStack.EMPTY) {
					if(!once_inserted_item||!void_excess_output_item) {
						ok=false;
					}
				}else
					once_inserted_item=true;
			}
			if(ok)
				rounds++;
		}while(ok);
		if(rounds==0)
			return 0;
		if(!void_excess_input_fluid&&sim_fluid.getFluidAmount()>0)
			return 0;
		return rounds;
	}
	
	
	public void transferFluid() {
		FluidActionResult empty=tryEmptyContainer(input.getStackInSlot(0), tank, Integer.MAX_VALUE, null, false);
		if(empty.success) {
			if(empty.getResult()!=null&&!empty.getResult().isEmpty()) {
				if(output.insertItem(0, empty.getResult(), true).isEmpty()) {
					//all fits
					output.insertItem(0, tryEmptyContainer(input.getStackInSlot(0), tank, Integer.MAX_VALUE, null, true).getResult(), false);
					input.extractItem(0, 1, false);
				}
			}else {
				//no container
				tryEmptyContainer(input.getStackInSlot(0), tank, Integer.MAX_VALUE, null, true);
				input.extractItem(0, 1, false);
			}
		}else {
			FluidActionResult fill=tryFillContainer(input.getStackInSlot(0), tank, Integer.MAX_VALUE, null, false);
			if(fill.success) {
				if(output.insertItem(0, fill.getResult(), true).isEmpty()) {
					//all fits
					output.insertItem(0, tryFillContainer(input.getStackInSlot(0), tank, Integer.MAX_VALUE, null, true).getResult(), false);
					input.extractItem(0, 1, false);
				}
			}else {
				//fail
			}
		}
	}
	//forge ingores doDrain
	public static FluidActionResult tryEmptyContainer(@Nonnull ItemStack container, IFluidHandler fluidDestination, int maxAmount, @Nullable PlayerEntity player, boolean doDrain) {
		ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // do not modify the input
        return FluidUtil.getFluidHandler(containerCopy)
                .map(containerFluidHandler -> {

                    // We are acting on a COPY of the stack, so performing changes is acceptable even if we are simulating.
                    FluidStack transfer = FluidUtil.tryFluidTransfer(fluidDestination, containerFluidHandler, maxAmount, false);
                    if (transfer.isEmpty())
                        return FluidActionResult.FAILURE;

                    if(doDrain) {
                    	FluidUtil.tryFluidTransfer(fluidDestination, containerFluidHandler, maxAmount, true);
                    }else {
                    	containerFluidHandler.drain(transfer, FluidAction.EXECUTE);
                    }
                    
                    if (doDrain && player != null)
                    {
                        SoundEvent soundevent = transfer.getFluid().getAttributes().getEmptySound(transfer);
                        player.world.playSound(null, player.getPosX(), player.getPosY() + 0.5, player.getPosZ(), soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }

                    ItemStack resultContainer = containerFluidHandler.getContainer();
                    return new FluidActionResult(resultContainer);
                })
                .orElse(FluidActionResult.FAILURE);
	}
	
	public static FluidActionResult tryFillContainer(@Nonnull ItemStack container, IFluidHandler fluidSource, int maxAmount, @Nullable PlayerEntity player, boolean doFill) {
		ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // do not modify the input
        return FluidUtil.getFluidHandler(containerCopy)
                .map(containerFluidHandler -> {
                    FluidStack simulatedTransfer = FluidUtil.tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, false);
                    if (!simulatedTransfer.isEmpty())
                    {
                        if (doFill)
                        {
                            FluidUtil.tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, true);
                            if (player != null)
                            {
                                SoundEvent soundevent = simulatedTransfer.getFluid().getAttributes().getFillSound(simulatedTransfer);
                                player.world.playSound(null, player.getPosX(), player.getPosY() + 0.5, player.getPosZ(), soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                        else
                        {
                            //broken in forge
                        	containerFluidHandler.fill(simulatedTransfer, IFluidHandler.FluidAction.EXECUTE);
                        }

                        ItemStack resultContainer = containerFluidHandler.getContainer();
                        return new FluidActionResult(resultContainer);
                    }
                    return FluidActionResult.FAILURE;
                })
                .orElse(FluidActionResult.FAILURE);
	}
	
	public void dropInventory() {
		InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), input.getStackInSlot(0));
		InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), output.getStackInSlot(0));
	}
	
	@CapabilityInject(IFluidHandler.class)
	public static Capability<IFluidHandler> FLUID=null;
	public LazyOptional<IFluidHandler> fluid_out=LazyOptional.of(()->tank);
	
	@CapabilityInject(IItemHandler.class)
	public static Capability<IItemHandler> ITEM=null;
	public LazyOptional<IItemHandler> item_out=LazyOptional.of(()->new IItemHandler() {
		
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).isPresent()||
					BarrelRecipe.isValidItem(stack, getWorld())||stack.getItem()==Items.GLASS_BOTTLE;
		}
		
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if(slot==0&&isItemValid(slot, stack))
				return input.insertItem(0, stack, simulate);
			return stack;
		}
		
		@Override
		public ItemStack getStackInSlot(int slot) {
			if(slot==0)
				return input.getStackInSlot(0);
			return output.getStackInSlot(0);
		}
		
		@Override
		public int getSlots() {
			return 2;
		}
		
		@Override
		public int getSlotLimit(int slot) {
			if(slot==0)
				return input.getSlotLimit(0);
			return output.getSlotLimit(0);
		}
		
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(slot==1)
				return output.extractItem(0, amount, simulate);
			return ItemStack.EMPTY;
		}
	});
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap==FLUID)
			return fluid_out.cast();
		if(cap==ITEM)
			return item_out.cast();
		return super.getCapability(cap, side);
	}
	
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Fluid", tank.writeToNBT(new CompoundNBT()));
		compound.put("input", input.serializeNBT());
		compound.put("output", output.serializeNBT());
		compound.putInt("process", process);
		compound.putInt("total", total);
		compound.putBoolean("valid", valid);
		compound.putString("recipeId", recipeId);
		return compound;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		tank.readFromNBT(nbt.getCompound("Fluid"));
		input.deserializeNBT(nbt.getCompound("input"));
		output.deserializeNBT(nbt.getCompound("output"));
		process=nbt.getInt("process");
		total=nbt.getInt("total");
		valid=nbt.getBoolean("valid");
		recipeId=nbt.getString("recipeId");
	}

}

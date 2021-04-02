package charcoalPit.gui;

import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.recipe.BarrelRecipe;
import charcoalPit.tile.TileBarrel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BarrelContainer extends Container{

	public BlockPos pos;
	public ItemStackHandler fluid_tag;
	public FluidStack fluid;
	public IFluidHandler tank;
	public IIntArray array;
	public BarrelContainer(int id, BlockPos pos, PlayerInventory inv) {
		super(ModContainerRegistry.Barrel, id);
		this.pos=pos;
		TileBarrel tile=((TileBarrel)inv.player.world.getTileEntity(pos));
		
		tank=tile.tank;
		fluid_tag=new ItemStackHandler();
		fluid_tag.setStackInSlot(0, new ItemStack(Items.PAPER));
		fluid=tank.getFluidInTank(0).copy();
		fluid_tag.getStackInSlot(0).setTagInfo("fluid", fluid.writeToNBT(new CompoundNBT()));
		
		this.addSlot(new SlotItemHandler(tile.input, 0, 98, 17) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()||
						BarrelRecipe.isValidItem(stack, tile.getWorld())||stack.getItem()==Items.GLASS_BOTTLE;
			}
			
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				tile.markDirty();
				tile.valid=false;
			}
		});
		this.addSlot(new SlotItemHandler(tile.output, 0, 98, 53) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		});
		
		for(int k = 0; k < 3; ++k) {
	         for(int i1 = 0; i1 < 9; ++i1) {
	            this.addSlot(new Slot(inv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	         }
	      }

	      for(int l = 0; l < 9; ++l) {
	         this.addSlot(new Slot(inv, l, 8 + l * 18, 142));
	      }
	      
	      this.addSlot(new SlotItemHandler(fluid_tag, 0, 0, 0) {
	    	  @Override
	    	public boolean isItemValid(ItemStack stack) {
	    		return false;
	    	}
	    	  @Override
	    	public boolean canTakeStack(PlayerEntity playerIn) {
	    		return false;
	    	}
	    	  @Override
	    	  @OnlyIn(Dist.CLIENT)
	    	public boolean isEnabled() {
	    		return false;
	    	}
	      });
	      array=new IIntArray() {
			
			@Override
			public int size() {
				return 2;
			}
			
			@Override
			public void set(int arg0, int arg1) {
				if(arg0==0)
					tile.process=arg1;
				else
					tile.total=arg1;
				
			}
			
			@Override
			public int get(int arg0) {
				if(arg0==0)
					return tile.process;
				else
					return tile.total;
			}
		};
		this.trackIntArray(array);
	}
	
	@Override
	public void detectAndSendChanges() {
		if(!tank.getFluidInTank(0).isFluidStackIdentical(fluid)) {
			fluid=tank.getFluidInTank(0).copy();
			fluid_tag.getStackInSlot(0).setTagInfo("fluid", fluid.writeToNBT(new CompoundNBT()));
		}
		super.detectAndSendChanges();
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return playerIn.world.getBlockState(pos).getBlock() == ModBlockRegistry.Barrel&&
				playerIn.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
	      Slot slot = this.inventorySlots.get(index);
	      if (slot != null && slot.getHasStack()) {
	         ItemStack itemstack1 = slot.getStack();
	         itemstack = itemstack1.copy();
	         if (index < 2) {
	            if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
	            return ItemStack.EMPTY;
	         }

	         if (itemstack1.isEmpty()) {
	            slot.putStack(ItemStack.EMPTY);
	         } else {
	            slot.onSlotChanged();
	         }

	         if (itemstack1.getCount() == itemstack.getCount()) {
	            return ItemStack.EMPTY;
	         }

	         slot.onTake(playerIn, itemstack1);
	      }

	      return itemstack;
	}
}

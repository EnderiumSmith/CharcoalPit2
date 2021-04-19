package charcoalPit.gui;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.recipe.OreKilnRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ClayPotContainer2 extends Container{
	
	
	ClayPotHandler pot;
	PlayerInventory inv;
	int slot;
	public ClayPotContainer2(int id, PlayerInventory inv, int slot, World world) {
		super(ModContainerRegistry.ClayPot, id);
		this.inv=inv;
		this.slot=slot;
		pot=new ClayPotHandler(9, ()->{
			this.inv.getStackInSlot(this.slot).setTagInfo("inventory", pot.serializeNBT());
		}, inv.player.world);
		if(this.inv.getStackInSlot(this.slot).hasTag()&&
				this.inv.getStackInSlot(this.slot).getTag().contains("inventory"))
			pot.deserializeNBT(this.inv.getStackInSlot(this.slot).getTag().getCompound("inventory"));
		
		for(int i = 0; i < 3; ++i) {
	         for(int j = 0; j < 3; ++j) {
	        	 this.addSlot(new SlotItemHandler(pot, getIndex(j+i*3), 62 + j * 18, 17 + i * 18){
					 @Override
					 public void onSlotChanged() {
						 super.onSlotChanged();
						 inv.getStackInSlot(slot).setTagInfo("inventory", pot.serializeNBT());
					 }
				 });
	         }
	      }
		
		for(int k = 0; k < 3; ++k) {
	         for(int i1 = 0; i1 < 9; ++i1) {
	            this.addSlot(new Slot(inv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	         }
	      }

	      for(int l = 0; l < 9; ++l) {
	         this.addSlot(new SlotLocked(inv, l, 8 + l * 18, 142, slot));
	      }
		
	}
	
	private int getIndex(int i) {
		if(i<4)
			return i+1;
		if(i==4)
			return 0;
		return i;
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
	      Slot slot = this.inventorySlots.get(index);
	      if (slot != null && slot.getHasStack()) {
	         ItemStack itemstack1 = slot.getStack();
	         itemstack = itemstack1.copy();
	         if (index < 9) {
	            if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
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
	
	public static class ClayPotHandler extends ItemStackHandler{
		Runnable function;
		World world;
		
		public ClayPotHandler(int slots,Runnable r,World world) {
			super(slots);
			function=r;
			this.world=world;
		}
		
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if(slot==0) {
				return stack.getItem().isIn(ItemTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "orekiln_fuels")));
			}else {
				return OreKilnRecipe.isValidInput(stack, world);
			}
		}
		
		@Override
		public int getSlotLimit(int slot) {
			if(slot==0)
				return 4;
			return 1;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			function.run();
		}
	}
	
	public static class SlotLocked extends Slot{

		int lock;
		public SlotLocked(IInventory inventoryIn, int index, int xPosition, int yPosition, int lock) {
			super(inventoryIn, index, xPosition, yPosition);
			this.lock=lock;
		}
		
		@Override
		public boolean canTakeStack(PlayerEntity playerIn) {
			return lock!=this.getSlotIndex();
		}
		
	}

}

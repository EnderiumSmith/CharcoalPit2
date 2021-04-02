package charcoalPit.gui;

import charcoalPit.block.BlockCeramicPot;
import charcoalPit.block.BlockClayPot;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.tile.TileClayPot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.SlotItemHandler;

public class ClayPotContainer extends Container{
	
	public BlockPos pos;
	public ClayPotContainer(int id,BlockPos pos, PlayerInventory inv) {
		super(ModContainerRegistry.ClayPot,id);
		this.pos=pos;
		TileClayPot tile=((TileClayPot)inv.player.world.getTileEntity(pos));
		
		for(int i = 0; i < 3; ++i) {
	         for(int j = 0; j < 3; ++j) {
	        	 this.addSlot(new SlotItemHandler(tile.inventory, getIndex(j+i*3), 62 + j * 18, 17 + i * 18));
	         }
	      }
		
		for(int k = 0; k < 3; ++k) {
	         for(int i1 = 0; i1 < 9; ++i1) {
	            this.addSlot(new Slot(inv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	         }
	      }

	      for(int l = 0; l < 9; ++l) {
	         this.addSlot(new Slot(inv, l, 8 + l * 18, 142));
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
		return playerIn.world.getBlockState(pos).getBlock() instanceof BlockClayPot&&
				playerIn.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
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
	
}

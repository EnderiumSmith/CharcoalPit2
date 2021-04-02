package charcoalPit.tile;

import charcoalPit.core.ModTileRegistry;
import charcoalPit.recipe.OreKilnRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileClayPot extends TileEntity{

	public ClayPotHandler inventory;
	@CapabilityInject(IItemHandler.class)
	public static Capability<IItemHandler> ITEM_CAP=null;
	public LazyOptional<IItemHandler> out;
	
	public TileClayPot() {
		super(null);
		inventory=new ClayPotHandler(9, ()->{
			markDirty();
		},this);
		out=LazyOptional.of(()->inventory);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap.equals(ITEM_CAP))
			return out.cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("inventory", inventory.serializeNBT());
		return compound;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		inventory.deserializeNBT(nbt.getCompound("inventory"));
	}
	
	public static class ClayPotHandler extends ItemStackHandler{
		Runnable function;
		TileEntity tile;
		
		public ClayPotHandler(int slots,Runnable r,TileEntity tile) {
			super(slots);
			function=r;
			this.tile=tile;
		}
		
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if(slot==0) {
				return stack.getItem()==Items.CHARCOAL||stack.getItem()==Items.COAL;
			}else {
				return OreKilnRecipe.isValidInput(stack, tile.getWorld());
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

}

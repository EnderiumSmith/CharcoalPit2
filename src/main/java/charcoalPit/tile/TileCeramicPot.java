package charcoalPit.tile;

import charcoalPit.block.BlockCeramicPot;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCeramicPot extends TileEntity{
	
	public CeramicPotHandler inventory;
	@CapabilityInject(IItemHandler.class)
	public static Capability<IItemHandler> ITEM_CAP=null;
	public LazyOptional<IItemHandler> out;
	
	public TileCeramicPot() {
		super(ModTileRegistry.CeramicPot);
		inventory=new CeramicPotHandler(9,()->{
			markDirty();
		});
		out=LazyOptional.of(()->inventory);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap.equals(ITEM_CAP)) {
			return out.cast();
		}
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
	
	public static class CeramicPotHandler extends ItemStackHandler{
		Runnable function;
		public CeramicPotHandler(int i,Runnable r) {
			super(i);
			function=r;
		}
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return !(Block.getBlockFromItem(stack.getItem()) instanceof BlockCeramicPot||Block.getBlockFromItem(stack.getItem()) instanceof ShulkerBoxBlock);
		}
		@Override
		protected void onContentsChanged(int slot) {
			function.run();
		}
	}

}

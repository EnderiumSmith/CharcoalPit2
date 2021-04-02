package charcoalPit.tile;

import charcoalPit.block.BlockBloom;
import charcoalPit.core.Config;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileBloom extends TileEntity implements ITickableTileEntity{

	public int cooldown;
	
	public TileBloom() {
		super(null);
		cooldown=Config.BloomCooldown.get();
	}

	@Override
	public void tick() {
		if(!world.isRemote) {
			cooldown--;
			if(cooldown==0)
				world.setBlockState(pos, world.getBlockState(pos).with(BlockBloom.HOT, false));
			if(cooldown%200==0)
				markDirty();
		}
		
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		cooldown=nbt.getInt("cooldown");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("cooldown", cooldown);
		return compound;
	}
	
	

}

package charcoalPit.tile;

import charcoalPit.core.Config;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileBloomery extends TileEntity implements ITickableTileEntity{

	public boolean dummy;
	public boolean isValid;
	public int burnTime;
	public int airTicks;
	public int airBuffer;
	public int invalidTicks;
	
	public TileBloomery() {
		super(null);
		isValid=false;
		invalidTicks=0;
		burnTime=Config.BloomeryTime.get()*2;
		airTicks=Config.BloomeryTime.get();
		airBuffer=0;
	}

	/*@Override
	public void tick() {
		if(!world.isRemote) {
			checkValid();
			if(!dummy) {
				if(burnTime>0&&airTicks>0) {
					burnTime--;
					if(burnTime%200==0)
						markDirty();
					if(airBuffer>0) {
						airBuffer--;
						airTicks--;
					}
				}else {
					boolean tall=false;
					if(world.getBlockState(pos.offset(Direction.UP)).getBlock()==ModBlockRegistry.ActiveBloomery)
						tall=true;
					if(tall||world.getBlockState(pos.offset(Direction.UP)).getBlock()==Blocks.FIRE)
						world.removeBlock(pos.offset(Direction.UP), false);
					world.setBlockState(pos, ModBlockRegistry.Bloom.getDefaultState().with(BlockBloom.HOT, true)
							.with(BlockBloom.LAYER, tall?8:4)
							.with(BlockBloom.DOUBLE, tall)
							.with(BlockBloom.FAIL, airTicks>0));
				}
			}
		}
		
	}
	
	public void blow(int air) {
		airBuffer=Math.max(1000, airBuffer+air);
	}
	
	/*public void checkValid() {
		if(!isValid) {
			if(MethodHelper.BloomeryIsValidPosition(world, pos)) {
				isValid=true;
				invalidTicks=0;
			}else {
				if(invalidTicks<100) {
					invalidTicks++;
					//set fire
					BlockPos up=pos.offset(Direction.UP);
					BlockState block=this.world.getBlockState(up);
					if(block.getBlock().isAir(block, this.world, up)||
							AbstractFireBlock.canLightBlock(this.world, up,Direction.UP)){
						BlockState blockstate1 = AbstractFireBlock.getFireForPlacement(this.world, up);
			            this.world.setBlockState(up, blockstate1, 11);
					}
				}else {
					world.setBlockState(pos, ModBlockRegistry.BloomeryPile.getDefaultState().with(BlockBloomeryPile.LAYER, 8));
					if(dummy) {
						if(world.getBlockState(pos.offset(Direction.DOWN)).getBlock()==ModBlockRegistry.ActiveBloomery)
							world.setBlockState(pos.offset(Direction.DOWN), ModBlockRegistry.BloomeryPile.getDefaultState().with(BlockBloomeryPile.LAYER, 8));
						if(world.getBlockState(pos.offset(Direction.UP)).getBlock()==Blocks.FIRE)
							world.removeBlock(pos.offset(Direction.UP), false);
					}else {
						if(world.getBlockState(pos.offset(Direction.UP)).getBlock()==ModBlockRegistry.ActiveBloomery) {
							world.setBlockState(pos.offset(Direction.UP), ModBlockRegistry.BloomeryPile.getDefaultState().with(BlockBloomeryPile.LAYER, 8));
							if(world.getBlockState(pos.offset(Direction.UP,2)).getBlock()==Blocks.FIRE)
								world.removeBlock(pos.offset(Direction.UP,2), false);
						}else if(world.getBlockState(pos.offset(Direction.UP)).getBlock()==Blocks.FIRE)
							world.removeBlock(pos.offset(Direction.UP), false);
					}
				}
			}
		}
	}*/
	//pile ignitr
	public void setDummy(boolean dum) {
		dummy=dum;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putBoolean("dummy", dummy);
		compound.putBoolean("valid", isValid);
		compound.putInt("burn", burnTime);
		compound.putInt("air", airTicks);
		compound.putInt("buffer", airBuffer);
		compound.putInt("invalid", invalidTicks);
		return compound;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		dummy=nbt.getBoolean("dummy");
		isValid=nbt.getBoolean("valid");
		burnTime=nbt.getInt("burn");
		airTicks=nbt.getInt("air");
		airBuffer=nbt.getInt("buffer");
		invalidTicks=nbt.getInt("invalid");
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}

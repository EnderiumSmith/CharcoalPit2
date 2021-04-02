package charcoalPit.tile;

import charcoalPit.core.Config;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.fluid.ModFluidRegistry;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileActivePile extends TileEntity implements ITickableTileEntity{

	//18H=18000ticks
	public int invalidTicks;
	public int burnTime;
	public int itemsLeft;
	public boolean isValid;
	public boolean isCoke;
	public FluidTank creosote;
	
	public TileActivePile() {
		this(false);
	}
	
	public TileActivePile(boolean coal) {
		super(ModTileRegistry.ActivePile);
		invalidTicks=0;
		burnTime=coal?Config.CokeTime.get()/10:Config.CharcoalTime.get()/10;
		itemsLeft=9;
		isValid=false;
		isCoke=coal;
		creosote=new FluidTank(1000);
	}

	@Override
	public void tick() {
		if(!this.world.isRemote){
			checkValid();
			if(burnTime>0){
				burnTime--;
				if(burnTime%1000==0)
					markDirty();
			}else{
				if(itemsLeft>0){
					itemsLeft--;
					creosote.fill(new FluidStack(ModFluidRegistry.CreosoteStill, isCoke?Config.CokeCreosote.get():Config.CharcoalCreosote.get()), FluidAction.EXECUTE);
					burnTime=isCoke?Config.CokeTime.get()/10:Config.CharcoalTime.get()/10;
				}else{
					this.world.setBlockState(this.pos, isCoke?ModBlockRegistry.CoalAsh.getDefaultState():ModBlockRegistry.WoodAsh.getDefaultState());
				}
			}
			if(creosote.getFluidAmount()>0){
				if(this.world.getTileEntity(this.pos.offset(Direction.DOWN))instanceof TileActivePile){
					TileActivePile down=(TileActivePile)this.world.getTileEntity(this.pos.offset(Direction.DOWN));
					creosote.drain(down.creosote.fill(creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE);
				}
			}
		}
	}
	
	public void checkValid(){
		if(!isValid){
			boolean valid=true;
			Direction[] neighbors=Direction.values();
			//check structure
			for(Direction facing:neighbors){
				if(!MethodHelper.CharcoalPitIsValidBlock(world, this.pos, facing, isCoke)){
					valid=false;
					break;
				}
			}
			if(valid){
				isValid=true;
				invalidTicks=0;
			}else{
				if(invalidTicks<100){
					invalidTicks++;
					for(Direction facing:neighbors){
						//set fire
						BlockState block=this.world.getBlockState(this.pos.offset(facing));
						if(block.getBlock().isAir(block, this.world, this.pos.offset(facing))||
								AbstractFireBlock.canLightBlock(this.world, this.pos.offset(facing),facing)){
							BlockState blockstate1 = AbstractFireBlock.getFireForPlacement(this.world, this.pos.offset(facing));
				            this.world.setBlockState(this.pos.offset(facing), blockstate1, 11);
						}
					}
				}else{
					this.world.removeBlock(this.pos, false);
				}
			}
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("invalid", invalidTicks);
		compound.putInt("time", burnTime);
		compound.putInt("items", itemsLeft);
		compound.putBoolean("valid", isValid);
		compound.putBoolean("coke", isCoke);
		compound.put("creosote", creosote.writeToNBT(new CompoundNBT()));
		return compound;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		invalidTicks=compound.getInt("invalid");
		burnTime=compound.getInt("time");
		itemsLeft=compound.getInt("items");
		isValid=compound.getBoolean("valid");
		isCoke=compound.getBoolean("coke");
		creosote.readFromNBT(compound.getCompound("creosote"));
		markDirty();
	}
}

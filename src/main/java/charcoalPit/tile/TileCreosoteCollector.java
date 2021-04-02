package charcoalPit.tile;

import charcoalPit.core.ModTileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileCreosoteCollector extends TileEntity implements ITickableTileEntity{
	
	public FluidTank creosote;
	int tick;
	@CapabilityInject(IFluidHandler.class)
	public static Capability<IFluidHandler> FLUID=null;
	boolean flag;
	
	public TileCreosoteCollector() {
		super(ModTileRegistry.CreosoteCollector);
		tick=0;
		creosote=new FluidTank(8000);
	}
	
	@Override
	public void tick() {
		if(tick<20){
			tick++;
		}else{
			tick=0;
			flag=false;
			//collect creosote
			if(creosote.getFluidAmount()<creosote.getCapacity()&&this.world.getTileEntity(this.pos.offset(Direction.UP))instanceof TileActivePile){
				TileActivePile up=(TileActivePile)this.world.getTileEntity(this.pos.offset(Direction.UP));
				flag=flag||up.creosote.drain(creosote.fill(up.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
				for(Direction facing:Direction.Plane.HORIZONTAL){
					if(creosote.getFluidAmount()<creosote.getCapacity())
						flag=flag||collectCreosote(this.pos.offset(Direction.UP).offset(facing), facing, 3);
				}
			}
			//chanel creosote
			if(this.world.isBlockPowered(this.pos)){
				for(Direction facing:Direction.Plane.HORIZONTAL){
					if(creosote.getFluidAmount()<creosote.getCapacity())
						flag=flag||chanelCreosote(this.pos.offset(facing), facing, 3);
				}
			}
			//output creosote
			if(creosote.getFluidAmount()>0&&this.world.isBlockPowered(this.pos)){
				TileEntity tile=this.world.getTileEntity(this.pos.offset(Direction.DOWN));
				if(tile!=null){
					tile.getCapability(FLUID, Direction.UP).ifPresent((handler)->{
						flag=flag||creosote.drain(handler.fill(creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
					});
				}
			}
			if(flag)
				markDirty();
		}
		
	}
	public boolean collectCreosote(BlockPos pos, Direction facing, int runs){
		boolean flag=false;
		if(this.world.getTileEntity(pos)instanceof TileActivePile){
			TileActivePile up=(TileActivePile)this.world.getTileEntity(pos);
			flag=up.creosote.drain(creosote.fill(up.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
			if(runs>0&&creosote.getFluidAmount()<creosote.getCapacity())
				flag=flag||collectCreosote(pos.offset(facing), facing, --runs);
		}
		return flag;
	}
	public boolean chanelCreosote(BlockPos pos, Direction facing, int runs){
		boolean flag=false;
		if(this.world.getTileEntity(pos)instanceof TileCreosoteCollector){
			TileCreosoteCollector up=(TileCreosoteCollector)this.world.getTileEntity(pos);
			flag=up.creosote.drain(creosote.fill(up.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
			if(runs>0&&creosote.getFluidAmount()<creosote.getCapacity())
				flag=flag||chanelCreosote(pos.offset(facing), facing, --runs);
		}
		return flag;
	}
	public IFluidHandler external=new IFluidHandler() {
		
		@Override
		public boolean isFluidValid(int tank, FluidStack stack) {
			return creosote.isFluidValid(tank, stack);
		}
		
		@Override
		public int getTanks() {
			return creosote.getTanks();
		}
		
		@Override
		public int getTankCapacity(int tank) {
			return creosote.getTankCapacity(tank);
		}
		
		@Override
		public FluidStack getFluidInTank(int tank) {
			return creosote.getFluidInTank(tank);
		}
		
		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return 0;
		}
		
		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return creosote.drain(maxDrain, action);
		}
		
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return creosote.drain(resource, action);
		}
	};
	
	public LazyOptional<IFluidHandler> fluid=LazyOptional.of(()->external);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if((side==Direction.DOWN||side==null)&&cap.equals(FLUID)) {
			return fluid.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void remove() {
		fluid.invalidate();
		super.remove();
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("creosote", creosote.writeToNBT(new CompoundNBT()));
		return compound;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		creosote.readFromNBT(nbt.getCompound("creosote"));
	}

}

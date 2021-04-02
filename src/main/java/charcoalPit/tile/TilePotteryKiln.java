package charcoalPit.tile;

import charcoalPit.block.BlockPotteryKiln;
import charcoalPit.block.BlockPotteryKiln.EnumKilnTypes;
import charcoalPit.core.Config;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.recipe.PotteryKilnRecipe;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.items.ItemStackHandler;

public class TilePotteryKiln extends TileEntity implements ITickableTileEntity{
	
	public int invalidTicks;
	public int burnTime;
	public float xp;
	public boolean isValid;
	public PotteryStackHandler pottery;
	
	public TilePotteryKiln() {
		super(ModTileRegistry.PotteryKiln);
		invalidTicks=0;
		burnTime=-1;
		xp=0;
		isValid=false;
		pottery=new PotteryStackHandler();
	}
	
	@Override
	public void tick() {
		if(!this.world.isRemote&&burnTime>-1){
			checkValid();
			if(burnTime>0) {
				burnTime--;
				if(burnTime%500==0)
					markDirty();
			}else{
				if(burnTime==0){
					PotteryKilnRecipe result=PotteryKilnRecipe.getResult(pottery.getStackInSlot(0), this.world);
					if(result!=null) {
						ItemStack out=PotteryKilnRecipe.processClayPot(pottery.getStackInSlot(0), world);
						if(out.isEmpty())
							out=new ItemStack(result.output,pottery.getStackInSlot(0).getCount());
						xp=result.xp*pottery.getStackInSlot(0).getCount();
						pottery.setStackInSlot(0, out);
					}
					this.world.setBlockState(pos, ModBlockRegistry.Kiln.getDefaultState().with(BlockPotteryKiln.TYPE, EnumKilnTypes.COMPLETE));
					this.world.removeBlock(pos.offset(Direction.UP), false);
					burnTime--;
					markDirty();
				}
			}
		}
	}
	public void setActive(boolean active){
		if(active){
			burnTime=Config.PotteryTime.get();
		}else{
			burnTime=-1;
		}
	}
	public void dropInventory(){
		InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), pottery.getStackInSlot(0));
		int x=(int)xp+Math.random()<(xp-(int)xp)?1:0;
		while(x>0){
			int i=ExperienceOrbEntity.getXPSplit(x);
			x-=i;
			world.addEntity(new ExperienceOrbEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, i));
		}
	}
	public void checkValid(){
		boolean valid=true;
		if(Config.RainyPottery.get()&&this.world.isRainingAt(this.pos.offset(Direction.UP))){
			valid=false;
		}else{
			if(isValid)
				return;
		}
		//check structure
		for(Direction facing:Direction.Plane.HORIZONTAL){
			BlockState block=this.world.getBlockState(this.pos.offset(facing));
			if(!block.isSolidSide(this.world, pos.offset(facing), facing.getOpposite())){
				valid=false;
				break;
			}
		}
		BlockState block=this.world.getBlockState(this.pos.offset(Direction.UP));
		if(block.getBlock()!=Blocks.FIRE){
			if(block.getBlock().isAir(block, this.world, this.pos.offset(Direction.UP))||
					AbstractFireBlock.canLightBlock(this.world, this.pos.offset(Direction.UP),Direction.UP)){
				BlockState blockstate1 = AbstractFireBlock.getFireForPlacement(this.world, this.pos.offset(Direction.UP));
	            this.world.setBlockState(this.pos.offset(Direction.UP), blockstate1, 11);
			}else{
				valid=false;
			}
		}
		if(valid){
			isValid=true;
			invalidTicks=0;
		}else{
			if(invalidTicks<100){
				invalidTicks++;
			}else{
				setActive(false);
				this.world.setBlockState(pos, ModBlockRegistry.Kiln.getDefaultState().with(BlockPotteryKiln.TYPE, EnumKilnTypes.WOOD), 2);
				this.world.setBlockState(pos.offset(Direction.UP), Blocks.AIR.getDefaultState(), 2);
				invalidTicks=0;
			}
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("invalid", invalidTicks);
		compound.putInt("time", burnTime);
		compound.putFloat("xp", xp);
		compound.putBoolean("valid", isValid);
		compound.put("pottery", pottery.serializeNBT());
		return compound;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		invalidTicks=nbt.getInt("invalid");
		burnTime=nbt.getInt("time");
		xp=nbt.getFloat("xp");
		isValid=nbt.getBoolean("valid");
		pottery.deserializeNBT(nbt.getCompound("pottery"));
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt=super.getUpdateTag();
		nbt.put("pottery", pottery.serializeNBT());
		return nbt;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(getPos(), 1, pottery.serializeNBT());
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		super.handleUpdateTag(state, tag);
		pottery.deserializeNBT(tag.getCompound("pottery"));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		pottery.deserializeNBT(pkt.getNbtCompound());
	}
	
	public class PotteryStackHandler extends ItemStackHandler{
		@Override
		public int getSlotLimit(int slot) {
			return 8;
		}
	}

}

package charcoalPit.core;

import charcoalPit.tile.TileCeramicPot;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class DispenserPlacePot extends DefaultDispenseItemBehavior{
	
	@Override
	protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		Direction facing = (Direction)source.getBlockState().get(DispenserBlock.FACING);
        BlockPos pos=source.getBlockPos().offset(facing);
        if(source.getWorld().getBlockState(pos).getMaterial().isReplaceable()){
        	source.getWorld().setBlockState(pos, Block.getBlockFromItem(stack.getItem()).getDefaultState());
        	if(stack.hasTag()&&stack.getTag().contains("inventory")){
    			((TileCeramicPot)source.getWorld().getTileEntity(pos)).inventory.deserializeNBT(stack.getTag().getCompound("inventory"));
    		}
        	source.getWorld().playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1F, 1F);
        	stack.shrink(1);
        	return stack;
        }else{
	        return stack;
        }
	}
	
}

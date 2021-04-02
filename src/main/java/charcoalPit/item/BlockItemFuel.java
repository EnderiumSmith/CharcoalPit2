package charcoalPit.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class BlockItemFuel extends BlockItem{

	public int burnTime;
	public BlockItemFuel(Block blockIn, Properties builder) {
		super(blockIn, builder);
		// TODO Auto-generated constructor stub
	}
	
	public BlockItemFuel setBurnTime(int t) {
		burnTime=t;
		return this;
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack) {
		return burnTime;
	}

}

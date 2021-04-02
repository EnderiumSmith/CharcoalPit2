package charcoalPit.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFuel extends Item{

	public int burnTime;
	public ItemFuel(Properties properties) {
		super(properties);
	}
	
	public ItemFuel setBurnTime(int t) {
		burnTime=t;
		return this;
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack) {
		return burnTime;
	}

}

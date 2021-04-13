package charcoalPit.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class ItemBlockLeaves extends BlockItem {
	
	public ItemBlockLeaves(Block blockIn, Properties builder) {
		super(blockIn, builder);
	}
	
	@Override
	public String getTranslationKey(ItemStack stack) {
		if(stack.hasTag()&&stack.getTag().contains("stage")){
			if(stack.getTag().getInt("stage")==2){
				return super.getTranslationKey(stack).concat("_flower");
			}
			if(stack.getTag().getInt("stage")==6){
				return super.getTranslationKey(stack).concat("_unripe");
			}
			if(stack.getTag().getInt("stage")==7){
				return super.getTranslationKey(stack).concat("_ripe");
			}
		}
		return super.getTranslationKey(stack);
	}
}

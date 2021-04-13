package charcoalPit.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class ItemSapling extends Item {
	public final BlockState state;
	public ItemSapling(Properties properties, BlockState state) {
		super(properties);
		this.state=state;
	}
	
	public ActionResultType onItemUse(ItemUseContext context) {
		ActionResultType actionresulttype = this.tryPlace(context);
		return !actionresulttype.isSuccessOrConsume() && this.isFood() ? this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType() : actionresulttype;
	}
	
	public ActionResultType tryPlace(ItemUseContext context){
		if(state.isValidPosition(context.getWorld(),context.getPos().offset(context.getFace()))){
			if(context.getWorld().getBlockState(context.getPos().offset(context.getFace())).getMaterial().isReplaceable()){
				context.getWorld().setBlockState(context.getPos().offset(context.getFace()),state);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}
}

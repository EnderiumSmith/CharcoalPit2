package charcoalPit.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemKebabs extends Item{
	
	public ItemKebabs(Item.Properties prop){
		super(prop);
	}
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if(stack.getCount()>1) {
			if (entityLiving instanceof PlayerEntity)
				ItemHandlerHelper.giveItemToPlayer((PlayerEntity) entityLiving, this.getContainerItem(stack).copy());
			return super.onItemUseFinish(stack, worldIn, entityLiving);
		}
		entityLiving.onFoodEaten(worldIn,stack);
		return this.getContainerItem(stack).copy();
	}
}

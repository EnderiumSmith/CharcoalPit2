package charcoalPit.item;

import java.util.List;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class ItemCrackedPot extends Item{

	public ItemCrackedPot() {
		super(new Item.Properties().maxStackSize(1).group(ModItemRegistry.CHARCOAL_PIT));
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(stack.hasTag()&&stack.getTag().contains("inventory")) {
			ItemStackHandler inv=new ItemStackHandler();
			inv.deserializeNBT(stack.getTag().getCompound("inventory"));
			tooltip.add(new StringTextComponent("").append(inv.getStackInSlot(0).getDisplayName()).append(new StringTextComponent(" x"+inv.getStackInSlot(0).getCount())));
		}
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if(context.getWorld().isRemote) {
			context.getWorld().playSound(context.getPlayer(), context.getPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			return ActionResultType.CONSUME;
		}else {
			if(context.getItem().hasTag()&&context.getItem().getTag().contains("inventory")) {
				ItemStackHandler inv=new ItemStackHandler();
				inv.deserializeNBT(context.getItem().getTag().getCompound("inventory"));
				ItemHandlerHelper.giveItemToPlayer(context.getPlayer(),inv.getStackInSlot(0));
			}
			if(context.getItem().hasTag()&&context.getItem().getTag().contains("xp")) {
				int x=context.getItem().getTag().getInt("xp");
				while(x>0){
					int i=ExperienceOrbEntity.getXPSplit(x);
					x-=i;
					context.getWorld().addEntity(new ExperienceOrbEntity(context.getWorld(), (double)context.getPlayer().getPosX() + 0.5D, (double)context.getPlayer().getPosY() + 0.5D, (double)context.getPlayer().getPosZ() + 0.5D, i));
				}
			}
			context.getWorld().playSound(context.getPlayer(), context.getPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			context.getItem().shrink(1);
			return ActionResultType.CONSUME;
		}
	}

}

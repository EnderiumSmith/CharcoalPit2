package charcoalPit.item;

import java.util.List;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemAeternalis extends Item{

	public ItemAeternalis() {
		super(new Properties().group(ModItemRegistry.CHARCOAL_PIT).rarity(Rarity.EPIC));
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack) {
		return 200;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		return new ItemStack(this);
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent(TextFormatting.LIGHT_PURPLE+"An ancient relic that poseses the power to burn eternal"));
	}

}

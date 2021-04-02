package charcoalPit.item;

import java.util.List;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.gui.ClayPotContainer2;
import charcoalPit.recipe.OreKilnRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

public class ItemClayPot extends Item{
	
	public ItemClayPot() {
		super(new Item.Properties().maxStackSize(1).group(ModItemRegistry.CHARCOAL_PIT).maxStackSize(1));
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(stack.hasTag()&&stack.getTag().contains("inventory")) {
			ItemStackHandler inv=new ItemStackHandler();
			inv.deserializeNBT(stack.getTag().getCompound("inventory"));
			if(OreKilnRecipe.oreKilnIsEmpty(inv)) {
				tooltip.add(new StringTextComponent("Empty"));
			}else {
				ItemStack out=OreKilnRecipe.OreKilnGetOutput(stack.getTag().getCompound("inventory"), Minecraft.getInstance().world);
				if(out.isEmpty()) {
					tooltip.add(new StringTextComponent(TextFormatting.DARK_RED+"Invalid"+" ("+OreKilnRecipe.oreKilnGetOreAmount(inv)+"/8)"));
				}else {
					ITextComponent tx=out.getDisplayName().copyRaw().append(new StringTextComponent(" x"+out.getCount()));
					tx.getStyle().applyFormatting(TextFormatting.GREEN);
					tooltip.add(tx);
				}
			}
			int f=OreKilnRecipe.oreKilnGetFuelAvailable(inv);
			int n=OreKilnRecipe.oreKilnGetFuelRequired(inv);
			if(f==0) {
				if(n==0) {
					tooltip.add(new StringTextComponent("No Fuel"));
				}else {
					tooltip.add(new StringTextComponent(TextFormatting.DARK_RED+"No Fuel (0/"+n+")"));
				}
			}else {
				if(f<n) {
					tooltip.add(new StringTextComponent(TextFormatting.DARK_RED+"Fuel x"+f+" ("+f+"/"+n+")"));
				}else {
					if(f>n) {
						tooltip.add(new StringTextComponent(TextFormatting.YELLOW+"Fuel x"+f+" ("+f+"/"+n+")"));
					}else{
						tooltip.add(new StringTextComponent(TextFormatting.GREEN+"Fuel x"+f+" ("+f+"/"+n+")"));
					}
				}
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(!worldIn.isRemote)
			NetworkHooks.openGui((ServerPlayerEntity)playerIn, new INamedContainerProvider() {
				
				@Override
				public Container createMenu(int arg0, PlayerInventory arg1, PlayerEntity arg2) {
					return new ClayPotContainer2(arg0, arg1, arg2.inventory.currentItem, worldIn);
				}
				
				@Override
				public ITextComponent getDisplayName() {
					return new TranslationTextComponent("screen.charcoal_pit.clay_pot");
				}
			},buf->buf.writeByte((byte)playerIn.inventory.currentItem));
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
}

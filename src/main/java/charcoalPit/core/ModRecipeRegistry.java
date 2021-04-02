package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.recipe.BarrelRecipe;
import charcoalPit.recipe.BloomeryRecipe;
import charcoalPit.recipe.OreKilnRecipe;
import charcoalPit.recipe.PotteryKilnRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModRecipeRegistry {
	
	@SubscribeEvent
	public static void registerRecipeType(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		event.getRegistry().registerAll(PotteryKilnRecipe.SERIALIZER.setRegistryName("pottery"),OreKilnRecipe.SERIALIZER.setRegistryName("orekiln"),
				BloomeryRecipe.SERIALIZER.setRegistryName("bloomery"),BarrelRecipe.SERIALIZER.setRegistryName("barrel"));
	}
	
}

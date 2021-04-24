package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.loot.*;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModLootModifiers {
	
	@SubscribeEvent
	public static void registerLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
		event.getRegistry().registerAll(new StrawFromGrass.Serializer().setRegistryName("straw_from_grass"),
				new KernalsFromGrass.Serializer().setRegistryName("kernels_from_grass"),
				new CherryFromBirch.Serializer().setRegistryName("cherry_from_birch"),
				new DragonFromAcacia.Serializer().setRegistryName("dragon_from_acacia"),
				new ChestnutFromDarkOak.Serializer().setRegistryName("chestnut_from_dark_oak"),
				new FruitFromJungle.Serializer().setRegistryName("fruit_from_jungle"));
	}
	
}

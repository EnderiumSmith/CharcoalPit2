package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.loot.CherryFromBirch;
import charcoalPit.loot.KernalsFromGrass;
import charcoalPit.loot.StrawFromGrass;
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
				new CherryFromBirch.Serializer().setRegistryName("cherry_from_birch"));
	}
	
}

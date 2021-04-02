package charcoalPit.core;

import charcoalPit.CharcoalPit;
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
		event.getRegistry().registerAll(new StrawFromGrass.Serializer().setRegistryName("straw_from_grass"));
	}
	
}

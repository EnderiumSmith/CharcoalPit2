package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.gui.BarrelContainer;
import charcoalPit.gui.CeramicPotContainer;
import charcoalPit.gui.ClayPotContainer2;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;


@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModContainerRegistry {
	
	public static ContainerType<CeramicPotContainer> CeramicPot=IForgeContainerType.create((id,inv,data)->{
		return new CeramicPotContainer(id,data.readBlockPos(),inv);
	});
	/*public static ContainerType<ClayPotContainer> ClayPot=IForgeContainerType.create((id,inv,data)->{
		return new ClayPotContainer(id, data.readBlockPos(), inv);
	});*/
	public static ContainerType<ClayPotContainer2> ClayPot=IForgeContainerType.create((id,inv,data)->{
		return new ClayPotContainer2(id, inv, data.readByte(), null);
	});
	public static ContainerType<BarrelContainer> Barrel=IForgeContainerType.create((id,inv,data)->{
		return new BarrelContainer(id, data.readBlockPos(), inv);
	});
	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"),ClayPot.setRegistryName("clay_pot"),Barrel.setRegistryName("barrel"));
	}
	
	
}

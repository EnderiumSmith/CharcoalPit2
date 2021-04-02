package charcoalPit;

import charcoalPit.core.ModContainerRegistry;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.gui.BarrelScreen;
import charcoalPit.gui.CeramicPotScreen;
import charcoalPit.gui.ClayPotScreen;
import charcoalPit.tile.TESRPotteryKiln;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=CharcoalPit.MODID ,bus=Bus.MOD ,value=Dist.CLIENT)
public class ClientSetup {
	
	@SubscribeEvent
	public static void init(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ModContainerRegistry.CeramicPot, CeramicPotScreen::new);
		ScreenManager.registerFactory(ModContainerRegistry.ClayPot, ClayPotScreen::new);
		ScreenManager.registerFactory(ModContainerRegistry.Barrel, BarrelScreen::new);
		ClientRegistry.bindTileEntityRenderer(ModTileRegistry.PotteryKiln, TESRPotteryKiln::new);
	}
	@SubscribeEvent
	public static void registerColors(ColorHandlerEvent.Item event){
		event.getItemColors().register(new IItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return PotionUtils.getColor(p_getColor_1_);
				}
				return 0xFFFFFF;
			}
		}, ModItemRegistry.AlcoholBottle);
	}
	
}

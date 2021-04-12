package charcoalPit;

import charcoalPit.block.BlockFruitLeaves;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.gui.BarrelScreen;
import charcoalPit.gui.CeramicPotScreen;
import charcoalPit.gui.ClayPotScreen;
import charcoalPit.tile.TESRPotteryKiln;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;

@EventBusSubscriber(modid=CharcoalPit.MODID ,bus=Bus.MOD ,value=Dist.CLIENT)
public class ClientSetup {
	
	@SubscribeEvent
	public static void init(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ModContainerRegistry.CeramicPot, CeramicPotScreen::new);
		ScreenManager.registerFactory(ModContainerRegistry.ClayPot, ClayPotScreen::new);
		ScreenManager.registerFactory(ModContainerRegistry.Barrel, BarrelScreen::new);
		ClientRegistry.bindTileEntityRenderer(ModTileRegistry.PotteryKiln, TESRPotteryKiln::new);
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.Leeks, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.Corn,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.AppleSapling,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.AppleLeaves,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.CherrySapling,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.CherryLeaves,RenderType.getCutout());
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
	
	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event){
		event.getBlockColors().register(new IBlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return Minecraft.getInstance().world.getBiome(p_getColor_3_).getFoliageColor();
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.AppleLeaves);
		
		event.getBlockColors().register(new IBlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					if(p_getColor_1_.get(BlockFruitLeaves.AGE)>1&&p_getColor_1_.get(BlockFruitLeaves.AGE)<5)
						return 0xffb7c5;
					else
						return Minecraft.getInstance().world.getBiome(p_getColor_3_).getFoliageColor();
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.CherryLeaves);
	}
	
}

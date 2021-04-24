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
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
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
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.DragonLeaves,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.DragonSapling,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.ChestnutSapling,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.ChestnutLeaves,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.BanananaPod,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.CoconutPod,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.BananaSapling,RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlockRegistry.CoconutSapling,RenderType.getCutout());
		event.enqueueWork(()->{
			ItemModelsProperties.registerProperty(ModItemRegistry.AppleLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack, arg2, entity)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==2)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemModelsProperties.registerProperty(ModItemRegistry.CherryLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==2)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemModelsProperties.registerProperty(ModItemRegistry.DragonLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==2)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemModelsProperties.registerProperty(ModItemRegistry.ChestnutLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==2)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
		});
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
		
		event.getItemColors().register(new IItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.AppleLeaves);
		
		event.getItemColors().register(new IItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					if(p_getColor_1_.hasTag()&&p_getColor_1_.getTag().contains("stage")&&p_getColor_1_.getTag().getInt("stage")==2)
						return 0xFFFFFF;
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.CherryLeaves);
		
		event.getItemColors().register(new IItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.DragonLeaves);
		
		event.getItemColors().register(new IItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.ChestnutLeaves);
		
		event.getItemColors().register(new IItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.BananaLeaves);
		
		event.getItemColors().register(new IItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.CoconutLeaves);
	}
	
	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event){
		event.getBlockColors().register(new IBlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return BiomeColors.getFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.AppleLeaves);
		
		event.getBlockColors().register(new IBlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					if(p_getColor_1_.get(BlockFruitLeaves.AGE)>1&&p_getColor_1_.get(BlockFruitLeaves.AGE)<5)
						return 0xFFFFFF;
					else
						return BiomeColors.getFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.CherryLeaves);
		
		event.getBlockColors().register(new IBlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.DragonLeaves);
		
		event.getBlockColors().register(new IBlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return BiomeColors.getFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.BananaLeaves);
		
		event.getBlockColors().register(new IBlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return BiomeColors.getFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.ChestnutLeaves);
		
		event.getBlockColors().register(new IBlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return BiomeColors.getFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.CoconutLeaves);
	}
	
}

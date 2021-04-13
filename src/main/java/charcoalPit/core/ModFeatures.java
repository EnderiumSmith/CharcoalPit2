package charcoalPit.core;

import charcoalPit.CharcoalPit;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber(modid=CharcoalPit.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {
	
	
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> APPLE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> CHERRY;
	
	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event){
		APPLE = WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"apple"), Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(ModBlockRegistry.AppleLeaves.getDefaultState()), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
		CHERRY = WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"cherry"), Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.BIRCH_LOG.getDefaultState()), new SimpleBlockStateProvider(ModBlockRegistry.CherryLeaves.getDefaultState()), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
		ModBlockRegistry.AppleLeaves.fruit= Items.APPLE;
		ModBlockRegistry.CherryLeaves.fruit=ModItemRegistry.Cherry;
	}
	
	public static class AppleTree extends Tree{
		
		@Nullable
		@Override
		protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
			return APPLE;
		}
	}
	public static class CherryTree extends Tree{
		
		@Nullable
		@Override
		protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
			return CHERRY;
		}
	}
	
}

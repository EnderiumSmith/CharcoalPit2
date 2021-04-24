package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.tree.BentTrunkPlacer;
import charcoalPit.tree.DragonFoliagePlacer;
import charcoalPit.tree.PalmFoliagePlacer;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.BigTree;
import net.minecraft.block.trees.Tree;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.*;
import net.minecraft.world.gen.trunkplacer.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;
import java.util.OptionalInt;
import java.util.Random;

@Mod.EventBusSubscriber(modid=CharcoalPit.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {
	
	
	public static FoliagePlacerType<DragonFoliagePlacer> DRAGON_PLACER=new FoliagePlacerType<>(DragonFoliagePlacer.CODEC);
	public static FoliagePlacerType<PalmFoliagePlacer> PALM_PLACER=new FoliagePlacerType<>(PalmFoliagePlacer.CODEC);
	
	public static TrunkPlacerType<BentTrunkPlacer> BENT_PLACER=new TrunkPlacerType<BentTrunkPlacer>(BentTrunkPlacer.CODEC);
	
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> APPLE;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> CHERRY;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> DRAGON;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> BANANA;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> CHESTNUT;
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> COCONUT;
	
	@SubscribeEvent
	public static void registerPlacers(RegistryEvent.Register<FoliagePlacerType<?>> event){
		event.getRegistry().registerAll(DRAGON_PLACER.setRegistryName("dragon_placer"),
				PALM_PLACER.setRegistryName("palm_placer"));
	}
	
	
	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event){
		
		Registry.register(Registry.TRUNK_REPLACER, new ResourceLocation(CharcoalPit.MODID), BENT_PLACER);
		
		APPLE = WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"apple"), Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(ModBlockRegistry.AppleLeaves.getDefaultState()), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
		CHERRY = WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"cherry"), Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.BIRCH_LOG.getDefaultState()), new SimpleBlockStateProvider(ModBlockRegistry.CherryLeaves.getDefaultState()), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
		DRAGON = WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"dragon"), Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.ACACIA_LOG.getDefaultState()), new SimpleBlockStateProvider(ModBlockRegistry.DragonLeaves.getDefaultState()), new DragonFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0)), new StraightTrunkPlacer(3, 0, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));
		BANANA=WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID, "banana"),Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(ModBlockRegistry.BananaLeaves.getDefaultState()), new PalmFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0)), new BentTrunkPlacer(5, 2, 2), new TwoLayerFeature(1, 0, 2))).setIgnoreVines().build()));
		CHESTNUT=WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID, "chestnut"),Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.DARK_OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(ModBlockRegistry.ChestnutLeaves.getDefaultState()), new DarkOakFoliagePlacer(FeatureSpread.func_242252_a(0), FeatureSpread.func_242252_a(0)), new DarkOakTrunkPlacer(6, 2, 1), new ThreeLayerFeature(1, 1, 0, 1, 2, OptionalInt.empty()))).setMaxWaterDepth(Integer.MAX_VALUE).func_236702_a_(Heightmap.Type.MOTION_BLOCKING).setIgnoreVines().build()));
		COCONUT=WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID, "coconut"),Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(ModBlockRegistry.CoconutLeaves.getDefaultState()), new PalmFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0)), new BentTrunkPlacer(5, 2, 2), new TwoLayerFeature(1, 0, 2))).setIgnoreVines().build()));
		
		ModBlockRegistry.AppleLeaves.fruit= Items.APPLE;
		ModBlockRegistry.CherryLeaves.fruit=ModItemRegistry.Cherry;
		ModBlockRegistry.DragonLeaves.fruit=ModItemRegistry.DragonFruit;
		ModBlockRegistry.ChestnutLeaves.fruit=ModItemRegistry.ChestNut;
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
	public static class DragonTree extends Tree{
		
		@Nullable
		@Override
		protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
			return DRAGON;
		}
	}
	public static class BananaTree extends Tree{
		
		@Nullable
		@Override
		protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
			return BANANA;
		}
	}
	public static class ChestnutTree extends BigTree {
		
		@Nullable
		@Override
		protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
			return null;
		}
		
		@Nullable
		@Override
		protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getHugeTreeFeature(Random rand) {
			return CHESTNUT;
		}
	}
	public static class CoconutTree extends Tree{
		
		@Nullable
		@Override
		protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
			return COCONUT;
		}
	}
	
}

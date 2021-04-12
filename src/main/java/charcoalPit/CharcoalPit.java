package charcoalPit;

import charcoalPit.core.Config;
import charcoalPit.core.ModBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

import javax.annotation.Nullable;
import java.util.Random;

@Mod(CharcoalPit.MODID)
public class CharcoalPit {
	
	public static final String MODID="charcoal_pit";

	{
		ForgeMod.enableMilkFluid();
	}
	
	public CharcoalPit() {
		ModLoadingContext.get().registerConfig(Type.COMMON, Config.CONFIG);
	}
}

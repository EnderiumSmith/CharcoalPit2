package charcoalPit;

import charcoalPit.core.Config;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

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

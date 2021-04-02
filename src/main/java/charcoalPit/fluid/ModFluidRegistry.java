package charcoalPit.fluid;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModItemRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.function.BiFunction;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModFluidRegistry {
	
	public static final ResourceLocation creosote_still=new ResourceLocation(CharcoalPit.MODID, "block/creosote_still"),
			creosote_flowing=new ResourceLocation(CharcoalPit.MODID, "block/creosote_flow");
	public static ForgeFlowingFluid CreosoteStill, CreosoteFlowing;
	public static ForgeFlowingFluid.Properties Creosote;
	
	public static final ResourceLocation alcohol_still=new ResourceLocation("minecraft:block/water_still"),
			alcohol_flowing=new ResourceLocation("minecraft:block/water_flow");
	public static ForgeFlowingFluid AlcoholStill, AlcoholFlowing;
	public static ForgeFlowingFluid.Properties Alcohol;

	public static final ResourceLocation vinegar_still=new ResourceLocation("minecraft:block/water_still"),
			vinegar_flowing=new ResourceLocation("minecraft:block/water_flow");
	public static ForgeFlowingFluid VinegarStill, VinegarFlowing;
	public static ForgeFlowingFluid.Properties Vinegar;
	
	@SubscribeEvent
	public static void registerFluids(RegistryEvent.Register<Fluid> event) {
		Creosote=new Properties(()->CreosoteStill, ()->CreosoteFlowing, FluidAttributes.builder(creosote_still, creosote_flowing).density(800).viscosity(2000))
				.bucket(()->ModItemRegistry.CreosoteBucket).block(()->ModBlockRegistry.Creosote);
		CreosoteStill=new ForgeFlowingFluid.Source(Creosote);
		CreosoteStill.setRegistryName("creosote_still");
		CreosoteFlowing=new ForgeFlowingFluid.Flowing(Creosote);
		CreosoteFlowing.setRegistryName("creosote_flowing");
		
		Alcohol=new Properties(()->AlcoholStill, ()->AlcoholFlowing, AlcoholProperties.builder(alcohol_still, alcohol_flowing).density(950).color(0xFFFFFF));
		AlcoholStill=new ForgeFlowingFluid.Source(Alcohol);
		AlcoholFlowing=new ForgeFlowingFluid.Flowing(Alcohol);
		AlcoholStill.setRegistryName("alcohol_still");
		AlcoholFlowing.setRegistryName("alcohol_flowing");

		Vinegar=new Properties(()->VinegarStill,()->VinegarFlowing, FluidAttributes.builder(vinegar_still,vinegar_flowing).color(0xFFCEB301)).bucket(()->ModItemRegistry.VinegarBucket);
		VinegarStill=new ForgeFlowingFluid.Source(Vinegar);
		VinegarFlowing=new ForgeFlowingFluid.Flowing(Vinegar);
		VinegarStill.setRegistryName("vinegar_still");
		VinegarFlowing.setRegistryName("vinegar_flowing");

		event.getRegistry().registerAll(CreosoteStill,CreosoteFlowing,AlcoholStill,AlcoholFlowing,VinegarStill,VinegarFlowing);
	}

	public static class AlcoholProperties extends FluidAttributes{
		AlcoholProperties(Builder builder, Fluid fluid){super(builder, fluid);}

		@Override
		public int getColor(FluidStack stack) {
			if(stack.hasTag()&&stack.getTag().contains("CustomPotionColor")){
				return stack.getTag().getInt("CustomPotionColor")+0xFF000000;
			}
			return 0xFFFFFFFF;
		}
		public static Builder builder(ResourceLocation stillTexture, ResourceLocation flowingTexture) {
			return new Builder2(stillTexture, flowingTexture, AlcoholProperties::new);
		}
		public  static class Builder2 extends Builder{
			public Builder2(ResourceLocation stillTexture, ResourceLocation flowingTexture, BiFunction<Builder,Fluid,FluidAttributes> factory) {
				super(stillTexture,flowingTexture,factory);
			}
		}
	}
	
}

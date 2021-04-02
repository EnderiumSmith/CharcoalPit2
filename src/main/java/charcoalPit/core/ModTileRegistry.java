package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.tile.TileActivePile;
import charcoalPit.tile.TileBarrel;
import charcoalPit.tile.TileBloomery2;
import charcoalPit.tile.TileCeramicPot;
import charcoalPit.tile.TileClayPot;
import charcoalPit.tile.TileCreosoteCollector;
import charcoalPit.tile.TilePotteryKiln;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModTileRegistry {
	
	
	public static TileEntityType<TileActivePile> ActivePile=TileEntityType.Builder.create(TileActivePile::new, ModBlockRegistry.ActiveLogPile,ModBlockRegistry.ActiveCoalPile).build(null);
	public static TileEntityType<TileCreosoteCollector> CreosoteCollector=TileEntityType.Builder.create(TileCreosoteCollector::new, ModBlockRegistry.BrickCollector,ModBlockRegistry.SandyCollector,
			ModBlockRegistry.NetherCollector,ModBlockRegistry.EndCollector).build(null);
	public static TileEntityType<TilePotteryKiln> PotteryKiln=TileEntityType.Builder.create(TilePotteryKiln::new, ModBlockRegistry.Kiln).build(null);
	public static TileEntityType<TileCeramicPot> CeramicPot=TileEntityType.Builder.create(TileCeramicPot::new, ModBlockRegistry.CeramicPot,ModBlockRegistry.BlackPot,
			ModBlockRegistry.BluePot,ModBlockRegistry.BrownPot,ModBlockRegistry.CyanPot,ModBlockRegistry.GrayPot,ModBlockRegistry.GreenPot,
			ModBlockRegistry.LightBluePot,ModBlockRegistry.LightGrayPot,ModBlockRegistry.LimePot,ModBlockRegistry.MagentaPot,ModBlockRegistry.OrangePot,
			ModBlockRegistry.PinkPot,ModBlockRegistry.PurplePot,ModBlockRegistry.RedPot,ModBlockRegistry.WhitePot,ModBlockRegistry.YellowPot).build(null);
	//public static TileEntityType<TileClayPot> ClayPot=TileEntityType.Builder.create(TileClayPot::new, ModBlockRegistry.ClayPot).build(null);
	public static TileEntityType<TileBloomery2> Bloomery2=TileEntityType.Builder.create(TileBloomery2::new, ModBlockRegistry.Bloomery).build(null);
	public static TileEntityType<TileBarrel> Barrel=TileEntityType.Builder.create(TileBarrel::new, ModBlockRegistry.Barrel).build(null);
	
	
	@SubscribeEvent
	public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
		event.getRegistry().registerAll(ActivePile.setRegistryName("active_pile"),CreosoteCollector.setRegistryName("creosote_collector"),
				PotteryKiln.setRegistryName("pottery_kiln"),CeramicPot.setRegistryName("ceramic_pot"),Bloomery2.setRegistryName("bloomery2"),
				Barrel.setRegistryName("barrel"));
	}
	
}

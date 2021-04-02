package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.block.*;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModBlockRegistry {
	
	public static BlockThatch Thatch=new BlockThatch();
	public static RotatedPillarBlock LogPile=new BlockLogPile();
	public static Block WoodAsh=new BlockAsh(),CoalAsh=new BlockAsh(),SandyBrick=new Block(Properties.create(Material.ROCK, MaterialColor.ADOBE).hardnessAndResistance(2, 6).setRequiresTool().harvestLevel(0).harvestTool(ToolType.PICKAXE)),
			CokeBlock=new Block(Properties.create(Material.WOOD, MaterialColor.BLACK).hardnessAndResistance(5F, 6F).harvestLevel(0).harvestTool(ToolType.PICKAXE).setRequiresTool()) {
		public int getFireSpreadSpeed(net.minecraft.block.BlockState state, net.minecraft.world.IBlockReader world, net.minecraft.util.math.BlockPos pos, net.minecraft.util.Direction face) {
			return 5;
		};
		public int getFlammability(net.minecraft.block.BlockState state, net.minecraft.world.IBlockReader world, net.minecraft.util.math.BlockPos pos, net.minecraft.util.Direction face) {
			return 5;
		};
	};
	public static FallingBlock Ash=new FallingBlock(Properties.create(Material.SAND, MaterialColor.LIGHT_GRAY).hardnessAndResistance(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.SAND));
	public static SlabBlock SandySlab=new SlabBlock(Properties.create(Material.ROCK, MaterialColor.ADOBE).hardnessAndResistance(2, 6).setRequiresTool().harvestLevel(0).harvestTool(ToolType.PICKAXE));
	public static StairsBlock SandyStair=new StairsBlock(()->SandyBrick.getDefaultState(), Properties.create(Material.ROCK, MaterialColor.ADOBE).hardnessAndResistance(2, 6).setRequiresTool().harvestLevel(0).harvestTool(ToolType.PICKAXE));
	public static WallBlock SandyWall=new WallBlock(Properties.create(Material.ROCK, MaterialColor.ADOBE).hardnessAndResistance(2, 6).setRequiresTool().harvestLevel(0).harvestTool(ToolType.PICKAXE));
	
	public static BlockActivePile ActiveLogPile=new BlockActivePile(false, Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2F).harvestTool(ToolType.AXE).sound(SoundType.WOOD));
	public static BlockActivePile ActiveCoalPile=new BlockActivePile(true, Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(5F, 6F).harvestLevel(0).harvestTool(ToolType.PICKAXE).setRequiresTool());
	public static BlockCreosoteCollector BrickCollector=new BlockCreosoteCollector(Properties.from(Blocks.BRICKS)), SandyCollector=new BlockCreosoteCollector(Properties.from(SandyBrick)),
			NetherCollector=new BlockCreosoteCollector(Properties.from(Blocks.NETHER_BRICKS)),EndCollector=new BlockCreosoteCollector(Properties.from(Blocks.END_STONE_BRICKS));
	public static BlockPotteryKiln Kiln=new BlockPotteryKiln();
	public static BlockCeramicPot CeramicPot=new BlockCeramicPot(MaterialColor.ADOBE),WhitePot=new BlockCeramicPot(MaterialColor.WHITE_TERRACOTTA),
			OrangePot=new BlockCeramicPot(MaterialColor.ORANGE_TERRACOTTA),MagentaPot=new BlockCeramicPot(MaterialColor.MAGENTA_TERRACOTTA),
			LightBluePot=new BlockCeramicPot(MaterialColor.LIGHT_BLUE_TERRACOTTA),YellowPot=new BlockCeramicPot(MaterialColor.YELLOW_TERRACOTTA),
			LimePot=new BlockCeramicPot(MaterialColor.LIME_TERRACOTTA),PinkPot=new BlockCeramicPot(MaterialColor.PINK_TERRACOTTA),
			GrayPot=new BlockCeramicPot(MaterialColor.GRAY_TERRACOTTA),LightGrayPot=new BlockCeramicPot(MaterialColor.LIGHT_GRAY_TERRACOTTA),
			CyanPot=new BlockCeramicPot(MaterialColor.CYAN_TERRACOTTA),PurplePot=new BlockCeramicPot(MaterialColor.PURPLE_TERRACOTTA),
			BluePot=new BlockCeramicPot(MaterialColor.BLUE_TERRACOTTA),BrownPot=new BlockCeramicPot(MaterialColor.BROWN_TERRACOTTA),
			GreenPot=new BlockCeramicPot(MaterialColor.GREEN_TERRACOTTA),RedPot=new BlockCeramicPot(MaterialColor.RED_TERRACOTTA),
			BlackPot=new BlockCeramicPot(MaterialColor.BLACK_TERRACOTTA);
	public static BlockBellows Bellows=new BlockBellows();
	public static Block TuyereBrick=new Block(Properties.from(Blocks.BRICKS)), TuyereSandy=new Block(Properties.from(SandyBrick)), TuyereNether=new Block(Properties.from(Blocks.NETHER_BRICKS)),
			TuyereEnd=new Block(Properties.from(Blocks.END_STONE_BRICKS));
	//public static BlockClayPot ClayPot=new BlockClayPot();
	public static BlockBloomery Bloomery=new BlockBloomery();
	
	public static Block CopperOre=new Block(Properties.create(Material.ROCK).hardnessAndResistance(3).setRequiresTool().harvestTool(ToolType.PICKAXE).harvestLevel(1)),
			CopperBlock=new Block(Properties.create(Material.IRON, MaterialColor.ORANGE_TERRACOTTA).setRequiresTool().hardnessAndResistance(4, 6).harvestTool(ToolType.PICKAXE).harvestLevel(1));
	
	public static BlockBarrel Barrel=new BlockBarrel();
	public static BlockMechanicalBellows MechanicalBellows=new BlockMechanicalBellows();
	
	/*public static DoorBlock BrickDoor=new DoorBlock(AbstractBlock.Properties.from(Blocks.IRON_DOOR)),
			SandyDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR)),
			NetherDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR)),
			EndDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR));*/
	
	public static BlockCreosote Creosote=new BlockCreosote();
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(Thatch.setRegistryName("thatch"),LogPile.setRegistryName("log_pile"),
				WoodAsh.setRegistryName("wood_ash"),CoalAsh.setRegistryName("coal_ash"),
				CokeBlock.setRegistryName("coke"),Ash.setRegistryName("ash"),
				ActiveLogPile.setRegistryName("active_log_pile"),ActiveCoalPile.setRegistryName("active_coal_pile"),
				SandyBrick.setRegistryName("sandy_brick"),SandySlab.setRegistryName("sandy_slab"),SandyStair.setRegistryName("sandy_stair"),SandyWall.setRegistryName("sandy_wall"),
				Creosote.setRegistryName("creosote_oil"),BrickCollector.setRegistryName("brick_collector"),SandyCollector.setRegistryName("sandy_collector"),
				NetherCollector.setRegistryName("nether_collector"),EndCollector.setRegistryName("end_collector"),Kiln.setRegistryName("pottery_kiln"),Bellows.setRegistryName("bellows"),
				TuyereBrick.setRegistryName("brick_tuyere"),TuyereSandy.setRegistryName("sandy_tuyere"),TuyereNether.setRegistryName("nether_tuyere"),TuyereEnd.setRegistryName("end_tuyere"),
				CopperOre.setRegistryName("copper_ore"),CopperBlock.setRegistryName("copper_block"),Bloomery.setRegistryName("bloomery"),
				Barrel.setRegistryName("barrel")/*,BrickDoor.setRegistryName("brick_door"),SandyDoor.setRegistryName("sandy_door"),NetherDoor.setRegistryName("nether_door"),
				EndDoor.setRegistryName("end_door")*/,MechanicalBellows.setRegistryName("mechanical_bellows"));
		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"),YellowPot.setRegistryName("yellow_pot"),WhitePot.setRegistryName("white_pot"),
				RedPot.setRegistryName("red_pot"),PurplePot.setRegistryName("purple_pot"),PinkPot.setRegistryName("pink_pot"),OrangePot.setRegistryName("orange_pot"),
				MagentaPot.setRegistryName("magenta_pot"),LimePot.setRegistryName("lime_pot"),LightGrayPot.setRegistryName("light_gray_pot"),
				LightBluePot.setRegistryName("light_blue_pot"),GreenPot.setRegistryName("green_pot"),GrayPot.setRegistryName("gray_pot"),CyanPot.setRegistryName("cyan_pot"),
				BrownPot.setRegistryName("brown_pot"),BluePot.setRegistryName("blue_pot"),BlackPot.setRegistryName("black_pot"));
	}
	
}

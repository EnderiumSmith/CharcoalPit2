package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.item.*;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModItemRegistry {
	
	public static ItemGroup CHARCOAL_PIT=new ItemGroup("charcoal_pit") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Items.CHARCOAL);
		}
	};

	public static BlockItemFuel Thatch=buildBlockItem(ModBlockRegistry.Thatch,200),LogPile=buildBlockItem(ModBlockRegistry.LogPile,3000),CokeBlock=buildBlockItem(ModBlockRegistry.CokeBlock,32000);
	public static BlockItem WoodAsh=buildBlockItem(ModBlockRegistry.WoodAsh),CoalAsh=buildBlockItem(ModBlockRegistry.CoalAsh),AshBlock=buildBlockItem(ModBlockRegistry.Ash),
			SandyBrick=buildBlockItem(ModBlockRegistry.SandyBrick),SandySlab=buildBlockItem(ModBlockRegistry.SandySlab),SandyStair=buildBlockItem(ModBlockRegistry.SandyStair),SandyWall=buildBlockItem(ModBlockRegistry.SandyWall);
	
	public static ItemFuel Straw=buildItem(CHARCOAL_PIT,50),Coke=buildItem(CHARCOAL_PIT,3200);
	public static Item Ash=buildItem(CHARCOAL_PIT),Aeternalis=new ItemAeternalis();
	public static BoneMealItem Fertilizer=new BoneMealItem(new Item.Properties().group(CHARCOAL_PIT));
	public static ItemFireStarter FireStarter=new ItemFireStarter();
	public static Item BloomCool=buildItem(CHARCOAL_PIT),BloomFail=buildItem(CHARCOAL_PIT),BloomNiCool=buildItem(CHARCOAL_PIT),BloomNiFail=buildItem(CHARCOAL_PIT);
	public static Item SandyBrickItem=buildItem(CHARCOAL_PIT),NetherBrickItem=buildItem(CHARCOAL_PIT),UnfireSandyBrick=buildItem(CHARCOAL_PIT),UnfiredBrick=buildItem(CHARCOAL_PIT);
	
	public static BlockItem BrickCollector=buildBlockItem(ModBlockRegistry.BrickCollector,CHARCOAL_PIT),
			SandyCollector=buildBlockItem(ModBlockRegistry.SandyCollector,CHARCOAL_PIT),
			NetherCollector=buildBlockItem(ModBlockRegistry.NetherCollector,CHARCOAL_PIT),
			EndCollector=buildBlockItem(ModBlockRegistry.EndCollector,CHARCOAL_PIT);
	public static BlockItem CeramicPot=buildBlockItemP(ModBlockRegistry.CeramicPot),WhitePot=buildBlockItemP(ModBlockRegistry.WhitePot),
			OrangePot=buildBlockItemP(ModBlockRegistry.OrangePot),MagentaPot=buildBlockItemP(ModBlockRegistry.MagentaPot),
			LightBluePot=buildBlockItemP(ModBlockRegistry.LightBluePot),YellowPot=buildBlockItemP(ModBlockRegistry.YellowPot),
			LimePot=buildBlockItemP(ModBlockRegistry.LimePot),PinkPot=buildBlockItemP(ModBlockRegistry.PinkPot),
			GrayPot=buildBlockItemP(ModBlockRegistry.GrayPot),LightGrayPot=buildBlockItemP(ModBlockRegistry.LightGrayPot),
			CyanPot=buildBlockItemP(ModBlockRegistry.CyanPot),PurplePot=buildBlockItemP(ModBlockRegistry.PurplePot),
			BluePot=buildBlockItemP(ModBlockRegistry.BluePot),BrownPot=buildBlockItemP(ModBlockRegistry.BrownPot),
			GreenPot=buildBlockItemP(ModBlockRegistry.GreenPot),RedPot=buildBlockItemP(ModBlockRegistry.RedPot),
			BlackPot=buildBlockItemP(ModBlockRegistry.BlackPot);
	public static BlockItem Bellows=buildBlockItem(ModBlockRegistry.Bellows),TuyereBrick=buildBlockItem(ModBlockRegistry.TuyereBrick),TuyereSandy=buildBlockItem(ModBlockRegistry.TuyereSandy),
			TuyereNether=buildBlockItem(ModBlockRegistry.TuyereNether),TuyereEnd=buildBlockItem(ModBlockRegistry.TuyereEnd);
	public static ItemClayPot ClayPot=new ItemClayPot();
	public static ItemCrackedPot CrackedPot=new ItemCrackedPot();
	
	public static BlockItem CopperOre=buildBlockItem(ModBlockRegistry.CopperOre),CopperBlock=buildBlockItem(ModBlockRegistry.CopperBlock);
	public static Item CopperIngot=buildItem(CHARCOAL_PIT);
	
	public static ItemBarrel Barrel=new ItemBarrel(ModBlockRegistry.Barrel,new Item.Properties().group(CHARCOAL_PIT));
	
	public static BucketItem CreosoteBucket=new BucketItem(()->ModFluidRegistry.CreosoteStill, new Item.Properties().group(CHARCOAL_PIT).maxStackSize(1).containerItem(Items.BUCKET));
	//public static BucketItem AlcoholBucket=new BucketItem(()->ModFluidRegistry.AlcoholStill, new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).containerItem(Items.BUCKET));
	public static ItemAlcoholBottle AlcoholBottle=new ItemAlcoholBottle();
	public static BucketItem VinegarBucket=new BucketItem(()->ModFluidRegistry.VinegarStill, new Item.Properties().group(CHARCOAL_PIT).maxStackSize(1).containerItem(Items.BUCKET));
	public static Item VinegarBottle=new Item(new Item.Properties().group(CHARCOAL_PIT).maxStackSize(16).containerItem(Items.GLASS_BOTTLE));
	public static Item Cheese=new Item(new Item.Properties().group(CHARCOAL_PIT).food(new Food.Builder().hunger(5).saturation(1.2F).build()));
	public static Item TinyCoke=buildItem(CHARCOAL_PIT,1600);
	public static BlockItem MechanicalBeellows=buildBlockItem(ModBlockRegistry.MechanicalBellows);
	
	/*public static TallBlockItem BrickDoor=new TallBlockItem(ModBlockRegistry.BrickDoor,new Item.Properties().group(CHARCOAL_PIT)),
			SandyDoor=new TallBlockItem(ModBlockRegistry.SandyDoor,new Item.Properties().group(CHARCOAL_PIT)),
			NetherDoor=new TallBlockItem(ModBlockRegistry.NetherDoor,new Item.Properties().group(CHARCOAL_PIT)),
			EndDoor=new TallBlockItem(ModBlockRegistry.EndDoor,new Item.Properties().group(CHARCOAL_PIT));*/
	
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(Thatch.setRegistryName("thatch"), LogPile.setRegistryName("log_pile"), WoodAsh.setRegistryName("wood_ash"), 
				CoalAsh.setRegistryName("coal_ash"), CokeBlock.setRegistryName("coke_block"), AshBlock.setRegistryName("ash_block"), 
				SandyBrick.setRegistryName("sandy_brick"), SandySlab.setRegistryName("sandy_slab"), SandyStair.setRegistryName("sandy_stair"), SandyWall.setRegistryName("sandy_wall"),
				BrickCollector.setRegistryName("brick_collector"),SandyCollector.setRegistryName("sandy_collector"),NetherCollector.setRegistryName("nether_collector"),EndCollector.setRegistryName("end_collector"),
				Bellows.setRegistryName("bellows"),TuyereBrick.setRegistryName("brick_tuyere"),TuyereSandy.setRegistryName("sandy_tuyere"),TuyereNether.setRegistryName("nether_tuyere"),TuyereEnd.setRegistryName("end_tuyere"),
				CopperOre.setRegistryName("copper_ore"),CopperBlock.setRegistryName("copper_block"),
				Barrel.setRegistryName("barrel")/*,BrickDoor.setRegistryName("brick_door"),SandyDoor.setRegistryName("sandy_door"),NetherDoor.setRegistryName("nether_door"),
				EndDoor.setRegistryName("end_door")*/,MechanicalBeellows.setRegistryName("mechanical_bellows"));
		event.getRegistry().registerAll(Straw.setRegistryName("straw"), Ash.setRegistryName("ash"), Coke.setRegistryName("coke"), 
				Aeternalis.setRegistryName("aeternalis_fuel"), Fertilizer.setRegistryName("fertilizer"), FireStarter.setRegistryName("fire_starter"),
				CreosoteBucket.setRegistryName("creosote_bucket"),ClayPot.setRegistryName("clay_pot"),BloomCool.setRegistryName("bloom_cool"),BloomFail.setRegistryName("bloom_fail"),
				CopperIngot.setRegistryName("copper_ingot"),CrackedPot.setRegistryName("cracked_pot"),BloomNiCool.setRegistryName("bloom_nickel_cool"),BloomNiFail.setRegistryName("bloom_nickel_fail"),
				SandyBrickItem.setRegistryName("sandy_brick_item"),UnfireSandyBrick.setRegistryName("unfired_sandy_brick"),NetherBrickItem.setRegistryName("nether_brick_item"),
				UnfiredBrick.setRegistryName("unfired_brick"),AlcoholBottle.setRegistryName("alcohol_bottle"),VinegarBucket.setRegistryName("vinegar_bucket"),
				VinegarBottle.setRegistryName("vinegar_bottle"),Cheese.setRegistryName("cheese"),TinyCoke.setRegistryName("tiny_coke"));
		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"),YellowPot.setRegistryName("yellow_pot"),WhitePot.setRegistryName("white_pot"),
				RedPot.setRegistryName("red_pot"),PurplePot.setRegistryName("purple_pot"),PinkPot.setRegistryName("pink_pot"),OrangePot.setRegistryName("orange_pot"),
				MagentaPot.setRegistryName("magenta_pot"),LimePot.setRegistryName("lime_pot"),LightGrayPot.setRegistryName("light_gray_pot"),
				LightBluePot.setRegistryName("light_blue_pot"),GreenPot.setRegistryName("green_pot"),GrayPot.setRegistryName("gray_pot"),CyanPot.setRegistryName("cyan_pot"),
				BrownPot.setRegistryName("brown_pot"),BluePot.setRegistryName("blue_pot"),BlackPot.setRegistryName("black_pot"));
		
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.CeramicPot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.BlackPot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.BluePot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.BrownPot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.CyanPot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.GrayPot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.GreenPot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.LightBluePot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.LightGrayPot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.LimePot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.MagentaPot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.OrangePot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.PinkPot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.PurplePot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.RedPot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.WhitePot, new DispenserPlacePot());
		DispenserBlock.registerDispenseBehavior(ModItemRegistry.YellowPot, new DispenserPlacePot());
	}
	
	public static BlockItemFuel buildBlockItem(Block block, int time) {
		return buildBlockItem(block, CHARCOAL_PIT, time);
	}
	
	public static BlockItemFuel buildBlockItem(Block block, ItemGroup group, int time) {
		return new BlockItemFuel(block, new Item.Properties().group(group)).setBurnTime(time);
	}
	
	public static BlockItem buildBlockItem(Block block) {
		return buildBlockItem(block, CHARCOAL_PIT);
	}
	
	public static BlockItem buildBlockItemP(Block block) {
		return new BlockItem(block, new Item.Properties().group(CHARCOAL_PIT).maxStackSize(1));
	}
	
	public static BlockItem buildBlockItem(Block block, ItemGroup group) {
		return new BlockItem(block, new Item.Properties().group(group));
	}
	
	public static ItemFuel buildItem(ItemGroup group,int time) {
		return new ItemFuel(new Item.Properties().group(group)).setBurnTime(time);
	}
	
	public static Item buildItem(ItemGroup group) {
		return new Item(new Item.Properties().group(group));
	}
	
}

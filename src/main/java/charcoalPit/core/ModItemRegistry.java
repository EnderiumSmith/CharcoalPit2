package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.item.*;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.system.CallbackI;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModItemRegistry {
	
	public static ItemGroup CHARCOAL_PIT=new ItemGroup("charcoal_pit") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Items.CHARCOAL);
		}
	};
	public static ItemGroup CHARCOAL_PIT_FOODS=new ItemGroup("charcoal_pit_foods") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItemRegistry.Kebabs);
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
	public static Item Cheese=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(5).saturation(1.2F).build()));
	public static Item TinyCoke=buildItem(CHARCOAL_PIT,1600);
	public static BlockItem MechanicalBeellows=buildBlockItem(ModBlockRegistry.MechanicalBellows);
	public static ItemKebabs Kebabs=new ItemKebabs(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(5).saturation(1.6F).meat().build()).containerItem(Items.STICK));
	public static BlockNamedItem Leek=new BlockNamedItem(ModBlockRegistry.Leeks,new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(1).saturation(0.6F).fastToEat().build()));
	public static ItemKebabs FarfetchStew=new ItemKebabs(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(8).saturation(1.6F).meat().build()).containerItem(Items.BOWL));
	public static ItemKebabs RabbitStew=new ItemKebabs(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(8).saturation(1.6F).meat().build()).containerItem(Items.BOWL));
	public static ItemKebabs MooshroomStew=new ItemKebabs(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(6).saturation(1.2F).build()).containerItem(Items.BOWL));
	public static ItemKebabs BeetStew=new ItemKebabs(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(6).saturation(1.2F).build()).containerItem(Items.BOWL));
	public static Item Calamari=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(1).saturation(0.2F).meat().build()));
	public static Item CookedCalamri=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(4).saturation(1.2F).meat().build()));
	public static Item CookedEgg=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(4).saturation(1.2F).meat().build()));
	public static BlockNamedItem CornKernels=new BlockNamedItem(ModBlockRegistry.Corn,new Item.Properties().group(CHARCOAL_PIT_FOODS));
	public static Item Corn=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(3).saturation(1.2F).build()));
	public static Item PopCorn=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(1).saturation(0.6F).fastToEat().build()));
	public static ItemKebabs CornStew=new ItemKebabs(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(6).saturation(1.6F).build()).containerItem(Items.BOWL));
	public static Item Sushi=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(5).saturation(1.2F).meat().build()));
	public static Item SushiCooked=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(6).saturation(1.6F).meat().build()));
	public static Item Fugu=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(5).saturation(2.4F).meat().effect(()->new EffectInstance(Effects.POISON,1200,3),0.02F).build()));
	public static Item Cherry=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(2).saturation(0.6F).fastToEat().build()));
	public static Item DragonFruit=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(6).saturation(0.6F).setAlwaysEdible().effect(()->new EffectInstance(Effects.FIRE_RESISTANCE,20*10),1F).build()));
	public static Item ChestNut=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS));
	public static Item CookedChestNut=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(4).saturation(1.2F).build()));
	public static Item Bananana=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(5).saturation(0.6F).build()));
	public static ItemKebabs Cococonut=new ItemKebabs(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(4).saturation(1.2F).build()).containerItem(Items.BOWL));
	public static Item TurtleRaw=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(2).saturation(0.2F).meat().effect(()->new EffectInstance(Effects.HUNGER,20*30),0.3F).build()));
	public static Item TurtleCooked=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(4).saturation(1.2F).meat().build()));
	public static ItemKebabs TurtleSoup=new ItemKebabs(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(8).saturation(1.6F).meat().build()).containerItem(Items.BOWL));
	public static Item ChocoPoweder=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS));
	public static Item Chocolate=new Item(new Item.Properties().group(CHARCOAL_PIT_FOODS).food(new Food.Builder().hunger(4).saturation(0.2F).fastToEat().build()));
	
	public static BlockItem AppleSapling=new BlockItem(ModBlockRegistry.AppleSapling,new Item.Properties().group(CHARCOAL_PIT));
	public static BlockItem CherrySapling=new BlockItem(ModBlockRegistry.CherrySapling,new Item.Properties().group(CHARCOAL_PIT));
	public static BlockItem DragonSapling=new BlockItem(ModBlockRegistry.DragonSapling, new Item.Properties().group(CHARCOAL_PIT));
	public static BlockItem BananaSapling=new BlockItem(ModBlockRegistry.BananaSapling, new Item.Properties().group(CHARCOAL_PIT));
	public static BlockItem ChestnutSapling=new BlockItem(ModBlockRegistry.ChestnutSapling, new Item.Properties().group(CHARCOAL_PIT));
	public static BlockItem CoconutSapling=new BlockItem(ModBlockRegistry.CoconutSapling,new Item.Properties().group(CHARCOAL_PIT));
	
	public static ItemBlockLeaves AppleLeaves=new ItemBlockLeaves(ModBlockRegistry.AppleLeaves,new Item.Properties().group(CHARCOAL_PIT));
	public static ItemBlockLeaves CherryLeaves=new ItemBlockLeaves(ModBlockRegistry.CherryLeaves,new Item.Properties().group(CHARCOAL_PIT));
	public static ItemBlockLeaves DragonLeaves=new ItemBlockLeaves(ModBlockRegistry.DragonLeaves,new Item.Properties().group(CHARCOAL_PIT));
	public static ItemBlockLeaves ChestnutLeaves=new ItemBlockLeaves(ModBlockRegistry.ChestnutLeaves,new Item.Properties().group(CHARCOAL_PIT));
	public static BlockItem BananaLeaves=new BlockItem(ModBlockRegistry.BananaLeaves,new Item.Properties().group(CHARCOAL_PIT));
	public static BlockItem CoconutLeaves=new BlockItem(ModBlockRegistry.CoconutLeaves,new Item.Properties().group(CHARCOAL_PIT));
	
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
				EndDoor.setRegistryName("end_door")*/,MechanicalBeellows.setRegistryName("mechanical_bellows"),
				AppleSapling.setRegistryName("apple_sapling"),CherrySapling.setRegistryName("cherry_sapling"),
				AppleLeaves.setRegistryName("apple_leaves"),CherryLeaves.setRegistryName("cherry_leaves"),
				DragonSapling.setRegistryName("dragon_sapling"),DragonLeaves.setRegistryName("dragon_leaves"),
				BananaSapling.setRegistryName("banana_sapling"),BananaLeaves.setRegistryName("banana_leaves"),
				ChestnutSapling.setRegistryName("chestnut_sapling"),ChestnutLeaves.setRegistryName("chestnut_leaves"),
				CoconutLeaves.setRegistryName("coconut_leaves"),CoconutSapling.setRegistryName("coconut_sapling"));
		event.getRegistry().registerAll(Straw.setRegistryName("straw"), Ash.setRegistryName("ash"), Coke.setRegistryName("coke"), 
				Aeternalis.setRegistryName("aeternalis_fuel"), Fertilizer.setRegistryName("fertilizer"), FireStarter.setRegistryName("fire_starter"),
				CreosoteBucket.setRegistryName("creosote_bucket"),ClayPot.setRegistryName("clay_pot"),BloomCool.setRegistryName("bloom_cool"),BloomFail.setRegistryName("bloom_fail"),
				CopperIngot.setRegistryName("copper_ingot"),CrackedPot.setRegistryName("cracked_pot"),BloomNiCool.setRegistryName("bloom_nickel_cool"),BloomNiFail.setRegistryName("bloom_nickel_fail"),
				SandyBrickItem.setRegistryName("sandy_brick_item"),UnfireSandyBrick.setRegistryName("unfired_sandy_brick"),NetherBrickItem.setRegistryName("nether_brick_item"),
				UnfiredBrick.setRegistryName("unfired_brick"),AlcoholBottle.setRegistryName("alcohol_bottle"),VinegarBucket.setRegistryName("vinegar_bucket"),
				VinegarBottle.setRegistryName("vinegar_bottle"),Cheese.setRegistryName("cheese"),TinyCoke.setRegistryName("tiny_coke"),Kebabs.setRegistryName("kebabs"),
				Leek.setRegistryName("leek"),FarfetchStew.setRegistryName("farfetch_stew"),RabbitStew.setRegistryName("rabbit_stew"),MooshroomStew.setRegistryName("mooshroom_stew"),
				BeetStew.setRegistryName("beet_stew"),Calamari.setRegistryName("calamari"),CookedCalamri.setRegistryName("cooked_calamari"),CookedEgg.setRegistryName("cooked_egg"),
				Corn.setRegistryName("corn"),CornKernels.setRegistryName("corn_kernels"),PopCorn.setRegistryName("popcorn"),CornStew.setRegistryName("corn_stew"),
				Sushi.setRegistryName("sushi"),SushiCooked.setRegistryName("cooked_sushi"),Fugu.setRegistryName("fugu"),Cherry.setRegistryName("cherry"),DragonFruit.setRegistryName("dragon_fruit"),
				ChestNut.setRegistryName("chestnut"),CookedChestNut.setRegistryName("cooked_chestnut"),Bananana.setRegistryName("banana"),Cococonut.setRegistryName("coconut"),
				TurtleRaw.setRegistryName("turtle_raw"),TurtleCooked.setRegistryName("turtle_cooked"),TurtleSoup.setRegistryName("turtle_stew"),
				ChocoPoweder.setRegistryName("choco_powder"),Chocolate.setRegistryName("chocolate"));
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

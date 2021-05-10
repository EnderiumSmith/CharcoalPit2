package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.block.BlockBloomery;
import charcoalPit.block.BlockCeramicPot;
import charcoalPit.block.BlockPotteryKiln;
import charcoalPit.block.BlockPotteryKiln.EnumKilnTypes;
import charcoalPit.recipe.BloomeryRecipe;
import charcoalPit.recipe.PotteryKilnRecipe;
import charcoalPit.tile.TileBloomery2;
import charcoalPit.tile.TilePotteryKiln;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.system.CallbackI;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.FORGE)
public class PileIgnitr {
	
	@SubscribeEvent
	public static void checkIgnition(NeighborNotifyEvent event){
		if(!event.isCanceled()&&
				event.getWorld().getBlockState(event.getPos()).getBlock()==Blocks.FIRE){
			for(Direction facing:event.getNotifiedSides()){
				BlockPos pos=event.getPos().offset(facing);
				if(event.getWorld().getBlockState(pos).getBlock()==ModBlockRegistry.LogPile){
					//found log pile to ignite
					igniteLogs(event.getWorld(),pos);
					
				}else if(event.getWorld().getBlockState(pos).getBlock()==Blocks.COAL_BLOCK){
					//found coal pile to ignite
					igniteCoal(event.getWorld(),pos);
				}else if(facing==Direction.DOWN&&event.getWorld().getBlockState(pos).getBlock()==ModBlockRegistry.Kiln){
					//found pottery kiln to ignite
					ignitePottery(event.getWorld(), pos);
				}else if(event.getWorld().getBlockState(pos).getBlock()==ModBlockRegistry.Bloomery){
					//found bloomery to ignite
					igniteBloomery(event.getWorld(), pos);
				}
			}
		}
	}
	public static void igniteLogs(IWorld world, BlockPos pos){
		if(world.getBlockState(pos).getBlock()==ModBlockRegistry.LogPile){
			world.setBlockState(pos, ModBlockRegistry.ActiveLogPile.getDefaultState().with(BlockStateProperties.AXIS, world.getBlockState(pos).get(BlockStateProperties.AXIS)),2);
			Direction[] neighbors=Direction.values();
			for(int i=0;i<neighbors.length;i++){
				igniteLogs(world, pos.offset(neighbors[i]));
			}
		}
	}
	public static void igniteCoal(IWorld world, BlockPos pos){
		if(world.getBlockState(pos).getBlock()==Blocks.COAL_BLOCK){
			world.setBlockState(pos, ModBlockRegistry.ActiveCoalPile.getDefaultState(),2);
			Direction[] neighbors=Direction.values();
			for(int i=0;i<neighbors.length;i++){
				igniteCoal(world, pos.offset(neighbors[i]));
			}
		}
	}
	public static void ignitePottery(IWorld world, BlockPos pos) {
		if(world.getBlockState(pos).getBlock()==ModBlockRegistry.Kiln&&
				world.getBlockState(pos).get(BlockPotteryKiln.TYPE)==EnumKilnTypes.WOOD) {
			world.setBlockState(pos, ModBlockRegistry.Kiln.getDefaultState().with(BlockPotteryKiln.TYPE, EnumKilnTypes.ACTIVE), 3);
			((TilePotteryKiln)world.getTileEntity(pos)).setActive(true);
			for(int x=-1;x<=1;x++) {
				for(int z=-1;z<=1;z++) {
					ignitePottery(world, new BlockPos(pos.getX()+x, pos.getY(), pos.getZ()+z));
				}
			}
		}
	}
	public static void igniteBloomery(IWorld world, BlockPos pos) {
		if(world.getBlockState(pos).getBlock()==ModBlockRegistry.Bloomery&&
				world.getBlockState(pos).get(BlockBloomery.STAGE)==8) {
			world.setBlockState(pos, world.getBlockState(pos).with(BlockBloomery.STAGE, 9), 3);
			TileBloomery2 tile=((TileBloomery2)world.getTileEntity(pos));
			tile.burnTime=Config.BloomeryTime.get()*2;
			tile.airTicks=Config.BloomeryTime.get();
			if(tile.dummy) {
				igniteBloomery(world, pos.offset(Direction.DOWN));
			}
		}
	}
	/*public static boolean igniteBloomery(IWorld world, BlockPos pos) {
		if(world.getBlockState(pos).getBlock()==ModBlockRegistry.BloomeryPile) {
			world.setBlockState(pos, ModBlockRegistry.ActiveBloomery.getDefaultState(), 3);
			if(igniteBloomery(world, pos.offset(Direction.DOWN))) {
				((TileBloomery)world.getTileEntity(pos)).setDummy(true);
			}else
				((TileBloomery)world.getTileEntity(pos)).setDummy(false);
			return true;
		}
		return false;
	}*/
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void placeKiln(PlayerInteractEvent.RightClickBlock event) {
		if(!event.isCanceled()&&event.getPlayer().isSneaking()&&PotteryKilnRecipe.isValidInput(event.getItemStack(), event.getWorld())&&
				event.getFace()==Direction.UP&&event.getWorld().getBlockState(event.getPos()).isSolidSide(event.getWorld(), event.getPos(), Direction.UP)&&
				event.getWorld().getBlockState(event.getPos().offset(Direction.UP)).getMaterial().isReplaceable()) {
			if(!event.getWorld().isRemote) {
				event.getWorld().setBlockState(event.getPos().offset(Direction.UP), ModBlockRegistry.Kiln.getDefaultState());
				TilePotteryKiln tile=((TilePotteryKiln)event.getWorld().getTileEntity(event.getPos().offset(Direction.UP)));
				event.getPlayer().setHeldItem(event.getHand(), tile.pottery.insertItem(0, event.getItemStack(), false));
				event.getWorld().playSound(null, event.getPos(), SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
			}
			event.setUseBlock(Result.DENY);
			event.setUseItem(Result.DENY);
		}
		//undye pots
		if(!event.isCanceled()&&event.getWorld().getBlockState(event.getPos()).getBlock()==Blocks.CAULDRON&&
				event.getWorld().getBlockState(event.getPos()).get(CauldronBlock.LEVEL)>0) {
			Block block=Block.getBlockFromItem(event.getItemStack().getItem());
			if(block instanceof BlockCeramicPot&&block!=ModBlockRegistry.CeramicPot) {
				ItemStack stack=new ItemStack(ModBlockRegistry.CeramicPot, 1);
				stack.setTag(event.getItemStack().getTag());
				event.getPlayer().setHeldItem(event.getHand(), stack);
				event.getWorld().setBlockState(event.getPos(), Blocks.CAULDRON.getDefaultState().with(CauldronBlock.LEVEL, event.getWorld().getBlockState(event.getPos()).get(CauldronBlock.LEVEL)-1));
				event.setUseBlock(Result.DENY);
				event.setUseItem(Result.DENY);
			}
		}
		//place bloomery
		if(!event.isCanceled()&&event.getPlayer().isSneaking()&&BloomeryRecipe.getRecipe(event.getItemStack(), event.getWorld())!=null&&
				event.getFace()==Direction.UP&&event.getWorld().getBlockState(event.getPos()).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls")))&&
				event.getWorld().getBlockState(event.getPos().offset(Direction.UP)).getMaterial().isReplaceable()&&
				MethodHelper.Bloomery2ValidPosition(event.getWorld(), event.getPos().offset(Direction.UP), false, false)) {
			if(!event.getWorld().isRemote) {
				event.getWorld().setBlockState(event.getPos().offset(Direction.UP), ModBlockRegistry.Bloomery.getDefaultState().with(BlockBloomery.STAGE, 1));
				TileBloomery2 tile=((TileBloomery2)event.getWorld().getTileEntity(event.getPos().offset(Direction.UP)));
				tile.recipe=BloomeryRecipe.getRecipe(event.getItemStack(), event.getWorld());
				event.getPlayer().setHeldItem(event.getHand(), tile.ore.insertItem(0, event.getItemStack(), false));
				event.getWorld().playSound(null, event.getPos(), SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
			}
			event.setUseBlock(Result.DENY);
			event.setUseItem(Result.DENY);
			
		}
		/*if(!event.isCanceled()&&event.getPlayer().isSneaking()&&event.getItemStack().getItem().isIn(Tags.Items.ORES_IRON)&&
				event.getFace()==Direction.UP&&event.getWorld().getBlockState(event.getPos()).isSolidSide(event.getWorld(), event.getPos(), Direction.UP)&&
				event.getWorld().getBlockState(event.getPos().offset(Direction.UP)).getMaterial().isReplaceable()&&
				MethodHelper.BloomeryIsValidPosition(event.getWorld(), event.getPos().offset(Direction.UP),true)) {
			if(!event.getWorld().isRemote) {
				event.getWorld().setBlockState(event.getPos().offset(Direction.UP), ModBlockRegistry.BloomeryPile.getDefaultState());
				event.getPlayer().getHeldItem(event.getHand()).shrink(1);
				event.getWorld().playSound(null, event.getPos(), SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1F, 1F);
			}
			event.setUseBlock(Result.DENY);
			event.setUseItem(Result.DENY);
		}*/
	}
	@SubscribeEvent
	public static void addLoot(LootTableLoadEvent event) {
		if(event.getName().toString().equals("minecraft:chests/end_city_treasure")){
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(CharcoalPit.MODID, "end_aeternalis"))).build());
		}
		if(event.getName().toString().equals("minecraft:entities/chicken")){
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(CharcoalPit.MODID,"leeks"))).build());
		}
		if(event.getName().toString().equals("minecraft:entities/squid")){
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(CharcoalPit.MODID,"calamari"))).build());
		}
		if(event.getName().toString().equals("minecraft:chests/simple_dungeon")){
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(CharcoalPit.MODID,"saplings"))).build());
		}
		if(event.getName().toString().equals("minecraft:chests/abandoned_mineshaft")){
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(CharcoalPit.MODID,"saplings"))).build());
		}
		if(event.getName().toString().equals("minecraft:chests/stronghold_corridor")){
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(CharcoalPit.MODID,"saplings"))).build());
		}
		if(event.getName().toString().equals("minecraft:chests/stronghold_crossing")){
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(CharcoalPit.MODID,"saplings"))).build());
		}
	}
	@SubscribeEvent
	public static void savePotInventory(ItemCraftedEvent event) {
		if(Block.getBlockFromItem(event.getCrafting().getItem()) instanceof BlockCeramicPot) {
			for(int i=0;i<event.getInventory().getSizeInventory();i++) {
				if(Block.getBlockFromItem(event.getInventory().getStackInSlot(i).getItem()) instanceof BlockCeramicPot) {
					event.getCrafting().setTag(event.getInventory().getStackInSlot(i).getTag());
					return;
				}
			}
		}
	}
	@SubscribeEvent
	public static void furnaceFuel(FurnaceFuelBurnTimeEvent event) {
		if(event.getItemStack().getItem()==ModItemRegistry.CreosoteBucket)
			event.setBurnTime(4800);
	}
	
	public static BasicTrade AppleSapling=new BasicTrade(12,new ItemStack(ModItemRegistry.AppleSapling),6,1);
	public static BasicTrade CherrySapling=new BasicTrade(12,new ItemStack(ModItemRegistry.CherrySapling),6,1);
	public static BasicTrade ChestnutSapling=new BasicTrade(6,new ItemStack(ModItemRegistry.ChestnutSapling),6,1);
	public static BasicTrade DragonSapling=new BasicTrade(18,new ItemStack(ModItemRegistry.DragonSapling),6,1);
	public static BasicTrade BananaSapling=new BasicTrade(12,new ItemStack(ModItemRegistry.BananaSapling),6,1);
	public static BasicTrade CoconutSapling=new BasicTrade(12,new ItemStack(ModItemRegistry.CoconutSapling),6,1);
	
	@SubscribeEvent
	public static void SaplingTrades(WandererTradesEvent event){
		event.getRareTrades().add(AppleSapling);
		event.getRareTrades().add(CherrySapling);
		event.getRareTrades().add(ChestnutSapling);
		event.getRareTrades().add(DragonSapling);
		event.getRareTrades().add(BananaSapling);
		event.getRareTrades().add(CoconutSapling);
	}
	
	public static BasicTrade Corn=new BasicTrade(new ItemStack(ModItemRegistry.Corn,24),ItemStack.EMPTY, new ItemStack(Items.EMERALD),16,2,0.05F);
	public static BasicTrade Leek=new BasicTrade(new ItemStack(ModItemRegistry.Leek,20),ItemStack.EMPTY, new ItemStack(Items.EMERALD),16,2,0.05F);
	
	public static BasicTrade Chocolate=new BasicTrade(1, new ItemStack(ModItemRegistry.Chocolate,4),12,15, 0.05F);
	public static BasicTrade Cherry=new BasicTrade(1,new ItemStack(ModItemRegistry.Cherry,10),16,5,0.05F);
	
	public static BasicTrade Kebab=new BasicTrade(1,new ItemStack(ModItemRegistry.Kebabs,6),12,30,0.05F);
	public static BasicTrade Vinegar=new BasicTrade(2,new ItemStack(ModItemRegistry.VinegarBottle,4),12,30,0.05F);
	
	@SubscribeEvent
	public static void CropTrades(VillagerTradesEvent event){
		if(event.getType()== VillagerProfession.FARMER){
			event.getTrades().get(1).add(Corn);
			event.getTrades().get(1).add(Leek);
			event.getTrades().get(2).add(Cherry);
			event.getTrades().get(4).add(Chocolate);
		}
		if(event.getType()==VillagerProfession.BUTCHER){
			event.getTrades().get(4).add(Vinegar);
			event.getTrades().get(5).add(Kebab);
		}
	}
}

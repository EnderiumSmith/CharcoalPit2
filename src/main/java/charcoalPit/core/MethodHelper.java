package charcoalPit.core;

import charcoalPit.CharcoalPit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class MethodHelper {
	
	public static boolean CharcoalPitIsValidBlock(World world, BlockPos pos, Direction facing, boolean isCoke) {
		BlockState state=world.getBlockState(pos.offset(facing));
		if(state.isFlammable(world, pos.offset(facing), facing.getOpposite())) {
			return false;
		}
		if(isCoke&&!CokeOvenIsValidBlock(state)) {
			return false;
		}
		return state.isSolidSide(world, pos.offset(facing), facing.getOpposite());
	}
	
	public static boolean CokeOvenIsValidBlock(BlockState state) {
		Block block=state.getBlock();
		return block==ModBlockRegistry.ActiveCoalPile||block.isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "coke_oven_walls")));
	}
	//for placement
	/*public static boolean BloomeryIsValidPosition(World world, BlockPos pos, boolean init) {
		for(Direction dir:Direction.Plane.HORIZONTAL) {
			if(!(world.getBlockState(pos.offset(dir)).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls")))))
				return false;
		}
		return (init&&world.getBlockState(pos.offset(Direction.DOWN)).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls"))))||
				(!init&&world.getBlockState(pos.offset(Direction.DOWN)).getBlock()==ModBlockRegistry.BloomeryPile);
	}
	//for active bloomery
	public static boolean BloomeryIsValidPosition(World world, BlockPos pos) {
		for(Direction dir:Direction.Plane.HORIZONTAL) {
			if(!(world.getBlockState(pos.offset(dir)).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls")))))
				return false;
		}
		if(!(world.getBlockState(pos.offset(Direction.DOWN)).getBlock()==ModBlockRegistry.ActiveBloomery||
				world.getBlockState(pos.offset(Direction.DOWN)).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls")))))
			return false;
		if(!(world.getBlockState(pos.offset(Direction.UP)).getBlock()==Blocks.FIRE||
				world.getBlockState(pos.offset(Direction.UP)).getBlock()==ModBlockRegistry.ActiveBloomery))
			return false;
		return true;
			
	}*/
	
	public static boolean Bloomery2ValidPosition(World world, BlockPos pos, boolean dummy, boolean active) {
		for(Direction dir:Direction.Plane.HORIZONTAL) {
			if(!(world.getBlockState(pos.offset(dir)).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls")))))
				return false;
		}
		Block block=world.getBlockState(pos.offset(Direction.DOWN)).getBlock();
		if(		!(
				(!dummy && block.isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls"))))
				||
				(dummy && block==ModBlockRegistry.Bloomery)
				)
			)
			return false;
		block=world.getBlockState(pos.offset(Direction.UP)).getBlock();
		if(
				!(
					(!dummy && block==ModBlockRegistry.Bloomery)||
					(block==Blocks.FIRE)
					)&&active
			)
			return false;
		return true;
	}
	
	public static int calcRedstoneFromInventory(IItemHandler inv) {
	      if (inv == null) {
	         return 0;
	      } else {
	         int i = 0;
	         float f = 0.0F;

	         for(int j = 0; j < inv.getSlots(); ++j) {
	            ItemStack itemstack = inv.getStackInSlot(j);
	            if (!itemstack.isEmpty()) {
	               f += (float)itemstack.getCount() / (float)Math.min(inv.getSlotLimit(j), itemstack.getMaxStackSize());
	               ++i;
	            }
	         }

	         f = f / (float)inv.getSlots();
	         return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
	      }
	   }
	
	public static boolean isItemInString(String key, Item item) {
		if(key.startsWith("item:")) {
			return key.substring(5).equals(item.getRegistryName().toString());
		}
		if(key.startsWith("tag:")) {
			ITag<Item> tag=ItemTags.getCollection().get(new ResourceLocation(key.substring(4)));
			return tag!=null&&tag.contains(item);
		}
		if(key.startsWith("ore:")) {
			ITag<Item> tag;
			String ore="forge:ores/".concat(key.substring(4));
			tag=ItemTags.getCollection().get(new ResourceLocation(ore));
			if(tag!=null&&tag.contains(item))
				return true;
			String ingot="forge:ingots/".concat(key.substring(4));
			tag=ItemTags.getCollection().get(new ResourceLocation(ingot));
			if(tag!=null&&tag.contains(item))
				return true;
			String dust="forge:dusts/".concat(key.substring(4));
			tag=ItemTags.getCollection().get(new ResourceLocation(dust));
			if(tag!=null&&tag.contains(item))
				return true;
		}
		return false;
	}
	
	public static boolean doesStringHaveItem(String key) {
		if(key.startsWith("item:")) {
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(key.substring(5)))!=null;
		}
		if(key.startsWith("tag:")) {
			return ItemTags.getCollection().get(new ResourceLocation(key.substring(4)))!=null;
		}
		if(key.startsWith("ore:")) {
			ITag<Item> tag;
			String ore="forge:ores/".concat(key.substring(4));
			tag=ItemTags.getCollection().get(new ResourceLocation(ore));
			if(tag!=null)
				return true;
			String ingot="forge:ingots/".concat(key.substring(4));
			tag=ItemTags.getCollection().get(new ResourceLocation(ingot));
			if(tag!=null)
				return true;
			String dust="forge:dusts/".concat(key.substring(4));
			tag=ItemTags.getCollection().get(new ResourceLocation(dust));
			if(tag!=null)
				return true;
		}
		return false;
	}
	
	public static Item getItemForString(String key) {
		if(key.startsWith("item:")) {
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(key.substring(5)));
		}
		if(key.startsWith("tag:")) {
			ITag<Item> tag=ItemTags.getCollection().get(new ResourceLocation(key.substring(4)));
			return tag==null?null:tag.getAllElements().get(0);
		}
		if(key.contains("ore:")) {
			ITag<Item> tag;
			String ore="forge:ores/".concat(key.substring(4));
			tag=ItemTags.getCollection().get(new ResourceLocation(ore));
			if(tag!=null)
				return tag.getAllElements().get(0);
			String ingot="forge:ingots/".concat(key.substring(4));
			tag=ItemTags.getCollection().get(new ResourceLocation(ingot));
			if(tag!=null)
				return tag.getAllElements().get(0);
			String dust="forge:dusts/".concat(key.substring(4));
			tag=ItemTags.getCollection().get(new ResourceLocation(dust));
			if(tag!=null)
				return tag.getAllElements().get(0);
		}
		return null;
	}
	
}

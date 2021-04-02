package charcoalPit.block;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class BlockAsh extends FallingBlock{

	public BlockAsh() {
		super(Properties.create(Material.SAND, MaterialColor.LIGHT_GRAY).hardnessAndResistance(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.SAND));
	}
	
	@Override
	public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
		return silktouch==0?this.RANDOM.nextInt(3):0;
	}
	
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
	      if (worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
	         FallingBlockEntity fallingblockentity = new FallingBlockEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos)) {
				@Override
	        	public ItemEntity entityDropItem(IItemProvider itemIn) {
	        		Block.getDrops(state, worldIn, new BlockPos(getPosX(), getPosY(), getPosZ()), null).forEach((stack)->{
	        			spawnAsEntity(worldIn, new BlockPos(getPosX(), getPosY(), getPosZ()), stack);
	        		});
	        		return null;
	        	}
	         };
	         this.onStartFalling(fallingblockentity);
	         worldIn.addEntity(fallingblockentity);
	      }
	   }
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		ResourceLocation resourcelocation = this.getLootTable();
	      if (resourcelocation == LootTables.EMPTY) {
	         return Collections.emptyList();
	      } else {
	    	 Entity entity=builder.get(LootParameters.THIS_ENTITY);
	    	 int fortune=0;
	    	 if(entity instanceof LivingEntity) {
	    		 fortune=EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FORTUNE, (LivingEntity)entity);
	    	 }
	         LootContext lootcontext = builder.withParameter(LootParameters.BLOCK_STATE, state).withLuck(fortune).build(LootParameterSets.BLOCK);
	         ServerWorld serverworld = lootcontext.getWorld();
	         LootTable loottable = serverworld.getServer().getLootTableManager().getLootTableFromLocation(resourcelocation);
	         return loottable.generate(lootcontext);
	      }
	}

}

package charcoalPit.loot;

import java.util.List;

import com.google.gson.JsonObject;

import charcoalPit.CharcoalPit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class StrawFromGrass extends LootModifier{

	public final Item straw;
	protected StrawFromGrass(ILootCondition[] conditionsIn, Item straw) {
		super(conditionsIn);
		this.straw=straw;
	}
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if(context.has(LootParameters.BLOCK_STATE)){
			if(context.get(LootParameters.BLOCK_STATE).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "straw_grass")))) {
				if(context.get(LootParameters.THIS_ENTITY) instanceof PlayerEntity) {
					((PlayerEntity)context.get(LootParameters.THIS_ENTITY)).getHeldItemMainhand().damageItem(1, (PlayerEntity)context.get(LootParameters.THIS_ENTITY), (stack)->{});
					generatedLoot.add(new ItemStack(straw));
				}
			}
			if(context.get(LootParameters.BLOCK_STATE).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "straw_grass_tall")))) {
				if(context.get(LootParameters.THIS_ENTITY) instanceof PlayerEntity) {
					((PlayerEntity)context.get(LootParameters.THIS_ENTITY)).getHeldItemMainhand().damageItem(1, (PlayerEntity)context.get(LootParameters.THIS_ENTITY), (stack)->{});
					generatedLoot.add(new ItemStack(straw,2));
				}
			}
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<StrawFromGrass>{

		@Override
		public StrawFromGrass read(ResourceLocation location, JsonObject object, ILootCondition[] conditionsIn) {
			Item straw=ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(object, "strawItem")));
			return new StrawFromGrass(conditionsIn, straw);
		}

		@Override
		public JsonObject write(StrawFromGrass instance) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
}

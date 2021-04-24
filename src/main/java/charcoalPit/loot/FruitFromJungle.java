package charcoalPit.loot;

import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class FruitFromJungle extends LootModifier {
	
	public Item item_a,item_b;
	
	public FruitFromJungle(ILootCondition[] iLootConditions, Item item_a,Item item_b){
		super(iLootConditions);
		this.item_a=item_a;
		this.item_b=item_b;
	}
	
	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if(context.has(LootParameters.BLOCK_STATE)&&context.get(LootParameters.BLOCK_STATE).getBlock()==Blocks.JUNGLE_LEAVES){
			if(context.getRandom().nextBoolean()){
				generatedLoot.add(new ItemStack(item_a));
			}else{
				generatedLoot.add(new ItemStack(item_b));
			}
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<FruitFromJungle>{
		
		@Override
		public FruitFromJungle read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
			Item straw= ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(object, "item_a")));
			Item item_b=ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(object, "item_b")));
			return new FruitFromJungle(ailootcondition,straw,item_b);
		}
		
		@Override
		public JsonObject write(FruitFromJungle instance) {
			return null;
		}
	}
}

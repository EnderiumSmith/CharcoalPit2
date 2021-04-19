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

public class DragonFromAcacia extends LootModifier {
	
	Item item;
	
	protected DragonFromAcacia(ILootCondition[] conditionsIn, Item item) {
		super(conditionsIn);
		this.item=item;
	}
	
	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if(context.has(LootParameters.BLOCK_STATE)&&context.get(LootParameters.BLOCK_STATE).getBlock()== Blocks.ACACIA_LEAVES){
			generatedLoot.add(new ItemStack(this.item));
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<DragonFromAcacia>{
		
		@Override
		public DragonFromAcacia read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
			Item straw= ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(object, "item")));
			return new DragonFromAcacia(ailootcondition,straw);
		}
		
		@Override
		public JsonObject write(DragonFromAcacia instance) {
			return null;
		}
	}
}

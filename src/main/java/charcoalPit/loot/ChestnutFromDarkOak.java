package charcoalPit.loot;

import charcoalPit.core.ModItemRegistry;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class ChestnutFromDarkOak extends LootModifier {
	protected ChestnutFromDarkOak(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}
	
	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if(context.has(LootParameters.BLOCK_STATE)&&context.get(LootParameters.BLOCK_STATE).getBlock()== Blocks.DARK_OAK_LEAVES) {
			for (int i = 0; i < generatedLoot.size(); i++) {
				if (generatedLoot.get(i).getItem() == Items.APPLE) {
					generatedLoot.remove(i);
					generatedLoot.add(new ItemStack(ModItemRegistry.ChestNut));
					break;
				}
			}
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<ChestnutFromDarkOak>{
		
		@Override
		public ChestnutFromDarkOak read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
			return new ChestnutFromDarkOak(ailootcondition);
		}
		
		@Override
		public JsonObject write(ChestnutFromDarkOak instance) {
			return null;
		}
	}
}

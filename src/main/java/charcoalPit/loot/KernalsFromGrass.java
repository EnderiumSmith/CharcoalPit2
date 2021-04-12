package charcoalPit.loot;

import charcoalPit.CharcoalPit;
import com.google.gson.JsonObject;
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
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class KernalsFromGrass extends LootModifier {
	public Item item;
	
	public KernalsFromGrass(ILootCondition[] conditionsIn,Item item) {
		super(conditionsIn);
		this.item=item;
	}
	
	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if(context.has(LootParameters.BLOCK_STATE)) {
			if (context.get(LootParameters.BLOCK_STATE).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "straw_grass"))) ||
					context.get(LootParameters.BLOCK_STATE).getBlock().isIn(BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID, "straw_grass_tall"))))
				generatedLoot.add(new ItemStack(item));
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<KernalsFromGrass>{
		
		@Override
		public KernalsFromGrass read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
			Item straw= ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(object, "item")));
			return new KernalsFromGrass(ailootcondition, straw);
		}
		
		@Override
		public JsonObject write(KernalsFromGrass instance) {
			return null;
		}
	}
	
}

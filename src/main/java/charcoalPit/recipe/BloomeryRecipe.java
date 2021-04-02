package charcoalPit.recipe;

import java.util.List;

import com.google.gson.JsonObject;

import charcoalPit.CharcoalPit;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class BloomeryRecipe implements IRecipe<IInventory>{
	
	public static final ResourceLocation BLOOMERY=new ResourceLocation(CharcoalPit.MODID, "bloomery");
	public static final IRecipeType<BloomeryRecipe> BLOOMERY_RECIPE=IRecipeType.register(BLOOMERY.toString());
	
	public final ResourceLocation id;
	public Ingredient input,output,fail,cool;
	
	public BloomeryRecipe(ResourceLocation id,Ingredient input,Ingredient output,Ingredient fail,Ingredient cool) {
		this.id=id;
		this.input=input;
		this.output=output;
		this.fail=fail;
		this.cool=cool;
	}
	
	public static final Serializer SERIALIZER=new Serializer();
	
	public static BloomeryRecipe getRecipe(ItemStack stack, World world) {
		if(stack.isEmpty())
			return null;
		List<BloomeryRecipe> recipes=world.getRecipeManager().getRecipesForType(BLOOMERY_RECIPE);
		for(BloomeryRecipe recipe:recipes) {
			if(recipe.input.test(stack)&&!recipe.output.hasNoMatchingItems())
				return recipe;
		}
		return null;
	}
	
	//junk
	@Override
	public boolean matches(IInventory inv, World worldIn) {
		return false;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		// TODO Auto-generated method stub
		return SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return BLOOMERY_RECIPE;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BloomeryRecipe>{

		@Override
		public BloomeryRecipe read(ResourceLocation recipeId, JsonObject json) {
			Ingredient input=Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
			Ingredient output=Ingredient.deserialize(JSONUtils.getJsonObject(json, "output"));
			Ingredient fail=Ingredient.deserialize(JSONUtils.getJsonObject(json, "fail"));
			Ingredient cool=Ingredient.deserialize(JSONUtils.getJsonObject(json, "cool"));
			return new BloomeryRecipe(recipeId, input, output, fail, cool);
		}

		@Override
		public BloomeryRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			Ingredient input=Ingredient.read(buffer);
			Ingredient output=Ingredient.read(buffer);
			Ingredient fail=Ingredient.read(buffer);
			Ingredient cool=Ingredient.read(buffer);
			return new BloomeryRecipe(recipeId, input, output, fail, cool);
		}

		@Override
		public void write(PacketBuffer buffer, BloomeryRecipe recipe) {
			recipe.input.write(buffer);
			recipe.output.write(buffer);
			recipe.fail.write(buffer);
			recipe.cool.write(buffer);
			
		}
		
	}
	
}

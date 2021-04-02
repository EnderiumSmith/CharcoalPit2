package charcoalPit.recipe;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModItemRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PotteryKilnRecipe implements IRecipe<IInventory>{
	
	public static final ResourceLocation POTTERY=new ResourceLocation(CharcoalPit.MODID, "pottery");
	public static final IRecipeType<PotteryKilnRecipe> POTTERY_RECIPE=IRecipeType.register(POTTERY.toString());
	
	public static final Serializer SERIALIZER=new Serializer();
	
	public final ResourceLocation id;
	public Ingredient input;
	public Item output;
	public float xp;
	
	public PotteryKilnRecipe(ResourceLocation id, Ingredient in, Item out, float exp) {
		this.id=id;
		input=in;
		output=out;
		xp=exp;
	}
	
	public static boolean isValidInput(ItemStack input, World world) {
		if(input.getItem()==ModItemRegistry.ClayPot) {
			if(input.hasTag()&&input.getTag().contains("inventory")) {
				ItemStackHandler inv=new ItemStackHandler();
				inv.deserializeNBT(input.getTag().getCompound("inventory"));
				if(!OreKilnRecipe.oreKilnIsEmpty(inv)) {
					if(OreKilnRecipe.OreKilnGetOutput(input.getTag().getCompound("inventory"), world)!=null) {
						 return OreKilnRecipe.oreKilnGetFuelAvailable(inv)>=OreKilnRecipe.oreKilnGetFuelRequired(inv);
					}else {
						return false;
					}
				}else {
					if(OreKilnRecipe.oreKilnGetFuelAvailable(inv)!=0)
						return false;
				}
			}
		}
		List<PotteryKilnRecipe> recipes=world.getRecipeManager().getRecipesForType(POTTERY_RECIPE);
		for(PotteryKilnRecipe recipe:recipes) {
			if(recipe.input.test(input))
				return true;
		}
		return false;
	}
	
	public static PotteryKilnRecipe getResult(ItemStack input, World world) {
		List<PotteryKilnRecipe> recipes=world.getRecipeManager().getRecipesForType(POTTERY_RECIPE);
		for(PotteryKilnRecipe recipe:recipes) {
			if(recipe.input.test(input))
				return recipe;
		}
		return null;
	}
	
	public static ItemStack processClayPot(ItemStack in, World world) {
		if(in.getItem()==ModItemRegistry.ClayPot) {
			if(in.hasTag()&&in.getTag().contains("inventory")) {
				ItemStackHandler tag=new ItemStackHandler(1);
				tag.setStackInSlot(0, OreKilnRecipe.OreKilnGetOutput(in.getTag().getCompound("inventory"), world));
				ItemStack out=new ItemStack(ModItemRegistry.CrackedPot);
				out.setTagInfo("inventory", tag.serializeNBT());
				ItemStackHandler inv=new ItemStackHandler();
				inv.deserializeNBT(in.getTag().getCompound("inventory"));
				out.getTag().putInt("xp", OreKilnRecipe.oreKilnGetFuelRequired(inv)/4);
				return out;
			}
			return ItemStack.EMPTY;
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn) {
		return input.test(inv.getStackInSlot(0));
	}
	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return new ItemStack(output);
	}
	@Override
	public boolean canFit(int width, int height) {
		return true;
	}
	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(output);
	}
	@Override
	public ResourceLocation getId() {
		return id;
	}
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	@Override
	public IRecipeType<?> getType() {
		return POTTERY_RECIPE;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PotteryKilnRecipe>{

		@SuppressWarnings("deprecation")
		@Override
		public PotteryKilnRecipe read(ResourceLocation recipeId, JsonObject json) {
			JsonElement jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
		    Ingredient ingredient = Ingredient.deserialize(jsonelement);
		    //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
		    if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		    ItemStack itemstack;
		    if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
		    else {
		    String s1 = JSONUtils.getString(json, "result");
		    ResourceLocation resourcelocation = new ResourceLocation(s1);
		    itemstack = new ItemStack(Registry.ITEM.getOrDefault(resourcelocation));
		    }
		    float f = JSONUtils.getFloat(json, "experience", 0.0F);
			return new PotteryKilnRecipe(recipeId, ingredient, itemstack.getItem(), f);
		}

		@Override
		public PotteryKilnRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			Ingredient in=Ingredient.read(buffer);
			Item out=buffer.readItemStack().getItem();
			float exp=buffer.readFloat();
			return new PotteryKilnRecipe(recipeId, in, out, exp);
		}

		@Override
		public void write(PacketBuffer buffer, PotteryKilnRecipe recipe) {
			recipe.input.write(buffer);
			buffer.writeItemStack(new ItemStack(recipe.output));
			buffer.writeFloat(recipe.xp);
			
		}
		
		
		
	}
	
}

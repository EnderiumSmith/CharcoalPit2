package charcoalPit.recipe;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import charcoalPit.CharcoalPit;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class OreKilnRecipe implements IRecipe<IInventory>{
	
	public static final ResourceLocation OREKILN=new ResourceLocation(CharcoalPit.MODID, "orekiln");
	public static final IRecipeType<OreKilnRecipe> ORE_KILN_RECIPE=IRecipeType.register(OREKILN.toString());
	
	public static final Serializer SERIALIZER=new Serializer();

	public final ResourceLocation id;
	public Ingredient[] input;
	public Ingredient output;
	public int amount;
	
	public OreKilnRecipe(ResourceLocation id, Ingredient output, int amount, Ingredient... input) {
		this.id=id;
		this.output=output;
		this.amount=amount;
		this.input=input;
	}
	
	//dynamic///////////
	public boolean isInputEqual(ItemStack in, int slot) {
		if(in.isEmpty())
			return false;
		if(slot>=input.length)
			return false;
		return input[slot].test(in);
	}
	
	public boolean isInputEqual(ItemStack in) {
		if(in.isEmpty())
			return false;
		for(int i=0;i<input.length;i++)
			if(input[i].test(in))
				return true;
		return false;
	}
	
	//static////////////
	public static boolean isValidInput(ItemStack stack, World world) {
		List<OreKilnRecipe> recipes=world.getRecipeManager().getRecipesForType(ORE_KILN_RECIPE);
		for(OreKilnRecipe recipe:recipes)
			if(recipe.isInputEqual(stack)&&!recipe.output.test(new ItemStack(Items.BARRIER)))
				return true;
		return false;
	}
	
	public static int oreKilnGetFuelRequired(IItemHandler inventory) {
		int f=0;
		for(int i=1;i<inventory.getSlots();i++) {
			Iterator<ResourceLocation> tags=inventory.getStackInSlot(i).getItem().getTags().iterator();
			while(tags.hasNext()) {
				ResourceLocation r=tags.next();
				if(r.toString().startsWith("forge:ores/")) {
					f++;
					break;
				}
			}
		}
		return f*4;
	}
	
	public static int oreKilnGetFuelAvailable(IItemHandler inventory) {
		return inventory.getStackInSlot(0).getCount()*(ForgeHooks.getBurnTime(inventory.getStackInSlot(0))/200);
	}
	
	public static boolean oreKilnIsEmpty(IItemHandler inventory) {
		for(int i=1;i<inventory.getSlots();i++)
			if(!inventory.getStackInSlot(i).isEmpty())
				return false;
		return true;
	}
	
	public static int oreKilnGetOreAmount(IItemHandler inventory) {
		int a=0;
		for(int i=1;i<inventory.getSlots();i++) {
			if(!inventory.getStackInSlot(i).isEmpty())
				a+=inventory.getStackInSlot(i).getCount();
		}
		return a;
	}
	
	public static ItemStack OreKilnGetOutput(CompoundNBT nbt, World world) {
		List<OreKilnRecipe> recipes=world.getRecipeManager().getRecipesForType(ORE_KILN_RECIPE);
		ItemStackHandler kiln=new ItemStackHandler();
		for(OreKilnRecipe recipe:recipes) {
			if(recipe.output.hasNoMatchingItems())
				continue;
			kiln.deserializeNBT(nbt);
			int r=0;
			while(!oreKilnIsEmpty(kiln)) {
				boolean b=false;
				for(int i=0;i<recipe.input.length;i++) {
					boolean e=false;
					for(int j=1;j<kiln.getSlots();j++) {
						if(recipe.isInputEqual(kiln.getStackInSlot(j), i)) {
							e=true;
							kiln.extractItem(j, 1, false);
							break;
						}
					}
					if(!e) {
						b=true;
						break;
					}
				}
				if(b) {
					r=0;
					break;
				}else {
					r++;
				}
			}
			if(r>0&&oreKilnIsEmpty(kiln)) {
				ItemStack out=recipe.output.getMatchingStacks()[0].copy();
				out.setCount(r*recipe.amount);
				return out;
			}
		}
		return ItemStack.EMPTY;
	}
	
	////////////////////////////////////////////////////////
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
		return true;
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
		return SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return ORE_KILN_RECIPE;
	}
	//////////////////////////////////////////////
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<OreKilnRecipe>{

		@Override
		public OreKilnRecipe read(ResourceLocation recipeId, JsonObject json) {
	         NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
	         if (nonnulllist.isEmpty()) {
	            throw new JsonParseException("No ingredients for shapeless recipe");
	         } else if (nonnulllist.size() > 8) {
	            throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 8);
	         } else {
	        	Ingredient output=Ingredient.deserialize(JSONUtils.getJsonObject(json, "result"));
	        	int amount=JSONUtils.getInt(json, "amount");
	            return new OreKilnRecipe(recipeId, output, amount, nonnulllist.toArray(new Ingredient[0]));
	         }
		}
		
		private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
	         NonNullList<Ingredient> nonnulllist = NonNullList.create();

	         for(int i = 0; i < ingredientArray.size(); ++i) {
	            Ingredient ingredient = Ingredient.deserialize(ingredientArray.get(i));
	            if (!ingredient.hasNoMatchingItems()) {
	               nonnulllist.add(ingredient);
	            }
	         }

	         return nonnulllist;
	      }

		@Override
		public OreKilnRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int l=buffer.readInt();
			Ingredient[] in=new Ingredient[l];
			for(int i=0;i<l;i++) {
				in[i]=Ingredient.read(buffer);
			}
			int a=buffer.readInt();
			Ingredient o=Ingredient.read(buffer);
			return new OreKilnRecipe(recipeId, o, a, in);
		}

		@Override
		public void write(PacketBuffer buffer, OreKilnRecipe recipe) {
			buffer.writeInt(recipe.input.length);
			for(int i=0;i<recipe.input.length;i++) {
				recipe.input[i].write(buffer);
			}
			buffer.writeInt(recipe.amount);
			recipe.output.write(buffer);
		}
		
	}
	

}

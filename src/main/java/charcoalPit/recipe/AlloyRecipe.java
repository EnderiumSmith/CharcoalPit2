package charcoalPit.recipe;

import java.util.ArrayList;
import java.util.List;

import charcoalPit.core.MethodHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class AlloyRecipe {
	
	public static ArrayList<AlloyRecipe> recipes;
	
	public String[] input;
	public String output;
	public int amount;
	public float xp;
	
	public AlloyRecipe(float xp, int amount, String output, String...input) {
		this.xp=xp;
		this.amount=amount;
		this.output=output;
		this.input=input;
	}
	
	public boolean isInputValid(ItemStack in) {
		if(in.isEmpty())
			return false;
		for(int i=0;i<input.length;i++) {
			if(MethodHelper.isItemInString(input[i], in.getItem()))
				return true;
		}
		return false;
	}
	
	public boolean isInputValid(ItemStack in, int slot) {
		if(in.isEmpty())
			return false;
		if(slot<0&&slot>=input.length)
			return false;
		return MethodHelper.isItemInString(input[slot], in.getItem());
	}
	
	public boolean isValid() {
		return MethodHelper.doesStringHaveItem(output);
	}
	
	public static boolean isValidInput(ItemStack in) {
		if(in.isEmpty())
			return false;
		for(AlloyRecipe recipe:recipes) {
			if(recipe.isValid()&&recipe.isInputValid(in))
				return true;
		}
		return false;
	}
	
	public static boolean oreKilnIsEmpty(IItemHandler inventory) {
		for(int i=1;i<inventory.getSlots();i++)
			if(!inventory.getStackInSlot(i).isEmpty())
				return false;
		return true;
	}
	
	public static ItemStack oreKilnGetOutput(CompoundNBT nbt) {
		ItemStackHandler kiln=new ItemStackHandler();
		for(AlloyRecipe recipe:recipes) {
			if(!MethodHelper.doesStringHaveItem(recipe.output))
				continue;
			kiln.deserializeNBT(nbt);
			int r=0;
			while(oreKilnIsEmpty(kiln)) {
				boolean b=false;
				for(int i=0;i<recipe.input.length;i++) {
					boolean e=false;
					for(int j=1;j<kiln.getSlots();j++) {
						if(recipe.isInputValid(kiln.getStackInSlot(j), 1)) {
							e=true;
							kiln.extractItem(i, 1, false);
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
				ItemStack out=new ItemStack(MethodHelper.getItemForString(recipe.output), recipe.amount);
				return out;
			}
		}
		return ItemStack.EMPTY;
	}
	
}

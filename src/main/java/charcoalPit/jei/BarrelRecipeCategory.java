package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.recipe.BarrelRecipe;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarrelRecipeCategory implements IRecipeCategory<BarrelRecipe> {
	
	public static final ResourceLocation ID=new ResourceLocation(CharcoalPit.MODID,"barrel_recipe");
	public final String loc_name;
	public final IDrawable backgroung;
	public final IDrawable icon;
	public final IDrawable overlay;
	public final IDrawable tank_overlay;
	public BarrelRecipeCategory(IGuiHelper helper){
		loc_name= I18n.format("charcoal_pit.jei.barrel");
		backgroung=helper.createBlankDrawable(175,85);
		icon=helper.createDrawableIngredient(new ItemStack(ModBlockRegistry.Barrel));
		overlay=helper.createDrawable(new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/barrel_recipe.png"),0,0,175,85);
		tank_overlay=helper.createDrawable(new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/barrel_recipe.png"),176,0,16,71-13);
	}
	
	
	@Override
	public ResourceLocation getUid() {
		return ID;
	}
	
	@Override
	public Class<? extends BarrelRecipe> getRecipeClass() {
		return BarrelRecipe.class;
	}
	
	@Override
	public String getTitle() {
		return loc_name;
	}
	
	@Override
	public IDrawable getBackground() {
		return backgroung;
	}
	
	@Override
	public IDrawable getIcon() {
		return icon;
	}
	
	@Override
	public void setIngredients(BarrelRecipe recipe, IIngredients iIngredients) {
		/*ArrayList<ItemStack> list=new ArrayList<>();
		for(ItemStack s:recipe.item_in.getMatchingStacks()){
			list.add(new ItemStack(s.getItem(), recipe.in_amount));
		}
		iIngredients.setInputs(VanillaTypes.ITEM, list);*/
		ArrayList<List<ItemStack>> list2=new ArrayList<>();
		list2.add(Arrays.asList(recipe.item_in.getMatchingStacks()));
		iIngredients.setInputLists(VanillaTypes.ITEM,list2);
		iIngredients.setInput(VanillaTypes.FLUID,new FluidStack(recipe.fluid_in.getFluid(),recipe.fluid_in.amount,recipe.fluid_in.nbt));
		if((recipe.flags&0b100)==0b100){
			iIngredients.setOutput(VanillaTypes.ITEM,new ItemStack(recipe.item_out.getMatchingStacks()[0].getItem(),recipe.out_amount,recipe.nbt_out));
		}
		if((recipe.flags&0b1000)==0b1000){
			iIngredients.setOutput(VanillaTypes.FLUID,new FluidStack(recipe.fluid_out.getFluid(), recipe.fluid_out.amount,recipe.fluid_out.nbt));
		}
	}
	
	@Override
	public void setRecipe(IRecipeLayout iRecipeLayout, BarrelRecipe recipe, IIngredients iIngredients) {
		iRecipeLayout.getItemStacks().init(0,true,79,16);
		iRecipeLayout.getItemStacks().set(0,iIngredients.getInputs(VanillaTypes.ITEM).get(0));
		iRecipeLayout.getFluidStacks().init(1,true,44,14,59-43,71-13,Math.min(16000,recipe.fluid_in.amount*2),false,tank_overlay);
		iRecipeLayout.getFluidStacks().set(1,iIngredients.getInputs(VanillaTypes.FLUID).get(0));
		if((recipe.flags&0b100)==0b100){
			iRecipeLayout.getItemStacks().init(2,false,79,52);
			iRecipeLayout.getItemStacks().set(2,iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
		}
		if((recipe.flags&0b1000)==0b1000){
			iRecipeLayout.getFluidStacks().init(3,false,116,14,59-43,71-13,Math.min(16000,recipe.fluid_out.amount*2),false,tank_overlay);
			iRecipeLayout.getFluidStacks().set(3,iIngredients.getOutputs(VanillaTypes.FLUID).get(0));
		}
	}
	
	@Override
	public void draw(BarrelRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		overlay.draw(matrixStack);
	}
}

package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModBlockRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CharcoalPitRecipeCategory implements IRecipeCategory<CharcoalPitRecipeCategory.CharcoalPitRecipe> {
	
	public static final ResourceLocation ID=new ResourceLocation(CharcoalPit.MODID,"charcoal_recipe");
	public final String loc_name;
	public final IDrawable backgroung;
	public final IDrawable icon;
	public final IDrawable overlay;
	public CharcoalPitRecipeCategory(IGuiHelper helper){
		loc_name= I18n.format("charcoal_pit.jei.charcoal");
		backgroung=helper.createBlankDrawable(175,85);
		icon=helper.createDrawableIngredient(new ItemStack(ModBlockRegistry.LogPile));
		overlay= helper.createDrawable(new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/charcoal_pit.png"),0,0,175,85);
	}
	
	@Override
	public ResourceLocation getUid() {
		return ID;
	}
	
	@Override
	public Class<? extends CharcoalPitRecipe> getRecipeClass() {
		return CharcoalPitRecipe.class;
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
	public void setIngredients(CharcoalPitRecipe recipe, IIngredients iIngredients) {
		iIngredients.setInput(VanillaTypes.ITEM,recipe.input);
		iIngredients.setOutput(VanillaTypes.ITEM,recipe.output);
	}
	
	@Override
	public void setRecipe(IRecipeLayout iRecipeLayout, CharcoalPitRecipe recipe, IIngredients iIngredients) {
		iRecipeLayout.getItemStacks().init(0,true,61,34);
		iRecipeLayout.getItemStacks().set(0,iIngredients.getInputs(VanillaTypes.ITEM).get(0));
		iRecipeLayout.getItemStacks().init(1,false,115,34);
		iRecipeLayout.getItemStacks().set(1,iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
		iRecipeLayout.getItemStacks().init(2,true,61,16);
		iRecipeLayout.getItemStacks().init(3,true,43,34);
		iRecipeLayout.getItemStacks().init(4,true,79,34);
		iRecipeLayout.getItemStacks().init(5,true,61,52);
		if(recipe.coke){
			ArrayList<ItemStack> brick=new ArrayList();
			for(Block i: BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID,"coke_oven_walls")).getAllElements()){
				brick.add(new ItemStack(i));
			}
			iRecipeLayout.getItemStacks().set(2,brick);
			iRecipeLayout.getItemStacks().set(3,brick);
			iRecipeLayout.getItemStacks().set(4,brick);
			iRecipeLayout.getItemStacks().set(5,brick);
		}else{
			iRecipeLayout.getItemStacks().set(2,new ItemStack(Blocks.DIRT));
			iRecipeLayout.getItemStacks().set(3,new ItemStack(Blocks.DIRT));
			iRecipeLayout.getItemStacks().set(4,new ItemStack(Blocks.DIRT));
			iRecipeLayout.getItemStacks().set(5,new ItemStack(Blocks.DIRT));
		}
		
	}
	
	@Override
	public void draw(CharcoalPitRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		overlay.draw(matrixStack);
	}
	
	@Override
	public List<ITextComponent> getTooltipStrings(CharcoalPitRecipe recipe, double mouseX, double mouseY) {
		if(mouseX>=97&&mouseX<115&&mouseY>=34&&mouseY<70)
			if(recipe.coke)
				return Arrays.asList(new TranslationTextComponent("charcoal_pit.instruction.coke_oven"));
			else
				return Arrays.asList(new TranslationTextComponent("charcoal_pit.instruction.charcoal_pit"));
		else
			return Collections.emptyList();
	}
	
	public static class CharcoalPitRecipe{
		ItemStack input,output;
		boolean coke;
		public CharcoalPitRecipe(ItemStack in,ItemStack out,boolean c){
			input=in;
			output=out;
			coke=c;
		}
	}
}

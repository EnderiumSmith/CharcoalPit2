package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.recipe.PotteryKilnRecipe;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PotteryRecipeCategory implements IRecipeCategory<PotteryKilnRecipe> {
	
	public static final ResourceLocation ID=new ResourceLocation(CharcoalPit.MODID,"pottery_recipe");
	public final String loc_name;
	public final IDrawable backgroung;
	public final IDrawable icon;
	public final IDrawable overlay;
	public PotteryRecipeCategory(IGuiHelper helper){
		loc_name= I18n.format("charcoal_pit.jei.pottery");
		backgroung=helper.createBlankDrawable(175,85);
		icon=helper.createDrawableIngredient(new ItemStack(ModItemRegistry.Straw));
		overlay=helper.createDrawable(new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/pottery_kiln.png"),0,0,175,85);
	}
	
	@Override
	public ResourceLocation getUid() {
		return ID;
	}
	
	@Override
	public Class<? extends PotteryKilnRecipe> getRecipeClass() {
		return PotteryKilnRecipe.class;
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
	public void setIngredients(PotteryKilnRecipe recipe, IIngredients iIngredients) {
		ArrayList<List<ItemStack>> list=new ArrayList<>();
		list.add(Arrays.asList(recipe.input.getMatchingStacks()));
		iIngredients.setInputLists(VanillaTypes.ITEM,list);
		//iIngredients.setInputs(VanillaTypes.ITEM, Arrays.asList(recipe.input.getMatchingStacks()));
		iIngredients.setOutput(VanillaTypes.ITEM, new ItemStack(recipe.output));
	}
	
	@Override
	public void setRecipe(IRecipeLayout iRecipeLayout, PotteryKilnRecipe potteryKilnRecipe, IIngredients iIngredients) {
		iRecipeLayout.getItemStacks().init(0,true,25,52);
		iRecipeLayout.getItemStacks().set(0, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
		iRecipeLayout.getItemStacks().init(1,false,133,34);
		iRecipeLayout.getItemStacks().set(1,iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
		iRecipeLayout.getItemStacks().init(2,true,25,34);
		iRecipeLayout.getItemStacks().set(2, new ItemStack(ModItemRegistry.Straw,6));
		iRecipeLayout.getItemStacks().init(3,true,25,16);
		ArrayList<ItemStack> logs=new ArrayList();
		for(Item i:ItemTags.LOGS_THAT_BURN.getAllElements()){
			logs.add(new ItemStack(i,3));
		}
		iRecipeLayout.getItemStacks().set(3,logs);
		iRecipeLayout.getItemStacks().init(4,true,61,34);
		iRecipeLayout.getItemStacks().init(5,true,97,34);
		iRecipeLayout.getItemStacks().init(6,true,79,52);
		iRecipeLayout.getItemStacks().set(4,new ItemStack(Items.DIRT));
		iRecipeLayout.getItemStacks().set(5,new ItemStack(Items.DIRT));
		iRecipeLayout.getItemStacks().set(6,new ItemStack(Items.DIRT));
		
	}
	
	@Override
	public void draw(PotteryKilnRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		overlay.draw(matrixStack);
	}
	
	@Override
	public List<ITextComponent> getTooltipStrings(PotteryKilnRecipe recipe, double mouseX, double mouseY) {
		if(mouseX>=79&&mouseX<97&&mouseY>=34&&mouseY<52)
			return Arrays.asList(new TranslationTextComponent("charcoal_pit.instruction.place_kiln"));
		else
			return Collections.emptyList();
	}
	
	/*@Override
	public boolean handleClick(PotteryKilnRecipe recipe, double mouseX, double mouseY, int mouseButton) {
		return false;
	}
	
	@Override
	public boolean isHandled(PotteryKilnRecipe recipe) {
		return false;
	}*/
}

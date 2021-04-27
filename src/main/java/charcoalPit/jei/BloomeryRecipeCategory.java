package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.recipe.BloomeryRecipe;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BloomeryRecipeCategory implements IRecipeCategory<BloomeryRecipe> {
	
	public static final ResourceLocation ID=new ResourceLocation(CharcoalPit.MODID,"bloomery_recipe");
	public final String loc_name;
	public final IDrawable backgroung;
	public final IDrawable icon;
	public final IDrawable overlay;
	public BloomeryRecipeCategory(IGuiHelper helper){
		loc_name=I18n.format("charcoal_pit.jei.bloomery");
		backgroung=helper.createBlankDrawable(175,103);
		icon=helper.createDrawableIngredient(new ItemStack(ModBlockRegistry.Bellows));
		overlay= helper.createDrawable(new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/bloomery.png"),0,0,175,103);
	}
	
	
	@Override
	public ResourceLocation getUid() {
		return ID;
	}
	
	@Override
	public Class<? extends BloomeryRecipe> getRecipeClass() {
		return BloomeryRecipe.class;
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
	public void setIngredients(BloomeryRecipe recipe, IIngredients iIngredients) {
		ArrayList<List<ItemStack>> list=new ArrayList<>();
		list.add(Arrays.asList(recipe.input.getMatchingStacks()));
		iIngredients.setInputLists(VanillaTypes.ITEM,list);
		//iIngredients.setInputs(VanillaTypes.ITEM, Arrays.asList(recipe.input.getMatchingStacks()));
		iIngredients.setOutput(VanillaTypes.ITEM,recipe.output.getMatchingStacks()[0]);
	}
	
	@Override
	public void setRecipe(IRecipeLayout iRecipeLayout, BloomeryRecipe bloomeryRecipe, IIngredients iIngredients) {
		iRecipeLayout.getItemStacks().init(0,true,25,34);
		iRecipeLayout.getItemStacks().set(0,iIngredients.getInputs(VanillaTypes.ITEM).get(0));
		iRecipeLayout.getItemStacks().init(1,false, 133,34);
		iRecipeLayout.getItemStacks().set(1,iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
		ArrayList<ItemStack> coal=new ArrayList();
		for(Item i: ItemTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID,"basic_fuels")).getAllElements()){
			coal.add(new ItemStack(i));
		}
		iRecipeLayout.getItemStacks().init(2,true,25,16);
		iRecipeLayout.getItemStacks().set(2,coal);
		ArrayList<ItemStack> brick=new ArrayList();
		for(Block i: BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID,"bloomery_walls")).getAllElements()){
			brick.add(new ItemStack(i));
		}
		iRecipeLayout.getItemStacks().init(3,true,61,34);
		iRecipeLayout.getItemStacks().init(4,true,97,34);
		iRecipeLayout.getItemStacks().init(5,true,79,70);
		iRecipeLayout.getItemStacks().set(3,brick);
		iRecipeLayout.getItemStacks().set(4,brick);
		iRecipeLayout.getItemStacks().set(5,brick);
		iRecipeLayout.getItemStacks().init(6,true,43,52);
		iRecipeLayout.getItemStacks().init(7,true,115,52);
		iRecipeLayout.getItemStacks().set(6,new ItemStack(ModBlockRegistry.Bellows));
		iRecipeLayout.getItemStacks().set(7,new ItemStack(ModBlockRegistry.Bellows));
		iRecipeLayout.getItemStacks().init(8,true,61,52);
		iRecipeLayout.getItemStacks().init(9,true,97,52);
		ArrayList<ItemStack> tuyere=new ArrayList();
		for(Block i: BlockTags.getCollection().get(new ResourceLocation(CharcoalPit.MODID,"tuyere_blocks")).getAllElements()){
			tuyere.add(new ItemStack(i));
		}
		iRecipeLayout.getItemStacks().set(8,tuyere);
		iRecipeLayout.getItemStacks().set(9,tuyere);
	}
	
	@Override
	public void draw(BloomeryRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		overlay.draw(matrixStack);
	}
	
	@Override
	public List<ITextComponent> getTooltipStrings(BloomeryRecipe recipe, double mouseX, double mouseY) {
		if(mouseX>=79&&mouseX<97&&mouseY>=34&&mouseY<70)
			return Arrays.asList(new TranslationTextComponent("charcoal_pit.instruction.place_kiln"));
		else
			return Collections.emptyList();
	}
}

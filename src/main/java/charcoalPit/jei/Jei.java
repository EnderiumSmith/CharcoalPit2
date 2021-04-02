package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.gui.BarrelScreen;
import charcoalPit.recipe.BarrelRecipe;
import charcoalPit.recipe.BloomeryRecipe;
import charcoalPit.recipe.OreKilnRecipe;
import charcoalPit.recipe.PotteryKilnRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class Jei implements IModPlugin {
	
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(CharcoalPit.MODID,"charcoal_pit");
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.useNbtForSubtypes(ModItemRegistry.AlcoholBottle);
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
	
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new PotteryRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new OreKilnRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new BloomeryRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new CharcoalPitRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new BarrelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
	
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(PotteryKilnRecipe.POTTERY_RECIPE),PotteryRecipeCategory.ID);
		registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(OreKilnRecipe.ORE_KILN_RECIPE).stream().filter((r)->!r.output.test(new ItemStack(Items.BARRIER))).collect(Collectors.toList()),
				OreKilnRecipeCategory.ID);
		registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(BloomeryRecipe.BLOOMERY_RECIPE),BloomeryRecipeCategory.ID);
		ArrayList<CharcoalPitRecipeCategory.CharcoalPitRecipe> recipes=new ArrayList<>();
		recipes.add(new CharcoalPitRecipeCategory.CharcoalPitRecipe(new ItemStack(ModBlockRegistry.LogPile),new ItemStack(Items.CHARCOAL,9),false));
		recipes.add(new CharcoalPitRecipeCategory.CharcoalPitRecipe(new ItemStack(Blocks.COAL_BLOCK),new ItemStack(ModItemRegistry.Coke,9),true));
		registration.addRecipes(recipes,CharcoalPitRecipeCategory.ID);
		registration.addIngredientInfo(new ItemStack(ModItemRegistry.Straw), VanillaTypes.ITEM, I18n.format("charcoal_pit.instruction.straw"));
		registration.addIngredientInfo(Arrays.asList(new ItemStack(Items.CHARCOAL),new ItemStack(ModItemRegistry.Coke),new ItemStack(ModBlockRegistry.LogPile),new ItemStack(Blocks.COAL_BLOCK)),
				VanillaTypes.ITEM,I18n.format("charcoal_pit.instruction.build_pit"));
		registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(BarrelRecipe.BARREL_RECIPE),BarrelRecipeCategory.ID);
	}
	
	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
	
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.Straw),PotteryRecipeCategory.ID);
		registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.ClayPot),OreKilnRecipeCategory.ID);
		registration.addRecipeCatalyst(new ItemStack(ModBlockRegistry.Bellows),BloomeryRecipeCategory.ID);
		registration.addRecipeCatalyst(new ItemStack(ModBlockRegistry.LogPile),CharcoalPitRecipeCategory.ID);
		registration.addRecipeCatalyst(new ItemStack(Blocks.COAL_BLOCK),CharcoalPitRecipeCategory.ID);
		registration.addRecipeCatalyst(new ItemStack(ModBlockRegistry.Barrel),BarrelRecipeCategory.ID);
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(BarrelScreen.class,98,35,16,16,BarrelRecipeCategory.ID);
	}
	
	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {
	
	}
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	
	}
}

package charcoalPit.item;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.potion.ModPotionRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemAlcoholBottle extends PotionItem{

	public ItemAlcoholBottle() {
		super(new Item.Properties().group(ModItemRegistry.CHARCOAL_PIT_FOODS).maxStackSize(16).containerItem(Items.GLASS_BOTTLE));
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity)entityLiving : null;
	      if (playerentity instanceof ServerPlayerEntity) {
	         CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)playerentity, stack);
	      }

	      if (!worldIn.isRemote) {
	         for(EffectInstance effectinstance : PotionUtils.getEffectsFromStack(stack)) {
	            if (effectinstance.getPotion().isInstant()) {
	               effectinstance.getPotion().affectEntity(playerentity, playerentity, entityLiving, effectinstance.getAmplifier(), 1.0D);
	            } else {
	            	if(effectinstance.getPotion()==ModPotionRegistry.DRUNK){
	            		EffectInstance effect2=entityLiving.getActivePotionEffect(ModPotionRegistry.DRUNK);
	            		int l=0;
	            		if(effect2!=null) {
							l = effect2.getAmplifier() + 1 + effectinstance.getAmplifier();
							entityLiving.addPotionEffect(new EffectInstance(ModPotionRegistry.DRUNK, effect2.getDuration(), l));
						}else
							entityLiving.addPotionEffect(new EffectInstance(effectinstance));
					}else {
						entityLiving.addPotionEffect(new EffectInstance(effectinstance));
					}
	            }
	         }
	      }

	      if (playerentity != null) {
	         playerentity.addStat(Stats.ITEM_USED.get(this));
	         if (!playerentity.abilities.isCreativeMode) {
	            stack.shrink(1);
	         }
	      }

	      if (playerentity == null || !playerentity.abilities.isCreativeMode) {
	         if (stack.isEmpty()) {
	            return new ItemStack(Items.GLASS_BOTTLE);
	         }

	         if (playerentity != null) {
	            playerentity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
	         }
	      }

	      return stack;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.isEnchanted();
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(group==ModItemRegistry.CHARCOAL_PIT_FOODS) {

			if(!didInit)
				this.initItems();

			items.add(cider);
			items.add(golden_cider);
			items.add(chorus_cider);
			items.add(vodka);
			items.add(beetroot_beer);
			items.add(beer);
			items.add(sweetberry_wine);
			items.add(warped_wine);
			items.add(mead);
			items.add(rum);
			items.add(honey_dewois);
			items.add(spider_spirit);
		}
	}

	public static boolean didInit=false;

	public void initItems()
	{
		didInit=true;
		cider=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.CIDER);
		golden_cider=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.GOLDEN_CIDER);
		vodka=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.VODKA);
		beetroot_beer=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.BEETROOT_BEER);
		sweetberry_wine=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.SWEETBERRY_WINE);
		mead=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.MEAD);
		beer=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.BEER);
		rum=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.RUM);
		chorus_cider=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.CHORUS_CIDER);
		spider_spirit=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.SPIDER_SPIRIT);
		honey_dewois=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.HONEY_DEWOIS);
		warped_wine=PotionUtils.addPotionToItemStack(new ItemStack(this), ModPotionRegistry.WARPED_WINE);

		cider.getTag().putInt("CustomPotionColor", 0xE50000);
		golden_cider.getTag().putInt("CustomPotionColor", 0xDBB40C);
		vodka.getTag().putInt("CustomPotionColor", 0xE6DAA6);
		beetroot_beer.getTag().putInt("CustomPotionColor", 0x840000);
		sweetberry_wine.getTag().putInt("CustomPotionColor", 0x06470C);
		mead.getTag().putInt("CustomPotionColor", 0xFAC205);
		beer.getTag().putInt("CustomPotionColor", 0xFDAA48);
		rum.getTag().putInt("CustomPotionColor", 0x650021);
		chorus_cider.getTag().putInt("CustomPotionColor", 0x9A0EAA);
		honey_dewois.getTag().putInt("CustomPotionColor", 0xF97306);
		warped_wine.getTag().putInt("CustomPotionColor", 0x0485D1);
		spider_spirit.getTag().putInt("CustomPotionColor", 0xA5A502);
	}

	public static ItemStack cider;
	public static ItemStack golden_cider;
	public static ItemStack vodka;
	public static ItemStack beetroot_beer;
	public static ItemStack sweetberry_wine;
	public static ItemStack mead;
	public static ItemStack beer;
	public static ItemStack rum;
	public static ItemStack chorus_cider;
	public static ItemStack spider_spirit;
	public static ItemStack honey_dewois;
	public static ItemStack warped_wine;

}

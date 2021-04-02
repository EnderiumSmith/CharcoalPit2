package charcoalPit.potion;

import charcoalPit.CharcoalPit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.potion.*;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber(modid = CharcoalPit.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModPotionRegistry {

    public static Effect DRUNK=new Effect(EffectType.HARMFUL,0xFFFFFF){
        @Override
        public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
            amplifier-=3;
            if(amplifier>=1){
                entityLivingBaseIn.addPotionEffect(new EffectInstance(Effects.NAUSEA, 20*5));
            }
            if(amplifier>=2){
                entityLivingBaseIn.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20*5));
                entityLivingBaseIn.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 20*5));
                entityLivingBaseIn.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 20*5));
            }
            if(amplifier>=3){
                entityLivingBaseIn.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 20*5));
                entityLivingBaseIn.addPotionEffect(new EffectInstance(Effects.POISON, 20*5));
            }
            if(amplifier>=4){
                entityLivingBaseIn.attackEntityFrom(DamageSource.GENERIC, (amplifier-3)*2);
            }
        }

        @Override
        public boolean isReady(int duration, int amplifier) {
            int j = 25 >> amplifier;
            if (j > 0) {
                return duration % j == 0;
            } else {
                return true;
            }
        }

        @Override
        public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AttributeModifierManager attributeMapIn, int amplifier) {
            super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
            if(amplifier>0)
                entityLivingBaseIn.addPotionEffect(new EffectInstance(DRUNK, 20*60*5, amplifier-1));
        }
    };
    public static Effect ROULETTE=new Effect(EffectType.NEUTRAL, 0x00FF00){
        @Override
        public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
            Random random=new Random();
            Potion potion=ForgeRegistries.POTION_TYPES.getValues().toArray(new Potion[]{})[random.nextInt(ForgeRegistries.POTION_TYPES.getValues().size())];
            for(EffectInstance effect:potion.getEffects()){
                if(effect.getPotion().isInstant()){
                    effect.getPotion().affectEntity(source, indirectSource ,entityLivingBaseIn, effect.getAmplifier(), health);
                }else{
                    entityLivingBaseIn.addPotionEffect(new EffectInstance(effect));
                }
            }
        }

        @Override
        public boolean isInstant() {
            return true;
        }
    };
    public static Effect POISON_RESISTANCE=new Effect(EffectType.BENEFICIAL, 0xFFFF00){
        @Override
        public boolean isReady(int duration, int amplifier) {
            int j = 25 >> amplifier;
            if (j > 0) {
                return duration % j == 0;
            } else {
                return true;
            }
        }

        @Override
        public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
            entityLivingBaseIn.removePotionEffect(Effects.POISON);
        }
    };
    public static Effect WITHER_RESISTANCE=new Effect(EffectType.BENEFICIAL, 0xFFFFFF){
        @Override
        public boolean isReady(int duration, int amplifier) {
            int j = 25 >> amplifier;
            if (j > 0) {
                return duration % j == 0;
            } else {
                return true;
            }
        }
        @Override
        public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
            entityLivingBaseIn.removePotionEffect(Effects.WITHER);
        }
    };
    public static Potion CIDER=new Potion("cider",
            new EffectInstance(DRUNK,20*60*5),
            new EffectInstance(Effects.REGENERATION,20*45));
    public static Potion GOLDEN_CIDER=new Potion("golden_cider",
            new EffectInstance(DRUNK,20*60*5),
            new EffectInstance(Effects.ABSORPTION,20*60*8,4));
    public static Potion VODKA=new Potion("vodka",
            new EffectInstance(DRUNK, 20*60*5),
            new EffectInstance(Effects.RESISTANCE,20*60*3));
    public static Potion BEETROOT_BEER=new Potion("beetroot_beer",
            new EffectInstance(DRUNK,20*60*5),
            new EffectInstance(Effects.STRENGTH,20*60*3));
    public static Potion SWEETBERRY_WINE=new Potion("sweetberry_wine",
            new EffectInstance(DRUNK, 20*60*5),
            new EffectInstance(Effects.JUMP_BOOST,20*60*3,1));
    public static Potion MEAD=new Potion("mead",
            new EffectInstance(DRUNK,20*60*5),
            new EffectInstance(POISON_RESISTANCE,20*60*3));
    public static Potion BEER=new Potion("beer",
            new EffectInstance(DRUNK,20*60*5),
            new EffectInstance(Effects.SATURATION,1,5));
    public static Potion RUM=new Potion("rum",
            new EffectInstance(DRUNK,20*60*5),
            new EffectInstance(Effects.SPEED,20*60*3));
    public static Potion CHORUS_CIDER=new Potion("chorus_cider",
            new EffectInstance(DRUNK,20*60*5),
            new EffectInstance(Effects.LEVITATION,20*20),
            new EffectInstance(Effects.SLOW_FALLING,20*30));
    public static Potion SPIDER_SPIRIT=new Potion("spider_spirit",
            new EffectInstance(DRUNK,20*60*5),
            new EffectInstance(ROULETTE));
    public static Potion HONEY_DEWOIS=new Potion("honey_dewois",
            new EffectInstance(DRUNK,20*60*5),
            new EffectInstance(Effects.HASTE,20*60*3));
    public static Potion WARPED_WINE=new Potion("warped_wine",
            new EffectInstance(DRUNK,20*60*5),
            new EffectInstance(WITHER_RESISTANCE,20*60*3));


    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Effect> event){
        event.getRegistry().registerAll(DRUNK.setRegistryName("drunk"),ROULETTE.setRegistryName("roulette"),POISON_RESISTANCE.setRegistryName("poison_resistance"),
                WITHER_RESISTANCE.setRegistryName("wither_resistance"));
    }
    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event){
        event.getRegistry().registerAll(CIDER.setRegistryName("cider"),GOLDEN_CIDER.setRegistryName("golden_cider"),VODKA.setRegistryName("vodka"),
                BEETROOT_BEER.setRegistryName("beetroot_beer"),SWEETBERRY_WINE.setRegistryName("sweetberry_wine"),MEAD.setRegistryName("mead"),
                BEER.setRegistryName("beer"),RUM.setRegistryName("rum"),CHORUS_CIDER.setRegistryName("chorus_cider"),SPIDER_SPIRIT.setRegistryName("spider_spirit"),
                HONEY_DEWOIS.setRegistryName("honey_dewois"),WARPED_WINE.setRegistryName("warped_wine"));
    }

}

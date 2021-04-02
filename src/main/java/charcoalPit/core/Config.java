package charcoalPit.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	
	public static ForgeConfigSpec CONFIG;
	
	public static ForgeConfigSpec.IntValue CharcoalTime,CokeTime;
	public static ForgeConfigSpec.IntValue CharcoalCreosote,CokeCreosote;
	
	public static ForgeConfigSpec.BooleanValue RainyPottery;
	public static ForgeConfigSpec.IntValue PotteryTime;
	public static ForgeConfigSpec.IntValue StrawAmount,WoodAmount;
	
	public static ForgeConfigSpec.IntValue BloomeryTime;
	public static ForgeConfigSpec.IntValue BloomCooldown;
	
	public static ForgeConfigSpec.BooleanValue EnableBloomeryPickRequirement;
	
	static {
		ForgeConfigSpec.Builder builder=new ForgeConfigSpec.Builder();
		charcoalPitConfig(builder);
		potteryKilnConfig(builder);
		bloomeryConfig(builder);
		CONFIG=builder.build();
	}
	
	public static void charcoalPitConfig(ForgeConfigSpec.Builder builder) {
		builder.push("CharcoalPit/CokeOven");
		CharcoalTime=builder.comment("The time charcoal pits take to finish. 1000 Ticks = 1 MC hour").defineInRange("CharcoalTime", 18000, 1000, Integer.MAX_VALUE);
		CokeTime=builder.comment("The time coke ovens take to finish. 1000 Ticks = 1 MC hour").defineInRange("CokeTime", 36000, 1000, Integer.MAX_VALUE);
		CharcoalCreosote=builder.comment("Amount of creosote oil in mB produced per log").defineInRange("CharcoalCreosote", 50, 0, 1000);
		CokeCreosote=builder.comment("Amount of creosote oil in mB produced per coal").defineInRange("CokeCreosote", 100, 0, 1000);
		builder.pop();
	}
	
	public static void potteryKilnConfig(ForgeConfigSpec.Builder builder) {
		builder.push("Pottery Kiln");
		RainyPottery=builder.comment("If rain should put out kilns").define("RainyPottery", true);
		PotteryTime=builder.comment("The time kilns take to finish. 1000 Ticks = 1 MC hour").defineInRange("PotteryTime", 8000, 1000, Integer.MAX_VALUE);
		StrawAmount=builder.comment("The amount of straw needed for the kiln").defineInRange("StrawAmount", 6, 1, 64);
		WoodAmount=builder.comment("The amount of logs needed for the kiln").defineInRange("WoodAmount", 3, 1, 64);
		builder.pop();
	}
	
	public static void bloomeryConfig(ForgeConfigSpec.Builder builder) {
		builder.push("Bloomery");
		BloomeryTime=builder.comment("The minimum time the bloomery takes to finish. 1000 Ticks = 1 MC hour").defineInRange("BloomeryTime", 4000, 1000, Integer.MAX_VALUE);
		BloomCooldown=builder.comment("The time blooms take to cool down").defineInRange("BloomCooldown", 2000, 1000, Integer.MAX_VALUE);
		EnableBloomeryPickRequirement=builder.comment("If the bloomery should require a higher tier pickaxe to mine. Disabled by default as none exist in vanilla before iron")
				.define("EnableBloomeryPickRequirement", false);
		builder.pop();
	}
	
}

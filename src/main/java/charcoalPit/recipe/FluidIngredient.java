package charcoalPit.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidIngredient {
	
	public Fluid fluid;
	public ITag<Fluid> tag;
	public int amount;
	public CompoundNBT nbt;
	
	public boolean test(Fluid in) {
		if(fluid!=null&&fluid==in)
			return true;
		if(tag!=null&&tag.contains(in))
			return true;
		return false;
	}
	
	public Fluid getFluid() {
		if(fluid!=null&&fluid!=Fluids.EMPTY)
			return fluid;
		if(tag!=null&&!tag.getAllElements().isEmpty())
			return tag.getAllElements().get(0);
		return Fluids.EMPTY;
	}
	
	public boolean isEmpty() {
		return getFluid()==Fluids.EMPTY;
	}
	
	public static FluidIngredient readJson(JsonObject json){
		FluidIngredient ingredient=new FluidIngredient();
		if(json.has("fluid")) {
			ResourceLocation f=new ResourceLocation(JSONUtils.getString(json, "fluid"));
			Fluid fluid=ForgeRegistries.FLUIDS.getValue(f);
			if(fluid!=null&&fluid!=Fluids.EMPTY)
				ingredient.fluid=fluid;
		}
		if(json.has("tag")) {
			ResourceLocation t=new ResourceLocation(JSONUtils.getString(json, "tag"));
			ITag<Fluid> tag=TagCollectionManager.getManager().getFluidTags().get(t);
			if(tag!=null&&!tag.getAllElements().isEmpty())
				ingredient.tag=tag;
		}
		if(ingredient.isEmpty())
			throw new JsonParseException("invalid fluid");
		if(json.has("amount")) {
			ingredient.amount=JSONUtils.getInt(json, "amount");
		}
		if(json.has("nbt")) {
			try {
				ingredient.nbt=JsonToNBT.getTagFromJson(JSONUtils.getString(json, "nbt"));
			} catch (CommandSyntaxException e) {
				throw new JsonParseException(e);
			}
		}
		return ingredient;
	}
	
	public void writeBuffer(PacketBuffer buffer) {
		int mode=0;
		if(fluid!=null)
			mode+=1;
		if(tag!=null)
			mode+=2;
		buffer.writeByte(mode);
		if((mode&1)==1) {
			buffer.writeResourceLocation(fluid.getRegistryName());
		}
		if((mode&2)==2) {
			buffer.writeResourceLocation(TagCollectionManager.getManager().getFluidTags().getValidatedIdFromTag(tag));
		}
		buffer.writeInt(amount);
		if(nbt!=null) {
			buffer.writeBoolean(true);
			buffer.writeCompoundTag(nbt);
		}else {
			buffer.writeBoolean(false);
		}
	}
	
	public static FluidIngredient readBuffer(PacketBuffer buffer) {
		FluidIngredient ingredient=new FluidIngredient();
		int mode=buffer.readByte();
		if((mode&1)==1) {
			ingredient.fluid=ForgeRegistries.FLUIDS.getValue(buffer.readResourceLocation());
		}
		if((mode&2)==2) {
			ingredient.tag=TagCollectionManager.getManager().getFluidTags().get(buffer.readResourceLocation());
		}
		ingredient.amount=buffer.readInt();
		if(buffer.readBoolean()) {
			ingredient.nbt=buffer.readCompoundTag();
		}
		return ingredient;
	}
	
	
}

package charcoalPit.block;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.IItemProvider;

public class BlockLeeks extends CropsBlock {
	
	public BlockLeeks(Properties builder) {
		super(builder);
	}
	
	@Override
	protected IItemProvider getSeedsItem() {
		return ModItemRegistry.Leek;
	}
}

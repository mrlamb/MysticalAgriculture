package com.blakebr0.mysticalagriculture.jei;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.mysticalagriculture.blocks.ModBlocks;
import com.blakebr0.mysticalagriculture.config.ModConfig;
import com.blakebr0.mysticalagriculture.crafting.TinkeringTableManager;
import com.blakebr0.mysticalagriculture.gui.ContainerTinkeringTable;
import com.blakebr0.mysticalagriculture.gui.GuiSeedReprocessor;
import com.blakebr0.mysticalagriculture.gui.GuiTinkeringTable;
import com.blakebr0.mysticalagriculture.lib.EssenceType;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class CompatJEI extends BlankModPlugin {

	public static CompatJEI instance;
    private IJeiRuntime runtime;
	
	public static List<Block> blocks = new ArrayList<Block>();
	public static List<Item> items = new ArrayList<Item>();
	
    @Override
    public void register(IModRegistry registry) {
        instance = this;
        
        for(Block block : blocks){
        	registry.addDescription(new ItemStack(block), "desc." + block.getUnlocalizedName());
        }
        
        for(Item item : items){
        	registry.addDescription(new ItemStack(item), "desc." + item.getUnlocalizedName());
        }
        
        if(ModConfig.confSeedReprocessor){
	        registry.addRecipeCategories(new ReprocessorCategory(registry.getJeiHelpers().getGuiHelper()));
	        registry.addRecipeHandlers(new ReprocessorHandler());
	        registry.addRecipes(ReprocessorRecipeMaker.getRecipes());
	        registry.addRecipeClickArea(GuiSeedReprocessor.class, 79, 26, 24, 17, ReprocessorCategory.UID);
	        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockSeedReprocessor, 1, 0), ReprocessorCategory.UID);
        }
        
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registry.addRecipeCategories(new TinkeringTableCategory(guiHelper));
        registry.addRecipeHandlers(new TinkeringTableHandler(jeiHelpers));
        registry.addRecipes(TinkeringTableManager.getInstance().getRecipeList());
        registry.addRecipeClickArea(GuiTinkeringTable.class, 109, 50, 22, 15, TinkeringTableCategory.UID);
        IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();
        transferRegistry.addRecipeTransferHandler(ContainerTinkeringTable.class, TinkeringTableCategory.UID, 1, 9, 10, 36);
        for(EssenceType.Type type : EssenceType.Type.values()){
            registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockTinkeringTable, 1, type.getMetadata()), TinkeringTableCategory.UID);
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        this.runtime = runtime;
    }

    public IJeiRuntime getRuntime() {
        return runtime;
    }
}

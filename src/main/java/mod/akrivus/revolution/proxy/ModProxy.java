package mod.akrivus.revolution.proxy;

import mod.akrivus.revolution.Revolution;
import mod.akrivus.revolution.world.WorldGenTribes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModProxy {
	public void serverStarting(final FMLServerStartingEvent event) {
		
	}
	public void serverStopped(final FMLServerStoppedEvent event) {
		
	}
	public void preInit(final FMLPreInitializationEvent event) {
		
	}
	public void init(final FMLInitializationEvent event) {
		FurnaceRecipes.instance().addSmelting(Revolution.MAN_MEAT, new ItemStack(Revolution.COOKED_MAN_MEAT), 0.35F);
		GameRegistry.registerWorldGenerator(new WorldGenTribes(), 5);
		MinecraftForge.EVENT_BUS.register(new Revolution.Events());
	}
	public void postInit(final FMLPostInitializationEvent event) {
		
	}
}

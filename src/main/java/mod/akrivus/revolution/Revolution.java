package mod.akrivus.revolution;

import mod.akrivus.revolution.entity.EntityFemale;
import mod.akrivus.revolution.entity.EntityMale;
import mod.akrivus.revolution.proxy.ModProxy;
import mod.akrivus.revolution.world.WorldGenTribes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Revolution.MODID, name = Revolution.NAME, version = Revolution.VERSION, acceptedMinecraftVersions = Revolution.MCVER)
public class Revolution {
	public static final String MODID = "revolution";
	public static final String NAME = "Revolution";
	public static final String VERSION = "@version";
	public static final String MCVER = "@mcversion";
	
	@Instance public static Revolution instance;
	@SidedProxy(clientSide = "mod.akrivus.revolution.proxy.ClientProxy", serverSide = "mod.akrivus.revolution.proxy.ServerProxy")
	public static ModProxy proxy;
	
	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		Revolution.proxy.preInit(event);
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		GameRegistry.registerWorldGenerator(new WorldGenTribes(), 5);
		Revolution.proxy.init(event);
	}

	@EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		Revolution.proxy.postInit(event);
	}

	@EventHandler
	public void serverStarting(final FMLServerStartingEvent event) {
		Revolution.proxy.serverStarting(event);
	}

	@EventHandler
	public void serverStopped(final FMLServerStoppedEvent event) {
		Revolution.proxy.serverStopped(event);
	}
	
	@Mod.EventBusSubscriber(modid = Revolution.MODID)
	public static class Registry {
		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
			event.getRegistry().register(EntityEntryBuilder.create().id("revolution:female", 0).name("female").entity(EntityFemale.class).tracker(256, 20, true).egg(0xF3DBBF, 0x89BD8A).build());
			event.getRegistry().register(EntityEntryBuilder.create().id("revolution:male", 1).name("male").entity(EntityMale.class).tracker(256, 20, true).egg(0x996C59, 0x008E8F).build());
		}
	}
}

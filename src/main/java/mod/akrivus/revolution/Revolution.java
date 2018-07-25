package mod.akrivus.revolution;

import mod.akrivus.revolution.entity.EntityFemale;
import mod.akrivus.revolution.entity.EntityHuman;
import mod.akrivus.revolution.entity.EntityMale;
import mod.akrivus.revolution.item.ItemHumanFlesh;
import mod.akrivus.revolution.item.ItemSpawnWand;
import mod.akrivus.revolution.proxy.ModProxy;
import mod.akrivus.revolution.world.WorldGenTribes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
	
	public static final ItemHumanFlesh HUMAN_FLESH_ITEM = new ItemHumanFlesh();
	public static final ItemSpawnWand SPAWN_WAND_ITEM = new ItemSpawnWand();
	
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
		MinecraftForge.EVENT_BUS.register(new Revolution.Events());
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
			event.getRegistry().register(EntityEntryBuilder.create().id("revolution:female", 0).name("female").entity(EntityFemale.class).tracker(256, 20, true).build());
			event.getRegistry().register(EntityEntryBuilder.create().id("revolution:male", 1).name("male").entity(EntityMale.class).tracker(256, 20, true).build());
		}
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			event.getRegistry().register(HUMAN_FLESH_ITEM);
			event.getRegistry().register(SPAWN_WAND_ITEM);
		}
	}
	public static class Events {
		@SubscribeEvent
		public void onEntitySpawn(EntityJoinWorldEvent e) {
			if (e.getEntity() instanceof EntityMob) {
				EntityMob mob = (EntityMob)(e.getEntity());
				if (!(mob instanceof EntityEnderman || mob instanceof EntityHuman)) {
					mob.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityHuman>(mob, EntityHuman.class, true, true));
				}
			}
		}
	}
}

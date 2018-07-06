package mod.akrivus.revolution.data;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class LearnedData extends WorldSavedData {
	public final Map<UUID, Memory> memories = new LinkedHashMap<UUID, Memory>();
	public static LearnedData get(World world) {
		if (!world.isRemote) {
			MapStorage storage = world.getPerWorldStorage();
			LearnedData instance = (LearnedData) storage.getOrLoadData(LearnedData.class, "memories");
			if (instance == null) {
				instance = new LearnedData();
				storage.setData("memories", instance);
			}
			return instance;
		}
		return null;
	}
	public LearnedData() {
		super("memories");
	}
	public LearnedData(String id) {
		super(id);
		this.memories.put(UUID.randomUUID(), new Memory(true, "EntityCow"));
		this.memories.put(UUID.randomUUID(), new Memory(true, "EntityChicken"));
		this.memories.put(UUID.randomUUID(), new Memory(true, "EntitySheep"));
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		Iterator<Entry<UUID, Memory>> it = this.memories.entrySet().iterator();
		while (it.hasNext()) {
			NBTTagCompound nbt = new NBTTagCompound();
			Entry<UUID, Memory> pair = it.next();
			nbt.setUniqueId("id", pair.getKey());
			nbt.setBoolean("destroy", pair.getValue().destroy);
			if (pair.getValue().destroy) {
				nbt.setString("item", pair.getValue().item);
			}
			else {
				nbt.setBoolean("avoid", pair.getValue().avoid);
				nbt.setBoolean("isPos", pair.getValue().isPos);
				if (pair.getValue().isPos) {
					nbt.setDouble("x", pair.getValue().location.getX());
					nbt.setDouble("y", pair.getValue().location.getY());
					nbt.setDouble("z", pair.getValue().location.getZ());
				}
				else {
					nbt.setUniqueId("entity", pair.getValue().entity);
				}
			}
			list.appendTag(nbt);
		}
		compound.setTag("memories", list);
		return compound;
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = compound.getTagList("memories", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			UUID id = UUID.fromString(nbt.getString("id"));
			boolean destroy = nbt.getBoolean("destroy");
			if (destroy) {
				this.memories.put(id, new Memory(destroy, nbt.getString("item")));
			}
			else {
				boolean avoid = nbt.getBoolean("avoid");
				boolean isPos = nbt.getBoolean("isPos");
				if (isPos) {
					this.memories.put(id, new Memory(avoid, new BlockPos(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"))));
				}
				else {
					this.memories.put(id, new Memory(avoid, nbt.getUniqueId("entity")));
				}
			}
		}
	}
	public void addMemory(Memory memory) {
		this.memories.put(UUID.randomUUID(), memory);
		this.markDirty();
	}
}

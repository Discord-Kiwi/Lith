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
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		Iterator<Entry<UUID, Memory>> it = this.memories.entrySet().iterator();
		while (it.hasNext()) {
			NBTTagCompound nbt = new NBTTagCompound();
			Entry<UUID, Memory> pair = it.next();
			nbt.setString("id", pair.getKey().toString());
			nbt.setString("type", pair.getValue().getType());
			nbt.setString("data", pair.getValue().getData());
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
			this.memories.put(id, new Memory(nbt.getString("type"), nbt.getString("data")));
		}
	}
	public UUID addMemory(Memory memory) {
		UUID id = UUID.randomUUID();
		this.memories.put(id, memory);
		this.markDirty();
		return id;
	}
}

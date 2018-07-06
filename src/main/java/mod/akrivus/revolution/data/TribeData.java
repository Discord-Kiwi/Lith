package mod.akrivus.revolution.data;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class TribeData extends WorldSavedData {
	private final Map<UUID, Tribe> tribes = new LinkedHashMap<UUID, Tribe>();
	public static TribeData get(World world) {
		TribeData data = (TribeData)(world.loadData(TribeData.class, "tribes"));
		if (data == null) {
			data = new TribeData();
			world.setData("tribes", data);
		}
		return data;
	}
	public TribeData() {
		super("tribes");
	}
	public TribeData(String id) {
		super(id);
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		Iterator<Entry<UUID, Tribe>> it = this.tribes.entrySet().iterator();
		while (it.hasNext()) {
			NBTTagCompound nbt = new NBTTagCompound();
			Entry<UUID, Tribe> pair = it.next();
			nbt.setString("id", pair.getKey().toString());
			nbt.setString("name", pair.getValue().name);
			list.appendTag(nbt);
		}
		compound.setTag("tribes", list);
		return compound;
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = compound.getTagList("tribes", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			UUID id = UUID.fromString(nbt.getString("id"));
			String name = nbt.getString("name");
			this.tribes.put(id, new Tribe(name));
		}
	}
	public Tribe getTribe(UUID id) {
		if (this.tribes.containsKey(id)) {
			return this.tribes.get(id);
		}
		return null;
	}
	public UUID addTribe(String name) {
		UUID id = UUID.randomUUID();
		this.tribes.put(id, new Tribe(name));
		this.markDirty();
		return id;
	}
}

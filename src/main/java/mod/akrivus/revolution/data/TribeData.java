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
			nbt.setBoolean("homeless", pair.getValue().isHomeless());
			nbt.setDouble("oldX", pair.getValue().getOldHome().getX());
			nbt.setDouble("oldY", pair.getValue().getOldHome().getY());
			nbt.setDouble("oldZ", pair.getValue().getOldHome().getZ());
			nbt.setDouble("homeX", pair.getValue().getHome().getX());
			nbt.setDouble("homeY", pair.getValue().getHome().getY());
			nbt.setDouble("homeZ", pair.getValue().getHome().getZ());
			nbt.setString("name", pair.getValue().getName());
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
			Tribe tribe = new Tribe(nbt.getString("name"));
			tribe.setOldHome(new BlockPos(nbt.getDouble("oldX"), nbt.getDouble("oldY"), nbt.getDouble("oldZ")), null);
			tribe.setHome(new BlockPos(nbt.getDouble("homeX"), nbt.getDouble("homeY"), nbt.getDouble("homeZ")), null);
			tribe.setHomeless(nbt.getBoolean("homeless"), null);
			this.tribes.put(id, tribe);
		}
	}
	public Tribe getTribe(UUID id) {
		if (this.tribes.containsKey(id)) {
			return this.tribes.get(id);
		}
		return new Tribe("undefined");
	}
	public UUID addTribe(String name) {
		UUID id = UUID.randomUUID();
		this.tribes.put(id, new Tribe(name));
		this.markDirty();
		return id;
	}
}

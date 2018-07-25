package mod.akrivus.revolution.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Tribe {
	private boolean homeless = true;
	private BlockPos oldHome = new BlockPos(0, 0, 0);
	private BlockPos home = new BlockPos(0, 0, 0);
	private String name;
	public Tribe(String name) {
		this.homeless = true;
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public BlockPos getOldHome() {
		return this.oldHome;
	}
	public void setOldHome(BlockPos oldHome, World world) {
		this.oldHome = oldHome;
		TribeData.get(world).markDirty();
	}
	public BlockPos getHome() {
		return this.home;
	}
	public void setHome(BlockPos home, World world) {
		this.home = home; this.homeless = false;
		if (world != null) {
			TribeData.get(world).markDirty();
		}
	}
	public void setHomeless(World world) {
		this.oldHome = this.home; this.homeless = true;
		if (world != null) {
			TribeData.get(world).markDirty();
		}
	}
	public void setHomeless(boolean homeless, World world) {
		this.homeless = homeless;
		if (world != null) {
			TribeData.get(world).markDirty();
		}
	}
	public boolean isHomeless() {
		return this.homeless;
	}
}

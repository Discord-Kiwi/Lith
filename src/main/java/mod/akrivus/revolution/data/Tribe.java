package mod.akrivus.revolution.data;

import net.minecraft.util.math.BlockPos;

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
	public void setOldHome(BlockPos oldHome) {
		this.oldHome = oldHome;
	}
	public BlockPos getHome() {
		return this.home;
	}
	public void setHome(BlockPos home) {
		this.home = home;
		this.homeless = false;
	}
	public void setHomeless() {
		this.oldHome = this.home;
		this.homeless = true;
	}
	public void setHomeless(boolean homeless) {
		this.homeless = homeless;
	}
	public boolean isHomeless() {
		return this.homeless;
	}
}

package mod.akrivus.revolution.data;

import net.minecraft.util.math.BlockPos;

public class Tribe {
	public boolean homeless = true;
	public BlockPos oldHome = new BlockPos(0, 0, 0);
	public BlockPos home = new BlockPos(0, 0, 0);
	public String name;
	public Tribe(String name) {
		this.homeless = true;
		this.name = name;
	}
	public void setHome(BlockPos home) {
		this.home = home;
		this.homeless = false;
	}
	public void setHomeless() {
		this.oldHome = this.home;
		this.homeless = true;
	}
}

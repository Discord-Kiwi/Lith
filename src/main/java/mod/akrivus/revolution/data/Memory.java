package mod.akrivus.revolution.data;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;

public class Memory {
	public BlockPos location;
	public UUID entity;
	public boolean avoid;
	public boolean isPos;
	public Memory(boolean avoid, BlockPos location) {
		this.avoid = avoid;
		this.location = location;
		this.isPos = true;
	}
	public Memory(boolean avoid, UUID entity) {
		this.avoid = avoid;
		this.entity = entity;
		this.isPos = false;
	}
}

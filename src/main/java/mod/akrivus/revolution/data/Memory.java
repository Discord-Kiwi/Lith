package mod.akrivus.revolution.data;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class Memory {
	private String type;
	private String data;
	public Memory(String type, String data) {
		this.type = type;
		this.data = data;
	}
	public Memory(String type, Entity data) {
		this.type = type;
		this.data = data.getClass().getSimpleName();
	}
	public Memory(String type, Block data) {
		this.type = type;
		this.data = data.getUnlocalizedName();
	}
	public Memory(String type, BlockPos data) {
		this.type = type;
		this.data = "" + data.getX() + " " + data.getY() + " " + data.getZ();
	}
	public Memory(String type, Item data) {
		this.type = type;
		this.data = data.getUnlocalizedName();
	}
	public String getType() {
		return this.type;
	}
	public String getData() {
		return this.data;
	}
	public String getFollow() {
		if (this.type.equals("FOLLOW")) {
			return this.data;
		}
		return null;
	}
	public String getFight() {
		if (this.type.equals("FIGHT")) {
			return this.data;
		}
		return null;
	}
	public String getFear() {
		if (this.type.equals("FEAR")) {
			return this.data;
		}
		return null;
	}
	public String getIgnore() {
		if (this.type.equals("IGNORE")) {
			return this.data;
		}
		return null;
	}
	public String getFind() {
		if (this.type.equals("FIND")) {
			return this.data;
		}
		return null;
	}
	public String getBreak() {
		if (this.type.equals("BREAK")) {
			return this.data;
		}
		return null;
	}
	public String getWalk() {
		if (this.type.equals("WALK")) {
			return this.data;
		}
		return null;
	}
	public String getPlace() {
		if (this.type.equals("PLACE")) {
			return this.data;
		}
		return null;
	}
	public BlockPos getGoto() {
		if (this.type.equals("GOTO")) {
			String[] coords = this.data.split(" ");
			return new BlockPos(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
		}
		return new BlockPos(0, 0, 0);
	}
	public BlockPos getAvoid() {
		if (this.type.equals("AVOID")) {
			String[] coords = this.data.split(" ");
			return new BlockPos(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
		}
		return new BlockPos(0, 0, 0);
	}
	public String getGive() {
		if (this.type.equals("GIVE")) {
			return this.data;
		}
		return null;
	}
	public String getCraft() {
		if (this.type.equals("CRAFT")) {
			return this.data;
		}
		return null;
	}
}

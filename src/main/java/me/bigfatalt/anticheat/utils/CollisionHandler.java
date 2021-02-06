package me.bigfatalt.anticheat.utils;


import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.utils.Materials;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.api.utils.PlayerUtils;
import cc.funkemunky.api.utils.world.BlockData;
import cc.funkemunky.api.utils.world.CollisionBox;
import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;
import lombok.Getter;
import lombok.Setter;
import me.bigfatalt.anticheat.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;

@Getter
public class CollisionHandler {
	private List<Block> blocks;
	private List<Entity> entities;
	private SimpleLocation location;
	private PlayerData data;

	private double width, height;
	private double shift;
	@Setter
	private boolean single = false;
	@Setter
	private boolean debugging;

	public CollisionHandler(List<Block> blocks, List<Entity> entities, PlayerData data) {
		this.blocks = blocks;
		this.entities = entities;
		this.location = new SimpleLocation(data);
		this.data = data;
	}

	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public void setOffset(double shift) {
		this.shift = shift;
	}

	public boolean containsFlag(int bitmask) {
		for (Block b : blocks) {
			if (Materials.checkFlag(b.getType(), bitmask)) return true;
		}
		return false;
	}

	public boolean contains(EntityType type) {
		return entities.stream().anyMatch(e -> e.getType() == type);
	}

	public boolean isCollidedWith(int bitmask) {
		SimpleCollisionBox playerBox = new SimpleCollisionBox()
				.offset(location.getX(), location.getY(), location.getZ())
				.expandMin(0, shift, 0)
				.expandMax(0, height, 0)
				.expand(width / 2, 0, width / 2);

		for (Block b : blocks) {
			Location block = b.getLocation();
			if (Materials.checkFlag(b.getType(), bitmask) && (!single || (block.getBlockX() == location.getBlockX() && block.getBlockZ() == location.getBlockZ()))) {
				if (BlockData.getData(b.getType()).getBox(b, ProtocolVersion.getGameVersion()).isCollided(playerBox))
					return true;
			}
		}

		return false;
	}

	public boolean isCollidedWith(CollisionBox box) {
		SimpleCollisionBox playerBox = new SimpleCollisionBox()
				.offset(location.getX(), location.getY(), location.getZ())
				.expandMin(0, shift, 0)
				.expandMax(0, height, 0)
				.expand(width / 2, 0, width / 2);

		return box.isCollided(playerBox);
	}

	public boolean isCollidedWith(Material... materials) {
		SimpleCollisionBox playerBox = new SimpleCollisionBox()
				.offset(location.getX(), location.getY(), location.getZ())
				.expandMin(0, shift, 0)
				.expandMax(0, height, 0)
				.expand(width / 2, 0, width / 2);

		for (Block b : blocks) {
			Location block = b.getLocation();
			if (MiscUtils.contains(materials, b.getType()) && (!single || (block.getBlockX() == location.getBlockX() && block.getBlockZ() == location.getBlockZ()))) {
				if (BlockData.getData(b.getType()).getBox(b, ProtocolVersion.getGameVersion()).isCollided(playerBox))
					return true;
			}
		}

		return false;
	}
}

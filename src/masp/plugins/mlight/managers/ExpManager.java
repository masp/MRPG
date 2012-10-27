package masp.plugins.mlight.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import masp.plugins.mlight.utils.CaseInsensitiveMap;

import org.bukkit.block.Block;
import org.getspout.spoutapi.block.SpoutBlock;

public class ExpManager {
	
	private Map<String, Double> blockSources = new CaseInsensitiveMap<Double>();
	
	private Map<UUID, Double> droppedExps = new HashMap<UUID, Double>();
	
	public void addBlockExpSource(String block, double amount) {
		blockSources.put(block, amount);
	}
	
	public double getBlockExp(Block vBlock) {
		SpoutBlock block = (SpoutBlock) vBlock;
		if (block.getCustomBlock() != null) {
			return blockSources.get(block.getCustomBlock().getName()) == null ? 0D : blockSources.get(block.getCustomBlock().getName());
		} else {
			return blockSources.get(vBlock.getType().name()) == null ? 0D : blockSources.get(vBlock.getType().name());
		}
	}
	
	public void addAttachedSource(UUID id, double value) {
		droppedExps.put(id, value);
	}
	
	public double getAttachedSource(UUID id) {
		if (!droppedExps.containsKey(id)) {
			return 0D;
		}
		return droppedExps.get(id);
	}
	
	public void removeAttachedSource(UUID id) {
		droppedExps.remove(id);
	}
	
}

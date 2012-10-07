package masp.plugins.mlight.enums;

public enum ItemType {
	RING, CAPE, BELT, HELMET, CHESTPLATE, LEGGINGS, BOOT, EARRING, AMULET, BADGE, PET, OTHER;
	
	public static ItemType getItemType(String name) {
		for (ItemType type : ItemType.values()) {
			if (type.toString().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}

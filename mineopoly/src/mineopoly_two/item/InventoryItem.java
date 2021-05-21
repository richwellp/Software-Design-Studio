package mineopoly_two.item;

public class InventoryItem {
    private ResourceType itemType;

    public InventoryItem(ResourceType itemType) {
        this.itemType = itemType;
    }

    public ResourceType getItemType() {
        return itemType;
    }
}

package sh.miles.dove.configuration.adapter.inventory;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sh.miles.dove.Dove;
import sh.miles.dove.configuration.JsonAdapter;
import sh.miles.ironpipe.api.inventory.item.ToolTip;
import sh.miles.ironpipe.loader.IronPipe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackAdapter implements JsonAdapter<ItemStack> {

    public static final String ITEM_TYPE = "item_type";
    public static final String NAME = "name";
    public static final String LORE = "lore";
    public static final String ENCHANTMENT = "enchantment";
    public static final String HIDE_TOOL_TIP = "hide_tool_tip";

    @Override
    public org.bukkit.inventory.ItemStack deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        JsonObject parent = element.getAsJsonObject();
        Material itemType = parseItemType(parent);
        sh.miles.ironpipe.api.inventory.item.ItemStack item = IronPipe.newItem(itemType);
        final BaseComponent[] name = parseName(parent);
        if (name != null) {
            item.setName(name);
        }
        item.setLoreArray(parseLore(parent));
        parseEnchantment(parent).forEach(item::enchant);
        parseHideToolTip(parent).forEach(item::hideToolTip);

        final org.bukkit.inventory.ItemStack bukkitItem = item.asBukkitCopy();
        final ItemMeta meta = bukkitItem.getItemMeta();
        final PersistentDataContainer container = meta.getPersistentDataContainer();

        bukkitItem.setItemMeta(meta);
        return bukkitItem;
    }

    @Override
    public JsonElement serialize(final ItemStack item, final Type type, final JsonSerializationContext context) {
        throw new UnsupportedOperationException("You can't serialize ItemStacks");
    }

    @Override
    public Class<ItemStack> getAdapterType() {
        return ItemStack.class;
    }

    @NotNull
    private static Material parseItemType(JsonObject parent) {
        final JsonElement temp = parent.get(ITEM_TYPE);
        if (temp == null) {
            Dove.log().severe("Unable to adapt item with no item_type field");
            throw new RuntimeException("Unable to adapt material");
        }

        Material itemType = Material.matchMaterial(temp.getAsString());
        if (itemType == null) {
            Dove.log().severe("Unable to adapt provided item_type field. more details below");
            Dove.log().severe("Invalid JSON item_type of: %s".formatted(parent.get(ITEM_TYPE).getAsString()));
            throw new RuntimeException("Unable to adapt material");
        }
        return itemType;
    }

    @Nullable
    private static BaseComponent[] parseName(JsonObject parent) {
        final JsonElement temp = parent.get(NAME);
        if (temp == null) {
            return null;
        }
        final String name = temp.getAsString();
        return name == null ? null : MineDown.parse(name);
    }

    @NotNull
    private static List<BaseComponent[]> parseLore(JsonObject parent) {
        final JsonArray loreArray = parent.getAsJsonArray(LORE);
        if (loreArray == null) {
            return new ArrayList<>();
        }
        final List<BaseComponent[]> lore = new ArrayList<>(loreArray.size());
        for (final JsonElement jsonElement : loreArray) {
            lore.add(MineDown.parse(jsonElement.getAsString()));
        }
        return lore;
    }

    @NotNull
    private static Map<Enchantment, Short> parseEnchantment(JsonObject parent) {
        final JsonObject enchantObject = parent.getAsJsonObject(ENCHANTMENT);
        if (enchantObject == null) {
            return new HashMap<>();
        }
        final Map<Enchantment, Short> map = new HashMap<>();
        for (final Map.Entry<String, JsonElement> entry : enchantObject.entrySet()) {
            final String key = entry.getKey();
            final short level = entry.getValue().getAsShort();
            final Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(key));
            if (enchantment == null) {
                Dove.log().severe("Unable to find enchantment with key %s, this enchantment does not exist, this is not a bug!, but rather a configuration mistake.".formatted(key));
                throw new RuntimeException("Invalid Enchantment key");
            }
            map.put(enchantment, level);
        }
        return map;
    }

    @NotNull
    private static List<ToolTip> parseHideToolTip(JsonObject parent) {
        final JsonArray tipArray = parent.getAsJsonArray(HIDE_TOOL_TIP);
        if (tipArray == null) {
            return new ArrayList<>();
        }
        final List<ToolTip> tip = new ArrayList<>();
        for (final JsonElement element : tipArray) {
            final String raw = element.getAsString();
            try {
                tip.add(ToolTip.valueOf(raw.toUpperCase()));
            } catch (IllegalArgumentException e) {
                Dove.log().severe("No tooltip with the name %s exists, this is not a bug!, but rather a configuration mistake".formatted(raw));
                throw new RuntimeException(e);
            }
        }
        return tip;
    }

}

package fr.lacaleche.ie;


import com.google.common.collect.ImmutableMap;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.state.State;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


import java.util.Map;
import java.util.function.BiFunction;

public class Utils {

    public static <T> Map<String, Object> buildStates(Context context, String title, State<ItemStack> itemState, BiFunction<Context, T, ItemStack> callback) {
        return buildStates(context, title, itemState, callback, Map.of());
    }

    public static <T> Map<String, Object> buildStates(Context context, String title, State<ItemStack> itemState, BiFunction<Context, T, ItemStack> callback, Map<String, Object> additionalStates) {
        return ImmutableMap.<String, Object>builder()
                .put("item", itemState.get(context).clone())
                .put("callback", callback)
                .put("title", title)
                .putAll(additionalStates)
                .build();
    }

}

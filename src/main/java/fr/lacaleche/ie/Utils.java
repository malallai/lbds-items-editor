package fr.lacaleche.ie;

import com.google.common.collect.ImmutableMap;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.state.State;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Utility methods for the item editor
 */
public class Utils {

    /**
     * Build a map of states to pass to a view
     * 
     * @param context The current context
     * @param title The view title
     * @param item The item to edit
     * @param callback The callback to run when finished
     * @return A map of states
     */
    public static <T> Map<String, Object> buildStates(Context context, String title, ItemStack item, BiFunction<Context, T, ItemStack> callback) {
        return buildStates(context, title, item, callback, Map.of());
    }

    /**
     * Build a map of states to pass to a view with additional states
     * 
     * @param context The current context
     * @param title The view title
     * @param item The item to edit
     * @param callback The callback to run when finished
     * @param additionalStates Additional states to include
     * @return A map of states
     */
    public static <T> Map<String, Object> buildStates(Context context, String title, ItemStack item, BiFunction<Context, T, ItemStack> callback, Map<String, Object> additionalStates) {
        return ImmutableMap.<String, Object>builder()
                .put("item", item.clone())
                .put("callback", callback)
                .put("title", title)
                .putAll(additionalStates)
                .build();
    }

    /**
     * Build a map of states when using a State<ItemStack>
     * 
     * @param context The current context
     * @param title The view title
     * @param itemState The state containing the item to edit
     * @param callback The callback to run when finished
     * @return A map of states
     */
    public static <T> Map<String, Object> buildStates(Context context, String title, State<ItemStack> itemState, BiFunction<Context, T, ItemStack> callback) {
        return buildStates(context, title, itemState.get(context).clone(), callback, Map.of());
    }

    /**
     * Build a map of states when using a State<ItemStack> with additional states
     * 
     * @param context The current context
     * @param title The view title
     * @param itemState The state containing the item to edit
     * @param callback The callback to run when finished
     * @param additionalStates Additional states to include
     * @return A map of states
     */
    public static <T> Map<String, Object> buildStates(Context context, String title, State<ItemStack> itemState, BiFunction<Context, T, ItemStack> callback, Map<String, Object> additionalStates) {
        return buildStates(context, title, itemState.get(context).clone(), callback, additionalStates);
    }
}

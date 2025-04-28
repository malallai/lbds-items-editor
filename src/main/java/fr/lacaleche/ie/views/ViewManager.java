package fr.lacaleche.ie.views;

import fr.lacaleche.ie.Utils;
import me.devnatan.inventoryframework.context.Context;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Manages view transitions and state passing between views
 */
public class ViewManager {

    /**
     * Opens the name editor view
     * 
     * @param context The current context
     * @param title The view title
     * @param item The item to edit
     * @param callback The callback to run when finished
     */
    public static void openNameEditor(Context context, String title, ItemStack item, 
            BiFunction<Context, Component, ItemStack> callback) {
        
        context.openForPlayer(EditItemNameView.class, Utils.buildStates(
                context, title, item, callback
        ));
    }

    /**
     * Opens the lore editor view
     * 
     * @param context The current context
     * @param title The view title
     * @param item The item to edit
     * @param callback The callback to run when finished
     */
    public static void openLoreEditor(Context context, String title, ItemStack item,
            BiFunction<Context, List<Component>, ItemStack> callback) {
        
        context.openForPlayer(EditLoreView.class, Utils.buildStates(
                context, title, item, callback
        ));
    }

    /**
     * Opens a single line editor view
     * 
     * @param context The current context
     * @param title The view title
     * @param item The item to edit
     * @param callback The callback to run when finished
     * @param line The line to edit (or null for a new line)
     * @param loreCallback The callback to update the lore
     */
    public static void openLineEditor(
            Context context, 
            String title, 
            ItemStack item,
            BiFunction<Context, Component, ItemStack> callback,
            Component line,
            BiFunction<Context, List<Component>, ItemStack> loreCallback) {
        
        Map<String, Object> additionalStates = Map.of(
                "editLoreCallback", loreCallback,
                "line", line == null ? Component.empty() : line
        );
        
        context.openForPlayer(EditLineView.class, Utils.buildStates(
                context, title, item, callback, additionalStates
        ));
    }
} 

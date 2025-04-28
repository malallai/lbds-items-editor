package fr.lacaleche.ie.views;

import fr.lacaleche.ie.Constants;
import fr.lacaleche.ie.utils.ItemBuilder;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.RenderContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * View for editing an item's display name
 */
public class EditItemNameView extends AbstractAnvilView<Component> {

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        super.onFirstRender(render);

        render.firstSlot(ItemBuilder.copyOf(itemState.get(render)).lore(List.of(
                Constants.CLICK_TO_EDIT
        )).build()).onClick(Context::back);
    }

    @Override
    protected ItemStack processInput(Context ctx, String inputValue) {
        Component newName = MiniMessage.miniMessage().deserialize(inputValue);
        return callback.get(ctx).apply(ctx, newName);
    }
}

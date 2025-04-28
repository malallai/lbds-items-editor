package fr.lacaleche.ie.views;

import fr.lacaleche.ie.utils.ItemBuilder;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.RenderContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditItemNameView extends AbstractAnvilView {

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        super.onFirstRender(render);

        render.firstSlot(ItemBuilder.copyOf(itemState.get(render)).lore(List.of(
                Component.text("Click to edit").color(NamedTextColor.GREEN)
        )).build()).onClick(Context::back);
    }

}

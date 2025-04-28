package fr.lacaleche.ie.views;

import com.google.common.collect.ImmutableMap;
import fr.lacaleche.ie.Utils;
import fr.lacaleche.ie.utils.ItemBuilder;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class EditLineView extends AbstractAnvilView {

    private final State<Component> lineState = initialState("line");
    private final State<BiFunction<Context, List<Component>, ItemStack>> editLoreCallback = initialState("editLoreCallback");

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        super.onFirstRender(render);

        final Component line = lineState.get(render);

        render.firstSlot(ItemBuilder.copyOf(itemState.get(render))
                .lore(List.of(
                        Component.text("Click to edit").color(NamedTextColor.GREEN)
                ))
                .name(line == null ? Component.text("New Line") : line)
                .build()).onClick(Context::back);
    }

    @Override
    protected Map<String, Object> getStates(Context ctx, String newName) {
        return ImmutableMap.of(
                "item", resultClickCallback(ctx, newName),
                "callback", editLoreCallback.get(ctx)
        );
    }
}

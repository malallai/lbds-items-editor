package fr.lacaleche.ie.views;

import com.google.common.collect.ImmutableMap;
import fr.lacaleche.ie.Constants;
import fr.lacaleche.ie.utils.ItemBuilder;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * View for editing a single line of an item's lore
 */
public class EditLineView extends AbstractAnvilView<Component> {

    private final State<Component> lineState = initialState("line");
    private final State<BiFunction<Context, List<Component>, ItemStack>> editLoreCallback = initialState("editLoreCallback");

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        super.onFirstRender(render);

        final Component line = lineState.get(render);

        render.firstSlot(ItemBuilder.copyOf(itemState.get(render))
                .lore(List.of(Constants.CLICK_TO_EDIT))
                .name(line == null ? Component.text(Constants.NEW_LINE_TITLE) : line)
                .build()).onClick(Context::back);
    }

    @Override
    protected ItemStack processInput(Context ctx, String inputValue) {
        Component newLine = MiniMessage.miniMessage().deserialize(inputValue);
        return callback.get(ctx).apply(ctx, newLine);
    }

    @Override
    protected Map<String, Object> getStates(Context ctx, String inputValue) {
        return ImmutableMap.of(
                "item", processInput(ctx, inputValue),
                "callback", editLoreCallback.get(ctx)
        );
    }
}

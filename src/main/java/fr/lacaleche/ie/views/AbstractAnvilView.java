package fr.lacaleche.ie.views;

import com.google.common.collect.ImmutableMap;
import fr.lacaleche.ie.Constants;
import me.devnatan.inventoryframework.AnvilInput;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Base class for all anvil-based views that process text input
 * @param <T> The type of data being processed by the view
 */
public abstract class AbstractAnvilView<T> extends View {

    protected final MutableState<ItemStack> itemState = initialState("item");
    protected final State<BiFunction<Context, T, ItemStack>> callback = initialState("callback");
    protected final State<String> titleState = initialState("title");

    protected final AnvilInput anvilInput = AnvilInput.createAnvilInput();

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.cancelOnClick()
                .type(ViewType.ANVIL)
                .use(anvilInput);
    }

    @Override
    public void onOpen(@NotNull OpenContext open) {
        open.modifyConfig().title(Component.text(titleState.get(open)).color(NamedTextColor.GREEN));
    }

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        render.resultSlot()
                .onClick((ctx) -> {
                    final String newValue = anvilInput.get(ctx);

                    if (newValue == null || newValue.isEmpty()) {
                        render.getPlayer().sendMessage(Constants.PLEASE_ENTER_VALID_NAME);
                        return;
                    }

                    ctx.back(getStates(ctx, newValue));
                });
    }

    /**
     * Process the input and apply it to the item
     * 
     * @param ctx The context
     * @param inputValue The user input string
     * @return The modified item
     */
    protected abstract ItemStack processInput(Context ctx, String inputValue);

    /**
     * Get states to pass back to the parent view
     * 
     * @param ctx The context
     * @param inputValue The user input string
     * @return A map of states
     */
    protected Map<String, Object> getStates(Context ctx, String inputValue) {
        return ImmutableMap.of("item", processInput(ctx, inputValue));
    }
}

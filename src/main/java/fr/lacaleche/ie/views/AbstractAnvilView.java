package fr.lacaleche.ie.views;

import com.google.common.collect.ImmutableMap;
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
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;

public abstract class AbstractAnvilView extends View {

    protected final MutableState<ItemStack> itemState = initialState("item");
    protected final State<BiFunction<Context, Component, ItemStack>> callback = initialState("callback");
    private final State<String> titleState = initialState("title");

    final AnvilInput anvilInput = AnvilInput.createAnvilInput();

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
                    final String newName = anvilInput.get(ctx);

                    if (newName == null || newName.isEmpty()) {
                        render.getPlayer().sendMessage(Component.text("Please enter a valid name.").color(NamedTextColor.RED));
                        return;
                    }

                    ctx.back(getStates(ctx, newName));
                });
    }

    protected ItemStack resultClickCallback(Context ctx, String newName) {
        return callback.get(ctx).apply(ctx, MiniMessage.miniMessage().deserialize(newName));
    }

    protected Map<String, Object> getStates(Context ctx, String newName) {
        return ImmutableMap.of("item", resultClickCallback(ctx, newName));
    }

}

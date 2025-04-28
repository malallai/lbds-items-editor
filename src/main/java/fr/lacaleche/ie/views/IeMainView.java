package fr.lacaleche.ie.views;

import fr.lacaleche.ie.Utils;
import fr.lacaleche.ie.utils.ItemBuilder;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableIntState;
import me.devnatan.inventoryframework.state.MutableState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;

public class IeMainView extends View {

    private final MutableState<ItemStack> itemState = initialState("item");
    private final MutableIntState countState = mutableState(0);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.cancelOnClick()
                .title(LegacyComponentSerializer.legacyAmpersand().serialize(Component.text("Items Editor").color(NamedTextColor.GREEN)))
                .type(ViewType.HOPPER);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext render) {

        render.lastSlot()
                .watch(itemState)
                .renderWith(() -> itemState.get(render).clone())
                .onClick((click) -> {
                    click.getPlayer().getInventory().setItemInMainHand(itemState.get(click).clone());
                    click.closeForPlayer();
                });

        render.firstSlot(new ItemBuilder(Material.NAME_TAG).name(Component.text("Edit Name").color(NamedTextColor.YELLOW)).build())
                .onClick((click) -> {
                    if (click.isMiddleClick()) {
                        itemState.set(ItemBuilder.copyOf(itemState.get(click)).resetName().build(), click);
                        return ;
                    }

                    click.openForPlayer(EditItemNameView.class, Utils.buildStates(
                            click, "Edit Name",
                            itemState,
                            (BiFunction<Context, Component, ItemStack>) (ctx, name) ->
                                    ItemBuilder.copyOf(itemState.get(ctx))
                                            .name(name)
                                            .build()
                    ));
                });

        render.availableSlot(new ItemBuilder(Material.BOOK).name(Component.text("Edit Lore").color(NamedTextColor.YELLOW)).build())
                .onClick((click) -> {
                    if (click.isMiddleClick()) {
                        itemState.set(new ItemBuilder(itemState.get(click).clone()).resetLore().build(), click);
                        return ;
                    }

                    click.openForPlayer(EditLoreView.class, Utils.buildStates(
                            click, "Edit Lore",
                            itemState,
                            (BiFunction<Context, List<Component>, ItemStack>) (ctx, lore) ->
                                    ItemBuilder.copyOf(itemState.get(ctx))
                                            .lore(lore)
                                            .build()
                    ));
                });
    }

    @Override
    public void onResume(@NotNull Context origin, @NotNull Context target) {
        target.update();
    }

}

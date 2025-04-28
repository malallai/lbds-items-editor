package fr.lacaleche.ie.views;

import com.google.common.collect.ImmutableMap;
import fr.lacaleche.ie.Utils;
import fr.lacaleche.ie.utils.ItemBuilder;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.component.Pagination;
import me.devnatan.inventoryframework.context.Context;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class EditLoreView extends View {

    private final MutableState<ItemStack> itemState = initialState("item");
    private final State<BiFunction<Context, List<Component>, ItemStack>> callback = initialState("callback");

    private final State<Pagination> paginationState =
            computedPaginationState(this::getItems, (context, builder, index, value) -> {
                builder.withItem(value);
                builder.onClick((click) -> handlePaginationItemClick(click, index, value));
            });

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.cancelOnClick()
                .title("test")
                .size(5)
                .layout("OOOOOOOOO", "OOOOOOOOO", "OOOOOOOOO", "P#######N", "AE  V   C");
    }

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        final Pagination pagination = paginationState.get(render);

        render.layoutSlot('#', ItemBuilder.DARK_GRAY_BACKGROUND.build());
        render.layoutSlot('P', ItemBuilder.DARK_GRAY_BACKGROUND.build());
        render.layoutSlot('N', ItemBuilder.DARK_GRAY_BACKGROUND.build());

        render.layoutSlot('P', new ItemBuilder(Material.ARROW).name(Component.text("Previous", NamedTextColor.YELLOW)).build())
                .displayIf(() -> canBack(render))
                .updateOnStateChange(paginationState)
                .onClick((ctx) -> pagination.back());

        render.layoutSlot('A', new ItemBuilder(Material.YELLOW_CONCRETE).name(Component.text("Add", NamedTextColor.YELLOW)).build())
                .onClick((click) -> {
                    click.openForPlayer(EditLineView.class, Utils.buildStates(
                            click, "New Line",
                            itemState,
                            (BiFunction<Context, Component, ItemStack>) (ctx, newLine) ->
                                    new ItemBuilder(itemState.get(ctx).clone())
                                            .addLine(newLine)
                                            .build(),
                            ImmutableMap.of("editLoreCallback", callback.get(click))
                    ));
                });

        render.layoutSlot('E', new ItemBuilder(Material.PURPLE_CONCRETE).name(Component.text("Add empty line", NamedTextColor.YELLOW)).build())
                .onClick((click) -> {
                    final List<Component> newLore = new ArrayList<>(getLines(click));
                    newLore.add(Component.empty());
                    itemState.get(click).lore(newLore);
                    updatePagination(click);
                });

        render.layoutSlot('V', new ItemBuilder(Material.GREEN_CONCRETE).name(Component.text("Validate", NamedTextColor.GREEN)).build())
                        .onClick((ctx) -> ctx.back(callback.get(ctx).apply(ctx, getLines(ctx))));

        render.layoutSlot('C', new ItemBuilder(Material.RED_CONCRETE).name(Component.text("Cancel", NamedTextColor.RED)).build())
                .displayIf(Context::canBack)
                .onClick(Context::back);

        render.layoutSlot('N', new ItemBuilder(Material.ARROW).name(Component.text("Next", NamedTextColor.YELLOW)).build())
                .displayIf(() -> canAdvance(render))
                .updateOnStateChange(paginationState)
                .onClick((ctx) -> pagination.advance());
    }

    private void handlePaginationItemClick(SlotClickContext click, int index, ItemStack value) {
        if (click.isMiddleClick()) {
            List<Component> newLore = new ArrayList<>(getLines(click));
            newLore.remove(index - 1);

            itemState.get(click).lore(newLore);
            updatePagination(click);
            return ;
        }

        click.openForPlayer(EditLineView.class, Utils.buildStates(
                click, "Edit Line",
                itemState,
                (BiFunction<Context, Component, ItemStack>) (ctx, editedLine) -> {
                    List<Component> newLines = new ArrayList<>(getLines(ctx));
                    newLines.set(index - 1, editedLine);
                    return new ItemBuilder(itemState.get(ctx).clone())
                            .lore(newLines)
                            .build();
                },
                ImmutableMap.of(
                        "editLoreCallback", callback.get(click),
                        "line", value.getItemMeta().displayName()
                )
        ));
    }

    private boolean canAdvance(Context context) {
        final int currentPage = paginationState.get(context).currentPageIndex();
        final int linesCount = getLines(context).size();
        final int linesPerPage = 27;
        return linesCount > (currentPage + 1) * linesPerPage;
    }

    private boolean canBack(Context context) {
        final int currentPage = paginationState.get(context).currentPageIndex();
        return currentPage > 0;
    }

    private List<Component> getLines(Context context) {
        return itemState.get(context).lore();
    }

    private List<ItemStack> getItems(Context context) {
        List<Component> lore = getLines(context);
        if (lore == null || lore.isEmpty()) {
            return List.of(new ItemBuilder(Material.RED_CONCRETE).name(Component.text("Nothing to show.", NamedTextColor.RED)).build());
        }

        final AtomicInteger index = new AtomicInteger(0);
        final int currentPage = paginationState.get(context).currentPageIndex();
        final int linesPerPage = 27;
        return lore.stream()
                .map(item -> new ItemBuilder(Material.PAPER)
                        .amount(index.incrementAndGet())
                        .name(item)
                        .lore(List.of(
                                Component.text("Line #" + index.get(), NamedTextColor.GRAY),
                                Component.empty(),
                                Component.text("Click to edit", NamedTextColor.YELLOW),
                                Component.text("Middle click to remove", NamedTextColor.RED)
                        ))
                        .build()
                )
                .skip((long) currentPage * linesPerPage)
                .toList();
    }

    @Override
    public void onUpdate(@NotNull Context update) {
        final Pagination pagination = paginationState.get(update);
        pagination.switchTo(pagination.currentPageIndex());
    }

    @Override
    public void onResume(@NotNull Context origin, @NotNull Context target) {
        target.update();
    }

    private void updatePagination(Context context) {
        final Pagination pagination = paginationState.get(context);
        pagination.switchTo(pagination.currentPageIndex());
    }

}

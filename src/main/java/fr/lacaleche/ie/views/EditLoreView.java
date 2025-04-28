package fr.lacaleche.ie.views;

import fr.lacaleche.ie.Constants;
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

/**
 * View for editing an item's lore (multiple lines)
 */
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
                .title(Constants.EDIT_LORE_TITLE)
                .size(Constants.EDIT_LORE_LAYOUT.length)
                .layout(Constants.EDIT_LORE_LAYOUT);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        final Pagination pagination = paginationState.get(render);
        final ItemBuilder backgroundItem = ItemBuilder.DARK_GRAY_BACKGROUND;

        render.layoutSlot('#', backgroundItem.build());
        render.layoutSlot('P', backgroundItem.build());
        render.layoutSlot('N', backgroundItem.build());

        setupNavigationButtons(render, pagination);
        setupActionButtons(render);
    }

    private void setupNavigationButtons(RenderContext render, Pagination pagination) {
        // Previous page button
        render.layoutSlot('P', new ItemBuilder(Material.ARROW)
                .name(Constants.PREVIOUS_TEXT)
                .build())
                .displayIf(() -> canBack(render))
                .updateOnStateChange(paginationState)
                .onClick((ctx) -> pagination.back());

        // Next page button
        render.layoutSlot('N', new ItemBuilder(Material.ARROW)
                .name(Constants.NEXT_TEXT)
                .build())
                .displayIf(() -> canAdvance(render))
                .updateOnStateChange(paginationState)
                .onClick((ctx) -> pagination.advance());
    }

    private void setupActionButtons(RenderContext render) {
        // Add line button
        render.layoutSlot('A', new ItemBuilder(Material.YELLOW_CONCRETE)
                .name(Constants.ADD_TEXT)
                .build())
                .onClick(this::handleAddLineClick);

        // Add empty line button
        render.layoutSlot('E', new ItemBuilder(Material.PURPLE_CONCRETE)
                .name(Constants.ADD_EMPTY_LINE_TEXT)
                .build())
                .onClick(this::handleAddEmptyLineClick);

        // Validate button
        render.layoutSlot('V', new ItemBuilder(Material.GREEN_CONCRETE)
                .name(Constants.VALIDATE_TEXT)
                .build())
                .onClick((ctx) -> ctx.back(callback.get(ctx).apply(ctx, getLines(ctx))));

        // Cancel button
        render.layoutSlot('C', new ItemBuilder(Material.RED_CONCRETE)
                .name(Constants.CANCEL_TEXT)
                .build())
                .displayIf(Context::canBack)
                .onClick(Context::back);
    }

    private void handleAddLineClick(SlotClickContext click) {
        ViewManager.openLineEditor(
                click,
                Constants.NEW_LINE_TITLE,
                itemState.get(click),
                (ctx, newLine) -> new ItemBuilder(itemState.get(ctx).clone())
                        .addLine(newLine)
                        .build(),
                null,
                callback.get(click)
        );
    }

    private void handleAddEmptyLineClick(SlotClickContext click) {
        final List<Component> newLore = new ArrayList<>(getLines(click));
        newLore.add(Component.empty());
        itemState.get(click).lore(newLore);
        updatePagination(click);
    }

    private void handlePaginationItemClick(SlotClickContext click, int index, ItemStack value) {
        if (click.isMiddleClick()) {
            removeLoreLine(click, index);
            return;
        }

        editLoreLine(click, index, value);
    }

    private void removeLoreLine(SlotClickContext click, int index) {
        List<Component> newLore = new ArrayList<>(getLines(click));
        newLore.remove(index - 1);

        itemState.get(click).lore(newLore);
        updatePagination(click);
    }

    private void editLoreLine(SlotClickContext click, int index, ItemStack value) {
        ViewManager.openLineEditor(
                click,
                Constants.EDIT_LINE_TITLE,
                itemState.get(click),
                (ctx, editedLine) -> {
                    List<Component> newLines = new ArrayList<>(getLines(ctx));
                    newLines.set(index - 1, editedLine);
                    return new ItemBuilder(itemState.get(ctx).clone())
                            .lore(newLines)
                            .build();
                },
                value.getItemMeta().displayName(),
                callback.get(click)
        );
    }

    private boolean canAdvance(Context context) {
        final int currentPage = paginationState.get(context).currentPageIndex();
        final List<Component> lines = getLines(context);
        final int linesCount = lines == null ? 0 : lines.size();
        return linesCount > (currentPage + 1) * Constants.LINES_PER_PAGE;
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
            return List.of(new ItemBuilder(Material.RED_CONCRETE)
                    .name(Component.text("Nothing to show.", NamedTextColor.RED))
                    .build());
        }

        final AtomicInteger index = new AtomicInteger(0);
        final int currentPage = paginationState.get(context).currentPageIndex();
        return lore.stream()
                .map(item -> createLoreLineItem(index.incrementAndGet(), item))
                .skip((long) currentPage * Constants.LINES_PER_PAGE)
                .toList();
    }
    
    private ItemStack createLoreLineItem(int index, Component content) {
        return new ItemBuilder(Material.PAPER)
                .amount(index)
                .name(content)
                .lore(List.of(
                        Component.text("Line #" + index, NamedTextColor.GRAY),
                        Component.empty(),
                        Constants.CLICK_TO_EDIT,
                        Constants.MIDDLE_CLICK_TO_REMOVE
                ))
                .build();
    }

    @Override
    public void onUpdate(@NotNull Context update) {
        updatePagination(update);
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

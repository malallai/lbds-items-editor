package fr.lacaleche.ie.views;

import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IeMainView extends View {

    private final MutableState<ItemStack> itemState = mutableState(null);

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.cancelOnClick()
                .title(Component.text("Items Editor").color(NamedTextColor.GREEN))
                .type(ViewType.HOPPER);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        render.lastSlot()
            .watch(itemState)
            .renderWith(() -> itemState.get(render))
            .onClick(() -> {
                render.getPlayer().getInventory().setItemInMainHand(itemState.get(render).clone());
                render.closeForPlayer();
            });

        render.slot(0, item(Material.NAME_TAG, Component.text("Edit Name").color(NamedTextColor.YELLOW)))
            .onClick(() -> render.openForPlayer(EditNameView.class, itemState.get(render)));
            
        render.slot(1, item(Material.BOOK, Component.text("Edit Lore").color(NamedTextColor.YELLOW)))
            .onClick(() -> {
                ItemStack currentItem = itemState.get(render).clone();
                itemState.set(currentItem, render);
            });
    }

    private ItemStack item(Material material, Component title) {
        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.displayName(title);
            meta.lore(List.of(Component.text("Click to edit").color(NamedTextColor.GRAY)));
        });
        return item;
    }
}

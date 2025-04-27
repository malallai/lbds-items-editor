package fr.lacaleche.ie.views;

import me.devnatan.inventoryframework.AnvilInput;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.context.CloseContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EditNameView extends View {

    private final MutableState<ItemStack> itemState = mutableState(null);
    final AnvilInput anvilInput = AnvilInput.createAnvilInput();

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.cancelOnClick()
                .type(ViewType.ANVIL)
                .title(Component.text("Edit Name").color(NamedTextColor.GREEN))
                .use(anvilInput);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        render.firstSlot(this.itemState.get(render));

        render.resultSlot().onClick((ctx) -> {
            final String newName = anvilInput.get(ctx);

            if (newName == null || newName.isEmpty()) {
                render.getPlayer().sendMessage(Component.text("Please enter a valid name.").color(NamedTextColor.RED));
                return ;
            }

            ItemStack currentItem = itemState.get(render).clone();
            currentItem.editMeta(meta -> meta.displayName(Component.text(newName).color(NamedTextColor.YELLOW)));
            render.openForPlayer(IeMainView.class, currentItem);
        });
    }

    @Override
    public void onClose(@NotNull CloseContext close) {
        close.getParent().openForPlayer(IeMainView.class, itemState.get(close));
    }
}

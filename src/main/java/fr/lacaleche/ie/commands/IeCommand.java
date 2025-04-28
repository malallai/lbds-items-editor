package fr.lacaleche.ie.commands;

import com.google.common.collect.ImmutableMap;
import fr.lacaleche.ie.views.IeMainView;
import me.devnatan.inventoryframework.ViewFrame;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IeCommand implements CommandExecutor {

    private final ViewFrame viewFrame;

    public IeCommand(ViewFrame viewFrame) {
        this.viewFrame = viewFrame;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player) || !player.hasPermission("dummy.ie.use"))
            return throwError(commandSender, "You don't have permission to use this command.");

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR && strings.length == 0) return throwError(commandSender, "Please, hold an item in your hand to edit, or use /ie <item> to edit a specific item.");

        if (strings.length > 0) {
            final Material material = Material.getMaterial(strings[0].toUpperCase());
            if (material == null) return throwError(commandSender, "Invalid item type. Please use a valid material name.");
            if (material.isAir()) return throwError(commandSender, "You cannot edit air.");

            item = new ItemStack(material);
        }

        this.viewFrame.open(IeMainView.class, player, ImmutableMap.of("item", item.clone()));
        return true;
    }

    private boolean throwError(CommandSender sender, String message) {
        sender.sendMessage(Component.text(message).color(NamedTextColor.RED));
        return true;
    }

}

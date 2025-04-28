package fr.lacaleche.ie;

import fr.lacaleche.ie.commands.IeCommand;
import fr.lacaleche.ie.views.*;
import me.devnatan.inventoryframework.AnvilInputFeature;
import me.devnatan.inventoryframework.ViewFrame;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ItemsEditor extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.printf("Debug: %b%n", Boolean.parseBoolean(System.getProperty("me.devnatan.inventoryframework.debug", "false")));
        ViewFrame viewFrame = ViewFrame.create(this)
                .install(AnvilInputFeature.AnvilInput)
                .with(new IeMainView(), new EditLineView(), new EditItemNameView(), new EditLoreView())
                .register();

        Objects.requireNonNull(getCommand("itemseditor")).setExecutor(new IeCommand(viewFrame));
    }
}

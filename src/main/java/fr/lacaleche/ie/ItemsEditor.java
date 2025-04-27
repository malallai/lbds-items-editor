package fr.lacaleche.ie;

import fr.lacaleche.ie.commands.IeCommand;
import fr.lacaleche.ie.views.EditNameView;
import fr.lacaleche.ie.views.IeMainView;
import me.devnatan.inventoryframework.AnvilInputFeature;
import me.devnatan.inventoryframework.ViewFrame;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ItemsEditor extends JavaPlugin {

    @Override
    public void onEnable() {
        ViewFrame viewFrame = ViewFrame.create(this)
                .install(AnvilInputFeature.AnvilInput)
                .with(new EditNameView(), new IeMainView())
                .register();

        Objects.requireNonNull(getCommand("itemseditor")).setExecutor(new IeCommand(viewFrame));
    }
}

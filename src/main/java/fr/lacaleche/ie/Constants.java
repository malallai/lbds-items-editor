package fr.lacaleche.ie;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

/**
 * Constants used throughout the plugin
 */
public final class Constants {
    
    // View Titles
    public static final String MAIN_VIEW_TITLE = "Items Editor";
    public static final String EDIT_NAME_TITLE = "Edit Name";
    public static final String EDIT_LORE_TITLE = "Edit Lore";
    public static final String EDIT_LINE_TITLE = "Edit Line";
    public static final String NEW_LINE_TITLE = "New Line";
    
    // Messages
    public static final Component CLICK_TO_EDIT = Component.text("Click to edit", NamedTextColor.GREEN);
    public static final Component MIDDLE_CLICK_TO_REMOVE = Component.text("Middle click to remove", NamedTextColor.RED);
    public static final Component PLEASE_ENTER_VALID_NAME = Component.text("Please enter a valid name.", NamedTextColor.RED);
    
    // UI Elements
    public static final Component VALIDATE_TEXT = Component.text("Validate", NamedTextColor.GREEN);
    public static final Component CANCEL_TEXT = Component.text("Cancel", NamedTextColor.RED);
    public static final Component PREVIOUS_TEXT = Component.text("Previous", NamedTextColor.YELLOW);
    public static final Component NEXT_TEXT = Component.text("Next", NamedTextColor.YELLOW);
    public static final Component ADD_TEXT = Component.text("Add", NamedTextColor.YELLOW);
    public static final Component ADD_EMPTY_LINE_TEXT = Component.text("Add empty line", NamedTextColor.YELLOW);
    
    // Layout
    public static final String[] EDIT_LORE_LAYOUT = new String[]{
            "OOOOOOOOO",
            "OOOOOOOOO",
            "OOOOOOOOO",
            "P#######N",
            "AE  V   C"
    };
    
    // Pagination
    public static final int LINES_PER_PAGE = 27;
    
    private Constants() {
        // Prevent instantiation
    }
} 

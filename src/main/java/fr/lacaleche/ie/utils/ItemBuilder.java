package fr.lacaleche.ie.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    public static final ItemBuilder EMPTY = new ItemBuilder(Material.AIR);
    public static final ItemBuilder DARK_GRAY_BACKGROUND = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("");

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemStack getItem() {
        return this.item;
    }

    public ItemStack build() {
        this.applyItemMeta();
        return this.getItem();
    }

    public static ItemBuilder from(ItemStack item) {
        return new ItemBuilder(item);
    }

    public static ItemBuilder copyOf(ItemStack item) {
        return new ItemBuilder(item.clone());
    }

    public ItemBuilder amount(int amount) {
        this.getItem().setAmount(amount);
        return this;
    }

    public ItemBuilder durability(int durability, @NotNull LivingEntity entity) {
        this.getItem().damage(durability, entity);
        return this;
    }

    public ItemBuilder type(Material type) {
        this.applyItemMeta();
        final ItemStack newItem = this.getItem().clone();
        newItem.setType(type);
        this.item = newItem;
        this.meta = this.getItem().getItemMeta();
        return this;
    }

    public ItemBuilder name(String name) {
        this.itemMeta().displayName(Component.text(name));
        return this;
    }

    public ItemBuilder resetName() {
        this.itemMeta().displayName(null);
        return this;
    }

    public ItemBuilder name(Component component) {
        this.itemMeta().displayName(overrideItalic(component));
        return this;
    }

    public ItemBuilder lore(List<Component> component) {
        if (component == null || component.isEmpty()) {
            this.itemMeta().lore(null);
            return this;
        }
        this.itemMeta().lore(component.stream().map(this::overrideItalic).toList());
        return this;
    }

    public ItemBuilder resetLore() {
        this.itemMeta().lore(null);
        return this;
    }

    public ItemBuilder addLine(List<Component> component) {
        List<Component> lore = this.itemMeta().lore();
        if (lore == null) lore = new ArrayList<>();

        lore.addAll(component.stream().map(this::overrideItalic).toList());
        this.itemMeta().lore(lore);
        return this;
    }

    public ItemBuilder addLine(Component component) {
        List<Component> lore = this.itemMeta().lore();
        if (lore == null) lore = new ArrayList<>();

        lore.add(overrideItalic(component));
        this.itemMeta().lore(lore);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        this.getItem().addEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment) {
        this.getItem().addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder title(String title) {
        this.bookMeta().setTitle(title);
        return this;
    }

    public ItemBuilder author(String author) {
        this.bookMeta().setAuthor(author);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        this.itemMeta().setUnbreakable(true);
        this.itemMeta().addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    public ItemMeta itemMeta() {
        return this.meta;
    }

    public BookMeta bookMeta() {
        return (BookMeta) this.meta;
    }

    public SkullMeta skullMeta() {
        return (SkullMeta) this.meta;
    }

    private void applyItemMeta() {
        this.getItem().setItemMeta(this.itemMeta());
    }

    @Override
    public ItemBuilder clone() {
        this.applyItemMeta();
        return ItemBuilder.copyOf(this.getItem());
    }

    private Component overrideItalic(Component component) {
        TextDecoration.State italicState = component.decoration(TextDecoration.ITALIC);
        if (italicState == TextDecoration.State.NOT_SET) {
            return component.decoration(TextDecoration.ITALIC, false);
        }
        return component;
    }

}

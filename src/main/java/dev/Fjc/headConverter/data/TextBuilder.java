package dev.Fjc.headConverter.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public abstract class TextBuilder {

    public static Component noPerms = format("You do not have permission to use this command.", 240, 10, 10);
    public static Component open = format("Opening inventory...", 10, 100, 150);

    public static Component badItem = format("This item is not a valid pet item and has been returned to you.", 190, 80, 80);

    /**
     * Formats a string text into a colored component object using RGB
     * @param text The text to format
     * @param red R color
     * @param green G color
     * @param blue B color
     * @return The new component
     */
    public static Component format(String text, int red, int green, int blue) {
        return Component.text(text).colorIfAbsent(TextColor.color(red, green, blue));
    }
}

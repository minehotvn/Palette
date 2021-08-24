# Palette

[![](https://jitpack.io/v/minehotvn/Palette.svg)](https://jitpack.io/#minehotvn/Palette)

### Usage

There are two compulsory components:
- First is the GUI configuration which defines the layout, the items and the title. The inventory size is auto-computed to fit the given layout.
- Second is the GUI handler which processes the player interaction

For convenience, "item-holder" slot is built-in (which allows place/remove items, as wells as the ability to do so using shift + click)

Moreover, there is a default event-listener that supports "item-holder" slots and also be customizable for extra needs.

### Implementation

Relocate packages using the following configuration:

```xml
<configuration>
    <relocations>
        <relocation>
            <pattern>dev.anhcraft.config</pattern>
            <shadedPattern>YOUR_PACKAGE.config</shadedPattern>
        </relocation>
        <relocation>
            <pattern>com.minehot.palette</pattern>
            <shadedPattern>YOUR_PACKAGE.palette</shadedPattern>
        </relocation>
    </relocations>
</configuration>
```

API-Version is required in plugin.yml for allowing 1.13+ items to work.

### Example

Check out the Experimental Palette plugin.

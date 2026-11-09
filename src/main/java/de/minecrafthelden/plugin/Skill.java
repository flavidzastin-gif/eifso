package de.minecrafthelden.plugin;

public final class Skill {
    private final String name;
    private final String displayName;
    private final String description;
    private final int cooldown;

    public Skill(String name, String displayName, String description, int cooldown) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.cooldown = cooldown;
    }

    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public int getCooldown() { return cooldown; }
}

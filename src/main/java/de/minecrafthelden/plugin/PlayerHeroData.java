package de.minecrafthelden.plugin;

import java.util.UUID;

public final class PlayerHeroData {
    private final UUID uuid;
    private String heroName;
    private int level;
    private int experience;

    public PlayerHeroData(UUID uuid) {
        this.uuid = uuid;
        this.level = 1;
        this.experience = 0;
    }

    public UUID getUuid() { return uuid; }
    public String getHeroName() { return heroName; }
    public void setHeroName(String heroName) { this.heroName = heroName; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }

    public int getRequiredExperience() {
        return level * 50;
    }

    public boolean addExperience(int amount) {
        if (amount <= 0) return false;
        experience += amount;
        boolean leveledUp = false;
        while (experience >= getRequiredExperience()) {
            experience -= getRequiredExperience();
            level++;
            leveledUp = true;
        }
        return leveledUp;
    }
}

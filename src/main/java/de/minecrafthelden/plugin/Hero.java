package de.minecrafthelden.plugin;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Hero {
    private final String name;
    private final String displayName;
    private final int baseHealth;
    private final double baseDamage;
    private final double baseSpeed;
    private final Map<String, Skill> skills = new LinkedHashMap<>();

    public Hero(String name, String displayName, int baseHealth, double baseDamage, double baseSpeed) {
        this.name = name;
        this.displayName = displayName;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
        this.baseSpeed = baseSpeed;
    }

    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public int getBaseHealth() { return baseHealth; }
    public double getBaseDamage() { return baseDamage; }
    public double getBaseSpeed() { return baseSpeed; }
    public Map<String, Skill> getSkills() { return skills; }
    public void addSkill(Skill skill) { skills.put(skill.getName(), skill); }
}

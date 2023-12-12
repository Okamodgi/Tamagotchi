package com.example.tamagotchi;
import java.io.Serializable;

public class Animal implements Serializable {
    private long id;
    private String name;
    private int happiness;
    private int hunger;

    public Animal(String name, int happiness, int hunger) {
        this.name = name;
        this.happiness = happiness;
        this.hunger = hunger;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return name + ": " + "Happiness: " + happiness + ", Hunger: " + hunger;
    }
}

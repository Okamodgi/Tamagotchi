package com.example.tamagotchi;
import java.io.Serializable;

public class Animal implements Serializable {
    private long id;
    private String name;
    private int happiness;
    private int hunger;
    private String type;
    private int imageResourceId;

    public Animal(String name, int happiness, int hunger, String type) {
        this.name = name;
        this.happiness = happiness;
        this.hunger = hunger;
        this.type = type;
        this.imageResourceId = imageResourceId;
    }

   /* public void updateAnimal(String Name, String Type, int Happiness, int Hunger) {
        this.name = Name;
        this.type = Type;
        this.happiness = Happiness;
        this.hunger = Hunger;
    }
    */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    @Override
    public String toString() {
        return name + "   " + type ;
    }
    public int getImageResourceId() {
        return imageResourceId;
    }
}

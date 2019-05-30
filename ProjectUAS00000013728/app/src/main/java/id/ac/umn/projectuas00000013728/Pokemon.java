package id.ac.umn.projectuas00000013728;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Pokemon {
    private String name;
    private int order;
    private String spriteURL;
    private String description;
    private ArrayList<String> abilityList;
    private String type;
    private String typ2;
    private int height;
    private int weight;

    public Pokemon(){}

    public Pokemon(String name, int order, String spriteURL, String description, ArrayList<String> abilityList, String type, String typ2, int height, int weight) {
        this.name = name;
        this.order = order;
        this.spriteURL = spriteURL;
        this.description = description;
        this.abilityList = abilityList;
        this.type = type;
        this.typ2 = typ2;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getSpriteURL() {
        return spriteURL;
    }

    public void setSpriteURL(String spriteURL) {
        this.spriteURL = spriteURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getAbilityList() {
        return abilityList;
    }

    public void setAbilityList(ArrayList<String> abilityList) {
        this.abilityList = abilityList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTyp2() {
        return typ2;
    }

    public void setTyp2(String typ2) {
        this.typ2 = typ2;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

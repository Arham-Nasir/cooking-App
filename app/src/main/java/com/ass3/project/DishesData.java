package com.ass3.project;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class DishesData
{
    String father;
    String Id;
    String image;
    String name;
    String author;
    String time;
    String difficulty;
    String category;
    String serving;
    ArrayList <String>  Ingridients_Name = new ArrayList<String>();
    ArrayList <String>  Ingridients_Amount = new ArrayList<String>();
    ArrayList <String>  Description = new ArrayList<String>();


    public DishesData() {
    }

    public DishesData(String father,String Id,String image, String name, String author, String time, String difficulty, String category, String serving) {
        this.father = father;
        this.image = image;
        this.name = name;
        this.Id = Id;
        this.author = author;
        this.time = time;
        this.difficulty = difficulty;
        this.category = category;
        this.serving = serving;
        Ingridients_Name = new ArrayList<String>();
        Ingridients_Amount = new ArrayList<String>();
        Description = new ArrayList<String>();
    }

    public DishesData(String father,String id, String image, String name, String author, String time, String difficulty, String category, String serving, ArrayList<String> ingridients_Name, ArrayList<String> ingridients_Amount, ArrayList<String> description) {
        this.father = father;
        this.Id = id;
        this.image = image;
        this.name = name;
        this.author = author;
        this.time = time;
        this.difficulty = difficulty;
        this.category = category;
        this.serving = serving;
        this.Ingridients_Name = ingridients_Name;
        this.Ingridients_Amount = ingridients_Amount;
        this.Description = description;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public ArrayList<String> getIngridients_Name() {
        return Ingridients_Name;
    }

    public void setIngridients_Name(ArrayList<String> ingridients_Name) {
        Ingridients_Name = ingridients_Name;
    }

    public ArrayList<String> getIngridients_Amount() {
        return Ingridients_Amount;
    }

    public void setIngridients_Amount(ArrayList<String> ingridients_Amount) {
        Ingridients_Amount = ingridients_Amount;
    }

    public ArrayList<String> getDescription() {
        return Description;
    }

    public void setDescription(ArrayList<String> description) {
        Description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }
}

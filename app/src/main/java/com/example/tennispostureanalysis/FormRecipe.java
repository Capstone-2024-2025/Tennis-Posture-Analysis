package com.example.tennispostureanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormRecipe {
    private String formName;
    private List<String> requiredAngles;

    public FormRecipe(String formName) {
        this.formName = formName;
        this.requiredAngles = new ArrayList<>();
    }

    public FormRecipe addAngle(String angleName) {
        requiredAngles.add(angleName);
        return this;
    }

    public List<String> getRequiredAngles() {
        return requiredAngles;
    }

    public String getFormName() {
        return formName;
    }

    // Static map that stores all recipes
    public static Map<String, FormRecipe> buildDefaultRecipes() {
        Map<String, FormRecipe> recipes = new HashMap<>();

        recipes.put("Overhead Serve", new FormRecipe("Overhead Serve")
                .addAngle("Right Elbow")
                .addAngle("Right Shoulder")
                .addAngle("Right Wrist")
                .addAngle("Right Hip")
                .addAngle("Right Knee"));

        recipes.put("Kick Serve", new FormRecipe("Kick Serve")
                .addAngle("Right Elbow")
                .addAngle("Right Shoulder")
                .addAngle("Right Wrist")
                .addAngle("Right Hip")
                .addAngle("Right Knee"));

        recipes.put("Flat Serve", new FormRecipe("Flat Serve")
                .addAngle("Right Elbow")
                .addAngle("Right Shoulder")
                .addAngle("Right Wrist"));

        recipes.put("Forehand Swing", new FormRecipe("Forehand Swing")
                .addAngle("Right Shoulder")
                .addAngle("Right Elbow")
                .addAngle("Right Wrist")
                .addAngle("Right Hip"));

        recipes.put("Backhand Swing", new FormRecipe("Backhand Swing")
                .addAngle("Left Shoulder")
                .addAngle("Left Elbow")
                .addAngle("Left Wrist")
                .addAngle("Left Hip"));

        recipes.put("Volley", new FormRecipe("Volley")
                .addAngle("Right Shoulder")
                .addAngle("Right Elbow")
                .addAngle("Right Wrist"));

        return recipes;
    }
}

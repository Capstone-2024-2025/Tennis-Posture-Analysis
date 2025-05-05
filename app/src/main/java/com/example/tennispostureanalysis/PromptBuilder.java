package com.example.tennispostureanalysis;

public class PromptBuilder {

    public static String buildPrompt(String formType, String poseSummary) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are a professional tennis coach giving me feedback\n");
        prompt.append("Analyze the player's posture for the following form: ").append(formType).append(".\n");
        prompt.append("Here are the observed pose features:\n\n");
        prompt.append(poseSummary).append("\n\n");
        prompt.append("Based on these observations, give 2–3 practical coaching tips to help the player improve their ").append(formType).append(".\n");
        prompt.append("Focus on actionable advice they can apply immediately.\n");
        prompt.append("Be encouraging but clear.\n");

        return prompt.toString();
    }
}

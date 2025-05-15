package com.DH.BookProject.ReadingGoal.DTO;

public enum GoalPeriod {
    MONTHLY("월간"),
    YEARLY("연간");

    private final String displayName;

    GoalPeriod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

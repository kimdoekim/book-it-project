package com.DH.BookProject.ReadingGoal.DTO;

// 목표 유형 enum
public enum GoalType {
    BOOKS_COUNT("책 수"),
    PAGES_COUNT("페이지 수");

    private final String displayName;

    GoalType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

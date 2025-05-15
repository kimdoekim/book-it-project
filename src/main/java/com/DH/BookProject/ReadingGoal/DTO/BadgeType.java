package com.DH.BookProject.ReadingGoal.DTO;

// 뱃지 유형 enum
public enum BadgeType {
    BOOKS_READ("읽은 책 수"),
    PAGES_READ("읽은 페이지 수"),
    GOALS_COMPLETED("완료한 목표 수"),
    STREAK("연속 독서일");

    private final String displayName;

    BadgeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

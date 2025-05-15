package com.DH.BookProject.ReadingGoal.Service;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.repository.MemberRepository;
import com.DH.BookProject.ReadingGoal.DTO.BadgeType;
import com.DH.BookProject.ReadingGoal.DTO.Badge;

import com.DH.BookProject.ReadingGoal.DTO.ReadingProgress;
import com.DH.BookProject.ReadingGoal.DTO.UserBadge;
import com.DH.BookProject.ReadingGoal.Repository.BadgeRepository;
import com.DH.BookProject.ReadingGoal.Repository.ReadingGoalRepository;
import com.DH.BookProject.ReadingGoal.Repository.ReadingProgressRepository;
import com.DH.BookProject.ReadingGoal.Repository.UserBadgeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final ReadingProgressRepository readingProgressRepository;
    private final ReadingGoalRepository readingGoalRepository;
    private final MemberRepository userRepository;

    // 사용자 독서 통계에 따른 뱃지 확인 및 부여
    @Transactional
    public void checkAndAwardBadges(String userId) {
        Member user = userRepository.findById(userId).orElseThrow();

        // 읽은 책 수에 따른 뱃지
        int booksRead = readingProgressRepository.countCompletedBooksByMemberMemberId(userId);
        awardBadgesForType(user, BadgeType.BOOKS_READ, booksRead);

        // 읽은 페이지 수에 따른 뱃지
        Integer pagesRead = readingProgressRepository.getTotalPagesReadByMemberMemberId(userId);
        if (pagesRead != null) {
            awardBadgesForType(user, BadgeType.PAGES_READ, pagesRead);
        }

        // 완료한 목표 수에 따른 뱃지
        int goalsCompleted = readingGoalRepository.countCompletedGoalsByMemberMemberId(userId);
        awardBadgesForType(user, BadgeType.GOALS_COMPLETED, goalsCompleted);

        // 연속 독서일 체크 및 뱃지
        int streak = calculateReadingStreak(userId);
        awardBadgesForType(user, BadgeType.STREAK, streak);
    }

    // 목표 완료시 뱃지 확인 및 부여
    @Transactional
    public void checkAndAwardGoalCompletionBadges(String userId) {
        Member user = userRepository.findById(userId).orElseThrow();

        // 완료한 목표 수에 따른 뱃지
        int goalsCompleted = readingGoalRepository.countCompletedGoalsByMemberMemberId(userId);
        awardBadgesForType(user, BadgeType.GOALS_COMPLETED, goalsCompleted);
    }

    // 특정 타입의 뱃지 부여
    private void awardBadgesForType(Member user, BadgeType type, int value) {
        List<Badge> eligibleBadges = badgeRepository.findEligibleBadges(type, value);

        for (Badge badge : eligibleBadges) {
            // 이미 획득한 뱃지인지 확인
            boolean alreadyAwarded = userBadgeRepository.existsByMemberMemberIdAndBadgeId(user.getMemberId(), badge.getId());

            if (!alreadyAwarded) {
                UserBadge userBadge = new UserBadge();
                userBadge.setMember(user);
                userBadge.setBadge(badge);
                userBadgeRepository.save(userBadge);

                // TODO: 알림 서비스 연동 - 뱃지 획득 알림
            }
        }
    }

    // 연속 독서일 계산
    private int calculateReadingStreak(String userId) {
        // 최근 활동 데이터 가져오기 (최대 30일치)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<ReadingProgress> recentProgress = readingProgressRepository.findRecentProgressByMemberMemberId(userId, thirtyDaysAgo);

        if (recentProgress.isEmpty()) {
            return 0;
        }

        // 날짜별로 그룹화
        Map<LocalDate, List<ReadingProgress>> progressByDate = recentProgress.stream()
                .collect(Collectors.groupingBy(p -> p.getUpdatedAt().toLocalDate()));

        // 오늘 날짜
        LocalDate today = LocalDate.now();
        // 가장 최근 독서 날짜 확인
        LocalDate lastReadDate = progressByDate.keySet().stream()
                .max(LocalDate::compareTo)
                .orElse(null);

        // 오늘 독서했는지 확인
        boolean readToday = lastReadDate != null && lastReadDate.equals(today);

        // 연속 독서일 계산
        int streak = 0;
        LocalDate checkDate = readToday ? today : (lastReadDate != null ? lastReadDate : today);

        while (true) {
            if (progressByDate.containsKey(checkDate)) {
                streak++;
                checkDate = checkDate.minusDays(1);
            } else {
                break;
            }
        }

        return streak;
    }

    // 사용자가 획득한 모든 뱃지 조회
    public List<UserBadge> getUserBadges(String userId) {
        return userBadgeRepository.findByMemberMemberIdOrderByAwardedAtDesc(userId);
    }
}

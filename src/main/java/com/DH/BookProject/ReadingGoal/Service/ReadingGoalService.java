package com.DH.BookProject.ReadingGoal.Service;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.repository.MemberRepository;
import com.DH.BookProject.ReadingGoal.DTO.GoalPeriod;
import com.DH.BookProject.ReadingGoal.DTO.GoalType;
import com.DH.BookProject.ReadingGoal.DTO.ReadingGoal;
import com.DH.BookProject.ReadingGoal.DTO.ReadingProgress;
import com.DH.BookProject.ReadingGoal.Repository.ReadingGoalRepository;
import com.DH.BookProject.ReadingGoal.Repository.ReadingProgressRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReadingGoalService {

    private final ReadingGoalRepository readingGoalRepository;
    private final ReadingProgressRepository readingProgressRepository;
    private final BadgeService badgeService;
    private final MemberRepository userRepository;

    // 독서 목표 생성
    @Transactional
    public ReadingGoal createGoal(String userId, GoalPeriod period, GoalType type,
                                  Integer targetValue, LocalDate startDate) {
        Member user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 기간에 따른 종료일 계산
        LocalDate endDate;
        if (period == GoalPeriod.MONTHLY) {
            endDate = startDate.plusMonths(1).minusDays(1);
        } else {
            endDate = startDate.plusYears(1).minusDays(1);
        }

        // 현재 진행 중인 같은 유형의 목표가 있는지 확인
        Optional<ReadingGoal> existingGoal = readingGoalRepository
                .findByMemberMemberIdAndPeriodAndEndDateAfterAndCompletedFalse(userId, period, LocalDate.now());

        if (existingGoal.isPresent()) {
            throw new IllegalStateException("User already has an active " + period.getDisplayName() + " goal");
        }

        ReadingGoal goal = new ReadingGoal();
        goal.setMember(user);
        goal.setPeriod(period);
        goal.setType(type);
        goal.setTargetValue(targetValue);
        goal.setCurrentValue(0);
        goal.setStartDate(startDate);
        goal.setEndDate(endDate);
        goal.setCompleted(false);

        return readingGoalRepository.save(goal);
    }

    // 독서 진행 상황 업데이트 및 목표 달성 체크
    @Transactional
    public void updateProgress(String userId, String bookId, Integer pagesRead, boolean completed) {
        // 사용자의 모든 활성 목표 가져오기
        List<ReadingGoal> activeGoals = readingGoalRepository
                .findByMemberMemberIdAndCompletedFalseAndEndDateAfter(userId, LocalDate.now());

        // 독서 진행 상황 추가
        ReadingProgress progress = new ReadingProgress();
        progress.setMember(userRepository.findById(userId).orElseThrow());
        progress.setIsbn13(bookId);

        if (!activeGoals.isEmpty()) {
            progress.setGoal(activeGoals.get(0)); // 첫 번째 활성 목표와 연결
        }

        progress.setPagesRead(pagesRead);
        progress.setCompleted(completed);
        if (completed) {
            progress.setCompletedAt(LocalDateTime.now());
        }

        readingProgressRepository.save(progress);

        // 각 활성 목표별로 진행 상황 업데이트
        for (ReadingGoal goal : activeGoals) {
            updateGoalProgress(goal, pagesRead, completed);
        }
    }

    // 목표 진행 상황 업데이트 및 달성 체크
    private void updateGoalProgress(ReadingGoal goal, Integer pagesRead, boolean bookCompleted) {
        if (goal.getType() == GoalType.PAGES_COUNT) {
            goal.setCurrentValue(goal.getCurrentValue() + pagesRead);
        } else if (goal.getType() == GoalType.BOOKS_COUNT && bookCompleted) {
            goal.setCurrentValue(goal.getCurrentValue() + 1);
        }

        // 목표 달성 여부 체크
        if (goal.getCurrentValue() >= goal.getTargetValue() && !goal.getCompleted()) {
            goal.setCompleted(true);

            // 목표 달성시 뱃지 체크
            badgeService.checkAndAwardGoalCompletionBadges(goal.getMember().getMemberId());
        }

        readingGoalRepository.save(goal);
    }

    // 사용자의 현재 활성 목표 가져오기
    public List<ReadingGoal> getActiveGoals(String userId) {
        return readingGoalRepository.findByMemberMemberIdAndCompletedFalseAndEndDateAfter(userId, LocalDate.now());
    }

    // 사용자의 완료된 목표 가져오기
    public List<ReadingGoal> getCompletedGoals(String userId) {
        return readingGoalRepository.findByMemberMemberIdAndCompletedTrue(userId);
    }

    // 목표 달성 진행률 계산
    public double calculateGoalProgress(ReadingGoal goal) {
        if (goal.getTargetValue() == 0) {
            return 0;
        }
        return Math.min(100.0, (goal.getCurrentValue() * 100.0) / goal.getTargetValue());
    }

}

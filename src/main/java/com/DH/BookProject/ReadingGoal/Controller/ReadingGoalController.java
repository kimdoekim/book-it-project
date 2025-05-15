package com.DH.BookProject.ReadingGoal.Controller;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.DTO.MemberUser;
import com.DH.BookProject.Member.service.MemberService;
import com.DH.BookProject.ReadingGoal.DTO.GoalPeriod;
import com.DH.BookProject.ReadingGoal.DTO.GoalType;
import com.DH.BookProject.ReadingGoal.DTO.ReadingGoal;
import com.DH.BookProject.ReadingGoal.DTO.UserBadge;
import com.DH.BookProject.ReadingGoal.Service.BadgeService;
import com.DH.BookProject.ReadingGoal.Service.ReadingGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reading-goals")
@RequiredArgsConstructor
public class ReadingGoalController {

    private final ReadingGoalService readingGoalService;
    private final BadgeService badgeService;
    private final MemberService memberService;

    // 목표 설정 페이지
    @GetMapping("/create")
    public String createGoalForm(Model model) {
        model.addAttribute("goalPeriods", GoalPeriod.values());
        model.addAttribute("goalTypes", GoalType.values());
        return "reading-goals/create";
    }

    // 목표 생성 처리
    @PostMapping("/create")
    public String createGoal(@AuthenticationPrincipal MemberUser userDetails,
                             @RequestParam GoalPeriod period,
                             @RequestParam GoalType type,
                             @RequestParam Integer targetValue) {
        Member member = userDetails.getMember();

        try {
            readingGoalService.createGoal(member.getMemberId(), period, type, targetValue, LocalDate.now());
            return "redirect:/reading-goals";
        } catch (IllegalStateException e) {
            return "redirect:/reading-goals/create?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }

    // 목표 및 진행상황 메인 페이지
    @GetMapping
    public String viewGoals(@AuthenticationPrincipal MemberUser userDetails, Model model) {
        Member user = userDetails.getMember();
        String userId = user.getMemberId();

        // 활성화된 목표 가져오기
        List<ReadingGoal> activeGoals = readingGoalService.getActiveGoals(userId);

        // 목표별 진행률 계산
        Map<Long, Double> goalProgress = new HashMap<>();
        for (ReadingGoal goal : activeGoals) {
            goalProgress.put(goal.getId(), readingGoalService.calculateGoalProgress(goal));
        }

        // 획득한 뱃지
        List<UserBadge> userBadges = badgeService.getUserBadges(userId);

        model.addAttribute("activeGoals", activeGoals);
        model.addAttribute("goalProgress", goalProgress);
        model.addAttribute("userBadges", userBadges);

        return "reading-goals/index";
    }

    // 완료된 목표 이력 보기
    @GetMapping("/history")
    public String viewGoalHistory(@AuthenticationPrincipal MemberUser userDetails, Model model) {
        Member user = userDetails.getMember();
        List<ReadingGoal> completedGoals = readingGoalService.getCompletedGoals(user.getMemberId());
        model.addAttribute("completedGoals", completedGoals);
        return "reading-goals/history";
    }

    // 뱃지 페이지
    @GetMapping("/badges")
    public String viewBadges(@AuthenticationPrincipal MemberUser userDetails, Model model) {
        Member user = userDetails.getMember();
        List<UserBadge> userBadges = badgeService.getUserBadges(user.getMemberId());
        model.addAttribute("userBadges", userBadges);
        return "reading-goals/badges";
    }

}

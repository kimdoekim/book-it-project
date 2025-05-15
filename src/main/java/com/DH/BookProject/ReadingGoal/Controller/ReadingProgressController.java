package com.DH.BookProject.ReadingGoal.Controller;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.DTO.MemberUser;
import com.DH.BookProject.ReadingGoal.Service.ReadingGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/reading-progress")
@RequiredArgsConstructor
public class ReadingProgressController {

    private final ReadingGoalService readingGoalService;

    // 독서 진행상황 추가 API 엔드포인트 (AJAX 호출용)
    @PostMapping
    public ResponseEntity<?> updateProgress(@AuthenticationPrincipal MemberUser userDetails,
                                            @RequestParam String bookId,
                                            @RequestParam Integer pagesRead,
                                            @RequestParam(defaultValue = "false") boolean completed) {
        try {
            Member user = userDetails.getMember();
            readingGoalService.updateProgress(user.getMemberId(), bookId, pagesRead, completed);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

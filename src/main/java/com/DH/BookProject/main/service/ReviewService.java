package com.DH.BookProject.main.service;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.DTO.Review;
import com.DH.BookProject.Member.repository.MemberRepository;
import com.DH.BookProject.main.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public Page<Review> getReviewsByBookId(String bookId, Pageable pageable) {
        return reviewRepository.findByBookId(bookId, pageable);
    }

    public boolean addReview(String username, String bookId, int rating, String comment, String title, String cover) {
        boolean exists = reviewRepository.existsByMemberMemberIdAndBookId(username, bookId);

        if (!exists) {
            Member member = memberRepository.findByMemberId(username)
                    .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

            String smallTitle = title.replace("cover500","cover200");

            Review review = new Review();
            review.setMember(member);
            review.setBookId(bookId);
            review.setRating(rating);
            review.setComment(comment);
            review.setBookTitle(smallTitle);
            review.setCover(cover);

            reviewRepository.save(review);
            return true; // 리뷰 추가 성공
        }

        return false; // 이미 리뷰가 존재함
    }

    public boolean deleteReview(Long reviewId, String username) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);

        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();

            // 작성자 확인 (본인 글만 삭제 가능)
            if (review.getMember().getMemberId().equals(username)) {
                reviewRepository.delete(review);
                return true;
            }
        }
        return false;
    }

    public List<Review> getReviewsByMemberId(String username) {
        return reviewRepository.findByMember_MemberId(username);
    }
}

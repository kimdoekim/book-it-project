package com.DH.BookProject.Member.service;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.DTO.Wishlist;
import com.DH.BookProject.Member.DTO.WishlistRequest;
import com.DH.BookProject.Member.repository.MemberRepository;
import com.DH.BookProject.Member.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;

    private LocalDateTime createDate = LocalDateTime.now();


    public boolean addToWishlist(String username, WishlistRequest request) {
        // 먼저 회원 정보 조회
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 이미 위시리스트에 존재하는지 확인
        boolean exists = wishlistRepository.existsByMemberAndBookId(member, request.getBookId());

        if (!exists) {
            String smallCover = request.getCoverUrl().replace("cover500","cover200");

            Wishlist wishlist = new Wishlist();
            wishlist.setMember(member);
            wishlist.setBookId(request.getBookId());
            wishlist.setTitle(request.getTitle());
            wishlist.setAuthor(request.getAuthor());
            wishlist.setCoverUrl(smallCover);
            wishlist.setPublisher(request.getPublisher());
            wishlist.setPrice(request.getPrice());

            wishlistRepository.save(wishlist);
            return true; // 성공적으로 추가됨
        }

        return false; // 이미 존재하여 추가하지 않음
    }

    public boolean removeWishlist(Long id, String username) {
        // 위시리스트 항목 조회
        Optional<Wishlist> wishlistOpt = wishlistRepository.findById(id);

        if (wishlistOpt.isPresent()) {
            Wishlist wishlist = wishlistOpt.get();

            // 해당 항목이 현재 사용자의 것인지 확인
            if (wishlist.getMember().getMemberId().equals(username)) {
                wishlistRepository.delete(wishlist);
                return true;
            }
        }

        return false;
    }

    public List<Wishlist> findByMemberId(String username) {
        return wishlistRepository.findByMember_MemberId(username);
    }
}

package com.DH.BookProject.Member.service;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.DTO.MemberUser;
import com.DH.BookProject.Member.repository.MemberRepository;
import com.DH.BookProject.myPage.DTO.MemberForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void saveMember(Member member) {
        // 중복 회원 검사
        if (repository.findByMemberId(member.getMemberId()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        repository.save(member);
    }
    // Spring Security 인증용 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = repository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return new MemberUser(member, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    // 회원 정보 조회 메서드
    public Member getMember(String memberId) {
        return repository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + memberId));
    }

    public Member getMemberByIdOrThrow(String username) {
        return repository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
    }

    public Member findByMemberId(String memberId) {
        return repository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + memberId));
    }

    @Transactional
    public void updateMember(MemberForm memberForm) {
        Member member = repository.findById(memberForm.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 회원 정보 업데이트
        member.setMemberName(memberForm.getMemberName());
        member.setEmail(memberForm.getEmail());

        // 비밀번호 변경 시에만 비밀번호 업데이트
        if (memberForm.getPassword() != null && !memberForm.getPassword().isEmpty()) {
            member.setPassword(memberForm.getPassword());
        }

        repository.save(member);
    }
}
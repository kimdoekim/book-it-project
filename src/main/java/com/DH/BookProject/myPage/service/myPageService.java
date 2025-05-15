package com.DH.BookProject.myPage.service;

import com.DH.BookProject.Member.DTO.Member;
import com.DH.BookProject.Member.repository.MemberRepository;
import com.DH.BookProject.myPage.DTO.MyPageUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class myPageService {

    private final MemberRepository repository;

    public MyPageUser getUserDTO(String username) throws Exception{
        Optional<Member> user = repository.findByMemberId(username);
        if(user.isEmpty()){throw new Exception();}
        Member member = user.get();

        return new MyPageUser(member.getMemberId(), member.getMemberName());
    }

}

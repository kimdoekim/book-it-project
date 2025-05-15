package com.DH.BookProject.Member.DTO;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MemberUser extends User {

    private String memberName;
    private Member member;
//    private String favoriteGenre;

    public MemberUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public MemberUser(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getMemberId(), member.getPassword(), authorities);
        this.memberName = member.getMemberName();
        this.member = member;
        //this.favoriteGenre = member.getFavoriteGenre();
    }


    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Member getMember() {
        return member;
    }

}

package com.DH.BookProject.myPage.DTO;

import com.DH.BookProject.Member.DTO.MemberUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyPageUser {
    private String userName;
    private String memberName;

    public static MyPageUser fromEntity(Object user) {
        if (user instanceof MemberUser memberUser) {
            MyPageUser dto = new MyPageUser();
            dto.setUserName(memberUser.getUsername());
            dto.setMemberName(memberUser.getMemberName());
            return dto;
        }
        throw new IllegalArgumentException("Not a MemberUser");
    }
}
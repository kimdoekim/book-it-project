package com.DH.BookProject.Member.DTO;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Member {

    @Id @Column(name = "member_id", length = 30)
    private String memberId;

    private String password;

    @Column(name = "member_name", length = 10)
    private String memberName;

    @Column(name = "email", length = 100)
    private String email;

    // 추가로 가입일자를 저장하면 유용합니다
    @Column(name = "join_date")
    private LocalDateTime joinDate;

    // 엔티티 생성 시 자동으로 현재 시간 설정
    @PrePersist
    public void prePersist() {
        this.joinDate = LocalDateTime.now();
    }
}
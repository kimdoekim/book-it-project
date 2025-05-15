package com.DH.BookProject.Member;

import com.DH.BookProject.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

 /*   private final MemberService memberService;

    public SecurityConfig(MemberService memberService) {
        this.memberService = memberService;
    }
*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())  // 개발 중에는 비활성화, 실제 서비스에서는 활성화 권장
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/","/search","/detail/**", "/index", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/myPage/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")  // 커스텀 로그인 페이지 경로
                        .loginProcessingUrl("/login")  // 폼에서 POST 요청을 보낼 URL
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/login?error=true")  // 로그인 실패 시 리다이렉트
                        .usernameParameter("username")  // 아이디 파라미터명
                        .passwordParameter("password")  // 비밀번호 파라미터명
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            String refererUrl = request.getHeader("Referer");
                            response.sendRedirect(refererUrl != null ? refererUrl : "/");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
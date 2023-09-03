package site.syncnote.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @DisplayName("회원가입을 한다")
    @Test
    void join() {
        // given
        String email = "test@gmail.com";
        String name = "test";
        String password = "1234";

        // when
        Member member = memberService.join(email, name, password);

        // then
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(password);
    }

    @DisplayName("회원가입 시 이메일이 중복되면 예외가 발생한다.")
    @Test
    void join_fail_if_duplicate_email() {
        // given
        String email = "test@gmail.com";
        String name = "test";
        String password = "1234";
        memberService.join(email, name, password);

        // when & then
        assertThatThrownBy(() -> {
            memberService.join(email, name, password);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
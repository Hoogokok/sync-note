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

    @DisplayName("회원정보를 수정한다.")
    @Test
    void updateMemberInfo() {
        // given
        String email = "test@gmail.com";
        String name = "test";
        String password = "1234";
        Member member = memberService.join(email, name, password);

        // when
        memberService.updateMemberInfo(member.getEmail(), "새이름", "12345!");

        // then
        assertThat(member.getName()).isEqualTo("새이름");
        assertThat(member.getPassword()).isEqualTo("12345!");
    }

    @DisplayName("회원정보를 수정할 때 존재하지 않는 이메일이면 예외가 발생한다.")
    @Test
    void testMethodNameHere() {
        // given
        String email = "test@gmail.com";
        String newName = "새이름";
        String password = "12345!";

        // when
        assertThatThrownBy(() -> {
            memberService.updateMemberInfo(email, newName, password);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
package site.syncnote.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberTest {

    @DisplayName("회원은 이메일과 비밀번호와 이름으로 만든다.")
    @Test
    void create_member() {
        // given
        String mail = "test@gmail.com";
        String name = "tester";
        String password = "1234";

        // when
        Member tester = new Member(mail, name, password);

        // then
        assertThat(tester.getEmail()).isEqualTo(mail);
        assertThat(tester.getName()).isEqualTo(name);
        assertThat(tester.getPassword()).isEqualTo(password);
    }

    @DisplayName("회원 정보를 수정할 수 있다.")
    @Test
    void edit() {
        // given
        String mail = "test@gmail.com";
        String name = "tester";
        String password = "1234";
        Member tester = new Member(mail, name, password);

        // when
        tester.edit("테스터 변경", "12345!");

        // then
        assertThat(tester.getName()).isEqualTo("테스터 변경");
        assertThat(tester.getPassword()).isEqualTo("12345!");
    }

    @DisplayName("회원 정보를 수정할 수 없다.")
    @Test
    void edit_fail_if_member_unsubscribed() {
        // given
        String mail = "test@gmail.com";
        String name = "tester";
        String password = "1234";
        Member tester = new Member(mail, name, password);

        // when
        ReflectionTestUtils.setField(tester, "unsubscribed", true);

        // then
        assertThrows(IllegalArgumentException.class, () -> tester.edit("테스터", "12345"));
    }

    @DisplayName("회원은 탈퇴할 수 있다.")
    @Test
    void unsubscribed() {
        // given
        String mail = "test@gmail.com";
        String name = "tester";
        String password = "1234";
        Member tester = new Member(mail, name, password);

        // when
        ReflectionTestUtils.setField(tester, "unsubscribed", true);

        // then
        assertThat(tester.isUnsubscribed()).isTrue();
    }
}
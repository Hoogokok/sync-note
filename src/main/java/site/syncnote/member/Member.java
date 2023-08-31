package site.syncnote.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private final String email;
    private String name;
    private String password;
    private boolean deleted;

    @Builder
    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.deleted = false;
    }

    public void updateMemberInfo(String name, String password) {
        if (deleted) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.password = password;
    }

    public void withdraw() {
        this.deleted = true;
    }
}

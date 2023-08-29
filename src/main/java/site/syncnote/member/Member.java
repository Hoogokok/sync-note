package site.syncnote.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private final String email;
    private String name;
    private String password;
    private boolean unsubscribed;

    @Builder
    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.unsubscribed = false;
    }

    public void edit(String name, String password) {
        if (unsubscribed) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.password = password;
    }

    public void unsubscribed() {
        this.unsubscribed = true;
    }
}

package site.syncnote.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
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

    public boolean haveYou(Long memberId) {
        return this.id.equals(memberId);
    }
}

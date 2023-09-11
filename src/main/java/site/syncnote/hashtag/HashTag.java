package site.syncnote.hashtag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@Entity
@Getter
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean deleted;

    public HashTag(String name) {
        this.name = name;
        this.deleted = false;
    }

    public void delete() {
        this.deleted = true;
    }

    @Override
    public String toString() {
        return "HashTag{" +
            "name='" + name + '\'' +
            '}';
    }
}
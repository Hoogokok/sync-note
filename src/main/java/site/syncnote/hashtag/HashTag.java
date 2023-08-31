package site.syncnote.hashtag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashTag hashTag)) return false;
        return deleted == hashTag.deleted && Objects.equals(name, hashTag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, deleted);
    }
}
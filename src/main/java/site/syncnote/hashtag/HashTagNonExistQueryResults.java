package site.syncnote.hashtag;

import lombok.Getter;

import java.util.List;

@Getter
public class HashTagNonExistQueryResults {
    private final List<HashTag> nonExistHashTags;

    public HashTagNonExistQueryResults(List<HashTag> nonExistHashTags) {
        this.nonExistHashTags = nonExistHashTags;
    }

    public boolean isAnyNonExist() {
        return !nonExistHashTags.isEmpty();
    }
}

package site.syncnote.hashtag;

import lombok.Getter;

import java.util.List;

@Getter
public class HashTagNonExistQueryResults {
    private final List<HashTag> nonExistHashTags;
    private final boolean anyNonExist;

    public HashTagNonExistQueryResults(List<HashTag> nonExistHashTags) {
        this.nonExistHashTags = nonExistHashTags;
        this.anyNonExist = !nonExistHashTags.isEmpty();
    }

}

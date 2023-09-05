package site.syncnote.hashtag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class HashTagQueryService {
    private final HashTagRepository hashTagRepository;

    public HashTagQueryService(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    public List<HashTag> findExistHashTag(List<String> hashTagNames) {
        return hashTagRepository.findByNameIn(hashTagNames);
    }

    public HashTagNonExistQueryResults findNonExist(final List<String> hashTagNames) {
        List<HashTag> existHashTag = findExistHashTag(hashTagNames);
        List<HashTag> nonExistHashTags = hashTagNames.stream()
            .filter(hashTagName -> existHashTag.stream().map(HashTag::getName).noneMatch(hashTagName::equals))
            .map(HashTag::new)
            .toList();

        return new HashTagNonExistQueryResults(nonExistHashTags);
    }
}

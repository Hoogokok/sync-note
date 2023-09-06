package site.syncnote.hashtag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class HashTagService {
    private final HashTagRepository hashTagRepository;

    public HashTagService(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    public List<HashTag> write(List<String> hashTagNames) {
        HashTagNonExistQueryResults nonExist = findNonExist(hashTagNames);
        if (nonExist.isAnyNonExist()) {
           hashTagRepository.saveAll(nonExist.getNonExistHashTags());
           return findExistHashTag(hashTagNames);
        }
        return findExistHashTag(hashTagNames);
    }

    public void delete(List<HashTag> hashTags) {
        hashTags.forEach(HashTag::delete);
        hashTagRepository.saveAll(hashTags);
    }

    private HashTagNonExistQueryResults findNonExist(final List<String> hashTagNames) {
        List<HashTag> existHashTag = findExistHashTag(hashTagNames);
        List<HashTag> nonExistHashTags = hashTagNames.stream()
            .filter(hashTagName -> existHashTag.stream().map(HashTag::getName).noneMatch(hashTagName::equals))
            .map(HashTag::new)
            .toList();

        return new HashTagNonExistQueryResults(nonExistHashTags);
    }

    private List<HashTag> findExistHashTag(List<String> hashTagNames) {
        return hashTagRepository.findByNameIn(hashTagNames);
    }
}

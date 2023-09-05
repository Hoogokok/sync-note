package site.syncnote.hashtag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class HashTagCommandService {
    private final HashTagRepository hashTagRepository;

    public HashTagCommandService(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    public List<HashTag> save(final HashTagNonExistQueryResults results) {
        if (results.isAnyNonExist()) {
           return hashTagRepository.saveAll(results.getNonExistHashTags());
        }
        return List.of();
    }

    public void delete(final List<HashTag> hashTags) {
        hashTags.forEach(HashTag::delete);
        hashTagRepository.saveAll(hashTags);
    }
}

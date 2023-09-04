package site.syncnote.hashtag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.post.PostHashTagRepository;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class HashTagService {
    private final HashTagRepository hashTagRepository;
    private final PostHashTagRepository postHashTagRepository;

    public HashTagService(HashTagRepository hashTagRepository, PostHashTagRepository postHashTagRepository) {
        this.hashTagRepository = hashTagRepository;
        this.postHashTagRepository = postHashTagRepository;
    }

    public List<HashTag> saveOrFind(List<String> hashTagNames) {
        List<HashTag> existHashTags = hashTagRepository.findByNameIn(hashTagNames);
        if (existHashTags.size() == hashTagNames.size()) {
            return existHashTags;
        }
        if (existHashTags.isEmpty()) {
            return hashTagRepository.saveAll(hashTagNames.stream().map(HashTag::new).toList());
        }
        List<String> existHashTagNames = existHashTags.stream().map(HashTag::getName).collect(Collectors.toList());
        hashTagNames.removeAll(existHashTagNames);
        List<HashTag> newHashTags = hashTagNames.stream().map(HashTag::new).collect(Collectors.toList());
        hashTagRepository.saveAll(newHashTags);
        existHashTags.addAll(newHashTags);

        return existHashTags;
    }

    public void delete(final List<HashTag> hashTags) {
        hashTags.forEach(HashTag::delete);
        hashTagRepository.saveAll(hashTags);
    }
}

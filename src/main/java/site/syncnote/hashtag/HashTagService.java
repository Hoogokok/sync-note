package site.syncnote.hashtag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.post.PostHashTagRepository;
import java.util.List;

@Transactional
@Service
public class HashTagService {
    private HashTagRepository hashTagRepository;
    private PostHashTagRepository postHashTagRepository;

    public HashTagService(HashTagRepository hashTagRepository, PostHashTagRepository postHashTagRepository) {
        this.hashTagRepository = hashTagRepository;
        this.postHashTagRepository = postHashTagRepository;
    }

    public List<HashTag> save(List<String> hashTagNames) {
        List<HashTag> existHashTags = hashTagRepository.findByNameIn(hashTagNames);
        if (existHashTags.size() == hashTagNames.size()) {
            return existHashTags;
        }
        if (existHashTags.isEmpty()) {
            return hashTagRepository.saveAll(hashTagNames.stream().map(HashTag::new).toList());
        }
        List<String> existHashTagNames = existHashTags.stream().map(HashTag::getName).toList();
        hashTagNames.removeAll(existHashTagNames);
        List<HashTag> newHashTags = hashTagNames.stream().map(HashTag::new).toList();
        hashTagRepository.saveAll(newHashTags);
        existHashTags.addAll(newHashTags);

        return existHashTags;
    }

    public void delete(List<HashTag> hashTags) {
        hashTags.forEach(hashTag -> {
            if (!postHashTagRepository.existsByHashTagId(hashTag.getId())) {
                hashTag.delete();
            }
        });
        hashTagRepository.saveAll(hashTags.stream().filter(HashTag::isDeleted).toList());
    }

    public void deleteAllInBatch() {
        hashTagRepository.deleteAllInBatch();
    }
}

package site.syncnote.hashtag;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.post.PostHashTagRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class HashTagService {
    private HashTagRepository hashTagRepository;
    private PostHashTagRepository postHashTagRepository;

    public HashTagService(HashTagRepository hashTagRepository, PostHashTagRepository postHashTagRepository) {
        this.hashTagRepository = hashTagRepository;
        this.postHashTagRepository = postHashTagRepository;
    }

    public List<HashTag> find(List<String> hashTagNames) {
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

    public HashTag find(String name) {
        Optional<HashTag> findByName = hashTagRepository.findByName(name);
        if (findByName.isPresent()) {
            return findByName.get();
        }
        HashTag newHashTag = new HashTag(name);
        hashTagRepository.save(newHashTag);
        return newHashTag;
    }

    public void delete(HashTag hashTag) {
        if (!postHashTagRepository.existsByHashTagId(hashTag.getId())) {
            hashTag.delete();
            hashTagRepository.save(hashTag);
        }
    }

    public void deleteAll() {
        hashTagRepository.deleteAllInBatch();
    }
}

package site.syncnote.hashtag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.syncnote.post.PostHashTagRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class HashTagService {
    private final HashTagRepository hashTagRepository;

    public HashTagService(HashTagRepository hashTagRepository, PostHashTagRepository postHashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    public List<HashTag> saveOrFind(List<String> hashTagNames) {
        List<HashTag> existHashTags = hashTagRepository.findByNameIn(hashTagNames);
        if (existHashTags.size() == hashTagNames.size()) {
            return existHashTags;
        }
//        List<HashTag> newHashTags = hashTagNames.stream().filter(hashTagName -> !existHashTags.contains(hashTagName))
//            .map(HashTag::new)
//            .collect(Collectors.toList());
//        hashTagRepository.saveAll(newHashTags);
//        CQRS를 적용하여 커맨드와 쿼리를 분리해보자. 현재는 너무 많은 일을 해서 복잡도가 올라감.
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

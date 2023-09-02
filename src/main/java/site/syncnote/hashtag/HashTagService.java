package site.syncnote.hashtag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class HashTagService {
    private HashTagRepository hashTagRepository;

    public HashTagService(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
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

    public void delete(String name) {
        Optional<HashTag> findByName = hashTagRepository.findByName(name);
        if (findByName.isPresent()) {
            HashTag hashTag = findByName.get();
            hashTag.delete();
            hashTagRepository.save(hashTag);
        }
    }
}

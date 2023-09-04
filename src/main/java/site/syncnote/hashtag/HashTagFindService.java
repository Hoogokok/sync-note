package site.syncnote.hashtag;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class HashTagFindService {
    private final HashTagRepository hashTagRepository;

    public HashTagFindService(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    public List<HashTag> findExistHashTag(List<String> hashTagNames) {
        return hashTagRepository.findByNameIn(hashTagNames);
    }
}

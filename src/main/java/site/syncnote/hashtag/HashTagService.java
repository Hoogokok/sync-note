package site.syncnote.hashtag;

import java.util.HashMap;
import java.util.Map;

public class HashTagService {
    private final Map<String, HashTag> map = new HashMap<>();

    public HashTag find(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        }
        HashTag hashTag = new HashTag(name);
        map.put(name, hashTag);
        return hashTag;
    }
}

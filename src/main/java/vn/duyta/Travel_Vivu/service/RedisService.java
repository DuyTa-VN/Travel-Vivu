package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    private final HashOperations<String, String, Object> hashOperations(){
        return redisTemplate.opsForHash();
    }

    public void save(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    public void save(String key, Object value, long duration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, duration, timeUnit);
    }

    public void setTimeToLive(String key, long timeout, TimeUnit unit){
        redisTemplate.expire(key, timeout, unit);
    }

    public void hashGet(String key, String field, Object value){
        hashOperations().put(key, field, value);
    }

    public boolean hashExists(String key, String field){
        return hashOperations().hasKey(key, field);
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public Map<String, Object> getField(String key){
        return hashOperations().entries(key);
    }

    public Object hashGet(String key, String field){
        return hashOperations().get(key, field);
    }

    public List<Object> hashGetFieldPrefix(String key, String fieldPrefix){
        List<Object> result = new ArrayList<>();

        Map<String, Object> entries = hashOperations().entries(key);
        for (Map.Entry<String, Object> entry : entries.entrySet()) {
            if (entry.getKey().startsWith(fieldPrefix)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    public Set<String> getFieldPrefix(String key){
        return hashOperations().entries(key).keySet();
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

    public void delete(String key, String field){
        hashOperations().delete(key, field);
    }

    public void delete(String key, List<String> fields){
        for (String field : fields) {
            hashOperations().delete(key, field);
        }
    }
}

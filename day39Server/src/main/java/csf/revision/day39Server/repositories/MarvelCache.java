package csf.revision.day39Server.repositories;

import java.io.StringReader;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import csf.revision.day39Server.configs.AppConfig;
import csf.revision.day39Server.models.MarvelCharacter;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


@Repository
public class MarvelCache {
    @Autowired
    @Qualifier(AppConfig.CACHE_MARVEL)
    private RedisTemplate<String, String> redisTemplate;

    public void cache(List<MarvelCharacter> values) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        // JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        // JsonObjectBuilder builder = Json.createObjectBuilder();
        values.stream()
            .forEach(c -> {
                ops.set(Integer.toString(c.getId()), c.toJson().toString(), Duration.ofHours(1));
            });

    }

    public void cacheOne(MarvelCharacter mc) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        //when storing into redis, you need to conert the object to Json first then to string
        //otherwise it will store it as a java object
        ops.set(Integer.toString(mc.getId()), mc.toJson().toString(), Duration.ofHours(1));
    } 

    public Optional<MarvelCharacter> get(Integer characterId) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        //get the character stream
        String value = ops.get(Integer.toString(characterId));
        if (value == null) {
            System.out.println("nothing in the cache");
            return Optional.empty();
            
        } 
        //read the character stream
        JsonReader reader = Json.createReader(new StringReader(value));
        JsonObject results = reader.readObject();
        System.out.println("debug:::::" + results);
        MarvelCharacter mc = MarvelCharacter.fromCache(results);

        // MarvelCharacter character = (MarvelCharacter) results.stream()
        //                         .map(v -> (JsonObject) v)
        //                         .map(v -> MarvelCharacter.fromCache(v));
                                                
        return Optional.of(mc);

    }
}

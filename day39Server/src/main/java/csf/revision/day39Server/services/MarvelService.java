package csf.revision.day39Server.services;

import java.io.StringReader;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import csf.revision.day39Server.models.Comments;
import csf.revision.day39Server.models.MarvelCharacter;
import csf.revision.day39Server.repositories.CommentsRepo;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class MarvelService {
    
    @Value("${marvel.private.key}")
    private String marvelPrivateKey;

    @Value("${marvel.public.key}")
    private String marvelPublicKey;
    
    @Autowired
    private CommentsRepo commentsRepo;

    final String URL_CHARACTER="https://gateway.marvel.com:443/v1/public/characters";

    public List<MarvelCharacter> search(String name) {

        // int offset=0;
        int limit=20;

        Long ts = System.currentTimeMillis();
        String signature= "%d%s%s".formatted(ts, marvelPrivateKey, marvelPublicKey);
        String hash="";

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(signature.getBytes());
            byte[] h = md5.digest();

            hash = HexFormat.of().formatHex(h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String marvelUrl = UriComponentsBuilder.fromUriString(URL_CHARACTER)
                        .queryParam("nameStartsWith", name)
                        .queryParam("limit", 20)
                        .queryParam("ts", ts)
                        .queryParam("apikey", marvelPublicKey)
                        .queryParam("hash", hash)
                        .toUriString();

        System.out.println(marvelUrl);

        RequestEntity<Void> req = RequestEntity.get(marvelUrl)
                                            .accept(MediaType.APPLICATION_JSON)
                                            .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.getForEntity(marvelUrl, String.class);
        String payload = resp.getBody();

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject result = reader.readObject();
        JsonArray data = result.getJsonObject("data").getJsonArray("results");

        //Retrieve name, desc, image
        List<MarvelCharacter> lsOfCharacters = new LinkedList<>();
        for (Integer i = 0; i< data.size(); i++) {
        lsOfCharacters.add(MarvelCharacter.create(data.getJsonObject(i)));
        }

         return lsOfCharacters;

 
    }

    public MarvelCharacter getCharacterById(int id) {

        MarvelCharacter mc = new MarvelCharacter();

        Long ts = System.currentTimeMillis();
        String signature= "%d%s%s".formatted(ts, marvelPrivateKey, marvelPublicKey);
        String hash="";

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(signature.getBytes());
            byte[] h = md5.digest();

            hash = HexFormat.of().formatHex(h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String marvelUrl = UriComponentsBuilder.fromUriString(URL_CHARACTER +"/" + id)
                        .queryParam("ts", ts)
                        .queryParam("apikey", marvelPublicKey)
                        .queryParam("hash", hash)
                        .toUriString();

        System.out.println(marvelUrl);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.getForEntity(marvelUrl, String.class);
        String payload = resp.getBody();

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject result = reader.readObject();
        JsonArray data = result.getJsonObject("data").getJsonArray("results");

        for (Integer i = 0; i< data.size(); i++) {
            mc= MarvelCharacter.create(data.getJsonObject(i));
        }

        return mc;

        //Retrieve name, desc, image

    }

    public String insertComment(int characterId, String comment) {
        Document insertedDoc= commentsRepo.insertNewComment(characterId, comment);
        //get the comment from the request body 

        String acknowledged = insertedDoc.getString("acknowledged");
        return acknowledged;
    }
        

    
}

package csf.revision.day39Server.controllers;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.webresources.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import csf.revision.day39Server.models.Comments;
import csf.revision.day39Server.models.MarvelCharacter;
import csf.revision.day39Server.repositories.CommentsRepo;
import csf.revision.day39Server.repositories.MarvelCache;
import csf.revision.day39Server.services.MarvelService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

@Controller
@RequestMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
public class MarvelController {

    @Autowired
    private MarvelService marvelService;

    @Autowired
    private MarvelCache marvelCache;

    @Autowired
    private CommentsRepo commentsRepo;
    
    @GetMapping(path="/api/characters") 
    @ResponseBody
    public ResponseEntity<String> searchCharacters(@RequestParam String name) {
        List<MarvelCharacter> results = new LinkedList<>();
        Optional<List<MarvelCharacter>> opt = Optional.of(marvelService.search(name));

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error");
        } else {
        
            results= opt.get();
            marvelCache.cache(results);

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                for (MarvelCharacter mc: results ) {
                    arrayBuilder.add(mc.toJson());
                }
            JsonArray payload = arrayBuilder.build();
            return ResponseEntity.ok(payload.toString());

        }
        
    }

    @GetMapping(path="/api/character/{characterId}")
    @ResponseBody
    public ResponseEntity<String> getCharacterById(@PathVariable Integer characterId) { 
        //initialise new character
        MarvelCharacter mc = new MarvelCharacter();
        //check redis for response
        Optional<MarvelCharacter> opt= marvelCache.get(characterId);
        if (opt.isEmpty()) {
            Optional <MarvelCharacter> optNew = Optional.of(marvelService.getCharacterById(characterId));
            if (optNew.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
            } else {
                mc = optNew.get();
            }
        } else{
            mc= opt.get();
        }


        Optional<List<String>> optComments = commentsRepo.getAllComments(characterId);
        if (optComments.isPresent()) {
            mc.setComments(optComments.get());
        } 

        return ResponseEntity.ok(mc.toJson().toString());
        
       
    }

    @PostMapping(path="/api/character/{characterId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> insertComments(@PathVariable Integer characterId, @RequestBody String body) {

        //get the comment from the body
        JsonReader reader = Json.createReader(new StringReader(body));
        JsonObject json = reader.readObject();
        String comment = json.getString("comments");


        String acknowledged = marvelService.insertComment(characterId, comment);

        if (acknowledged=="false") {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insert failed");
        } 
        //If you don't do your payload correctly you may get an error despite a successful insert
            JsonObject payload = Json.createObjectBuilder()
                                .add("message", "successfully inserted comment")
                                .build(); 
            return ResponseEntity.ok(payload.toString());
        

    }
        
    

}

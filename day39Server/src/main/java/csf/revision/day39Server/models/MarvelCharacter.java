package csf.revision.day39Server.models;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class MarvelCharacter {

    private Integer id;
    private String name;
    private String description;
    private String image;
    private String details;
    private List<String> comments;

    public void setId(Integer id) {
        this.id = id;
    }
    public List<String> getComments() {
        return comments;
    }
    public void setComments(List<String> comments) {
        this.comments = comments;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String imageToUrl(String image) {
        return image +"+jpg";
    }

    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    
    public static MarvelCharacter fromCache(JsonObject doc) {

        final MarvelCharacter mc = new MarvelCharacter();
        mc.setId(doc.getInt("id"));
        mc.setName(doc.getString("name"));
        mc.setDescription(doc.getString("description"));
        mc.setImage(doc.getString("image"));
        mc.setDetails(doc.getString("details"));
        return mc;
    }


    public JsonObject toJson() {
    JsonObjectBuilder json=  Json.createObjectBuilder()
                    .add("id", id)
                    .add("name", name)
                    .add("description", description)
                    .add("image", image)
                    .add("details", details);
        //build array if comments is not null
        if(comments != null) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            comments.forEach(c-> arrayBuilder.add(c));
            JsonArray commentsArray = arrayBuilder.build();
            json.add("comments", commentsArray);
        }

        return json.build();

    }

    public MarvelCharacter createFromDoc (Document doc) {
        final MarvelCharacter mc =new MarvelCharacter();
        mc.setId(doc.getInteger("id"));
        mc.setName(doc.getString("name"));
        mc.setDescription(doc.getString("description"));
        mc.setImage(doc.getString("image"));
        return mc;
    }


    public static MarvelCharacter create(JsonObject doc) {
        final MarvelCharacter mc = new MarvelCharacter();

        //build the character
        mc.setId(doc.getInt("id"));
        mc.setName(doc.getString("name"));
        mc.setDescription(doc.getString("description"));

        //Replace doc with jsonObject
        JsonObject img = doc.getJsonObject("thumbnail");
        mc.setImage("%s.%s".formatted(img.getString("path"), (img.getString("extension"))));

        JsonArray urls = doc.getJsonArray("urls");
        for (int i =0; i< urls.size(); i++) {
            JsonObject d = urls.getJsonObject(i);
            if ("detail".equals(d.getString("type"))) {
                mc.setDetails(d.getString("url"));
                break;
            }

        }

        return mc;
    }

    // @Override
    // public String toString() {
    //     return "Marvel Character {id=%d, name=%s, description=%s, image=%s, details=%s"
    //             .formatted(id, name, description, image, details);
    // }


    
}

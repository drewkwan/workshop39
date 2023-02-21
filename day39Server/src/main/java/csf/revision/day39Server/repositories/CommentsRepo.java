package csf.revision.day39Server.repositories;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import csf.revision.day39Server.models.Comments;

@Repository
public class CommentsRepo {

    @Autowired 
    private MongoTemplate mongoTemplate;

    /*
     db.comments.insert( 
    {
        characterId: 1009313,
        name: "Gambit",
        comments: "fella",
        timestamp: "date"
    }

    )
     */ 

     public Optional<List<String>> getAllComments(int characterId) {
        /*
        * db.comments.find({characterId:"5"})
        */

        //Create criteria 
        Criteria c = Criteria.where("characterId").is(characterId);

        Query q = Query.query(c);

        Sort s = Sort.by(Sort.Direction.DESC, "timestamp");
        q.with(s).limit(10);
        //make query
        List<Document> results = mongoTemplate.find(q, Document.class, "comments");
        if (results.size()<1) {
            return Optional.empty();
        }
        List<String> comments = results.stream()
                            .map(d-> d.getString("comments"))
                            .toList();

        return Optional.of(comments);

     }

     public Document insertNewComment(int characterId, String comments) {
        Document document = new Document();
        Date timestamp = new Date();
        document.put("characterId", characterId);
        document.put("comments", comments);
        document.put("timestamp", timestamp);

        Document inserted = mongoTemplate.insert(document, "comments");
        System.out.printf("inserted: %s\n", inserted);
        return inserted;



     }
     
}

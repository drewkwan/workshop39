package csf.revision.day39Server.models;

public class Comments {

    private int characterId;
    private String comment;
    private Long ts;


    public int getCharacterId() {
        return characterId;
    }
    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Long getTs() {
        return ts;
    }
    public void setTs(Long ts) {
        this.ts = ts;
    }
    
}

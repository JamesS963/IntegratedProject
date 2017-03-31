package main.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Dean on 30/03/2017.
 */
@Entity
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private long docId;

    @NotNull
    private long revisionNo;

    @NotNull
    private long authorId;

    @NotNull
    private long distribId;

    private String type = "share"; // for checking at clientside

    public Share() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDocId() {
        return docId;
    }

    public void setDocId(long docId) {
        this.docId = docId;
    }

    public long getRevisionNo() {
        return revisionNo;
    }

    public void setRevisionNo(long revisionNo) {
        this.revisionNo = revisionNo;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public long getDistribId() {
        return distribId;
    }

    public void setDistribId(long distribId) {
        this.distribId = distribId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

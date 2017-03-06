package main;

import main.models.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Temporary class to simulate document db, will be lost when program is terminated
 * Created by Dean on 16/02/2017.
 */
public class TempStorage {

    private List<Document> docs = new ArrayList<Document>();


    public List<Document> getDocs() {
        return docs;
    }

    public void setDocs(List<Document> docs) {
        this.docs = docs;
    }

    public void printAll() {
        System.out.println("-------------Printing All ------------- \n");
        for(Document doc : docs) { System.out.println(doc); }
    }
    public void addDoc(Document doc) {
        docs.add(doc);
    }
}

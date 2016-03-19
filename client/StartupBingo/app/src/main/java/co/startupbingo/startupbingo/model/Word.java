package co.startupbingo.startupbingo.model;

/**
 * Created by jubb on 19/03/16.
 */
public class Word {
    public static final String STATUS = "status";
    public static final String WORD_LIST_DATA = "data";
    public String associatedWord;
    public boolean isChecked = false;

    public String getTileString() {
        return associatedWord!=null&&!associatedWord.isEmpty()?
                associatedWord:
                "Missing Word";
    }

    public Word(String associatedWord){
        this.associatedWord = associatedWord;
    }
}

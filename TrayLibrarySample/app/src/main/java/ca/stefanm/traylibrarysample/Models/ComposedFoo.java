package ca.stefanm.traylibrarysample.Models;

import com.squareup.moshi.Moshi;

/**
 * Created by Stefan on 10/22/2016.
 */

public class ComposedFoo {


    public String ChildId;
    public String ChildNote;

    public Integer ChildNumber;

    public ComposedFoo() {

    }

    public ComposedFoo(String childId, String childNote, Integer childNumber){
        this.ChildId = childId;
        this.ChildNote = childNote;
        this.ChildNumber = childNumber;
    }

    @Override
    public String toString() {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(ComposedFoo.class).toJson(this);
    }

}

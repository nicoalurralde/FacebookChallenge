package rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatePostResponse {

    @SerializedName("id")
    @Expose
    public String id;

    public CreatePostResponse(String toString) {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
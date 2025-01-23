package model;

import com.google.gson.annotations.SerializedName;

@lombok.Data
public class Data {
    @SerializedName("_id")
    private String id;
}

package app.roam.se.models.test;

import lombok.Data;

@Data
public class TestData {
    public static final int ID = 1;
    public static final int CATEGORY =2 ;
    public static final int SUBCATEGORY = 3;
    public static final int DATAGROUP = 4;
    public static final int LABEL = 5;
    public static final int VALUE = 6 ;
    private int id;
    private String category;
    private String subCategory;
    private String dataGroup ;
    private String label;
    private String value;
}

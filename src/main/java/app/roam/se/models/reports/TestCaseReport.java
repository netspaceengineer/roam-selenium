package app.roam.se.models.reports;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestCaseReport {
    public static final String TESTCASENAME = "testCase" ;
    public static final String STEPS = "steps";
    public List<Result> steps = new ArrayList<>();
    public String testCaseName;
    public TestCaseReport(String testCaseName){
        this.testCaseName = testCaseName;
    }

    public JSONObject getJSONObject(){
        JSONObject object = new JSONObject();
        object.put(TESTCASENAME ,getTestCaseName());
        JSONArray stepArray = new JSONArray();
        for(Result r: steps){
            stepArray.put(r.getJSONObject());
        }
        object.put(STEPS,stepArray);
        return object;
    }
}

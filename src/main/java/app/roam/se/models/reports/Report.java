package app.roam.se.models.reports;

import app.roam.se.App;
import app.roam.se.utils.FilesUtil;
import app.roam.se.utils.TimeUtil;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Data
public class Report {
    private static final String SUITE = "suite";
    private static final String TESTCASES = "testcases" ;
    private static final String BROWSER ="browser";
    private static final String BROWSER_VERSION = "browserVersion";
    private static final String OS = "os";
    private static final String EXECUTION_DATE = "date";
    private static final String DURATION = "duration";
    public String browserVersion;
    public String browser;
    public String os;

    public String suiteName;
    public List<TestCaseReport> testCases = new ArrayList<TestCaseReport>();
    public String duration;

    public Report(String name ){
        this.suiteName = name;
    }
    public long startTime;
    public long endTime;
    public String date;
    public Report( ){

    }

    public JSONObject getJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(SUITE,getSuiteName());
        jsonObject.put(BROWSER,browser);
        jsonObject.put(BROWSER_VERSION,browserVersion);
        jsonObject.put(OS,os);
        jsonObject.put(EXECUTION_DATE,date);
        float elapse = (endTime-startTime)/1000f;
        jsonObject.put(DURATION,String.valueOf(elapse) +" sec.");
        JSONArray arrayTestCases= new JSONArray();
        for(TestCaseReport t: testCases){
            arrayTestCases.put(t.getJSONObject());
        }
        jsonObject.put(TESTCASES,arrayTestCases);
        return  jsonObject;
    }

    public void saveReport(){
        FilesUtil.writeFile(App.testProject.getLocation()+"/Results/" +suiteName+"_"+ TimeUtil.getTimeStamp()+ ".json",getJSONObject().toString());
    }

    public void loadReport(String path){
        String raw = FilesUtil.readFile(path);
        JSONObject object = new JSONObject(raw);
        this.suiteName = object.getString(SUITE);
        this.browser=object.getString(BROWSER);
        this.browserVersion=object.getString(BROWSER_VERSION);
        this.os=object.getString(OS);
        this.duration=object.getString(DURATION);
        this.date = object.getString(EXECUTION_DATE);
        JSONArray testCaseArray = object.getJSONArray(TESTCASES);
        for(int i=0; i<testCaseArray.length();i++){
            JSONObject testCaseObject = testCaseArray.getJSONObject(i);
            TestCaseReport testCaseReport = new TestCaseReport(testCaseObject.getString(TestCaseReport.TESTCASENAME));
            JSONArray stepArray = testCaseObject.getJSONArray(TestCaseReport.STEPS);
            for(int j=0;j<stepArray.length();j++){
                Result result = new Result(stepArray.getJSONObject(j));
                testCaseReport.steps.add(result);
            }
            this.testCases.add(testCaseReport);
        }
    }
}

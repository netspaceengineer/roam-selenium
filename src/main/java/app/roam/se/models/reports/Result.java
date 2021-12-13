package app.roam.se.models.reports;

import lombok.Data;
import org.json.JSONObject;


public class Result {
    private static final String STEP_DESCRIPTION ="step";
    private static final String STEP_STATUS = "status";
    private static final String STEP_SCREENSHOT = "screenshot";
    public String screenshot;
    public boolean isPassed;
    private String action = "";
    private String noun = "";
    private String data = "";
    private Exception[] exception;
    private String statement=null;
    public Result(String action, String noun, String data) {
        this.isPassed = true;
        this.action = action;
        this.noun = noun;
        this.data = data;
    }

    public Result(JSONObject object){
        statement = object.getString(STEP_DESCRIPTION);
        isPassed = object.getString(STEP_STATUS).equals("PASSED");
        screenshot = object.getString(STEP_SCREENSHOT);

    }

    public Result(String action, Exception... ex) {
        this.isPassed = false;
        this.exception = ex;

    }

    public String getResult() {
        return (isPassed ? "[PASSED] " + getStatement()  : "[FAILED] " +(exception.length>1?"Return more than one exception!":exception[0].getMessage()))  ;
    }


    public String getStatement() {
        if(statement!=null){
            return statement;
        }
        String verb = action.toUpperCase();
        switch (action.toLowerCase()) {

            case "switchwindowbyname":
            case "switchwindowbyindex":
                return String.format("%s to %s.", verb, data);
            case "sendtext":
            case "appendtext":
            case "selectbytext":
            case "selectbyvalue":
            case "selectbyindex":
                return String.format("%s '%s' to '%s'", verb, data, noun);
            case "verifyunchecked":
            case "verifychecked":
            case "accept":
            case "dismiss":
            case "refresh":
            case "back":
            case "forward":
            case "click":
            case "hover":
            case "doubleclick":
            case "rightclick":
            case "cleartext":
                return String.format("%s %s.", verb, noun);
            case "navigate":
                return String.format("%s to %s.", verb, noun);
            default:
                return String.format("%s %s.", verb, noun);
        }
    }

    public JSONObject getJSONObject(){
        JSONObject object = new JSONObject();
        object.put(STEP_DESCRIPTION,getStatement());
        object.put(STEP_STATUS,isPassed?"PASSED":"FAILED");
        object.put(STEP_SCREENSHOT,screenshot);
        return object;
    }
}

package app.roam.se.models.reports;


public class Result {
    public boolean isPassed;
    private String action = "";
    private String noun = "";
    private String data = "";
    private Exception[] exception;

    public Result(String action, String noun, String data) {
        this.isPassed = true;
        this.action = action;
        this.noun = noun;
        this.data = data;
    }

    public Result(String action, Exception... ex) {
        this.isPassed = false;
        this.exception = ex;

    }

    public String getResult() {
        return (isPassed ? "[PASSED] " + getStatement()  : "[FAILED] " +(exception.length>1?"Return more than one exception!":exception[0].getMessage()))  ;
    }


    public String getStatement() {
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
}

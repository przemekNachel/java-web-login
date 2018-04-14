package view;

public class getHtml {
    public static String form = "<html><body>" +
            "<form method=\"POST\">\n" +
            "  username:<br>\n" +
            "  <input type=\"text\" name=\"username\" required=\"required\">\n" +
            "  <br>\n" +
            "  password:<br>\n" +
            "  <input type=\"text\" name=\"password\" required=\"required\">\n" +
            "  <br><br>\n" +
            "  <input type=\"submit\" value=\"Sign in\">\n" +
            "</form> " +
            "</body></html>";

    public static String refreshPage = "<script language=\"JavaScript\" type=\"text/javascript\">location.href=\"/\"</script>";

    public static String badCredentials = form + "Bad username or password";

    public static String mainPage(String username) {
        return "Hello " + username;
    }
}



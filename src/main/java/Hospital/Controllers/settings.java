package Hospital.Controllers;

import java.util.IdentityHashMap;

public interface settings {
    String siteHelp = "http://localhost/medart/news.html";

    IdentityHashMap<String, Boolean> login = new IdentityHashMap<>();
    IdentityHashMap<String, String> user = new IdentityHashMap<>();
    IdentityHashMap<String, String> name = new IdentityHashMap<>();
    IdentityHashMap<String, String> id = new IdentityHashMap<>();
    IdentityHashMap<String, String> hospital = new IdentityHashMap<>();
    IdentityHashMap<String, Boolean> changepassword = new IdentityHashMap<>();
    String[] des = {"jdbc:mysql://127.0.0.1/medica_client", "root", "1997"};
    String[] desSession = {"jdbc:mysql://127.0.0.1/medica_client_sessions", "root", "1997"};
    //    String[] des = {"jdbc:mysql://nanotechsoftwares.co.ke:3306/nanotech_HospitalSystem", "nanotech_admin", ",4=y4,Zv6hR}"};
    String encryptionkey = "0123456789abcdef0123456789abcdef";
    String initVector = "abcdef9876543210";
    String appName = "Medica ";
    String APPLICATION_ICON = "Hospital/images/logo.png";
    String apiFileUploadEndPoint = "http://127.0.0.1:8000/api/v1/files/";
}

package Hospital.Controllers.Physicians;

import java.io.InputStream;
import java.util.IdentityHashMap;

public interface Physician {
    IdentityHashMap<String, String> temporarySession = new IdentityHashMap<>();

    IdentityHashMap<String, String> currentSession = new IdentityHashMap<>();
    IdentityHashMap<String, InputStream> imageResult = new IdentityHashMap<String, InputStream>();
    IdentityHashMap<String, String> resultText = new IdentityHashMap<>();
    IdentityHashMap<String, String> diagnosisText = new IdentityHashMap<>();

    void addPatientDetails();

    void viewPatientDetails();

    void viewPatientHistory();

    void viewPatientLabTests();

    void viewPatientAppointments();

    void Patientdiagnosis();

    void Patientprescription();
}

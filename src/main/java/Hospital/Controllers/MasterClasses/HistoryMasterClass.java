package Hospital.Controllers.MasterClasses;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class HistoryMasterClass {
    private final SimpleStringProperty id = new SimpleStringProperty();
    private final SimpleStringProperty date = new SimpleStringProperty();
    private final SimpleStringProperty doctor = new SimpleStringProperty();
    private final SimpleStringProperty ratings = new SimpleStringProperty();
    private final SimpleStringProperty patientEmail = new SimpleStringProperty();
    private final SimpleStringProperty prescription = new SimpleStringProperty();
    private final SimpleStringProperty tests = new SimpleStringProperty();
    private final SimpleIntegerProperty times = new SimpleIntegerProperty(0);
    private final SimpleBooleanProperty completed = new SimpleBooleanProperty();

    public boolean isCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    public SimpleBooleanProperty completedProperty() {
        return completed;
    }

    public String getPatientEmail() {
        return patientEmail.get();
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail.set(patientEmail);
    }

    public SimpleStringProperty patientEmailProperty() {
        return patientEmail;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public int getTimes() {
        return times.get();
    }

    public void setTimes(int times) {
        this.times.set(times);
    }

    public SimpleIntegerProperty timesProperty() {
        return times;
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public String getDoctor() {
        return doctor.get();
    }

    public void setDoctor(String doctor) {
        this.doctor.set(doctor);
    }

    public SimpleStringProperty doctorProperty() {
        return doctor;
    }

    public String getRatings() {
        return ratings.get();
    }

    public void setRatings(String ratings) {
        this.ratings.set(ratings);
    }

    public SimpleStringProperty ratingsProperty() {
        return ratings;
    }

    public String getPrescription() {
        return prescription.get();
    }

    public void setPrescription(String prescription) {
        this.prescription.set(prescription);
    }

    public SimpleStringProperty prescriptionProperty() {
        return prescription;
    }

    public String getTests() {
        return tests.get();
    }

    public void setTests(String tests) {
        this.tests.set(tests);
    }

    public SimpleStringProperty testsProperty() {
        return tests;
    }


}

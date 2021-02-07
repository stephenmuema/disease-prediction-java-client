import Hospital.Controllers.Super;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static Hospital.Controllers.settings.apiFileUploadEndPoint;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MINUTES;

class ApiPushControl {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    Super aSuper = new Super();
    private final Connection conn = aSuper.getHospitalDbConnection();
    private final Connection connSess = aSuper.getSessionsDbConnection();


    //    method to perform backup and push to api endpoint
    public void periodicBackUp() {
        final Runnable apiPushControl = () -> {

//                perform db export to csv after checking the last time an export was done
            boolean periodDone = false;
            try {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                Timestamp lastBackup = null, currentTime = null;
                PreparedStatement preparedStatementCheckLastBackUp = connSess.prepareStatement("SELECT * FROM backup_list ORDER BY id DESC LIMIT 1");
                ResultSet backUpCheck = preparedStatementCheckLastBackUp.executeQuery();
                if (backUpCheck.isBeforeFirst()) {
                    while (backUpCheck.next()) {
                        lastBackup = backUpCheck.getTimestamp("time_baq");
                    }
                    if ((timestamp.getTime() / 1000 - lastBackup.getTime() / 1000) > 60 * 60 * 24) {
                        System.out.println("backup time passed");
                        periodDone = true;
                    } else {
                        System.out.println("backup time within range");
                    }
                } else {
                    periodDone = true;
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (periodDone) {
                sqlToCSV("SELECT dateprescribed,diagnosis,prescription,gender,location FROM prescriptions", "prescription");

                final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

                File file = new File("prescription.csv");
                final FileDataBodyPart filePart = new FileDataBodyPart("file", file);
                FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
                final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("name", file.getName()).bodyPart(filePart);

                final WebTarget target = client.target(apiFileUploadEndPoint);
                final Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));

                //Use response object to verify upload success
//                System.out.println(response.getStatusInfo());

                if (response.getStatus() == 201) {
                    PreparedStatement preparedStatementCheckLastBackUp = null;
                    try {
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        preparedStatementCheckLastBackUp = connSess.prepareStatement("INSERT INTO backup_list(time_baq) VALUES(?) ");
                        preparedStatementCheckLastBackUp.setTimestamp(1, timestamp);
                        preparedStatementCheckLastBackUp.executeUpdate();

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    System.out.println("push failed");
                }

                try {
                    formDataMultiPart.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    multipart.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
//sqlToCSV("SELECT * FROM labtests","labtests");
        };
        final ScheduledFuture<?> beeperHandle =
                scheduler.scheduleAtFixedRate(apiPushControl, 0, 30, MINUTES);
        scheduler.schedule(() -> {
            beeperHandle.cancel(true);
        }, 365, DAYS);
    }

    private void sqlToCSV(String query, String filename) {

        try {

            FileWriter fw = new FileWriter(filename + ".csv");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            int cols = rs.getMetaData().getColumnCount();

            for (int i = 1; i <= cols; i++) {
                fw.append(rs.getMetaData().getColumnLabel(i));
                if (i < cols) fw.append(',');
                else fw.append('\n');
            }

            while (rs.next()) {

                for (int i = 1; i <= cols; i++) {
                    fw.append(rs.getString(i));
                    if (i < cols) fw.append(',');
                }
                fw.append('\n');
            }
            fw.flush();
            fw.close();
            System.out.println("Exported to table Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package Hospital.Controllers.Admins;

import Hospital.Controllers.Super;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.data.general.DefaultPieDataset;

public class ReportsClass extends Super {
    static PdfWriter writer = null;
    static Document document = new Document();
    int width = 700;
    int height = 500;

    public void hospitalStatistics() {

    }

    public void patientHistory(String email) {

    }

    /**
     * calculate disease statistics for patients at various  locations
     * DIAGNOSIS--------NUMBER OF PATIENTS------LOCATION
     * <p>
     * CONCLUSION FROM ABOVE TABLE
     */
    public void diseaseStats() {
        ArrayList<String> maximumValues = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        int max = 0;
        String maxdiseaseName = "";
        HashMap<String, Integer> hashMap = new HashMap<>();
        StringBuilder prescriptions = new StringBuilder();
        StringBuilder diseases = new StringBuilder();
        PrintWriter outfile = null;
        try {
            outfile = new PrintWriter(System.getProperty("user.home") + "" + File.separator + "Desktop" + File.separator + "reports" + File.separator + "Report.txt");//output file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("SELECT * FROM prescriptions");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    prescriptions.append(resultSet.getString("diagnosis"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("select * from disease_database");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    int count = 0, start = 0;

                    diseases.append(resultSet.getString("disease"));
                    while ((start = prescriptions.indexOf(resultSet.getString("disease"), start)) != -1) {
                        start++;
                        count++;
                    }
                    hashMap.put(resultSet.getString("disease"), count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("SELECT * FROM disease_database");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                assert outfile != null;
                outfile.println("DISEASES AND THEIR FREQUENCY OF OCCURRENCE\n**********************************");
                while (resultSet.next()) {
                    try {
                        int current = hashMap.get(resultSet.getString("disease"));
                        if (current >= max) {
                            if (current > max) {
                                maximumValues.clear();
                            }
                            maximumValues.add(resultSet.getString("disease"));
                            max = current;
                            outfile.println(resultSet.getString("disease") + ":\t" + hashMap.get(resultSet.getString("disease")));

                        } else {
                            outfile.println(resultSet.getString("disease") + ":\t" + hashMap.get(resultSet.getString("disease")));

                        }
                        keys.add(resultSet.getString("disease"));
                        values.add(String.valueOf(hashMap.get(resultSet.getString("disease"))));
//                        System.out.println(resultSet.getString("disease")+"::"+hashMap.get(resultSet.getString("disease")));

                    } catch (Exception ignore) {

                    }
                }
//                drawPieChart(keys,values, new String[]{"DISEASES CHART"});
//                writeChartToPDF(generatePieChart(keys, values, new String[]{"DISEASES CHART"}), width / 2, height / 2, System.getProperty("user.home") + ""+File.separator+"Desktop"+File.separator+"reports"+File.separator+"REPORT.pdf");


                outfile.println("\nCONCLUSION\nTHE MOST PREVALENT DISEASES ARE:");//+maxdiseaseName +" WHICH INFECTED "+max+" PEOPLE");
                int count = 1;
                for (String disease : maximumValues
                ) {
                    if (count == 1) {
                        outfile.print(disease.toUpperCase());

                    } else if (count < maximumValues.size()) {
                        outfile.print("," + disease.toUpperCase());

                    } else {
                        outfile.print(" AND " + disease.toUpperCase());

                    }
                    count++;
                }
                outfile.print(" WHICH INFECTED " + max + " PEOPLE");
                outfile.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void conditionStats() {

    }

//    public static void writeChartToPDF(JFreeChart chart, int width, int height, String fileName) {
//
//
//        try {
//            writer = PdfWriter.getInstance(document, new FileOutputStream(
//                    fileName));
//            document.open();
//            PdfContentByte contentByte = writer.getDirectContent();
//            PdfTemplate template = contentByte.createTemplate(width, height);
//            Graphics2D graphics2d = template.createGraphics(width, height,
//                    new DefaultFontMapper());
//            Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,
//                    height);
//
//            chart.draw(graphics2d, rectangle2d);
//
//            graphics2d.dispose();
//            contentByte.addTemplate(template, 0, 0);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void closeDocument() {
        document.close();

    }

//    public static JFreeChart generatePieChart(ArrayList<String> keys, ArrayList<String> values, String[] title) {
//        DefaultPieDataset myPiedataset = new DefaultPieDataset();
//
//        for (String key : keys
//        ) {
//            int index = keys.indexOf(key);
//            myPiedataset.setValue(key, Double.parseDouble(values.get(index)));
//
//        }
//
//
//        JFreeChart chart = ChartFactory.createPieChart(
//                title[0], myPiedataset, true, true, false);
//        chart.getPlot().setBackgroundPaint(Color.WHITE);
//        return chart;
//    }
//
//    public static JFreeChart generateBarChart(ArrayList<String> keys, ArrayList<String> values, String[] title) {
//        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
//        dataSet.setValue(791, "Population", "1750 AD");
//        dataSet.setValue(978, "Population", "1800 AD");
//        dataSet.setValue(1262, "Population", "1850 AD");
//        dataSet.setValue(1650, "Population", "1900 AD");
//        dataSet.setValue(2519, "Population", "1950 AD");
//        dataSet.setValue(6070, "Population", "2000 AD");
//
//        JFreeChart chart = ChartFactory.createBarChart(
//                "World Population growth", "Year", "Population in millions",
//                dataSet, PlotOrientation.VERTICAL, false, true, false);
//
//        return chart;
//    }

    public void drawLineGraph(String[] sizes, String[] labels, String[] authorAndDescription) {

    }

    public void drawTable(String[] sizes, String[] labels, String[] authorAndDescription) {

    }

}

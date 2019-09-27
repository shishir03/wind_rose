import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class DataLoader {
    // 2019-09-12T20:25:00.000Z

    /*
    1. Read in mm/dd/yyyy format
    2. Convert String to timestamp
    3. Pass timestamps into loadData()
    4. Convert timestamp into yyyy-mm-ddThh:mm:ss.000Z format for endDate
    5. Stop loading new requests when last request has a timestamp less than startDate
     */
    private static ArrayList<String> lines = new ArrayList<>();

    public static ArrayList<WindVector> loadData(long startDate, long endDate) {
        ArrayList<WindVector> results = new ArrayList<>();

        load(startDate, endDate);
        double sp = 0;
        int d = 0;

        for(String s : lines) {
            String[] ss = s.split("},");

            for(String sss : ss) {
                int n = sss.indexOf("windspeedmph");
                int m = sss.indexOf("winddir");

                if (n != -1) sp = Double.parseDouble(sss.substring(sss.indexOf(":", n) + 1, sss.indexOf(",", n)));
                if (m != -1) d = Integer.parseInt(sss.substring(sss.indexOf(":", m) + 1, sss.indexOf(",", m)));
                results.add(new WindVector(sp, d));
            }
        }

        return results;
    }

    private static void load(long startDate, long endDate) {
        try {
            Date date = new Date(endDate);
            SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            jdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String dateString = jdf.format(date);
            dateString = dateString.substring(0, dateString.lastIndexOf(' '));
            dateString += ".000Z";
            dateString = dateString.replace(' ', 'T');

            String url = "https://api.ambientweather.net/v1/devices/84:F3:EB:67:9C:8F?apiKey=ae6adf691961484cad033bf3b9c6d6fcc7fcd509e19145e1a4f2c18d057e4de9&" +
                    "applicationKey=0bdd59132b424e0c832a53c1f6c0960e46eb59f9a7704afd8821f4692b2c9a54&endDate=" + dateString + "&limit=288";
            System.out.println(url);
            URL api = new URL(url);
            Scanner input = new Scanner(api.openStream());
            String s = input.nextLine();
            lines.add(s);
            int n = s.lastIndexOf("dateutc");
            long newEnd = Long.parseLong(s.substring(s.indexOf(":", n) + 1, s.indexOf(",", n)));

            if(!(n != -1 && newEnd < startDate)) {
                Thread.sleep(1000);
                load(startDate, newEnd);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
import java.io.*;
import java.lang.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GetPDFfromLink {

        // returns numeric representation of a month, -1 if invalid
        // convention throughout the code: Months start indexed at 0 (Jan = 0, Dec = 11)
        private static int monthToNum (String monthString) {
            int monthNum;
            switch (monthString) {
                case "January":  monthNum = 0;
                     break;
                case "February":  monthNum = 1;
                     break;
                case "March":  monthNum = 2;
                     break;
                case "April":  monthNum = 3;
                     break;
                case "May":  monthNum = 4;
                     break;
                case "June":  monthNum = 5;
                     break;
                case "July":  monthNum = 6;
                     break;
                case "August":  monthNum = 7;
                     break;
                case "September":  monthNum = 8;
                     break;
                case "October": monthNum = 9;
                     break;
                case "November": monthNum = 10;
                     break;
                case "December": monthNum = 11;
                     break;
                default: monthNum = -1;
                     break;
                }
            return monthNum;
        }

        // returns a string representing a month, empty string
        private static String numToMonth (int monthNum) {
            String monthString;
            switch (monthNum) {
            case 0:  monthString = "January";
                     break;
            case 1:  monthString = "February";
                     break;
            case 2:  monthString = "March";
                     break;
            case 3:  monthString = "April";
                     break;
            case 4:  monthString = "May";
                     break;
            case 5:  monthString = "June";
                     break;
            case 6:  monthString = "July";
                     break;
            case 7:  monthString = "August";
                     break;
            case 8:  monthString = "September";
                     break;
            case 9: monthString = "October";
                     break;
            case 10: monthString = "November";
                     break;
            case 11: monthString = "December";
                     break;
            default: monthString = "";
                     break;
            }
            return monthString;
        }

        // returns a month/day range [month1, day1, month2, day2], and null otherwise
        private static int[] getDateRange (String linkText) throws NumberFormatException {
            int[] result = new int[4];

            // split linkText into tokens (string array)
            String[] tokens  = linkText.split(" ");

            // token 0 should be the start month of the range
            String leftMonth = tokens[0];
            int monthL = monthToNum(leftMonth);
            // go to the next string if it doesn't match the format
            if (monthL == -1) return null;
            result[0] = monthL;

            // token 1 is the left day
            int dayL;
            try {
                dayL = Integer.parseInt(tokens[1]);
            }
            catch (NumberFormatException e) {
                return null;
            }
            result[1] = dayL;

            // token 3 should be the end month of the range
            String rightMonth = tokens[3];
            int monthR = monthToNum(rightMonth);
            // go to the next string if it doesn't match the format
            if (monthR == -1) return null;
            result[2] = monthR;

            // token 4 should be the right day, plus a comma
            String token4 = tokens[4];
            // remove the comma
            token4 = token4.substring(0, token4.length() - 1);
            int dayR;
            try {
                dayR = Integer.parseInt(token4);
            }
            catch (NumberFormatException e) {
                return null;
            }
            // check if invalid, go to the next string
            result[3] = dayR;

            return result;
        }

        private static boolean inDateRange (int[] dateRange, int month, int day) {
            if ((dateRange[0] <= month) && (dateRange[1] <= day)) {
                if (dateRange[2] > month) return true;
                else if (dateRange[2] == month) {
                    if (dateRange[3] >= day) {
                        return true;
                    }
                }
            }
            return false;
        }

        // returns the URL of the schedule we want, null if not found
        public static String get_PDF_URL (String url) throws IOException {

            String targetURL = null;

            // What is today's month and day?

            Calendar calendar = Calendar.getInstance(); 

            // testing 
            // System.out.println("The current date is : " + calendar.getTime()); 
            // System.out.println("At present Calendar's Month: " + calendar.get(Calendar.MONTH));   
             
            int month = calendar.get(Calendar.MONTH); 
            String monthString = numToMonth(month);
            int day = calendar.get(Calendar.DATE); 
        
            // Find the correct links from the website
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");

            // for each element
            for (Element link : links) {

                // link text format: November 6 - November 12, 2017
                // assume that this is the format we're looking for
                String linkString = link.attr("href");
                String linkText = link.text();

                // does the linkText contain the monthString?
                if (!linkText.contains(monthString)) continue;

                int[] dateRange = getDateRange(linkText);
                if (dateRange == null) continue;

                // we've found the link we want!
                if (inDateRange(dateRange, month, day)) {
                    targetURL = link.attr("href");
                    break;
                }

                // testing
                // for (String s : tokens) System.out.println(s);
                // System.out.println("link : " + linkString);
                // System.out.println("text : " + linkText);
            }
            return targetURL;
        }

        public static void main(String[] args) throws IOException {
            String url = "https://campusrec.princeton.edu/facilities-operations/facility-hours";

            String targetURL = get_PDF_URL(url);

            System.out.println(targetURL);
        }
}
import java.lang.*;

public class Test {  
    public static boolean inDateRange (int[] dateRange, int month, int day) {
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

    public static void main( String[] args ) throws NumberFormatException {  
    	/*
    	int[] dateRange = {10, 6, 10, 1};
    	int month = 10;
    	int day = 11;

    	boolean inRange = inDateRange(dateRange, month, day);

    	System.out.println(inRange);
    	*/
    	String token4 = "hello";
    	
    		int dayL;
            try {
                dayL = Integer.parseInt(token4);
            }
            catch (NumberFormatException e) {
                System.out.println("problem!");
            }
    }  
}  
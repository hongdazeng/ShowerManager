import java.util.*;
import javax.swing.*;

public class ShowerManager {
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to Shower Manager 1.0", "Welcome", JOptionPane.PLAIN_MESSAGE);

        int numstall = 0;   // the number of shower stalls availiable in a dorm
        int starttime = 8;  // start of reserved time in pm
        int endtime = 8;    // end of reserved time in am
        int totalhours = 0; // total number of hours managed

        final int maxint = Integer.MAX_VALUE;
        final int minint = Integer.MIN_VALUE;

        String uin = JOptionPane.showInputDialog(null, "Please enter the number of shower stalls you are managing", "Set number of stalls");
        numstall = inputProcessor(0, maxint, uin, "Set stall");
        uin = JOptionPane.showInputDialog(null, "Please enter the start of the managed time \n(in integer pm, midnight is 12 am)", "Set start time");
        starttime = inputProcessor(0, 13, uin, "Set start time");
        uin = JOptionPane.showInputDialog(null, "Please enter the end of the managed time \n(in integer am, midnight is 12 am)", "Set start time");
        endtime = inputProcessor(0, 13, uin, "set end time");

        totalhours = totaltime(starttime, endtime);
        String[][] timetable = new String[totalhours * 3][numstall];
        for (int a1 = 0; a1 < timetable.length ; a1++) {
            for (int a2 = 0; a2 < timetable[a1].length; a2++) {
                timetable[a1][a2] = "isFree    ";
            }
        }
        display(timetable, starttime, endtime);



    }   // end main

    public static int inputProcessor (int botlimit, int toplimit, String userinput, String reminder) {
        String uin = userinput;
        int finalreturn = 0;
        boolean notgoodenough = true;
        String erroroutput = ("Please enter a valid number between " + botlimit + " and " + toplimit + "");
        while (notgoodenough) {
            try {
                finalreturn = Integer.parseInt(uin);
                if (finalreturn > botlimit && finalreturn < toplimit) {
                    notgoodenough = false;
                } else {
                    uin = JOptionPane.showInputDialog(null, erroroutput, reminder);
                }
            } catch (Exception e) {
                uin = JOptionPane.showInputDialog(null, "Please enter a valid integer", reminder);
            }
        }
        return finalreturn;
    }

    public static int totaltime (int start, int end) {
        int top = 0;    // total number of hours managed before midnight
        int bot = 0;    // total number of hours managed after midnight



        if (start == 12) {
            top = 12;
        } else {
            top = 12 - start;
        }

        if (end == 12) {
            bot = 1;
        } else {
            bot = end + 1;
        }

        return top + bot;
    }

    public static void display(String[][] tb, int start, int end) {
        int curstart = start;
        int curend =  end + 12;
        String[] minlist = {"00", "20", "40"};
        int currbuild = 0;

        int curmin = 0;
        System.out.println("<><><>Shower Listings<><><>");
        System.out.print("Stalls:   ");
        for (int stall = 0; stall < tb[0].length; stall++) {
            System.out.print("Stall " + (stall + 1) + "     ");
        } // print the line of stalls
        System.out.println();

        for (int b1 = 0; b1 < tb.length; b1++) {
            if (curstart < 10) {
                System.out.print("0" + curstart + ":" + minlist[currbuild % 3] + "     ");
            } else {
                System.out.print(curstart + ":" + minlist[currbuild % 3] + "     ");
            }
            currbuild++;
            if (currbuild == 3) {
                currbuild = 0;
                curstart++;
            }
            if (curstart > 12) {
                curstart = 1;
            }
            for (int b2 = 0; b2 < tb[0].length; b2++) {
                System.out.print(tb[b1][b2] + "  ");
            }
            System.out.println(""); //move to a new line
        }

    }

}
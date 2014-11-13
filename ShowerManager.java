import java.util.*;
import javax.swing.*;
import java.io.*;

public class ShowerManager {

    static String[][] timetable;
    static String message = "";
    static boolean keepgoing = false;

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to Shower Manager 1.0", "Welcome", JOptionPane.PLAIN_MESSAGE);

        int numstall = 0;   // the number of shower stalls availiable in a dorm
        int starttime = 8;  // start of reserved time in pm
        int endtime = 8;    // end of reserved time in am
        int totalhours = 0; // total number of hours managed

        final int maxint = Integer.MAX_VALUE;
        final int minint = Integer.MIN_VALUE;

        message = JOptionPane.showInputDialog(null, "Please enter the message of the day", "anything?");
        String uin = JOptionPane.showInputDialog(null, "Please enter the number of shower stalls you are managing", "Set number of stalls (e.g. \"3\")");
        numstall = inputProcessor(0, maxint, uin, "Set stall");
        uin = JOptionPane.showInputDialog(null, "Please enter the start of the managed time \n(in integer pm, midnight is 12 am)", "Set start time (e.g. \"3\")");
        starttime = inputProcessor(0, 13, uin, "Set start time");
        uin = JOptionPane.showInputDialog(null, "Please enter the end of the managed time \n(in integer am, midnight is 12 am)", "Set start time (e.g. \"3\")");
        endtime = inputProcessor(0, 13, uin, "set end time");

        totalhours = totaltime(starttime, endtime);
        timetable = new String[totalhours * 3][numstall];
        for (int a1 = 0; a1 < timetable.length ; a1++) {
            for (int a2 = 0; a2 < timetable[a1].length; a2++) {
                timetable[a1][a2] = "isFree    ";
            }
        }
        captureddisplay(timetable, starttime, endtime);

        while (true) {
            try {
                forcechange();
                if (keepgoing) {
                    break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please pick a valid time slot and stall number");
            }
            captureddisplay(timetable, starttime, endtime);
        }
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

    public static String captureddisplay(String[][] tb, int start, int end) {
        // code from Ernest Friedman-Hill via stackoverflow
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        // The display to spill its guts
        display(tb, start, end);
        // Put things back
        System.out.flush();
        System.setOut(old);
        // Show what happened
        System.out.println("Here: " + baos.toString());
        return baos.toString();
    }

    public static void display(String[][] tb, int start, int end) {
        int curstart = start;
        int curend =  end + 12;
        String[] minlist = {"00", "20", "40"};
        int currbuild = 0;

        int curmin = 0;
        System.out.println("=======================================================================");
        System.out.println("Shower Manager 1.2");
        System.out.println("Today's Message: " + message + "\n");
        System.out.println("<><><>Shower Listings<><><>");
        System.out.print("Stalls:   ");
        for (int stall = 0; stall < tb[0].length; stall++) {
            System.out.print("Stall " + (stall + 1) + "     ");
        } // print the line of stalls
        System.out.println();
        System.out.println("Slot");

        for (int b1 = 0; b1 < tb.length; b1++) {

            if (b1 < 10) {
                System.out.print("0" + b1 + " ");
            } else {
                System.out.print(b1 + " ");
            }

            if (curstart < 10) {
                System.out.print("0" + curstart + ":" + minlist[currbuild % 3] + "  ");
            } else {
                System.out.print("" + curstart + ":" + minlist[currbuild % 3] + "  ");
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

    public static void savecell(int slot, int stall) {
        int trueslot = slot;
        int stall1 = stall - 1;
        String newstr = "";
        if (timetable[trueslot][stall1].equals("isFree    ")) {
            newstr = ("You have reserved stall " + stall + " at time slot " + slot);
            timetable[trueslot][stall1] = ("onHold    ");
            JOptionPane.showMessageDialog(null, newstr, "Success", JOptionPane.PLAIN_MESSAGE);
        } else {
            newstr = ("Stall " + stall1 + " at time slot " + slot + " is in use, please try again");
            JOptionPane.showMessageDialog(null, newstr, "Success", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void userchange() {
        String uni = (String)JOptionPane.showInputDialog("Please enter the time slot you wish to reserve", "integer");
        int slotnumber = inputProcessor(-1, timetable.length, uni, "Time (e.g. \"3\")");
        uni = (String)JOptionPane.showInputDialog("Please enter the stall number you wish to reserve", "integer");
        int stallnumber = inputProcessor(-1, timetable.length, uni, "Stall (e.g. \"3\")");
        savecell(slotnumber, stallnumber);
    }

    public static void forcechange() {
        String[] options = {"Reserve a spot", "Free up a spot", "Exit"};
        String coffee = (String) JOptionPane.showInputDialog(null, "What do you want to do next?", "Next Step", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        System.out.println(coffee);
        if (coffee.equals(options[0])) {
            userchange();
        } else if (coffee.equals(options[1])) {
            String uni = (String)JOptionPane.showInputDialog("Please enter the time slot you wish to reset", "integer (e.g. \"3\")");
            int slot = inputProcessor(-1, timetable.length, uni, "slot number");
            uni = (String)JOptionPane.showInputDialog("Please enter the stall number you wish to reset", "integer (e.g. \"3\")");
            int stall = inputProcessor(-1, timetable.length, uni, "stall number");
            int trueslot = slot;
            int stall1 = stall - 1;
            String newstr = "";
            newstr = ("You have freed up stall " + stall + " at time " + slot);
            timetable[trueslot][stall1] = ("isFree    ");
            JOptionPane.showMessageDialog(null, newstr, "Success", JOptionPane.PLAIN_MESSAGE);
        } else {
            keepgoing = true;
        }
    }
}
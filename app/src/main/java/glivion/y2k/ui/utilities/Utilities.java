package glivion.y2k.ui.utilities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.regex.Pattern;

/**
 * A helper class for accomplishing common tasks
 * Created by Saeed on 7/18/2016.
 */
public final class Utilities {


    /**
     * A method to check the validity of an email address using regex
     *
     * @param email the email to verify
     * @return true if email is valid; false otherwise
     */
    public static boolean checkForValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return emailPattern.matcher(email).find();
    }

    public static void shakeView(View viewToShake) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ObjectAnimator.ofFloat(viewToShake, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0));
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    public static void shakeViews(View firstView, View secondView) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(firstView, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0), ObjectAnimator.ofFloat(secondView, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0));
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    public static String getMonthName(int month) {
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "";
        }
    }

    public static String getDay(int day) {
        String dayString;

        if (day >= 11 && day <= 13) {
            return "th";
        }

        switch (day % 10) {
            case 1:
                dayString = "st";
                break;
            case 2:
                dayString = "nd";
                break;
            case 3:
                dayString = "rd";
                break;
            default:
                dayString = "th";
        }
        return dayString;
    }
}

package glivion.y2k.ui.ui;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import glivion.y2k.R;

/**
 * Created by saeedissah on 5/17/16.
 */
public class ColoredSnakBar {

    private static View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }

    private static Snackbar colorSnackBar(Snackbar snackbar, int colorId) {
        View snackBarView = getSnackBarLayout(snackbar);
        if (snackBarView != null) {
            snackBarView.setBackgroundColor(ContextCompat.getColor(snackBarView.getContext(),colorId));
        }
        TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setMaxLines(3);
        snackbar.setActionTextColor(Color.WHITE);
        return snackbar;
    }

    public static Snackbar get(Snackbar snackbar,int colorAccent) {
        return colorSnackBar(snackbar, colorAccent);
    }
}
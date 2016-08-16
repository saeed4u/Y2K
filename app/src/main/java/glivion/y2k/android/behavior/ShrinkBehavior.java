package glivion.y2k.android.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import java.util.List;

/**
 * Created by saeed on 16/08/2016.
 */
public class ShrinkBehavior extends CoordinatorLayout.Behavior<AHBottomNavigation> {
    public ShrinkBehavior() {
    }

    public ShrinkBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, AHBottomNavigation child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, AHBottomNavigation child, View dependency) {
        float translationY = getButtonTranslation(parent, child);
        float percentComplete = -translationY / dependency.getHeight();
        float scaleFactor = 1 - percentComplete;

        child.setScaleX(scaleFactor);
        child.setScaleY(scaleFactor);
        return false;
    }

    private float getButtonTranslation(CoordinatorLayout parent, AHBottomNavigation child) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(child);
        for (int i = 0; i < dependencies.size(); i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(child, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }
        return minOffset;
    }
}

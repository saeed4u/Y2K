package glivion.y2k.ui.ui;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.chyrta.onboarder.OnboarderPage;
import com.chyrta.onboarder.utils.ColorsArrayBuilder;
import com.chyrta.onboarder.views.CircleIndicatorView;

import java.util.ArrayList;
import java.util.List;

import glivion.y2k.R;
import glivion.y2k.ui.adapter.OnBoarderAdapter;
import glivion.y2k.ui.statemanager.Y2KStateManager;

/**
 * Created by saeedissah on 5/16/16.
 */
public class OnBoardingActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private List<OnboarderPage> mOnBoarderPageList;


    private Integer[] mColors;
    private Integer[] mFabColors;
    private Integer[] mDarkColors;
    private CircleIndicatorView mCircleIndicator;
    private ViewPager vpOnboarderPager;
    private OnBoarderAdapter onboarderAdapter;
    private ImageButton ibNext;
    private Button btnSkip, btnFinish;
    private FrameLayout buttonsLayout;
    private FloatingActionButton fab;
    private View divider;
    private ArgbEvaluator evaluator;

    private boolean shouldDarkenButtonsLayout = false;
    private boolean shouldUseFloatingActionButton = false;

    private int[][] states = new int[][]{
            new int[]{android.R.attr.state_enabled}, // enabled
            new int[]{-android.R.attr.state_enabled}, // disabled
    };

    private Y2KStateManager mStateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStateManager = new Y2KStateManager(this);
        if (!mStateManager.isFirstRun()) {
            startDashboard();
        } else {
            mOnBoarderPageList = new ArrayList<>();

            mFabColors = new Integer[6];
            mFabColors[0] = getResources().getColor(R.color.colorAccentIncome);
            mFabColors[1] = getResources().getColor(R.color.colorAccentExpenditure);
            mFabColors[2] = getResources().getColor(R.color.colorAccentLoans);
            mFabColors[3] = getResources().getColor(R.color.colorAccentBudget);
            mFabColors[4] = getResources().getColor(R.color.colorAccentTips);
            mFabColors[5] = getResources().getColor(R.color.colorAccentSettings);

            mDarkColors = new Integer[6];
            mDarkColors[0] = R.color.colorPrimaryDarkIncome;
            mDarkColors[1] = R.color.colorPrimaryDarkExpenditure;
            mDarkColors[2] = R.color.colorPrimaryDarkLoans;
            mDarkColors[3] = R.color.colorPrimaryDarkBudget;
            mDarkColors[4] = R.color.colorPrimaryDarkTips;
            mDarkColors[5] = R.color.colorPrimaryDarkSettings;

            OnboarderPage income = new OnboarderPage("Income", "How much do you make at the end of the month?", R.drawable.ic_income);
            income.setBackgroundColor(R.color.colorPrimaryIncome);


            OnboarderPage expenditure = new OnboarderPage("Expenditure", "Know how you spend your income. Y2K will help you plan your expenses well.", R.drawable.ic_expenditure);
            expenditure.setBackgroundColor(R.color.colorPrimaryExpenditure);

            OnboarderPage loans = new OnboarderPage("Loans", "Do you like giving out loans or taking one? Y2K will help you keep track of both.", R.drawable.ic_loan);
            loans.setBackgroundColor(R.color.colorPrimaryLoans);

            OnboarderPage budget = new OnboarderPage("Budget", "Do you want to budget out your income? Fear not! Y2K is there for you.", R.drawable.ic_budget);
            budget.setBackgroundColor(R.color.colorPrimaryBudget);

            OnboarderPage configuration = new OnboarderPage("Configuration", "Let Y2K know your currency and time configurations.", R.drawable.ic_configuration);
            configuration.setBackgroundColor(R.color.colorPrimarySettings);

            OnboarderPage tips = new OnboarderPage("Tips", "Tips on how to manage your money.", R.drawable.ic_tip);
            tips.setBackgroundColor(R.color.colorPrimaryTips);

            mOnBoarderPageList.add(income);
            mOnBoarderPageList.add(expenditure);
            mOnBoarderPageList.add(loans);
            mOnBoarderPageList.add(budget);
            mOnBoarderPageList.add(tips);
            mOnBoarderPageList.add(configuration);


            setContentView(R.layout.onboarding_activity);
            setStatusBackgroundColor(0);
            hideActionBar();
            mCircleIndicator = (CircleIndicatorView) findViewById(R.id.circle_indicator_view);
            ibNext = (ImageButton) findViewById(R.id.ib_next);
            btnSkip = (Button) findViewById(R.id.btn_skip);
            btnFinish = (Button) findViewById(R.id.btn_finish);
            buttonsLayout = (FrameLayout) findViewById(R.id.buttons_layout);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            divider = findViewById(R.id.divider);
            vpOnboarderPager = (ViewPager) findViewById(com.chyrta.onboarder.R.id.vp_onboarder_pager);
            vpOnboarderPager.addOnPageChangeListener(this);
            ibNext.setOnClickListener(this);
            btnSkip.setOnClickListener(this);
            btnFinish.setOnClickListener(this);
            fab.setOnClickListener(this);
            evaluator = new ArgbEvaluator();

            shouldUseFloatingActionButton(true);
            setOnboardPagesReady();
        }
    }

    private void startDashboard() {
        Intent intent = new Intent(this, DashBoard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setOnboardPagesReady() {
        onboarderAdapter = new OnBoarderAdapter(mOnBoarderPageList, getSupportFragmentManager());
        vpOnboarderPager.setAdapter(onboarderAdapter);
        mColors = ColorsArrayBuilder.getPageBackgroundColors(this, mOnBoarderPageList);
        mCircleIndicator.setPageIndicators(mOnBoarderPageList.size());
    }

    private void setInactiveIndicatorColor(int color) {
        this.mCircleIndicator.setInactiveIndicatorColor(color);
    }

    private void setActiveIndicatorColor(int color) {
        this.mCircleIndicator.setActiveIndicatorColor(color);
    }

    private void shouldDarkenButtonsLayout(boolean shouldDarkenButtonsLayout) {
        this.shouldDarkenButtonsLayout = shouldDarkenButtonsLayout;
    }

    private void setDividerColor(@ColorInt int color) {
        if (!this.shouldDarkenButtonsLayout)
            this.divider.setBackgroundColor(color);
    }

    private void setDividerHeight(int heightInDp) {
        if (!this.shouldDarkenButtonsLayout)
            this.divider.getLayoutParams().height =
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightInDp,
                            getResources().getDisplayMetrics());
    }

    public void setDividerVisibility(int dividerVisibility) {
        this.divider.setVisibility(dividerVisibility);
    }

    public void setSkipButtonTitle(CharSequence title) {
        this.btnSkip.setText(title);
    }

    public void setSkipButtonHidden() {
        this.btnSkip.setVisibility(View.GONE);
    }

    public void setSkipButtonTitle(@StringRes int titleResId) {
        this.btnSkip.setText(titleResId);
    }

    public void setFinishButtonTitle(CharSequence title) {
        this.btnFinish.setText(title);
    }

    public void setFinishButtonTitle(@StringRes int titleResId) {
        this.btnFinish.setText(titleResId);
    }

    public void shouldUseFloatingActionButton(boolean shouldUseFloatingActionButton) {

        this.shouldUseFloatingActionButton = shouldUseFloatingActionButton;

        if (shouldUseFloatingActionButton) {
            this.fab.setVisibility(View.VISIBLE);
            this.setDividerVisibility(View.GONE);
            this.shouldDarkenButtonsLayout(false);
            this.btnFinish.setVisibility(View.GONE);
            this.btnSkip.setVisibility(View.GONE);
            this.ibNext.setVisibility(View.GONE);
            this.ibNext.setFocusable(false);
            this.buttonsLayout.getLayoutParams().height =
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
                            getResources().getDisplayMetrics());
        }

    }

    private int darkenColor(@ColorInt int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }

    public void setStatusBackgroundColor(int mPosition) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, mDarkColors[mPosition]));
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        boolean isInLastPage = vpOnboarderPager.getCurrentItem() == onboarderAdapter.getCount() - 1;
        if (i == R.id.ib_next || i == R.id.fab && !isInLastPage) {
            vpOnboarderPager.setCurrentItem(vpOnboarderPager.getCurrentItem() + 1);
        } else if (i == R.id.btn_skip) {
            onSkipButtonPressed();
        } else if (i == R.id.btn_finish || i == R.id.fab) {
            onFinishButtonPressed();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position < (onboarderAdapter.getCount() - 1) && position < (mColors.length - 1)) {
            vpOnboarderPager.setBackgroundColor((Integer) evaluator.evaluate(positionOffset, mColors[position], mColors[position + 1]));
            if (shouldDarkenButtonsLayout) {
                buttonsLayout.setBackgroundColor(darkenColor((Integer) evaluator.evaluate(positionOffset, mColors[position], mColors[position + 1])));
                divider.setVisibility(View.GONE);
            }
        } else {
            vpOnboarderPager.setBackgroundColor(mColors[mColors.length - 1]);
            if (shouldDarkenButtonsLayout) {
                buttonsLayout.setBackgroundColor(darkenColor(mColors[mColors.length - 1]));
                divider.setVisibility(View.GONE);
            }
        }
    }

    public void onFinishButtonPressed() {
        mStateManager.saveFirstRun(false);
        startDashboard();
    }

    @Override
    public void onPageSelected(int position) {
        //setTheme_(position);
        setStatusBackgroundColor(position);
        ColorStateList colorStateList = new ColorStateList(states, new int[]{mFabColors[position], mFabColors[position]});
        fab.setBackgroundTintList(colorStateList);
        int lastPagePosition = onboarderAdapter.getCount() - 1;
        mCircleIndicator.setCurrentPage(position);
        ibNext.setVisibility(position == lastPagePosition && !this.shouldUseFloatingActionButton ? View.GONE : View.VISIBLE);
        btnFinish.setVisibility(position == lastPagePosition && !this.shouldUseFloatingActionButton ? View.VISIBLE : View.GONE);
        if (this.shouldUseFloatingActionButton)
            this.fab.setImageResource(position == lastPagePosition ? com.chyrta.onboarder.R.drawable.ic_done_white_24dp : com.chyrta.onboarder.R.drawable.ic_arrow_forward_white_24dp);
    }

    private void setTheme_(int position) {
        int theme = R.style.Income;
        if (position == 1) {
            theme = R.style.Expenditure;
        } else if (position == 2) {
            theme = R.style.Loans;
        } else if (position == 3) {
            theme = R.style.Budget;
        } else if (position == 4) {
            theme = R.style.Settings;
        }
        super.setTheme(theme);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void hideActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    protected void onSkipButtonPressed() {
        vpOnboarderPager.setCurrentItem(onboarderAdapter.getCount());
    }


}

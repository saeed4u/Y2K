package glivion.y2k.ui.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chyrta.onboarder.OnboarderPage;

import glivion.y2k.R;
import glivion.y2k.ui.statemanager.Y2KStateManager;

/**
 * Created by saeedissah on 5/16/16.
 */
public class OnBoardFragment extends Fragment {

    private static final String ONBOARDER_PAGE_TITLE = "onboarder_page_title";
    private static final String ONBOARDER_PAGE_TITLE_RES_ID = "onboarder_page_title_res_id";
    private static final String ONBOARDER_PAGE_TITLE_COLOR = "onboarder_page_title_color";
    private static final String ONBOARDER_PAGE_TITLE_TEXT_SIZE = "onboarder_page_title_text_size";
    private static final String ONBOARDER_PAGE_DESCRIPTION = "onboarder_page_description";
    private static final String ONBOARDER_PAGE_DESCRIPTION_RES_ID = "onboarder_page_description_res_id";
    private static final String ONBOARDER_PAGE_DESCRIPTION_COLOR = "onborader_page_description_color";
    private static final String ONBOARDER_PAGE_DESCRIPTION_TEXT_SIZE = "onboarder_page_description_text_size";
    private static final String ONBOARDER_PAGE_IMAGE_RES_ID = "onboarder_page_iamge_res_id";
    private static final String ONBOARDER_PAGE_CURRENCY = "onboarder_page_currency";
    private static final String ONBOARDER_PAGE_TIME_CONF = "onboarder_page_time_conf";

    private String onboarderTitle;
    private String onboarderDescription;
    @StringRes
    private int onboarderTitleResId;
    @ColorRes
    private int onboarderTitleColor;
    @StringRes
    private int onboarderDescriptionResId;
    @ColorRes
    private int onboarderDescriptionColor;
    @DrawableRes
    private int onboarderImageResId;
    private float onboarderTitleTextSize;
    private float onboarderDescriptionTextSize;

    private View onboarderView;
    private ImageView ivOnboarderImage;
    private TextView tvOnboarderTitle;
    private TextView tvOnboarderDescription;

    private static String mCurrency;
    private static String mTimeConf;
    private Y2KStateManager mStateManager;

    public OnBoardFragment() {
    }

    public static OnBoardFragment newInstance(OnboarderPage page) {
        Bundle args = new Bundle();
        args.putString(ONBOARDER_PAGE_TITLE, page.getTitle());
        args.putString(ONBOARDER_PAGE_DESCRIPTION, page.getDescription());
        args.putInt(ONBOARDER_PAGE_TITLE_RES_ID, page.getTitleResourceId());
        args.putInt(ONBOARDER_PAGE_DESCRIPTION_RES_ID, page.getDescriptionResourceId());
        args.putInt(ONBOARDER_PAGE_TITLE_COLOR, page.getTitleColor());
        args.putInt(ONBOARDER_PAGE_DESCRIPTION_COLOR, page.getDescriptionColor());
        args.putInt(ONBOARDER_PAGE_IMAGE_RES_ID, page.getImageResourceId());
        args.putFloat(ONBOARDER_PAGE_TITLE_TEXT_SIZE, page.getTitleTextSize());
        args.putString(ONBOARDER_PAGE_CURRENCY, mCurrency);
        args.putString(ONBOARDER_PAGE_TIME_CONF, mTimeConf);
        args.putFloat(ONBOARDER_PAGE_DESCRIPTION_TEXT_SIZE, page.getDescriptionTextSize());
        OnBoardFragment fragment = new OnBoardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStateManager = new Y2KStateManager(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        onboarderTitle = bundle.getString(ONBOARDER_PAGE_TITLE, null);
        onboarderTitleResId = bundle.getInt(ONBOARDER_PAGE_TITLE_RES_ID, 0);
        onboarderTitleColor = bundle.getInt(ONBOARDER_PAGE_TITLE_COLOR, 0);
        onboarderTitleTextSize = bundle.getFloat(ONBOARDER_PAGE_TITLE_TEXT_SIZE, 0f);
        onboarderDescription = bundle.getString(ONBOARDER_PAGE_DESCRIPTION, null);
        onboarderDescriptionResId = bundle.getInt(ONBOARDER_PAGE_DESCRIPTION_RES_ID, 0);
        onboarderDescriptionColor = bundle.getInt(ONBOARDER_PAGE_DESCRIPTION_COLOR, 0);
        onboarderDescriptionTextSize = bundle.getFloat(ONBOARDER_PAGE_DESCRIPTION_TEXT_SIZE, 0f);
        onboarderImageResId = bundle.getInt(ONBOARDER_PAGE_IMAGE_RES_ID, 0);
        mCurrency = bundle.getString(ONBOARDER_PAGE_CURRENCY, "GHC");
        mTimeConf = bundle.getString(ONBOARDER_PAGE_TIME_CONF, "Weekly");

        onboarderView = inflater.inflate(R.layout.fragment_onboarder, container, false);
        View mChooseCurrency = onboarderView.findViewById(R.id.scroll_view);
        RadioButton mGhc = (RadioButton) onboarderView.findViewById(R.id.ghc);
        mGhc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCurrency = "GHC";

                }
            }
        });
        RadioButton mDollar = (RadioButton) onboarderView.findViewById(R.id.dollar);
        mDollar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCurrency = "$";
                }
            }
        });

        RadioButton mWeekly = (RadioButton) onboarderView.findViewById(R.id.weekly);
        mWeekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTimeConf = "Weekly";
                }
            }
        });
        RadioButton mMonthly = (RadioButton) onboarderView.findViewById(R.id.monthly);
        mMonthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTimeConf = "Monthly";
                }
            }
        });
        if (onboarderTitle.equals("Configuration")) {
            mChooseCurrency.setVisibility(View.VISIBLE);
        } else {
            mChooseCurrency.setVisibility(View.GONE);
        }

        ivOnboarderImage = (ImageView) onboarderView.findViewById(R.id.iv_onboarder_image);
        tvOnboarderTitle = (TextView) onboarderView.findViewById(R.id.tv_onboarder_title);
        tvOnboarderDescription = (TextView) onboarderView.findViewById(R.id.tv_onboarder_description);

        if (onboarderTitle != null) {
            tvOnboarderTitle.setText(onboarderTitle);
        }

        if (onboarderTitleResId != 0) {
            tvOnboarderTitle.setText(getResources().getString(onboarderTitleResId));
        }

        if (onboarderDescription != null) {
            tvOnboarderDescription.setText(onboarderDescription);
        }

        if (onboarderDescriptionResId != 0) {
            tvOnboarderDescription.setText(getResources().getString(onboarderDescriptionResId));
        }

        if (onboarderTitleColor != 0) {
            tvOnboarderTitle.setTextColor(ContextCompat.getColor(getActivity(), onboarderTitleColor));
        }

        if (onboarderDescriptionColor != 0) {
            tvOnboarderDescription.setTextColor(ContextCompat.getColor(getActivity(), onboarderDescriptionColor));
        }

        if (onboarderImageResId != 0) {
            ivOnboarderImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), onboarderImageResId));
        }

        if (onboarderTitleTextSize != 0f) {
            tvOnboarderTitle.setTextSize(onboarderTitleTextSize);
        }

        if (onboarderDescriptionTextSize != 0f) {
            tvOnboarderDescription.setTextSize(onboarderDescriptionTextSize);
        }

        return onboarderView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mStateManager.saveCurrency(mCurrency);
        mStateManager.saveQueryType(mTimeConf);
    }

}

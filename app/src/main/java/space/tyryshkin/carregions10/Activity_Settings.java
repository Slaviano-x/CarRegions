package space.tyryshkin.carregions10;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class Activity_Settings extends AppCompatActivity {

    Window window;

    private RelativeLayout themeStroke, aboutStroke, estimateStroke, feedbackStroke;
    private TextView theme_sub_text;

    private ImageButton btn_back;

    RadioButton radioButtonTheme;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String theme = "";

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        window = this.getWindow();

        sharedPreferences = getSharedPreferences(Enum_Constant_Settings.APP_SETTING_MODE.getString(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        theme = sharedPreferences.getString(Enum_Constant_Settings.APP_THEME.getString(), "");

        if (theme.equals("") && AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.ThemeCarRegionsDark);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        } else if (theme.equals("Темная тема")) {
            setTheme(R.style.ThemeCarRegionsDark);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        } else {
            setTheme(R.style.ThemeCarRegionsLight);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.green_500));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
        onClicks();
        onTouches();
    }

    private void init() {

        btn_back = findViewById(R.id.btn_back);
        themeStroke = findViewById(R.id.themeStroke);
        theme_sub_text = findViewById(R.id.theme_sub_text);
        aboutStroke = findViewById(R.id.aboutStroke);
        estimateStroke = findViewById(R.id.estimateStroke);
        feedbackStroke = findViewById(R.id.feedbackStroke);

        if (theme.equals("Темная тема")) {
            theme_sub_text.setText("Темная тема");
        } else {
            theme_sub_text.setText("Светлая тема");
        }
    }
    private void onClicks() {
        btn_back.setOnClickListener((view) -> {
            onBackPressed();
        });
        themeStroke.setOnClickListener((view) -> {
            openDialogChangeTheme();
        });
        aboutStroke.setOnClickListener((view) -> {
            openDialogAboutApp();
        });
        estimateStroke.setOnClickListener((view) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=space.tyryshkin.carregions10"));
            intent.setPackage("com.android.vending");
            startActivity(intent);
        });
        feedbackStroke.setOnClickListener((view) -> {
            String[] emailSupport = {"support@jkarta.ru"};
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, emailSupport);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Авто коды регионов России");

            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Launch email"));
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceType"})
    private void onTouches() {
        //noinspection AndroidLintClickableViewAccessibility
        btn_back.setOnTouchListener((view, motionEvent) -> {
            return setTouching(btn_back, motionEvent, R.drawable.fon_primary_300, R.attr.ripple_300, R.attr.theme_color);
        });
        //noinspection AndroidLintClickableViewAccessibility
        themeStroke.setOnTouchListener((view, motionEvent) -> {
            return setTouching(themeStroke, motionEvent, R.drawable.fon_primary_100, R.attr.ripple_100, R.attr.fon_element_color);
        });
        //noinspection AndroidLintClickableViewAccessibility
        aboutStroke.setOnTouchListener((view, motionEvent) -> {
            return setTouching(aboutStroke, motionEvent, R.drawable.fon_primary_100, R.attr.ripple_100, R.attr.fon_element_color);
        });
        //noinspection AndroidLintClickableViewAccessibility
        estimateStroke.setOnTouchListener((view, motionEvent) -> {
            return setTouching(estimateStroke, motionEvent, R.drawable.fon_primary_100, R.attr.ripple_100, R.attr.fon_element_color);
        });
        //noinspection AndroidLintClickableViewAccessibility
        feedbackStroke.setOnTouchListener((view, motionEvent) -> {
            return setTouching(feedbackStroke, motionEvent, R.drawable.fon_primary_100, R.attr.ripple_100, R.attr.fon_element_color);
        });
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void openDialogChangeTheme() {
        Dialog dialog = createDialog(R.layout.dialog_change_theme);
        placeDialogBottom(dialog);

        RadioGroup radio_group_list_of_theme = dialog.findViewById(R.id.radio_group_list_of_theme);
        RadioButton radio_btn_light = dialog.findViewById(R.id.radio_btn_light);
        RadioButton radio_btn_dark = dialog.findViewById(R.id.radio_btn_dark);
        MaterialButton cancel = dialog.findViewById(R.id.btn_cancel);

        if (sharedPreferences.getString(Enum_Constant_Settings.APP_THEME.getString(), "").equals("Темная тема")) {
            radio_btn_dark.setChecked(true);
        } else {
            radio_btn_light.setChecked(true);
        }

        createColorOfRadioButton(radio_btn_light);
        createColorOfRadioButton(radio_btn_dark);

        radio_group_list_of_theme.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (radioGroup, position) -> {
            radioButtonTheme = (RadioButton) radioGroup.findViewById(position);

            editor.putString(Enum_Constant_Settings.APP_THEME.getString(), radioButtonTheme.getText().toString());
            editor.apply();

            if (radioButtonTheme.getText().toString().equals("Светлая тема")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            resetActivity(R.style.ThemeCarRegionsDark);
        });

        cancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void createColorOfRadioButton(RadioButton radioButton) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        getResources().getColor(R.color.dark_grey),
                        getResources().getColor(R.color.green_500),
                }
        );
        radioButton.setButtonTintList(colorStateList);
        radioButton.invalidate();
    }

    private void openDialogAboutApp() {
        Dialog dialog = createDialog(R.layout.dialog_about_app);
        placeDialogBottom(dialog);

        MaterialButton understand = dialog.findViewById(R.id.btn_understand);

        understand.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Dialog createDialog(int layout) {
        Dialog dialog = new Dialog(Activity_Settings.this);
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.fon_with_margin));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        return dialog;
    }

    private void placeDialogBottom(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();

        windowParams.gravity = Gravity.BOTTOM;
        window.setAttributes(windowParams);
    }
    private boolean setTouching(View view, MotionEvent event, int drawable, int attributeDown, int attributeUp) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            view.setBackgroundDrawable(wrapDrawable(drawable, attributeDown));
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            view.setBackgroundDrawable(wrapDrawable(drawable, attributeUp));
        }
        return false;
    }

    private int applyAttribute(int attribute) {
        TypedValue typedValue = new TypedValue();
        this.getTheme().resolveAttribute(attribute, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }
    private Drawable wrapDrawable(int drawable, int attribute) {
        Drawable wrappedDrawable = DrawableCompat.wrap(getResources().getDrawable(drawable));
        DrawableCompat.setTint(wrappedDrawable, applyAttribute(attribute));
        return wrappedDrawable;
    }

    private void resetActivity(int themeCarRegionsDark) {
        Intent intent = new Intent(getApplicationContext(), Activity_Settings.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Activity_Main.class);
        startActivity(intent);
    }
}
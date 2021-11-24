package space.tyryshkin.carregions10;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Activity_Main extends AppCompatActivity {
    Window window;

    RelativeLayout relative;

    private RelativeLayout layout_image;
    private ImageButton btn_settings;

    private ProgressDialog progressDialog;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CodeDatabaseHelper myDB;

    public static Activity_Main instance;

    private FirebaseDatabase database;
    private DatabaseReference myDataCodesAndRegions;
    private DatabaseReference myVersion;

    public Map<String, String> code_region = new HashMap<>();
    public Map<String, String> region_cities = new HashMap<>();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int version;
    private int versionTemp;
    private String theme = "";

    boolean isInserted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        window = this.getWindow();

        sharedPreferences = getSharedPreferences(Enum_Constant_Settings.APP_SETTING_MODE.getString(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

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
        setContentView(R.layout.activity_main);

        initialisation();
        onClicks();
        onTouches();
        createProgressDialog();

        if (isNetworkConnected()) {
            new LoadDataBase().execute();
        } else {
            if (version != 0) {
                myDB = new CodeDatabaseHelper(getApplicationContext(), version);

                finishLoad();
            } else {
                createSnackBarError();
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void initialisation() {
        instance = this;
        layout_image = findViewById(R.id.layout_image);
        btn_settings = findViewById(R.id.btn_settings);

        relative = findViewById(R.id.relative);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        database = FirebaseDatabase.getInstance();
        myDataCodesAndRegions = database.getReference(Enum_Constant_Firebase.FIREBASE_REF_LIST.getString());
        myVersion = database.getReference(Enum_Constant_Firebase.FIREBASE_REF_VERSION.getString());

        version = sharedPreferences.getInt(Enum_Constant_Settings.APP_VERSION.getString(), 0);
    }
    private void onClicks() {
        btn_settings.setOnClickListener(view -> {
            Intent intent = new Intent(Activity_Main.instance, Activity_Settings.class);
            startActivity(intent);
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void onTouches() {
        //noinspection AndroidLintClickableViewAccessibility
        btn_settings.setOnTouchListener((view, motionEvent) -> {
            return setTouching(btn_settings, motionEvent, R.drawable.fon_primary_300, R.attr.ripple_300, R.attr.theme_color);
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void createTabLayouts() {
        tabLayout.setupWithViewPager(viewPager);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new Fragment_Code(), getString(R.string.code_region));
        pagerAdapter.addFragment(new Fragment_Region(), getString(R.string.region_code));
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setBackgroundColor(applyAttribute(R.attr.theme_color));

        tabLayout.setSelectedTabIndicatorColor(applyAttribute(R.attr.indicator));
        tabLayout.setTabTextColors(applyAttribute(R.attr.ripple_100), getColor(R.color.white));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideKeyBoard();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadDataBase extends AsyncTask<Integer, Void, Boolean> {

        @SuppressLint("ClickableViewAccessibility")
        protected void onPreExecute() {

        }

        protected Boolean doInBackground(Integer... integers) {
                myVersion.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        versionTemp = Integer.parseInt(Objects.requireNonNull(snapshot.getValue()).toString());

                        if (versionTemp != version) {
                            version = versionTemp;
                            editor.putInt(Enum_Constant_Settings.APP_VERSION.getString(), versionTemp);
                            editor.apply();

                            myDB = new CodeDatabaseHelper(getApplicationContext(), version);

                            updateDatabaseToInnerSQL();
                        } else {
                            myDB = new CodeDatabaseHelper(getApplicationContext(), version);
                        }
                        finishLoad();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            return isInserted;
        }

        protected void onPostExecute(Boolean success) {

        }
    }

    private void updateDatabaseToInnerSQL() {
        myDataCodesAndRegions.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                code_region.clear();
                region_cities.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String codes = ds.child(Enum_Constant_Firebase.FIREBASE_LIST_CHILD_CODE.getString()).getValue(String.class);
                    String region = ds.child(Enum_Constant_Firebase.FIREBASE_LIST_CHILD_REGION.getString()).getValue(String.class);
                    String cities = ds.child(Enum_Constant_Firebase.FIREBASE_LIST_CHILD_CITY.getString()).getValue(String.class);
                    assert codes != null;
                    if (codes.contains(";")) {
                        String[] codeArray = codes.split(";");
                        for (String array : codeArray) {
                            code_region.put(array, region);

                            if (cities != null) {
                                region_cities.put(region, cities);
                            } else {
                                region_cities.put(region, "");
                            }
                        }
                    } else {
                        code_region.put(codes, region);

                        if (cities != null) {
                            region_cities.put(region, cities);
                        } else {
                            region_cities.put(region, "");
                        }
                    }
                }
                isInserted = myDB.jsonParse(code_region, region_cities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }

    private void finishLoad() {
        tabLayout.setVisibility(View.VISIBLE);
        layout_image.setVisibility(View.VISIBLE);
        createTabLayouts();
        progressDialog.dismiss();
    }

    private void createSnackBarError() {
        Snackbar snackbar = Snackbar.make(relative,
                getResources().getString(R.string.error_internet),
                Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.setTextColor(getColor(R.color.white));
        snackbar.setBackgroundTint(getColor(R.color.red));
        snackbar.show();
    }

    //Проверка на интернет соединение
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return  connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(Activity_Main.instance);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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

    public static void hideKeyBoard() {
        View keyBoard = instance.getCurrentFocus();
        if (keyBoard != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) instance.getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(keyBoard.getWindowToken(), 0);
        }
    }
}
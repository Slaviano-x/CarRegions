package space.tyryshkin.carregions10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;

public class Activity_Main extends AppCompatActivity {

    ProgressDialog progressDialog;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SQLiteDatabase db;

    public static Activity_Main instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new LoadDataBase().execute();
    }

    private void initialisation() {
        instance = this;
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void createTabLayouts() {
        tabLayout.setupWithViewPager(viewPager);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new Fragment_Code(), getString(R.string.code_region));
        pagerAdapter.addFragment(new Fragment_Region(), getString(R.string.region_code));
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setBackgroundColor(getColor(R.color.green_500));

        tabLayout.setSelectedTabIndicatorColor(getColor(R.color.white));
        tabLayout.setTabTextColors(getColor(R.color.teal_100), getColor(R.color.white));

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
            initialisation();

            progressDialog = new ProgressDialog(Activity_Main.instance);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        protected Boolean doInBackground(Integer... integers) {
            CodeDatabaseHelper codeDatabaseHelper = new CodeDatabaseHelper(Activity_Main.this);
            try {
                db = codeDatabaseHelper.getWritableDatabase();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            tabLayout.setVisibility(View.VISIBLE);
            createTabLayouts();
            progressDialog.dismiss();
            if (!success) {
                Toast.makeText(Activity_Main.this, "Ошибка в работе с базой данных", Toast.LENGTH_SHORT).show();
            }
        }
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
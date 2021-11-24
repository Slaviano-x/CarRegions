package space.tyryshkin.carregions10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_Region extends Fragment {

    String myCode = "";
    String myRegion = "";
    String muCity = "";

    private TextInputLayout inputLayout;
    private AutoCompleteTextView name_of_place_editText;
    private TextView place_of_search;
    private GridLayout gridLayout;

    private View view;

    private final Map<String, String> mapCodesAndRegions = new HashMap<>();
    private final Map<String, String> mapRegionsAndCities = new HashMap<>();
    private final List<String> listOfRegions = new ArrayList<>();
    private final List<String> listOfCities = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private int version;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_region, container, false);

        initialization();

        name_of_place_editText.setOnEditorActionListener(editorListener);
        name_of_place_editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chosenPlace = String.valueOf(parent.getItemAtPosition(position));
                validateEditText();
                gridLayout.removeAllViews();

                setTextRegion(chosenPlace);

                Activity_Main.hideKeyBoard();

                findCode(chosenPlace, listOfCities.contains(chosenPlace));
            }
        });
        //noinspection AndroidLintClickableViewAccessibility
        name_of_place_editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //обработка нажатия
                    setAutoCompleteForEditText();
                }
                return false;
            }
        });

        inputLayout.setEndIconOnClickListener(v -> {
            name_of_place_editText.setText("");
            place_of_search.setText("");
            gridLayout.removeAllViews();
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
        });

        return view;
    }

    private void initialization() {
        inputLayout = view.findViewById(R.id.inputLayout);
        name_of_place_editText = view.findViewById(R.id.name_of_place_editText);
        place_of_search = view.findViewById(R.id.place_of_search);
        gridLayout = view.findViewById(R.id.gridLayout);

        sharedPreferences = this.requireActivity().getSharedPreferences(Enum_Constant_Settings.APP_SETTING_MODE.getString(), Context.MODE_PRIVATE);
        version = sharedPreferences.getInt(Enum_Constant_Settings.APP_VERSION.getString(), 0);
    }

    private final TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (validateEditText()) {
                    String chosenPlace = name_of_place_editText.getText().toString();
                    gridLayout.removeAllViews();
                    name_of_place_editText.dismissDropDown();

                    Activity_Main.hideKeyBoard();

                    setTextRegion(chosenPlace);

                    findCode(chosenPlace, listOfCities.contains(chosenPlace));
                }
            }
            return false;
        }
    };

    private void setTextRegion(String chosenPlace) {
        if (listOfCities.contains(chosenPlace)) {
            for (Map.Entry<String, String> entry : mapRegionsAndCities.entrySet()) {
                String[] array = entry.getValue().split(";");
                ArrayList<String> list = new ArrayList<>(Arrays.asList(array));
                if (list.contains(chosenPlace)) {
                    place_of_search.setText(entry.getKey());
                }
            }
        } else {
            place_of_search.setText(chosenPlace);
        }
    }

    private boolean validateEditText() {
        String chosenPlace = name_of_place_editText.getText().toString();
        if (!listOfRegions.contains(chosenPlace)) {
            if (!listOfCities.contains(chosenPlace)) {
                inputLayout.setError(getString(R.string.inputError));
                inputLayout.setErrorIconDrawable(null);
                return false;
            } else {
                inputLayout.setError(null);
                inputLayout.setErrorEnabled(false);
                return true;
            }
        } else {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private void findCode(String chosenPlace, boolean isCity) {
        ArrayList<Integer> listOfCodes = new ArrayList<>();

        if (!isCity) {
            for (Map.Entry<String, String> entry : mapCodesAndRegions.entrySet()) {
                if (entry.getValue().equals(chosenPlace)) {
                    listOfCodes.add(Integer.parseInt(entry.getKey()));
                }
            }
        } else {
            for (Map.Entry<String, String> entry : mapRegionsAndCities.entrySet()) {
                String[] array = entry.getValue().split(";");
                ArrayList<String> list = new ArrayList<>(Arrays.asList(array));
                if (list.contains(chosenPlace)) {
                    for (Map.Entry<String, String> entr : mapCodesAndRegions.entrySet()) {
                        if (entr.getValue().equals(entry.getKey())) {
                            listOfCodes.add(Integer.parseInt(entr.getKey()));
                        }
                    }
                    break;
                }
            }
        }
        createViewForGridLayout(sortArray(listOfCodes));
    }

    private ArrayList<Integer> sortArray(ArrayList<Integer> listOfCodes) {
        for (int i = 0; i < listOfCodes.size() - 1; i++) {
            int helper;
            int min = listOfCodes.get(i);

            for (int j = i + 1; j < listOfCodes.size(); j++) {
                if (listOfCodes.get(j) < min) {
                    min = listOfCodes.get(j);
                    helper = listOfCodes.get(j);
                    listOfCodes.remove(j);
                    listOfCodes.add(i, helper);
                }
            }
        }
        return listOfCodes;
    }

    @SuppressLint("ResourceType")
    private void createViewForGridLayout(ArrayList<Integer> listOfCodes) {
        for (int i = 0; i < listOfCodes.size(); i++) {
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 0f),
                    GridLayout.spec(GridLayout.UNDEFINED,1f));
            RelativeLayout relativeLayout = new RelativeLayout(getActivity());
            relativeLayout.setLayoutParams(gridLayoutParam);

            RelativeLayout.LayoutParams paramImage =
                    new RelativeLayout.LayoutParams(300, 300);
            paramImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
            ImageView fon = new ImageView(getActivity());
            fon.setId(100);
            fon.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.number_fon));
            fon.setLayoutParams(paramImage);

            RelativeLayout.LayoutParams param =
                    new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            param.addRule(RelativeLayout.ALIGN_TOP, 100);
            param.addRule(RelativeLayout.CENTER_HORIZONTAL);
            param.topMargin = 72;
            param.bottomMargin = 16;
            TextView myCode = new TextView(getActivity());

            String text = String.valueOf(listOfCodes.get(i));
            if (text.length() == 1) {
                text = "0" + text;
            }
            myCode.setText(text);
            myCode.setTextSize(54);
            Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.road_numbers);
            myCode.setTypeface(typeface);
            myCode.setTextColor(getResources().getColor(R.color.black));
            myCode.setLayoutParams(param);

            gridLayout.addView(relativeLayout);
            relativeLayout.addView(fon);
            relativeLayout.addView(myCode);
        }
    }

    private void setAutoCompleteForEditText() {
        try {
            SQLiteOpenHelper codeDatabaseHelper = new CodeDatabaseHelper(getActivity(), version);
            SQLiteDatabase db = codeDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("CODES",
                    new String[]{"CODE", "REGION", "CITIES"},
                    null, null, null, null, null);

            while (cursor.moveToNext()) {
                mapCodesAndRegions.put(cursor.getString(0), cursor.getString(1));
                mapRegionsAndCities.put(cursor.getString(1), cursor.getString(2));
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(getActivity(), "Ошибка в работе с базой данных", Toast.LENGTH_SHORT).show();
        }
        for (Map.Entry<String, String> entry : mapCodesAndRegions.entrySet()) {
            if (!listOfRegions.contains(entry.getValue())) {
                listOfRegions.add(entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : mapRegionsAndCities.entrySet()) {
            String[] list = entry.getValue().split(";");

            for (String string : list) {
                if (!listOfCities.contains(string)) {
                    listOfCities.add(string);
                }
            }
        }
        listOfRegions.remove("Территория за пределами РФ");
        listOfRegions.remove("Не выпускается с 2000 года (ранее Чеченская Республика)");
        listOfCities.remove("Москва");
        listOfCities.remove("Санкт-Петербург");
        listOfCities.remove("Севастополь");

        Collections.sort(listOfRegions);
        Collections.sort(listOfCities);

        ArrayList<String> listOfRegionsAndCities = new ArrayList<>();
        listOfRegionsAndCities.addAll(listOfRegions);
        listOfRegionsAndCities.addAll(listOfCities);
        AutoCompleteSuggestAdapter adapter = new AutoCompleteSuggestAdapter(getActivity(), android.R.layout.simple_list_item_1, listOfRegionsAndCities);
        name_of_place_editText.setAdapter(adapter);
    }
}
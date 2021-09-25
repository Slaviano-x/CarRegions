package space.tyryshkin.carregions10;

import android.annotation.SuppressLint;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import space.tyryshkin.carregions10.R;

public class Fragment_Region extends Fragment {

    private TextInputLayout inputLayout;
    private AutoCompleteTextView name_of_region_editText;
    private TextView region;
    private GridLayout gridLayout;

    private View view;

    private final Map<String, String> mapCodesAndRegions = new HashMap<>();
    private final List<String> listOfRegions = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_region, container, false);


        initialization();

        name_of_region_editText.setOnEditorActionListener(editorListener);
        name_of_region_editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chosenRegion = String.valueOf(parent.getItemAtPosition(position));
                validateEditText();
                gridLayout.removeAllViews();
                region.setText(chosenRegion);

                Activity_Main.hideKeyBoard();

                findCode(chosenRegion);
            }
        });
        //noinspection AndroidLintClickableViewAccessibility
        name_of_region_editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //обработка нажатия
                    setAutoCompleteForEditText();
                }
                return false;
            }
        });

        inputLayout.setEndIconOnClickListener(v -> {
            name_of_region_editText.setText("");
            region.setText("");
            gridLayout.removeAllViews();
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
        });

        return view;
    }

    private void initialization() {
        inputLayout = view.findViewById(R.id.inputLayout);
        name_of_region_editText = view.findViewById(R.id.name_of_region_editText);
        region = view.findViewById(R.id.region);
        gridLayout = view.findViewById(R.id.gridLayout);
    }

    private final TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (validateEditText()) {
                    String chosenRegion = name_of_region_editText.getText().toString();
                    gridLayout.removeAllViews();
                    name_of_region_editText.dismissDropDown();

                    Activity_Main.hideKeyBoard();

                    region.setText(chosenRegion);
                    findCode(chosenRegion);
                }
            }
            return false;
        }
    };

    private boolean validateEditText() {
        String chosenRegion = name_of_region_editText.getText().toString();
        if (!listOfRegions.contains(chosenRegion)) {
            inputLayout.setError(getString(R.string.inputError));
            inputLayout.setErrorIconDrawable(null);
            return false;
        } else {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private void findCode(String chosenRegion) {
        ArrayList<Integer> listOfCodes = new ArrayList<>();
        for (Map.Entry<String, String> entry : mapCodesAndRegions.entrySet()) {
            if (entry.getValue().equals(chosenRegion)) {
                listOfCodes.add(Integer.parseInt(entry.getKey()));
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
            SQLiteOpenHelper codeDatabaseHelper = new CodeDatabaseHelper(getActivity());
            SQLiteDatabase db = codeDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("CODES",
                    new String[]{"CODE", "REGION"},
                    null, null, null, null, null);

            while (cursor.moveToNext()) {
                mapCodesAndRegions.put(cursor.getString(0), cursor.getString(1));
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
        AutoCompleteSuggestAdapter adapter = new AutoCompleteSuggestAdapter(getActivity(), android.R.layout.simple_list_item_1, listOfRegions);
        name_of_region_editText.setAdapter(adapter);
    }
}
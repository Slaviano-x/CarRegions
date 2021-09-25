package space.tyryshkin.carregions10;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import space.tyryshkin.carregions10.R;

public class Fragment_Code extends Fragment {
    private TextView code;
    private TextView region;

    private Button num1;
    private Button num2;
    private Button num3;
    private Button num4;
    private Button num5;
    private Button num6;
    private Button num7;
    private Button num8;
    private Button num9;
    private Button num0;
    private Button numC;
    private Button backspace;

    private String textOfCode = "";

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_code, container, false);

        initialisation();
        setOnClicks();

        return view;
    }

    private void initialisation() {
        code = view.findViewById(R.id.code);
        region = view.findViewById(R.id.region);
        num0 = view.findViewById(R.id.num0);
        num1 = view.findViewById(R.id.num1);
        num2 = view.findViewById(R.id.num2);
        num3 = view.findViewById(R.id.num3);
        num4 = view.findViewById(R.id.num4);
        num5 = view.findViewById(R.id.num5);
        num6 = view.findViewById(R.id.num6);
        num7 = view.findViewById(R.id.num7);
        num8 = view.findViewById(R.id.num8);
        num9 = view.findViewById(R.id.num9);
        numC = view.findViewById(R.id.numC);
        backspace = view.findViewById(R.id.backspace);
    }

    private void setOnClicks() {
        num0.setOnClickListener(v -> {
            if (textOfCode.equals("00")) {
                textOfCode = "0";
                code.setText(textOfCode);
            }
            if (textOfCode.length() < 3) {

                if (textOfCode.length() == 2) {
                    if (!textOfCode.equals("15") && !textOfCode.equals("19") &&
                            !textOfCode.equals("75") && !textOfCode.equals("79")) {
                        checkCode();
                    } else {
                        textOfCode = textOfCode + "0";
                        code.setText(textOfCode);
                    }
                } else {
                    textOfCode = textOfCode + "0";
                    code.setText(textOfCode);
                }
            }
            checkCode();
        });
        num1.setOnClickListener(v -> {
            if (textOfCode.equals("00")) {
                textOfCode = "1";
                code.setText(textOfCode);
            }
            if (textOfCode.length() < 3) {

                if (textOfCode.length() == 2) {
                    if (!textOfCode.equals("12") && !textOfCode.equals("16") && !textOfCode.equals("76")) {
                        checkCode();
                    } else {
                        textOfCode = textOfCode + "1";
                        code.setText(textOfCode);
                    }
                } else {
                    textOfCode = textOfCode + "1";
                    code.setText(textOfCode);
                }
            }
            checkCode();
        });
        num2.setOnClickListener(v -> {
            if (textOfCode.equals("00")) {
                textOfCode = "2";
                code.setText(textOfCode);
            }
            if (textOfCode.length() < 3) {

                if (textOfCode.length() == 2) {
                    if (!textOfCode.equals("10") && !textOfCode.equals("12") &&
                            !textOfCode.equals("14") && !textOfCode.equals("15") && !textOfCode.equals("70")) {
                        checkCode();
                    } else {
                        textOfCode = textOfCode + "2";
                        code.setText(textOfCode);
                    }
                } else {
                    textOfCode = textOfCode + "2";
                    code.setText(textOfCode);
                }
            }
            checkCode();
        });
        num3.setOnClickListener(v -> {
            if (textOfCode.equals("00")) {
                textOfCode = "3";
                code.setText(textOfCode);
            }
            if (textOfCode.length() < 3) {

                if (textOfCode.length() == 2) {
                    if (!textOfCode.equals("11") && !textOfCode.equals("12") &&
                            !textOfCode.equals("16") && !textOfCode.equals("17") &&
                            !textOfCode.equals("19") && !textOfCode.equals("76")) {
                        checkCode();
                    } else {
                        textOfCode = textOfCode + "3";
                        code.setText(textOfCode);
                    }
                } else {
                    textOfCode = textOfCode + "3";
                    code.setText(textOfCode);
                }
            }
            checkCode();
        });
        num4.setOnClickListener(v -> {
            if (textOfCode.equals("00")) {
                textOfCode = "4";
                code.setText(textOfCode);
            }
            if (textOfCode.length() < 3) {

                if (textOfCode.length() == 2) {
                    if (!textOfCode.equals("12") && !textOfCode.equals("13") &&
                            !textOfCode.equals("15") && !textOfCode.equals("16") &&
                            !textOfCode.equals("17") && !textOfCode.equals("77")) {
                        checkCode();
                    } else {
                        textOfCode = textOfCode + "4";
                        code.setText(textOfCode);
                    }
                } else {
                    textOfCode = textOfCode + "4";
                    code.setText(textOfCode);
                }
            }
            checkCode();
        });
        num5.setOnClickListener(v -> {
            if (textOfCode.equals("00")) {
                textOfCode = "5";
                code.setText(textOfCode);
            }
            if (textOfCode.length() < 3) {

                if (textOfCode.length() == 2) {
                    if (!textOfCode.equals("12")) {
                        checkCode();
                    } else {
                        textOfCode = textOfCode + "5";
                        code.setText(textOfCode);
                    }
                } else {
                    textOfCode = textOfCode + "5";
                    code.setText(textOfCode);
                }
            }
            checkCode();
        });
        num6.setOnClickListener(v -> {
            if (textOfCode.equals("00")) {
                textOfCode = "6";
                code.setText(textOfCode);
            }
            if (textOfCode.length() < 3) {
                if (textOfCode.length() == 2) {
                    if (!textOfCode.equals("11") && !textOfCode.equals("12") &&
                            !textOfCode.equals("13") && !textOfCode.equals("15") &&
                            !textOfCode.equals("18") && !textOfCode.equals("19") &&
                            !textOfCode.equals("71")) {
                        checkCode();
                    } else {
                        textOfCode = textOfCode + "6";
                        code.setText(textOfCode);
                    }
                } else {
                    textOfCode = textOfCode + "6";
                    code.setText(textOfCode);
                }
            }
            checkCode();
        });
        num7.setOnClickListener(v -> {
            if (textOfCode.equals("00")) {
                textOfCode = "7";
                code.setText(textOfCode);
            }
            if (textOfCode.length() < 3) {
                if (textOfCode.length() == 2) {
                    if (!textOfCode.equals("14") && !textOfCode.equals("17") &&
                            !textOfCode.equals("19") && !textOfCode.equals("77") &&
                            !textOfCode.equals("79")) {
                        checkCode();
                    } else {
                        textOfCode = textOfCode + "7";
                        code.setText(textOfCode);
                    }
                } else {
                    textOfCode = textOfCode + "7";
                    code.setText(textOfCode);
                }
            }
            checkCode();
        });
        num8.setOnClickListener(v -> {
            if (textOfCode.equals("00")) {
                textOfCode = "8";
                code.setText(textOfCode);
            }
            if (textOfCode.length() < 3) {
                if (textOfCode.length() == 2) {
                    if (!textOfCode.equals("13") && !textOfCode.equals("17") &&
                            !textOfCode.equals("19")) {
                        checkCode();
                    } else {
                        textOfCode = textOfCode + "8";
                        code.setText(textOfCode);
                    }
                } else {
                    textOfCode = textOfCode + "8";
                    code.setText(textOfCode);
                }
            }
            checkCode();
        });
        num9.setOnClickListener(v -> {
            if (textOfCode.equals("00")) {
                textOfCode = "9";
                code.setText(textOfCode);
            }
            if (textOfCode.length() < 3) {
                if (textOfCode.length() == 2) {
                    if (!textOfCode.equals("15") && !textOfCode.equals("19") &&
                            !textOfCode.equals("79")) {
                        checkCode();
                    } else {
                        textOfCode = textOfCode + "9";
                        code.setText(textOfCode);
                    }
                } else {
                    textOfCode = textOfCode + "9";
                    code.setText(textOfCode);
                }
            }
            checkCode();
        });
        numC.setOnClickListener(v -> {
            textOfCode = "";
            code.setText(textOfCode);
            region.setText("Введите код региона");
        });
        backspace.setOnClickListener(v -> backspace(textOfCode));

    }

    public void checkCode() {
        if (textOfCode.length() < 2) {
            region.setText("Введите код региона");
        } else {
            try {
                SQLiteOpenHelper codeDatabaseHelper = new CodeDatabaseHelper(getActivity());
                SQLiteDatabase db = codeDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.query("CODES",
                        new String[]{"CODE", "REGION"},
                        "CODE = ?", new String[]{textOfCode}, null, null, null);

                if (cursor.moveToFirst()) {
                    region.setText(cursor.getString(1));
                }
                cursor.close();
                db.close();
            } catch (SQLiteException e) {
                region.setText("Введите код региона");
            }
        }
    }

    private void backspace(String text) {
        int length = text.length();
        if (length != 0) {
            text = text.substring(0, length - 1);
            code.setText(text);

            textOfCode = text;
        }
        checkCode();
        //Получилось
    }
}
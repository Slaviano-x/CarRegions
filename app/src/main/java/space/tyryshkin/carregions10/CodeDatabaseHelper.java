package space.tyryshkin.carregions10;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CodeDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "region";

    CodeDatabaseHelper(Context context, int DB_VERSION) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE CODES (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CODE TEXT, "
                + "REGION TEXT, "
                + "CITIES TEXT);");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CODES");
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CODES");
        onCreate(db);
    }

    public boolean jsonParse(Map<String, String> code_region, Map<String, String> region_cities) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        for (String code : code_region.keySet()) {
            ContentValues values = new ContentValues();

            String region = code_region.get(code);
            String cities = region_cities.get(region);

            values.put("CODE", code);
            values.put("REGION", region);
            values.put("CITIES", cities);

            result = db.insert("CODES", null, values);
        }
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}
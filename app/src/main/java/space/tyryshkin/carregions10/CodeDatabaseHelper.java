package space.tyryshkin.carregions10;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CodeDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "region";
    private static final int DB_VERSION = 1;
    private final Context context;

    CodeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CODES");
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CODES (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CODE TEXT, "
                + "REGION TEXT);");

        jsonParse();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CODES");
        onCreate(db);
    }

    private static void insertCodes(SQLiteDatabase db, String code, String region) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("CODE", code);
        drinkValues.put("REGION", region);
        db.insert("CODES", null, drinkValues);
    }

    private void jsonParse() {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String url = "https://run.mocky.io/v3/8a077dcf-78f4-4217-a5e2-041118c3f9ed";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("codes");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject codes = jsonArray.getJSONObject(i);

                        String code = codes.getString("code");
                        String region = codes.getString("region");

                        insertCodes(new CodeDatabaseHelper(context).getReadableDatabase(), code, region);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
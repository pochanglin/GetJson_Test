package idv.allen.getjson_test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String MY_URL = "http://10.120.39.16:8081/IBM2/Test_JDBC_JSON";
    private LinearLayout linearLayout;
    private Spinner spLoc;
    String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = (LinearLayout)findViewById(R.id.linearlayout);
        spLoc = (Spinner)findViewById(R.id.spLoc);
    }
    public void onClickbtnGet(View view) throws IOException {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            new MyAsyncTask().execute();
                    }
                });
            }
        }, 0, 10000);


    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = null;

            try {
                URL url = new URL(MY_URL);
                InputStream is = url.openStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                sb = new StringBuilder();
                String str = "";
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                br.close();
                isr.close();
                is.close();
            } catch (IOException io) {

            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
            StringBuilder sb2 = new StringBuilder();
//            String dname = null;
            HashSet<String> dn = new HashSet<>();


            try {
                String jsonstr = s;
                JSONArray jArray = new JSONArray(jsonstr);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_dep = jArray.getJSONObject(i);
                    String loc = json_dep.getString("loc");
                    String dname = json_dep.getString("dname");
                    int deptno = json_dep.getInt("deptno");
                    dn.add(dname);

                    sb2.append("\n").append("LOC = ").append(loc).append("\n")
                            .append("DNAME = ").append(dname).append("\n")
                            .append("DEPTNO = ").append(deptno).append("\n")
                            .append("==================================");
                }
            } catch (Exception e) {

            }

//            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
//                    android.R.layout.simple_spinner_item, dn);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spLoc.setAdapter(adapter);

            TextView textView = new TextView(MainActivity.this);
            textView.setText(sb2.toString());
            linearLayout.addView(textView);
        }
    }
}

package com.example.dell.server;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText reg;
    TextView mac;
    Button send;
    String address;
    TextView disp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reg=findViewById(R.id.reg);
        mac=findViewById(R.id.mac);
        send=findViewById(R.id.send);
        disp=findViewById(R.id.disp);
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        address = info.getMacAddress();
        mac.setText(address);

    }
     public void onSend(View view)
     {
         serve s=new serve(this);
         String r=reg.getText().toString();
         String m=mac.getText().toString();
         s.execute(r,m);
         Context context = getApplicationContext();

         int duration = Toast.LENGTH_SHORT;


     }


    public class serve extends AsyncTask<String, Void, String> {
        Context context;

        public serve(Context ctx) {
            context=ctx;
        }

        @Override
        protected String doInBackground(String... input) {
            String r=input[0];
            String m=input[1];
            String u="https://android-club-project.herokuapp.com/upload_details?reg_no="+r.trim()+"&mac="+m.trim();
            try {
                URL url=new URL(u);

                HttpsURLConnection httpURLConnection= (HttpsURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");

                InputStream inputStreamReader=new BufferedInputStream(httpURLConnection.getInputStream());
                //Log.d("what hapen", inputStreamReader.toString());
                BufferedReader reader=new BufferedReader(new InputStreamReader(inputStreamReader));
                StringBuilder stringBuilder = new StringBuilder();

                //Log.d("what url",u);
                String reading;
                //Log.d("what buffer",reader.readLine());
                if((reading=reader.readLine())!=null)
                    stringBuilder.append(reading);
                reader.close();
                String res=stringBuilder.toString().substring(0,m.length());
                httpURLConnection.disconnect();

                return res;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "fail";
        }

        @Override
        protected void  onPostExecute(String res){

            disp.setText(res);
        }

    }

}

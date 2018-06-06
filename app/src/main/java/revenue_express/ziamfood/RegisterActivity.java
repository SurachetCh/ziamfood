package revenue_express.ziamfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class RegisterActivity extends AppCompatActivity {

    String access_token;
    String serverUrl;
    String m_fb_id = "";

    EditText edt_firstname,edt_lastname,edt_email,edt_password,edt_confirmpassword;
    CheckBox cb_accept;
    String firstname,lastname,email,password,confirmpassword;
    Button btn_Register;
    TextView tv_term_condition;

    String msg;
    JSONObject json = null;


    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sendNamePageGoogleAnalytics("RegisterActivity");

        serverUrl = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_register);
        access_token = getResources().getString(R.string.access_token);

        edt_firstname = (EditText)findViewById(R.id.edt_firstname);
        edt_lastname = (EditText)findViewById(R.id.edt_lastname);
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_password = (EditText)findViewById(R.id.edt_password);
        edt_confirmpassword = (EditText)findViewById(R.id.edt_confirmpassword);
        cb_accept = (CheckBox)findViewById(R.id.cb_accept);

        tv_term_condition = (TextView)findViewById(R.id.tv_term_condition);
        tv_term_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showLocationDialog("Term & Condition",getResources().getString(R.string.term_condition));
            }
        });

        btn_Register = (Button)findViewById(R.id.btn_Register);

        btn_Register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                firstname = edt_firstname.getText().toString();
                lastname = edt_lastname.getText().toString();
                email = edt_email.getText().toString();
                password = edt_password.getText().toString();
                confirmpassword = edt_confirmpassword.getText().toString();

                if (firstname.equals("")) {
                    Toast.makeText(RegisterActivity.this, "firstname must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                if(lastname.equals("")){
                    Toast.makeText(RegisterActivity.this, "lastname must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                if(email.equals("")){
                    Toast.makeText(RegisterActivity.this, "email must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.equals("") || confirmpassword.equals("")){
                    Toast.makeText(RegisterActivity.this, "password or confirmpassword must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!password.equals(confirmpassword)){
                    Toast.makeText(RegisterActivity.this, "password and confirmpassword must be different", Toast.LENGTH_LONG).show();
                    return;
                }

                Log.i("Register : ","New user");

                if (cb_accept.isChecked()) {
                // request authentication with remote server4
                        callSyncGet(serverUrl);
                }else{
                    Toast.makeText(RegisterActivity.this, "Please click accept and condition before register", Toast.LENGTH_LONG).show();
                    return;
                }
            }

        });
    }


    private void callSyncGet(final String url) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                imgLoad.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url Value:"+url.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("m_firstname",firstname);
                formBuilder.add("m_lastname", lastname);
                formBuilder.add("m_email", email);
                formBuilder.add("m_pass1",password);
                formBuilder.add("m_pass2",confirmpassword);
                formBuilder.add("m_fb_id",m_fb_id);
                formBuilder.add("access_token",access_token);

                MediaType.parse("application/json; charset=utf-8");
                RequestBody formBody = formBuilder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                client.newCall(request)
                        .enqueue(new Callback() {

                            @Override
                            public void onFailure(final Call call, IOException e) {
                                // Error

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                if (!response.isSuccessful()) {
                                    throw new IOException("Unexpected code " + response);
                                } else {
                                    Log.i("Response:",response.toString());
                                    jsonData = response.body().string();
                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            json = new JSONObject(jsonData);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            msg = json.getString("message");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if(jsonData.toLowerCase().contains("success")){
                                            showLocationDialog("Register",msg);
                                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                            startActivity(intent);
                                        }else{
                                            showLocationDialog("Register",msg);
                                            return;
                                        }
                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }

    //-----Show dialog box-----//
    private void showLocationDialog(String title,String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //-----End show dialog box-----//
}

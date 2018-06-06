package revenue_express.ziamfood;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ForgetPasswordActivity extends AppCompatActivity {

    String serverUrl ;
    String API_Key ;

    EditText editemail;
    String email;
    Button btnAccept;

    String msg;
    JSONObject json = null;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        sendNamePageGoogleAnalytics("ForgetPasswordActivity");

        serverUrl = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_froget_password);
        API_Key = getResources().getString(R.string.access_token);

        editemail = (EditText)findViewById(R.id.editemail);
        btnAccept = (Button)findViewById(R.id.btnAccept);

        btnAccept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                email = editemail.getText().toString();
                email.trim();

                if(email.equals("")){
                    Toast.makeText(ForgetPasswordActivity.this, "Username or password must be filled", Toast.LENGTH_LONG).show();
                    return;
                }

                // request authentication with remote server4
                    callSyncGet(serverUrl);
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
                formBuilder.add("m_email", email);
                formBuilder.add("api_key", String.valueOf(API_Key));

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
                                                showLocationDialog("Forget Password",msg);
                                                return;
                                            }else{
                                                showLocationDialog("Forget Password",msg);
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

    private int returnParsedJsonObject(String result){

        JSONObject resultObject = null;
        int returnedResult = 0;

        try {
            if(result != null) {
                returnedResult = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
    //-----End forget password user-----//

    //-----Show dialog box-----//
    private void showLocationDialog(String title ,String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
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
}

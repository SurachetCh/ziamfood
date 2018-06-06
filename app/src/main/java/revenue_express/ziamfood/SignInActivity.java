package revenue_express.ziamfood;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.ziamfood.model.User;

import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    EditText editName, editPassword;
    String URL_email, URL_facebook_token,URL_google_token , access_token, access_user_key, Name, Password, name_user, firstname_user, lastname_user, email_user, level_user, gender, photo_thumb, email;
    Integer id_user;
    ImageView imgSkip;

    private static final String TAG = MainAppActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    TextView tvRegister, tvForgetPassword;

    public static GoogleApiClient mGoogleApiClient;

    //login facebook or google
    Button fb, btnSignIn;
    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;

    String msg;
    JSONObject json = null;
    Realm realm;

    //Okhttp3
    private static final OkHttpClient client = new OkHttpClient();
    static String jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_in);

        sendNamePageGoogleAnalytics("SignInActivity");
        Typeface myTypeface = Typeface.createFromAsset(SignInActivity.this.getAssets(),getResources().getString( R.string.FontText));
        setInitView();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        URL_email = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_signin);
        URL_facebook_token = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_facebook_login_token);
        URL_google_token = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_google_login_token);
        access_token = getResources().getString(R.string.access_token);

        tvRegister = (TextView) findViewById(R.id.tv_Register);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });


        imgSkip = (ImageView) findViewById(R.id.imgSkip);
        imgSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        editName.setOnFocusChangeListener(ofcListener);
        editPassword.setOnFocusChangeListener(ofcListener);
        editPassword.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            Name = editName.getText().toString();
                            Password = editPassword.getText().toString();
                            if (Name.equals("") || Password.equals("")) {
                                Toast.makeText(SignInActivity.this, "Username or password must be filled", Toast.LENGTH_LONG).show();
                            }
                            if (Name.length() <= 1 || Password.length() <= 1) {
                                Toast.makeText(SignInActivity.this, "Username or password length must be greater than one", Toast.LENGTH_LONG).show();
                            }
                            try {
                                doGetRequest(URL_email, Name, Password, access_token);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Name = editName.getText().toString();
                Password = editPassword.getText().toString();
                if (Name.equals("") || Password.equals("")) {
                    Toast.makeText(SignInActivity.this, "Username or password must be filled", Toast.LENGTH_LONG).show();
                    return;
                }else if (Name.length() <= 1 || Password.length() <= 1) {
                    Toast.makeText(SignInActivity.this, "Username or password length must be greater than one", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    try {
                        doGetRequest(URL_email, Name, Password, access_token);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Login by email user
//                AsyncDataClass asyncRequestObject = new AsyncDataClass();
//                asyncRequestObject.execute(URL_email, Name, Password, access_token);
            }

        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        fb = (Button) findViewById(R.id.fb);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.e("Content", "User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken());

                String m_fb_token = loginResult.getAccessToken().getToken();

                // request authentication with remote server4
//                AsyncSocialDataClass asyncRequestObject = new AsyncSocialDataClass();
//                asyncRequestObject.execute(URL_facebook_token, access_token, m_fb_token);

                try {
                    doGetRequestSocial(URL_facebook_token, access_token, m_fb_token);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {


                            }
                        });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

                /**
                 * AccessTokenTracker to manage logout facebook
                 */
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                               AccessToken currentAccessToken) {
                        if (currentAccessToken == null) {
                            Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
                            intent.putExtra("name", "false");
                            intent.putExtra("pic", "false");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                };
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        TextView tv_email = (TextView)findViewById(R.id.tv_email);
        TextView tv_password = (TextView)findViewById(R.id.tv_password);
        TextView txt_register = (TextView)findViewById(R.id.txt_register);
        TextView txt_forget_password = (TextView)findViewById(R.id.txt_forget_password);

        editName.setTypeface(myTypeface);
        editPassword.setTypeface(myTypeface);
        tvRegister.setTypeface(myTypeface);
        tvForgetPassword.setTypeface(myTypeface);
        btnSignIn.setTypeface(myTypeface);
        tv_email.setTypeface(myTypeface);
        tv_password.setTypeface(myTypeface);
        txt_register.setTypeface(myTypeface);
        txt_forget_password.setTypeface(myTypeface);



    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }


    //-----Login user general-----//
    void doGetRequest(String url,String Name,String Password,String access_token) throws IOException{
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("m_user", Name);
        formBuilder.add("m_pass", Password);
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
                    public void onResponse(Call call, final Response response) throws IOException {
                        jsonData = response.body().string();
                        Log.i("Result login user", jsonData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    json = new JSONObject(jsonData);
                                    msg = json.getString("message");

                                    if (jsonData.toLowerCase().contains("success")) {
                                        access_user_key = json.getString("access_user_key");
                                        JSONObject parent = (JSONObject) json.get("access_user");
                                        id_user = parent.getInt("id");
                                        name_user = parent.getString("name");
                                        firstname_user = parent.getString("firstname");
                                        lastname_user = parent.getString("lastname");
                                        email_user = parent.getString("email");
                                        level_user = parent.getString("level");
                                        gender = parent.getString("gender");
                                        photo_thumb = parent.getString("photo_thumb");

                                        executeRealmTransaction(id_user, access_user_key, name_user, firstname_user, lastname_user, email_user, level_user, gender, photo_thumb,"0");

                                        Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else {
                                        showLocationDialog("Login Ziamfoods", msg);
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.getMessage();
                                }
                            }
                        });

                    }
                });
    }



    //-----Login Social facebook/google-----//
    void doGetRequestSocial(String url,String access_token,String m_fb_token) throws IOException{
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("access_token", access_token);
        formBuilder.add("m_oauth_token", m_fb_token);
        System.out.println("Facebook/Google Value: " + m_fb_token);

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
                    public void onResponse(Call call, final Response response) throws IOException {
                        jsonData = response.body().string();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    json = new JSONObject(jsonData);
                                    Log.e("Result facebook/google:", jsonData.toString());

                                    access_user_key = json.getString("access_user_key");

                                    if (jsonData.toLowerCase().contains("success")) {

                                        JSONObject parent = (JSONObject) json.get("access_user");
                                        id_user = parent.getInt("id");
                                        name_user = parent.getString("name");
                                        firstname_user = parent.getString("firstname");
                                        lastname_user = parent.getString("lastname");
                                        email_user = parent.getString("email");
                                        level_user = parent.getString("level");
                                        gender = parent.getString("gender");
                                        photo_thumb = parent.getString("photo_thumb");

                                        executeRealmTransaction(id_user, access_user_key, name_user, firstname_user, lastname_user, email_user, level_user, gender, photo_thumb,"1");

                                        Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else {
                                        showLocationDialog("Login Ziamfoods", msg);
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.getMessage();
                                }
                            }
                        });

                    }
                });
    }
    @Override
    public void onClick(View v) {

        if (v == fb) {
            loginButton.performClick();
        }

        int id = v.getId();
        switch (id) {
            case R.id.btnSignInGoogle:
                signIn();
                break;
        }
    }

    private void setInitView() {
        editName = (EditText) findViewById(R.id.editName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        tvRegister = (TextView) findViewById(R.id.tv_Register);
    }

    //-----Show dialog box-----//
    private void showLocationDialog(String title , String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
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

    public static void logoutUser() {
        // Logout google
        signOut();
    }

    //-----Login user Google+-----//
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //-----Check access token-----//
    private class CheckAccessToken extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String token = null;
            String Scopes = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";

            try {

                token = GoogleAuthUtil.getToken(SignInActivity.this, email, Scopes);

            } catch (IOException transientEx) {
                // Network or server error, try later
                Log.e(TAG, transientEx.toString());
            } catch (UserRecoverableAuthException e) {
                // Recover (with e.getIntent())
                Log.e(TAG, e.toString());
            } catch (GoogleAuthException authEx) {
                // The call is not ever expected to succeed
                // assuming you have already verified that
                // Google Play services is installed.
                Log.e(TAG, authEx.toString());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            Log.i(TAG, "Access token :" + token);
            // Call api google
//            AsyncSocialDataClass asyncRequestObject = new AsyncSocialDataClass();
//            asyncRequestObject.execute(URL_google_token, access_token, token);
            try {
                doGetRequestSocial(URL_google_token, access_token, token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "Status login of google: Success");

            String personName = acct.getDisplayName();
            String personPhotoUrl = String.valueOf(acct.getPhotoUrl());
            email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email + ", image user url: "+personPhotoUrl);

            new CheckAccessToken().execute();

//            Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
//            intent.putExtra("text","Log Out");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        }else{
            Log.e(TAG, "Status login of google: False");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void executeRealmTransaction(final int id , final String access_user_key, final String name , final String firstname, final String lastname, final String email, final String level,final  String gender,final String photo_thumb,final String checkconnect) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                User user = realm.createObject(User.class);
                user.setId(id);
                user.setAccess_user_key(access_user_key);
                user.setName(name);
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setEmail(email);
                user.setLevel(level);
                user.setGender(gender);
                user.setPhoto_thumb(photo_thumb);
                user.setCheckconnect(checkconnect);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(SignInActivity.this,"Create Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus){

//            if(hasFocus = false) {

                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

//            }
        }
    }
}

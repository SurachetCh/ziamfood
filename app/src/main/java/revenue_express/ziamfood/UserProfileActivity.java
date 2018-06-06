package revenue_express.ziamfood;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import revenue_express.ziamfood.model.User;

import static android.view.View.GONE;
import static revenue_express.ziamfood.manager.googleAnalytics.sendNamePageGoogleAnalytics;

public class UserProfileActivity extends AppCompatActivity {
    String URL_userprofile_update_info,URL_userprofile_update_pwd,URL_userprofile_update_photo,access_token;
    Button btnSaveInfo,btnBack,btnSavePassword;
    CircleImageView buttonChoose,image_user;
    ImageView imgBack;
    Integer id ;
    String memh_display,memh_firstname,memh_lastname,memh_email,memh_gender,access_user_key,memh_level,photo_thumb;
    String memh_display_last,memh_firstname_last,memh_lastname_last,memh_gender_last,memh_photo,memh_checkconnect,OldPassword,NewPassword,NewConPassword;
    Realm realm;
    EditText txtMemh_display,txtMemh_firstname,txtMemh_lastname,txtMemh_email,edtOldPassword,edtNewPassword,edtNewConPassword;
    RadioButton gender_radio_male,gender_radio_female;
    String gendertoPass = "male",return_json = "1",msg,path;
    JSONObject json = null;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    //Ok http3
    private final OkHttpClient client = new OkHttpClient();
    String jsonData;

    TextView txt_toolbar,tvDisplayName,tvFirstname,tvLastName,tvEmail,tvGender,tvChangePassword,tvOldPassword,tvNewPassword,tvConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        sendNamePageGoogleAnalytics("UserProfileActivity");
        Typeface myTypeface = Typeface.createFromAsset(UserProfileActivity.this.getAssets(),getResources().getString( R.string.FontText));

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        realm.beginTransaction();
        RealmResults<User> user = realm.where(User.class).findAll();
            id = user.get(0).getId();
            access_user_key = user.get(0).getAccess_user_key();
            memh_display = user.get(0).getName();
            memh_firstname = user.get(0).getFirstname();
            memh_lastname = user.get(0).getLastname();
            memh_email = user.get(0).getEmail();
            memh_gender = user.get(0).getGender();
            memh_photo = user.get(0).getPhoto_thumb();
            memh_checkconnect = user.get(0).getCheckconnect();
        realm.commitTransaction();

        URL_userprofile_update_info = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_userprofile_update_info);
        URL_userprofile_update_pwd = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_userprofile_update_pwd);
        URL_userprofile_update_photo = getResources().getString(R.string.Base_URL)+getResources().getString(R.string.URL_userprofile_update_photo);
        access_token = getResources().getString(R.string.access_token);

        //Change password card view is check status
        CardView change_password_card = (CardView)findViewById(R.id.change_password_card);
        if(memh_checkconnect.equals("1")) {
            change_password_card.setVisibility(GONE);
        }

        //Info user all
        txtMemh_display = (EditText)findViewById(R.id.txtMemh_display);
        txtMemh_firstname = (EditText)findViewById(R.id.txtMemh_firstname);
        txtMemh_lastname = (EditText)findViewById(R.id.txtMemh_lastname);
        txtMemh_email = (EditText)findViewById(R.id.txtMemh_email);

        //New password
        edtOldPassword = (EditText)findViewById(R.id.edtOldPassword);
        edtNewPassword = (EditText)findViewById(R.id.edtNewPassword);
        edtNewConPassword = (EditText)findViewById(R.id.edtNewConPassword);

        txt_toolbar = (TextView)findViewById(R.id.txt_toolbar);
        tvDisplayName= (TextView)findViewById(R.id.tvDisplayName);
        tvFirstname= (TextView)findViewById(R.id.tvFirsName);
        tvLastName= (TextView)findViewById(R.id.tvLastName);
        tvEmail= (TextView)findViewById(R.id.tv_email);
        tvGender= (TextView)findViewById(R.id.tvGender);
        tvChangePassword= (TextView)findViewById(R.id.tvChangePassword);
        tvOldPassword= (TextView)findViewById(R.id.tvOldPassword);
        tvNewPassword= (TextView)findViewById(R.id.tvNewPassword);
        tvConfirmPassword= (TextView)findViewById(R.id.tvConfirmPassword);

        //Initializing views
        buttonChoose = (CircleImageView) findViewById(R.id.fa_upload_image);
        image_user = (CircleImageView) findViewById(R.id.header);

        txtMemh_display.setText(memh_display);
        txtMemh_firstname.setText(memh_firstname);
        txtMemh_lastname.setText(memh_lastname);
        txtMemh_email.setText(memh_email);
        txtMemh_email.setFocusable(false);
        txtMemh_email.setClickable(false);

        //Set gender radio
        gender_radio_male = (RadioButton) findViewById(R.id.rdb_male);
        gender_radio_female = (RadioButton) findViewById(R.id.rdb_female);

        if(memh_gender.equals("male")){
            gender_radio_male.setChecked(true);
        }else{
            gender_radio_female.setChecked(true);
        }

        RadioGroup rg = (RadioGroup) findViewById(R.id.rdg_gender);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.rdb_male:
                        gendertoPass = "male";
                        break;
                    case R.id.rdb_female:
                        gendertoPass = "female";
                        break;
                }
            }
        });

        Log.i("Image memh_photo: ",memh_photo.toString());
        Glide.with(this)
                .load(memh_photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(image_user);

        imgBack =(ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Requesting storage permission
        requestStoragePermission();

        //Save info user all
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnSaveInfo = (Button)findViewById(R.id.btnSaveInfo);
        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //uploadMultipart();

                memh_display_last = txtMemh_display.getText().toString();
                memh_firstname_last = txtMemh_firstname.getText().toString();
                memh_lastname_last = txtMemh_lastname.getText().toString();
                memh_gender_last = gendertoPass;

                callSyncUserSave(URL_userprofile_update_info, access_token, access_user_key, memh_display_last, memh_firstname_last, memh_lastname_last, gendertoPass);

            }
        });

        //Save new password
        btnSavePassword = (Button)findViewById(R.id.btnSavePassword);
        btnSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OldPassword = edtOldPassword.getText().toString();
                NewPassword = edtNewPassword.getText().toString();
                NewConPassword = edtNewConPassword.getText().toString();

                callSyncUserSavePassword(URL_userprofile_update_pwd, access_token, access_user_key, OldPassword, NewPassword, NewConPassword);
            }
        });

        //Info user all
        txtMemh_display.setTypeface(myTypeface);
        txtMemh_firstname.setTypeface(myTypeface);
        txtMemh_lastname.setTypeface(myTypeface);
        txtMemh_email.setTypeface(myTypeface);

        //New password
        edtOldPassword .setTypeface(myTypeface);
        edtNewPassword .setTypeface(myTypeface);
        edtNewConPassword.setTypeface(myTypeface);

        //Set gender radio
        gender_radio_male.setTypeface(myTypeface);
        gender_radio_female.setTypeface(myTypeface);

        txt_toolbar.setTypeface(myTypeface);
        tvDisplayName.setTypeface(myTypeface);
        tvFirstname.setTypeface(myTypeface);
        tvLastName.setTypeface(myTypeface);
        tvEmail.setTypeface(myTypeface);
        tvGender.setTypeface(myTypeface);
        tvChangePassword.setTypeface(myTypeface);
        tvOldPassword.setTypeface(myTypeface);
        tvNewPassword.setTypeface(myTypeface);
        tvConfirmPassword.setTypeface(myTypeface);

        btnSaveInfo.setTypeface(myTypeface);
        btnSavePassword.setTypeface(myTypeface);

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    public void uploadMultipart() {
        //getting the actual path of the image
        path = getPath(filePath);
        //Uploading code
        try {
            callSyncUploadImage(URL_userprofile_update_photo, access_token , access_user_key , path , return_json);
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_user.setImageBitmap(bitmap);
                uploadMultipart();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    //-----Start user profile save-----//
    private void callSyncUserSave(final String URL_userprofile_update_info, final String access_token, final String access_user_key, final String memh_display_last, final String memh_firstname_last, final String memh_lastname_last, final String gendertoPass) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url ข้อมูลผู้ใช้งาน: "+URL_userprofile_update_info.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("access_user_key", access_user_key);
                formBuilder.add("m_display", memh_display_last);
                formBuilder.add("m_firstname", memh_firstname_last);
                formBuilder.add("m_lastname", memh_lastname_last);
                formBuilder.add("m_gender", gendertoPass);

                MediaType.parse("application/json; charset=utf-8");

                RequestBody formBody = formBuilder.build();
                Request request = new Request.Builder()
                        .url(URL_userprofile_update_info)
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
                                            msg = json.getString("message");

                                            if(jsonData.toLowerCase().contains("success")){
                                                showLocationDialog("Update User Profile",msg);

                                                realm.beginTransaction();
                                                RealmResults<User> result = realm.where(User.class).findAll();
                                                result.deleteAllFromRealm();
                                                realm.commitTransaction();

                                                executeRealmTransaction(id ,access_user_key,memh_display_last ,memh_firstname_last,memh_lastname_last,memh_email,memh_level,gendertoPass,memh_photo);

                                                return;
                                            }else{
                                                showLocationDialog("Update User Profile",msg);
                                                return;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }

    //-----Start user new password-----//
    private void callSyncUserSavePassword(final String URL_userprofile_update_pwd, final String access_token, final String access_user_key, final String OldPassword, final String NewPassword, final String NewConPassword) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url รหัสผ่านใหม่ผู้ใช้งาน: "+URL_userprofile_update_info.toString());

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("access_token", access_token);
                formBuilder.add("access_user_key", access_user_key);
                formBuilder.add("old_pass", OldPassword);
                formBuilder.add("new_pass", NewPassword);
                formBuilder.add("confirm_pass", NewConPassword);

                MediaType.parse("application/json; charset=utf-8");

                RequestBody formBody = formBuilder.build();
                Request request = new Request.Builder()
                        .url(URL_userprofile_update_pwd)
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
                                            msg = json.getString("message");

                                            if(jsonData.toLowerCase().contains("success")){
                                                showLocationDialog("Update New Password",msg);
                                                return;
                                            }else{
                                                showLocationDialog("Update New Password",msg);
                                                return;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        });
                return null;
            }

        }.execute();
    }

    //-----Upload image profile-----//
    private void callSyncUploadImage(final String URL_userprofile_update_photo, final String access_token , final String access_user_key ,final String path , final String return_json) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                System.out.println("Url upload image:"+URL_userprofile_update_photo.toString());
                System.out.println("Access_token: "+access_token+" Access_user_key: "+access_user_key);

                MultipartBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("m_picture", "logo-square.png",
                                RequestBody.create(MEDIA_TYPE_PNG, new File(path)))
                        .addFormDataPart("access_token", access_token)
                        .addFormDataPart("access_user_key", access_user_key)
                        .addFormDataPart("return_json", return_json)
                        .build();

                MediaType.parse("application/json; charset=utf-8");

                Request request = new Request.Builder()
                        .url(URL_userprofile_update_photo)
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

                                        Log.i("Result Upload", jsonData);
                                        if (jsonData.toLowerCase().contains("success")){
                                            showImageUpload("Upload image Status", "Upload image Success");
                                        }else {
                                            showImageUpload("Upload image Status", "Upload image Fail");
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
    private void showLocationDialog(String title , String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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

    //-----Show success upload image-----//
    private void showImageUpload(String title , String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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


    private void executeRealmTransaction(final int id , final String access_user_key, final String name , final String firstname, final String lastname, final String email, final String level,final  String gender,final String photo_thumb) {
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
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(UserProfileActivity.this,"Create Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
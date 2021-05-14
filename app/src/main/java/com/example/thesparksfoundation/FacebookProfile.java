package com.example.thesparksfoundation;
//
//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.squareup.picasso.Picasso;
//
//public class FacebookProfile extends AppCompatActivity {
//
//    private TextView my_name;
//    private TextView my_email;
//    private ImageView profile;
//    private LoginButton login;
//    CallbackManager callbackManager;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_facebook_profile);
//
//        my_name = findViewById(R.id.textView);
//        my_email = findViewById(R.id.textView2);
//        profile = findViewById(R.id.imageView);
//        login = findViewById(R.id.login);
//
//     callbackManager = CallbackManager.Factory.create();
//        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                my_name.setText(loginResult.getAccessToken().getUserId());
//                String imageURL = "https://graph.facebook.com"+ loginResult.getAccessToken().getUserId() +"/picture?return_ssl_resources=1";
//                Picasso.get().load(imageURL).into(profile);
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode,resultCode,data);
//
//    }
//}

















        import android.content.Intent;
       // import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.facebook.AccessToken;
        import com.facebook.AccessTokenTracker;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.GraphRequest;
        import com.facebook.GraphResponse;
        import com.facebook.Profile;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;
        import com.squareup.picasso.Picasso;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.Arrays;


public class FacebookProfile extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    ImageView imageView;
    TextView txtUsername, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_profile);

        loginButton = findViewById(R.id.login_button);
        imageView = findViewById(R.id.imageView);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);

        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        if (!loggedOut) {
            Picasso.get().load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);
            Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());

            //Using Graph API
            getUserProfile(AccessToken.getCurrentAccessToken());
        }


        AccessTokenTracker fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    txtUsername.setText(null);
                    txtEmail.setText(null);
                    imageView.setImageResource(0);
                    Toast.makeText(getApplicationContext(),"User Logged Out.",Toast.LENGTH_LONG).show();
                }
            }
        };
        fbTracker.startTracking();

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

                if (!loggedOut) {
                    Picasso.get().load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);
                    Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());

                    //Using Graph API
                    getUserProfile(AccessToken.getCurrentAccessToken());
                }

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
                            txtEmail.setText(email);
                            Picasso.get().load(image_url).into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }
}


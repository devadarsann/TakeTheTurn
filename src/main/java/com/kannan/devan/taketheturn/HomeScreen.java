package com.kannan.devan.taketheturn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.*;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.MapFragment;

public class HomeScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,HomeFragment.OnButtonClickListenetr {

    SignInButton mSignInButton;
    private static int RC_SIGN_IN=1;
    GoogleSignInOptions mGoogleOptions;
    GoogleApiClient mGoogleApiClient;
    private static String APP_PREFS="preferences";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr=Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()){

            GoogleSignInResult result=opr.get();
            HandleSignInResult(result);
        }
        else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    HandleSignInResult(googleSignInResult);
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void HandleSignInResult(GoogleSignInResult signInResult) {
        if (signInResult.isSuccess()){
            GoogleSignInAccount mGoogleAccount=signInResult.getSignInAccount();
            Intent mainIntent=new Intent(HomeScreen.this, MainActivity.class);
            mainIntent.putExtra("GoogleAccount",mGoogleAccount);
            startActivity(mainIntent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void OnButtonClickListenetr() {

    }
}

package com.kannan.devan.taketheturn;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements HomeFragment.OnButtonClickListenetr, GoogleApiClient.OnConnectionFailedListener,MapListFragment.OnFragmentInteractionListener,SubmitFragment.OnFragmentInteractionListener {

    ListView ls;
    List<BlockData> mBlockDatas;
    boolean isFirstStartup;
    BlockDataAdapter mBlockDataAdapter;
    GoogleSignInOptions mGoogleOptions;
    GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN=1;
    String APP_PREFS="com.kannan.devan.AppPrefs";
    ProgressDialog mProgressDialog;
    ImageView mProfileImageView;
    TextView mProfileName;
    String mReference;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    FloatingActionButton mFloatingSubmitButton;

    String GOOGLE_CLIENTID="Your id";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){
            GoogleSignInResult signInResult=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            HandleSignInResult(signInResult);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar= (Toolbar) findViewById(R.id.profile_toolbar);
        mProfileImageView= (ImageView) findViewById(R.id.profile_image);
        mProfileName= (TextView) findViewById(R.id.profile_name);
        mFloatingSubmitButton= (FloatingActionButton) findViewById(R.id.floatingActionSubmitButton);
        mFirebaseAuth=FirebaseAuth.getInstance();

        mFloatingSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            SubmitFragment mLocationSubmit=new SubmitFragment();
                mLocationSubmit.show(getSupportFragmentManager(),"Location Submit");
            }
        });

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser=firebaseAuth.getCurrentUser();
                if (mUser!=null){
                    //Snackbar.make(getCurrentFocus(),"Signed in as "+mUser.getDisplayName(),Snackbar.LENGTH_SHORT).show();
                }
                else {
                    //Snackbar.make(getCurrentFocus(),"Authorization failed",Snackbar.LENGTH_SHORT).show();
                }
            }
        };

        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mProgressDialog.create();
        }
        mProgressDialog.show();
        mToolbar.setCollapsible(true);
        mGoogleOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_CLIENTID)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleOptions)
                .build();

        isFirstStartup=CheckIsFirstStartup();
        if (isFirstStartup) {
           ShowSignIn();
            //mSignInButton= (SignInButton) findViewById(R.id.userLogin);
        }
        else {
            IsAlreadySignedIn();
        }
        mBlockDatas=new ArrayList<>();

        DateFormat dateFormat=new DateFormat();
        mReference = dateFormat.format("yyyyMMdd",new Date()).toString();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("20170128");
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
//            requestLocationPermission();
//        }
//
//        final Gpslocator locator=new Gpslocator(this);
//        final SimpleDateFormat mDateFormat=new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            Date dt= mDateFormat.parse("14/01/2014");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        DatabaseReference newPost=myRef.push();
//        Map<String,String> posts=new HashMap<String, String>();
//        posts.put("Cause","Under construction");
//        posts.put("UpdatedDate",new Date().toString());
//        posts.put("Latitude",""+locator.getLatitude());
//        posts.put("Longitude",""+locator.getLongitude());
//        double lat=locator.getLatitude();
//        double lng=locator.getLongitude();

//        BlockData blockData=new BlockData("Under construction",new Date().toString(),""+lat,""+lng);
//        newPost.setValue(blockData);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.setMessage("Loading data....");
                if (!mProgressDialog.isShowing()){
                    mProgressDialog.show();
                }
                long count=dataSnapshot.getChildrenCount();
//              GenericTypeIndicator<Map<String,BlockData>> t=new GenericTypeIndicator<Map<String, BlockData>>() {};
//                Map<String,BlockData> mBlockDatas=dataSnapshot.getValue(t);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    mBlockDatas.add(snapshot.getValue(BlockData.class));
                }
                mBlockDataAdapter=new BlockDataAdapter(mBlockDatas,MainActivity.this);
                if (mProgressDialog.isShowing()){
                    mProgressDialog.cancel();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ShowSignIn() {
        if (mProgressDialog.isShowing()){
            mProgressDialog.cancel();
        }
        if (mFloatingSubmitButton.isShown()){
            mFloatingSubmitButton.setVisibility(View.GONE);
        }
        HomeFragment LoginFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.loginView, LoginFragment)
                .commit();
    }

    private boolean CheckIsFirstStartup() {
        boolean StartValue=false;
        SharedPreferences AppPrefs=this.getSharedPreferences(APP_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor PrefsEditor=AppPrefs.edit();
        StartValue=AppPrefs.getBoolean("is_first_Start",true);
        if (StartValue) {
            PrefsEditor.putBoolean("is_first_Start", false);
            PrefsEditor.apply();
            PrefsEditor.commit();
        }
        return StartValue;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
        IsAlreadySignedIn();
    }

    private void HandleSignInResult(GoogleSignInResult signInResult) {

        if (signInResult.isSuccess()){
            GoogleSignInAccount mGoogleAccount=signInResult.getSignInAccount();
            if (isFirstStartup) {
//                HomeFragment mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.loginView);
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.remove(mHomeFragment);
//                ft.commit();
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);


            }
            SetAccountInfo(mGoogleAccount);
            FirebaseAuthWithGoogle(mGoogleAccount);
            MapListFragment mDataFragment= new MapListFragment();
//            FragmentTransaction mTransaction= getSupportFragmentManager().beginTransaction();
//            mTransaction.addToBackStack("DataList");
//            mTransaction.replace(R.id.loginView,mDataFragment);
//            mTransaction.commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.loginView,mDataFragment)
                    .commit();
            if (mProgressDialog.isShowing()){
                mProgressDialog.cancel();
            }
            if (!mFloatingSubmitButton.isShown()){
                mFloatingSubmitButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void FirebaseAuthWithGoogle(GoogleSignInAccount mGoogleAccount) {
        String idToken=mGoogleAccount.getIdToken();
        AuthCredential mAuthCredential=GoogleAuthProvider.getCredential(mGoogleAccount.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(mAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        boolean tsk=task.isSuccessful();
                        if (!task.isSuccessful()){

                        }
                    }
                });
    }

    private void SetAccountInfo(GoogleSignInAccount mGoogleAccount) {
        mProfileName.setText(mGoogleAccount.getDisplayName());
        Uri profilImageUri=mGoogleAccount.getPhotoUrl();
        Glide.with(getApplicationContext())
                .load(profilImageUri.toString())
                .sizeMultiplier(1.0f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mProfileImageView);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.d("Permision request","Permissin Granted");
            }

        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
    }


    @Override
    public void OnButtonClickListenetr() {
        Intent mSignInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(mSignInIntent,RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.con_failed,Toast.LENGTH_SHORT).show();
    }

    public void IsAlreadySignedIn(){
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
            ShowSignIn();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
     String data=uri.getFragment();
    }


}

package za.example.sqalo.jhscanner;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ImageView imagelogo;
    //---------con--------
    Connection con;
    String un,pass,db,ip;
    int click=0;
    Calendar c;
    //---------con--------

    int PLACE_PICKER_REQUEST = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String API_KEY = "AIzaSyA1RX5FgK6qKIuHOkQP6D40uEEaL5eLi0w";
    Calendar cal;
    int hour ;
    int min=0;
    int sec ;
    static MainActivity instance;
    String name=null,username=null,password=null;//
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    EditText edtbarcode,edtuseremail,edtpass,edthidenuserid,edthidenuserrole;
    Button btnlogin;

    boolean doubleBackToExitPressedOnce = false;

    LinearLayout loginlayout;
    TextView txtcreate;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    String m_Text_user = "";

    Intent mServiceIntent;
    private SensorService mSensorService;
    String status="";
    String active_status="";
    Bundle bundle=new Bundle();
    Context ctx;
    public static final String PREFS_NAME = "MyApp_Settings";
    public Context getCtx() {
        return ctx;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
       /* if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }*/

        loginlayout =(LinearLayout) findViewById(R.id.userloginlayout);
        imagelogo = (ImageView) findViewById(R.id.imgLogo);
        fragmentManager = getFragmentManager();

        // Getting values from button, texts and progress bar
        btnlogin = (Button) findViewById(R.id.button);
        edtbarcode = (EditText) findViewById(R.id.edtbarcode);
        edtuseremail = (EditText) findViewById(R.id.editText);
        edtpass = (EditText) findViewById(R.id.editText2);
        edthidenuserid = (EditText) findViewById(R.id.edthidenuserid);
        edthidenuserrole = (EditText) findViewById(R.id.edthidenuserrole);
        txtcreate=this.findViewById(R.id.txtcreate);
        radioSexGroup=(RadioGroup)findViewById(R.id.radioGroup);

        instance = this;


        Intent intent = getIntent();
        bundle = intent.getExtras();

//instance.getIntent();


        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";



        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion > Build.VERSION_CODES.JELLY_BEAN_MR1){
            // Do something for lollipop and above versions

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setItemIconTintList(null);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setVisibility(View.GONE);


        } else{
            // do something for phones running an SDK before lollipop

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();


            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setItemIconTintList(null);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setVisibility(View.GONE);

        }


        try{
            if(bundle != null){
                if(!bundle.getString("barcode").equals("")){
                    status = bundle.getString("barcode");
                    if(!status.equals("barcodefault")){
                        edtbarcode.setText(status);
                    }


                    edtuseremail.setText(bundle.getString("username"));
                    edtpass.setText(bundle.getString("password"));

                    FillHiddenData();
                    DoLogin doLogin = new DoLogin();
                    doLogin.execute("");
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    // Writing data to SharedPreferences
                    SharedPreferences.Editor editor = settings.edit();
                    if(!edthidenuserid.getText().toString().equals("")){
                        editor.clear();
                        editor.putString("keycouserid", edthidenuserid.getText().toString());
                        if (!isMyServiceRunning(mSensorService.getClass())) {
                            startService(mServiceIntent);
                        }
                    }else {
                        editor.clear();
                        editor.putString("keyassistuserid", edthidenuserrole.getText().toString());
                        if (!isMyServiceRunning(mSensorService.getClass())) {
                            startService(mServiceIntent);
                        }
                    }

                    editor.commit();


                }

            }
        }catch (Exception ex){
            Log.i ("isMyServiceRunning?", ex.getMessage());
        }
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FillHiddenData();

                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                // Writing data to SharedPreferences
                SharedPreferences.Editor editor = settings.edit();
                if(!edthidenuserid.getText().toString().equals("")){
                    editor.clear();
                    editor.putString("keycouserid", edthidenuserid.getText().toString());
                    if (!isMyServiceRunning(mSensorService.getClass())) {
                        startService(mServiceIntent);
                    }
                }else {
                    editor.clear();
                    editor.putString("keyassistuserid", edthidenuserrole.getText().toString());
                    if (!isMyServiceRunning(mSensorService.getClass())) {
                        startService(mServiceIntent);
                    }
                }

                editor.commit();
            }
        });
        radioSexGroup.clearCheck();
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId=radioSexGroup.getCheckedRadioButtonId();
                radioSexButton=(RadioButton)findViewById(checkedId);
                if(radioSexButton.getText().toString().trim().equals("Co-user")){
                    ForgotLoginCouser();
                }else if(radioSexButton.getText().toString().trim().equals("Assistant")){
                    ForgotLoginAssist();
                }

            }
        });
     /*
        // Construct an intent that will execute the SensorRestarterBroadcastReceiver
        Intent intent = new Intent(getApplicationContext(), SensorRestarterBroadcastReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, SensorRestarterBroadcastReceiver.REQUEST_CODE,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,5000, pIntent);*/
    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }
    public void CreateAccout(View view){
       Fragment fragment = new RegisterFrag();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainFrame, fragment).commit();
    }
    public void ForgotLoginOption(View view){
        radioSexGroup.setVisibility(View.VISIBLE);
    }

    public void ForgotLoginCouser(){



    try {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title=new TextView(this);
        title.setPadding(10,10,10,10);
        title.setText("Forgot Co-user Login");
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.colorPrimary));
        builder.setCustomTitle(title);
        builder.setMessage("Co-user Login Details:");
        final EditText input = new EditText(this);
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setBackground(this.getResources().getDrawable(R.drawable.toasttext_bground));
        input.setHint("Enter Email Address");
        input.setHintTextColor(Color.GRAY);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text_user = input.getText().toString();
                   /* Bundle bundle = new Bundle();
                    bundle.putString("subject", "Forgot Login");
                    bundle.putString("body", "Please assist with Login details for Username:"+m_Text_user);
                    ContactUs fragment = new ContactUs();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/
                SendMail send =new SendMail();
                send.execute("");
                radioSexGroup.setVisibility(View.INVISIBLE);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();



    } catch (Exception ex) {

    }



    }

    public void ForgotLoginAssist(){





        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView title=new TextView(this);
            title.setPadding(10,10,10,10);
            title.setText("Forgot Assistant Login");
            title.setGravity(Gravity.CENTER);
            title.setTextColor(getResources().getColor(R.color.colorPrimary));
            builder.setCustomTitle(title);
            builder.setMessage("Co-user Email Address:");
            final EditText input = new EditText(this);
            input.setGravity(Gravity.CENTER);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setBackground(this.getResources().getDrawable(R.drawable.toasttext_bground));
            input.setHint("Enter Co-user Email Address?");
            input.setHintTextColor(Color.GRAY);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text_user = input.getText().toString();
                    try {

                        ConnectionClass cn=new ConnectionClass();
                        con =cn.connectionclass(un, pass, db, ip);        // Connect to database
                        if (con == null)
                        {
                            Toast.makeText(MainActivity.this, "Check your network connection!!", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            try {
                            String query = "select * from [AppUserAssistance] where [email]= '" + m_Text_user + "'";
                           PreparedStatement ps = con.prepareStatement(query);
                           final ResultSet rs = ps.executeQuery();
                            rs.next();

                            if(rs.getRow()!=0){

                                //############################
                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Assistant Name:");
                                    builder.setIcon(getResources().getDrawable(R.drawable.locki));
                                    final EditText input = new EditText(MainActivity.this);
                                    input.setGravity(Gravity.CENTER);
                                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                                    input.setBackground(getResources().getDrawable(R.drawable.toasttext_bground));
                                    input.setHint("Enter Your First Name");
                                    input.setHintTextColor(Color.GRAY);
                                    builder.setView(input);
                                    builder.setMessage("First Name?");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            try {
                                                name=input.getText().toString();
                                                if(!name.equals("")&&rs.getString("assistancename").toString().toLowerCase().equals(name.toLowerCase())){
                                                    name=rs.getString("assistancename").toString();
                                                    username=rs.getString("email").toString();
                                                    password=rs.getString("password").toString();

                                                        //############################
                                                        try {
                                                            String message = "Username :"+username+"\nPassword :"+password;
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                            builder.setTitle("Welcome:"+name);
                                                            builder.setIcon(getResources().getDrawable(R.drawable.profil));
                                                            builder.setMessage(message);
                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    radioSexGroup.setVisibility(View.INVISIBLE);
                                                                    dialog.cancel();
                                                                }
                                                            });
                                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.cancel();
                                                                }
                                                            });
                                                            builder.show();



                                                        } catch (Exception ex) {
                                                            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                                        }
                                                        //############################

                                                }else{
                                                    Toast.makeText(MainActivity.this, "Assistant with this name does not exit!!!", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (Exception ex) {
                                                Toast.makeText(MainActivity.this, "Assistant with this name does not exit!!!", Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();



                                } catch (Exception ex) {
                                    // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                }
                                //############################



                            }else{
                                Toast.makeText(MainActivity.this, "Account with this email does not exit!!!", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception ex){
                                Toast.makeText(MainActivity.this, ex.getMessage()+"1", Toast.LENGTH_LONG).show();
                        }



                        }

                    } catch(Exception e) {

                        Toast.makeText(MainActivity.this, "Assistant with this name does not exit!!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();



        } catch (Exception ex) {

        }


        try {
            if(name!=null){
                if(!name.equals("")){
                    //############################
                    try {
                        String message = "Username :"+username+"\nPassword :"+password;
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Welcome:"+name);
                        builder.setIcon(getResources().getDrawable(R.drawable.profil));
                        builder.setMessage(message);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();



                    } catch (Exception ex) {
                        // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                    }
                    //############################
                }else {

                }
            }
        } catch (Exception ex) {

    }




    }


    @Override
    public void onBackPressed() {


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;


            }
            else{
                if(loginlayout.getVisibility()==View.GONE  ){

                    if (!edthidenuserid.getText().toString().equals("")) {
                        Fragment fragment = new HomeFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, fragment).commit();
                        click=click+1;
                        if(click>=3){
                            finish();
                            System.exit(0);
                        }
                    }else {
                        Fragment fragment = new HomePremiumFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainFrame, fragment).commit();
                        click=click+1;
                        if(click>=3){
                            finish();
                            System.exit(0);
                        }
                    }

                }

            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                   // click=1;

                }
            }, 2000);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent fragment_mapy in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment=null;
        if (id == R.id.homepage) {
            // Handle the camera action
            fragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();

        }
      else if (id == R.id.addproduct) {
            fragment = new AddProductFrag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
        }
        else if (id == R.id.grorequest) {
            fragment = new GroRequestListFrag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
        }
        else if (id == R.id.grocery) {
            fragment = new MyListFrag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
        } else if (id == R.id.insight) {
            fragment = new GroceryInsightsFrag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
        }
        else if (id == R.id.kitchen) {
            fragment = new HomePremiumFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
        }
        else if (id == R.id.dish) {
            fragment = new HomeDishCollectionFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
        }
        else if (id == R.id.donationpointer) {
            fragment = new HomeFragmentDonationPointer();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
        }
        else if (id == R.id.orderpoint) {
            fragment = new HomeFragmentOrderPoint();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
        }
        else if (id == R.id.foodsafety) {
            fragment = new FoodSafetyFrag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
        }
        else if (id == R.id.profile) {
            fragment = new RegisterFrag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();
        }

        else if (id == R.id.contact) {

              Bundle bundle = new Bundle();
                    bundle.putString("email", edtuseremail.getText().toString());
                    fragment = new ContactUs();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
            /*fragment = new ContactUs();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, fragment).commit();*/
        }
        else if (id == R.id.logout) {


            finish();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void FillHiddenData() {
        //==============Fill Data=
        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

               CharSequence text = "Check your network connection!!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this.getBaseContext(), text, duration);
                toast.show();
            } else {

                //String query = "select * from [AppUser] where [id]=1";// + Integer.parseInt(edtappuserId.getText().toString());
                String query = "select * from [AppUser] where [email]= '" + edtuseremail.getText().toString() + "' and [password] = '"+ edtpass.getText().toString() +"'";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();


                if (rs.getRow() != 0) {

                     edthidenuserid.setText( rs.getString("id"));
                    // edthidenuserrole.setText(rs.getString("userRole"));

                }
                else{
                    //Assistance
                    String queryas = "select * from [AppUserAssistance] where [email]= '" + edtuseremail.getText().toString() + "' and [password] = '" + edtpass.getText().toString() + "'";
                    PreparedStatement psas = con.prepareStatement(queryas);
                    ResultSet rsas = psas.executeQuery();
                    rsas.next();


                    if (rsas.getRow() != 0) {

                       // edthidenuserid.setText(rsas.getString("userid"));
                        edthidenuserrole.setText(rsas.getString("id"));
                    }

                }


            }


        } catch (Exception ex) {
            Toast.makeText(this.getBaseContext(), ex.getMessage().toString()+"Main",Toast.LENGTH_LONG).show();

        }
//==========

    }

    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        NavigationView navigationView1;

        String userid = edtuseremail.getText().toString();
        String password = edtpass.getText().toString();


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {



            if(isSuccess) {
               // Toast.makeText(MainActivity.this,r,Toast.LENGTH_LONG).show();

                if(r.equals("Co-User Login Successfully")){
                    if(active_status.equals("false")){
                        Toast.makeText(MainActivity.this,"Profile Deactivate",Toast.LENGTH_LONG).show();
                    }else{
                        navigationView.setVisibility(View.VISIBLE);
                        loginlayout.setVisibility(View.GONE);
                        imagelogo.setVisibility(View.GONE);
                        if(!status.equals("")){
                            if(status.equals("barcodefault")){
                                Fragment fragment = new HomeFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.mainFrame, fragment).commit();
                            }else{
                                Fragment fragment = new AddProductFragOcr();
                                fragment.setArguments(bundle);
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                            }


                        }else{
                            Fragment fragment = new HomeFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.mainFrame, fragment).commit();
                        }
                    }


                }else{

                    if(active_status.equals("false")){
                        Toast.makeText(MainActivity.this,"Profile Deactivate",Toast.LENGTH_LONG).show();
                    }else{
                        // navigationView.setVisibility(View.VISIBLE);
                        loginlayout.setVisibility(View.GONE);
                        imagelogo.setVisibility(View.GONE);
                        Bundle bundle = new Bundle();
                        bundle.putString("assist", "assistant");

                        Fragment fragment = new HomePremiumFragment();
                        // Fragment fragment = new HomeFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                    }



                }
            }else{
                Toast.makeText(MainActivity.this,r,Toast.LENGTH_LONG).show();
            }

        }

        @Override

        protected String doInBackground(String... params) {
            if(userid.trim().equals("")|| password.trim().equals(""))
                z = "Please enter User Id and Password";
            else
            {
                try
                {
                    ConnectionClass cn=new ConnectionClass();
                    con =cn.connectionclass(un, pass, db, ip);        // Connect to database
                    if (con == null)
                    {
                        z = "Check Your Internet Access!";
                    }
                    else
                    {
                        //Co-user
                        String query = "select * from [AppUser] where [email]= '" + userid.toString() + "' and [password] = '"+ password.toString() +"'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        //Assistance
                        String queryas = "select * from [AppUserAssistance] where [email]= '" + userid.toString() + "' and [password] = '"+ password.toString() +"'";
                        PreparedStatement psas = con.prepareStatement(queryas);
                        ResultSet rsas = psas.executeQuery();
                        rsas.next();

                        //Keep notification date as today if assistant never login until pass notification date
                        String commands = "update [UserProductNotification] set [noticedate]=REPLACE(CONVERT(VARCHAR(10), GETDATE(), 111), '/', '-')  where [userid] = '" + rs.getString("id") + "' and (CONVERT(VARCHAR(10), [noticedate], 110) )>=(CONVERT(VARCHAR(10), GETDATE(), 110) ) and [issent]='No'";
                        PreparedStatement preStmt = con.prepareStatement(commands);
                        preStmt.executeUpdate();
//


                        if(rs.getRow()!=0){
                            active_status=String.valueOf(rs.getBoolean("isactive"));

                     z = "Co-User Login Successfully";
                            isSuccess=true;
                            con.close();
                        }
                        else if(rsas.getRow()!=0)
                        {
                            active_status=String.valueOf(rs.getBoolean("isactive"));
                            z = "Assistance Login Successfully";
                            isSuccess=true;
                           con.close();
                        }
                        else{
                            z = "Invalid Login!";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                   // z = "Invalid data input or lost connection!!!";
                    z=ex.getMessage();
                }

            }
            return z;
        }
    }

    private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;
        private String z="";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {
            // super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Sending mail...", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // super.onPostExecute(aVoid);
            progressDialog.dismiss();

            if(isSuccess) {
                Toast.makeText(MainActivity.this, z, Toast.LENGTH_LONG).show();

            }else{
               // Toast.makeText(MainActivity.this, "Could not send email", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, z, Toast.LENGTH_LONG).show();
            }
        }

        protected Void doInBackground(String... params) {


            try {

                ConnectionClass cn=new ConnectionClass();
                con =cn.connectionclass(un, pass, db, ip);        // Connect to database
                if (con == null)
                {
                    z = "Check Your Internet Access!";
                }
                else
                {
                    String query = "select * from [AppUser] where [email]= '" + m_Text_user + "'";
                    // Statement stmt = con.createStatement();
                    // ResultSet rs = stmt.executeQuery(query);

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    rs.next();



                    if(rs.getRow()!=0){
                        Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                        //String to = "jabun@ngobeniholdings.co.za";
                        // String to = "sntshangase3@gmail.com";
                       // String to = "SibusisoN@sqaloitsolutions.co.za";
                        String to = m_Text_user;
                        String subject = "Your Login Details";
                        String message = "Username :"+rs.getString("email")+"\nPassword :"+rs.getString("password")+"\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";
                        String[] toArr = {to};
                        m.setTo(toArr);
                        m.setFrom("Info@ngobeniholdings.co.za");

                        m.setSubject(subject);
                        m.setBody(message);
                        if(m.send()) {

                            z="Login details sent to your email";
                            isSuccess=true;
                            con.close();
                        } else {
                            isSuccess = false;
                        }
                    }else{
                        z="Account with this email does not exit!!!";
                        isSuccess = false;
                    }
                }

            } catch(Exception e) {

                z="Account with this email does not exit!!!";
            }
            return null;
        }
    }
}

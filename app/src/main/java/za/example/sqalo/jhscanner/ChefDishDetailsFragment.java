package za.example.sqalo.jhscanner;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * Created by sibusison on 2017/07/30.
 */
public class ChefDishDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public int counter = 1;
    int useridchef;
    View rootView;
    ViewPager viewPager;
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bitmap decodebitmap1;
    Spinner spinnercategory, spinnerassistant;
    ImageView b1, chefprofileImage, share,moretap,moretap1, callno, map,d1,d2,d3,d4,d5;
    TextView txtname,txtmore, txtdistance, txtaddress, txtdishname, txtingredient, txtestimateprice, txtestimatecookingtime;
    int catergoryclick = 0;
    ArrayList<String> catergory = new ArrayList<String>();
    Bundle bundle;
    String latlon = "";
    String fulladdress_search = "";
    String chefmobileno = "";
    int wid;
    ArrayAdapter adapterassistant;
    String assistant = "";
    String prepdate = "";

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    private int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;
    Calendar c;
    ImageButton donestatus;
    LinearLayout b1_layout;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dishdetails, container, false);

        callno = (ImageView) rootView.findViewById(R.id.callno);
        map = (ImageView) rootView.findViewById(R.id.map);
        share = (ImageView) rootView.findViewById(R.id.share);
        moretap = (ImageView) rootView.findViewById(R.id.moretap);
        moretap1 = (ImageView) rootView.findViewById(R.id.moretap1);
        chefprofileImage = (ImageView) rootView.findViewById(R.id.chefprofileImage);
        b1 = (ImageView) rootView.findViewById(R.id.b1);

        d1 = (ImageView) rootView.findViewById(R.id.d1);
        d2 = (ImageView) rootView.findViewById(R.id.d2);
        d3 = (ImageView) rootView.findViewById(R.id.d3);
        d4 = (ImageView) rootView.findViewById(R.id.d4);
        d5 = (ImageView) rootView.findViewById(R.id.d5);

       // wid = b1.getWidth();

        txtname = (TextView) rootView.findViewById(R.id.txtname);
        txtmore = (TextView) rootView.findViewById(R.id.txtmore);
        txtdishname = (TextView) rootView.findViewById(R.id.txtdishname);
        txtdistance = (TextView) rootView.findViewById(R.id.txtdistance);
        txtaddress = (TextView) rootView.findViewById(R.id.txtaddress);
        txtingredient = (TextView) rootView.findViewById(R.id.txtingredient);
        txtestimateprice = (TextView) rootView.findViewById(R.id.txtestimateprice);
        txtestimatecookingtime = (TextView) rootView.findViewById(R.id.txtestimatecookingtime);

        spinnerassistant = (Spinner) rootView.findViewById(R.id.spinnerassistant);
        donestatus = (ImageButton) rootView. findViewById(R.id.donestatus);

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        bundle = this.getArguments();

        try {
            if (bundle != null) {
                if (activity.edthidenuserid.getText().toString().equals("")){
                    useridchef = Integer.parseInt(bundle.getString("useridchef"));
                    setProfile();
                    share.setVisibility(View.INVISIBLE);
                }else{
                    useridchef = Integer.parseInt(bundle.getString("useridchef"));
                    setProfile();
                    FillAppUserAssistance();
                    spinnerassistant.setOnItemSelectedListener(this);
                }

            }
        } catch (Exception ex) {

        }


        callno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  try {
                    Intent callnoIntent = new Intent(Intent.ACTION_CALL);
                    callnoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callnoIntent.setData(Uri.parse("tel:" + chefmobileno));
                    getActivity().startActivity(callnoIntent);
                } catch (SecurityException ex) {
                    Toast.makeText(rootView.getContext(), "Tel/Cell No Invalid!!", Toast.LENGTH_LONG).show();
                }*/

            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGoogleMapsInstalled()) {
                    try {
                        if (!latlon.equals("")) {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latlon);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            getActivity().startActivity(mapIntent);
                        } else if (!fulladdress_search.equals("")) {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + fulladdress_search);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            getActivity().startActivity(mapIntent);
                        } else {

                            try {
                                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + fulladdress_search);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            } catch (Exception ex) {
                                Toast.makeText(rootView.getContext(), "Address/Google Maps Not Found...", Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception ex) {
                        Toast.makeText(rootView.getContext(), "Enable Device GPS...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setMessage("Install Google Maps");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Install", getGoogleMapsListener());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });


        moretap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Fragment frag = new ChefListFrag();
                Bundle bundle = new Bundle();
                bundle.putString("useridchef", String.valueOf(useridchef));
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
                return false;
            }
        });
        moretap1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Toast.makeText(rootView.getContext(), "Loading Please Wait...", Toast.LENGTH_LONG).show();
                Fragment frag = new IngredientsCheckListFrag();
                bundle.putString("useridchef", String.valueOf(useridchef));
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
                return false;
            }
        });
        txtmore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Fragment frag = new ChefListFrag();
                Bundle bundle = new Bundle();
                bundle.putString("useridchef", String.valueOf(useridchef));
                frag.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, frag).commit();
                return false;
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Get Current Date
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        //textValue.setText();
                        //=========
                        try {
                            Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                            //String to = "jabun@ngobeniholdings.co.za";
                            // String to = "sntshangase3@gmail.com";
                            // m.setFrom("Info@ngobeniholdings.co.za");
                            // String to = "SibusisoN@sqaloitsolutions.co.za";
                            String to = "SibusisoN@sqaloitsolutions.co.za";
                            String from = "info@goingdots.com";
                            String subject = "Dish share";
                            String message = "Dear Sibusiso\nDish Shared" + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                            String[] toArr = {to};
                            m.setTo(toArr);
                            m.setFrom(from);
                            m.setSubject(subject);
                            m.setBody(message);

                            m.send();


                        } catch (Exception e) {


                        }
                        //==========

                        Date bestbefore = null;
                        Date today = new Date();
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String todaydate = date_format.format(today);
                        try {
                            Date date = new Date(date_format.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth).getTime());
                            Date date2 = new Date(date_format.parse(todaydate).getTime());
                            bestbefore = date;
                            today = date2;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        // daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);


                        prepdate = (date_format.format(bestbefore));
                        spinnerassistant.performClick();


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });

        donestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activity.edthidenuserid.getText().toString().equals("")) {

                    // donestatus.setVisibility(View.GONE);
                    Fragment fragment = new HomePremiumFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("couser", "couser");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();

                }else {
                    Fragment fragment = new HomePremiumFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("assist", "assistant");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();
                }
            }
        });


        return rootView;
    }
    public void setShare(){
        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {

                //=========insert into assist dish share
                try {
                    String query5 = "select * from [Dish] where [keyingredient]='" + bundle.getString("keyingredient") + "'";
                    PreparedStatement ps5 = con.prepareStatement(query5);
                    ResultSet rs5 = ps5.executeQuery();
                    rs5.next();
                    //Check if Dish Shared with Assistance Already
                    String query6 = "select * from [DishAssistantShare] where [keyingredient]='" + rs5.getString("keyingredient").toString() + "' and [assistancename]='"+assistant+"' and [userid] ='"+Integer.parseInt(activity.edthidenuserid.getText().toString())+"'";
                    PreparedStatement ps6 = con.prepareStatement(query6);
                    ResultSet rs6 = ps6.executeQuery();
                    rs6.next();
                    if (rs6.getRow() == 0) {
                        String query7 = "insert into [DishAssistantShare]([name],[image],[keyingredient],[recipe],[preptime],[cost],[prepdate],[isread],[assistancename],[userid]) " +
                                "values ('" + rs5.getString("name").toString() + "','" + rs5.getString("image").toString() + "','" + rs5.getString("keyingredient").toString() + "','" + rs5.getString("recipe").toString() + "','" + rs5.getString("preptime").toString() + "','" + rs5.getString("cost").toString() + "','" + prepdate + "','No','" + assistant + "','" + Integer.parseInt(activity.edthidenuserid.getText().toString()) + "')";
                        PreparedStatement preparedStatement = con.prepareStatement(query7);
                        preparedStatement.executeUpdate();
                        Toast.makeText(rootView.getContext(), "Dish Shared Successfully!!", Toast.LENGTH_LONG).show();


                           Fragment fragment = new HomePremiumFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("couser", "couser");
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();


                    }else{
                        Toast.makeText(rootView.getContext(), "Already Shared with this Assistance!!", Toast.LENGTH_LONG).show();

                           /* Fragment fragment = new HomePremiumFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("couser", "couser");
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();*/


            }
                } catch (Exception ex) {
                    Log.d("ReminderService In", "An error occurred1: " + ex.getMessage());
                }
                con.close();
            }
        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred2: " + ex.getMessage());
        }
        //=========
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        spinnerassistant.setSelection(position);
        // Toast.makeText(rootView.getContext(), "You've selected " + spinnercategory.getSelectedItem().toString()+String.valueOf(position) , Toast.LENGTH_SHORT).show();
        Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
        Drawable bg = getResources().getDrawable(R.drawable.edittext_bground);
        if ((spinnerassistant.getSelectedItem().toString().trim().equals("Select Assistant Name Here"))) {
            spinnerassistant.setBackground(errorbg);
        } else {
            spinnerassistant.setBackground(bg);
            assistant = spinnerassistant.getSelectedItem().toString();
            setShare();

        }

        // FillDataOrderBy(spinnercategory.getSelectedItemPosition() + 1);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en"));
                startActivity(intent);

                //Finish the activity so they can't circumvent the check
                // finish();
            }
        };
    }

    public void setProfile() {


        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {


                String query = "select * from [AppUser]  where [id]=" + useridchef;
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getRow() != 0) {
                    if (rs.getString("image") != null) {
                        byte[] decodeString = Base64.decode(rs.getString("image"), Base64.DEFAULT);
                        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                        decodebitmap1 = decodebitmap;

                    } else {
                        decodebitmap1 = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.profilephoto);
                    }

                    //Fill according to Chef Table
                    String query2 = "select * from [Chef] where [userid]=" + useridchef;
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    txtname.setText("Dish By " + rs2.getString("name"));
                    txtaddress.setText(rs2.getString("address"));
                    txtdishname.setText( bundle.getString("name"));
                    txtingredient.setText(bundle.getString("keyingredient"));
                    chefmobileno = rs2.getString("telephone").toString();

                    String query1 = "select * from [Dish] where [userid]='" + useridchef + "' and [name]='" + bundle.getString("name") + "'  and [keyingredient]='" + bundle.getString("keyingredient") + "'";
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    final ResultSet rs1 = ps1.executeQuery();
                    rs1.next();
                    String ingredients=rs1.getString("keyingredient").toString().replace(", ",",");
                    StringTokenizer tokenizer = new StringTokenizer(ingredients, ",");
                    int dnum=1;
                    while (tokenizer.hasMoreTokens()) {
                        String description=tokenizer.nextToken();
                        String   query3 = "select * from [UserProduct] where [description] ='" +  description+ "'";
                        PreparedStatement ps3 = con.prepareStatement(query3);
                        ResultSet rs3 = ps3.executeQuery();
                        while (rs3.next()) {
                            if(dnum==1){
                                byte[] decodeString = Base64.decode(rs3.getString("image"), Base64.DEFAULT);
                                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                d1.setImageBitmap(decodebitmap);
                            }else if(dnum==2){
                                byte[] decodeString = Base64.decode(rs3.getString("image"), Base64.DEFAULT);
                                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                d2.setImageBitmap(decodebitmap);
                            }else if(dnum==3){
                                byte[] decodeString = Base64.decode(rs3.getString("image"), Base64.DEFAULT);
                                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                d3.setImageBitmap(decodebitmap);

                            }else if(dnum==4){
                                byte[] decodeString = Base64.decode(rs3.getString("image"), Base64.DEFAULT);
                                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                d4.setImageBitmap(decodebitmap);

                            }else if(dnum==5){
                                byte[] decodeString = Base64.decode(rs3.getString("image"), Base64.DEFAULT);
                                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                d5.setImageBitmap(decodebitmap);
                            }
                        }
                        Log.d("ReminderService In", description);
                        dnum++;
                    // System.out.println(tokenizer.nextToken());
                    }


                    byte[] decodeString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    b1.setImageBitmap(decodebitmap);
//===============
                   /* BitmapFactory.Options options = null;
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 3;
                    Bitmap   bitmap = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length, options);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();
                  String  encodedString = Base64.encodeToString(byte_arr, 0);*/

                  int newWidth = rootView.getContext().getResources().getDisplayMetrics().widthPixels;

                                      // b1.setImageBitmap(scaleToFitWidthHeight(decodeBase64(bundle.getString("image")), 300, 200));
                    // b1.setImageBitmap(decodebitmap);


                   /* byte[] decodedByte = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
                    GlideApp
                            .with(this)
                            .load(decodedByte)
                           // .override(newWidth, 100) // resizes the image to these dimensions (in pixel)
                        //  .centerCrop()
                            .into(b1);*/






                    String query3 = "select * from [Dish] where [userid]='" + useridchef + "' and [name]='" + bundle.getString("name") + "'  and [keyingredient]='" + bundle.getString("keyingredient") + "'";
                    PreparedStatement ps3 = con.prepareStatement(query3);
                    ResultSet rs3 = ps3.executeQuery();
                    rs3.next();
                    txtestimateprice.setText(rs3.getString("cost").toString());
                    txtestimatecookingtime.setText(rs3.getString("preptime").toString());

                    fulladdress_search = rs2.getString("address").toString().replace(" ", "+");//Making use of address if lonlat fails

                    String lat = rs2.getString("lonlat").toString().substring(10, rs2.getString("lonlat").toString().indexOf(','));
                    String lon = rs2.getString("lonlat").toString().substring(rs2.getString("lonlat").toString().indexOf(',') + 1, rs2.getString("lonlat").toString().indexOf(')'));
                    latlon = lat + "," + lon;

                    double latdouble = Double.parseDouble(lat);
                    double londouble = Double.parseDouble(lon);

                    // create class object
                    GPSTracker mGPS = new GPSTracker(rootView.getContext());
                    if (mGPS.canGetLocation) {
                        mGPS.getLocation();
                        double latitude = mGPS.getLatitude();
                        double longitude = mGPS.getLongitude();
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);

                        String address = addresses.get(0).getAddressLine(0);
                        String fulladdress = address;
                        // Get reults

                        double distance;
                        Location locationA = new Location("");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);

                        Location locationB = new Location("");
                        locationB.setLatitude(latdouble);
                        locationB.setLongitude(londouble);


                        distance = locationA.distanceTo(locationB) / 1000;   // in km
                        txtdistance.setText((double) Math.round(distance) + "km Away");


                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(rootView.getContext().getResources(), decodebitmap1);
                        roundedBitmapDrawable.setCornerRadius(50.0f);
                        roundedBitmapDrawable.setAntiAlias(true);
                        chefprofileImage.setImageDrawable(roundedBitmapDrawable);
                    }


                }

            }
            con.close();
        } catch (Exception ex) {
            Log.d("ReminderService In", "An error occurred: " + ex.getMessage());
        }

    }
    public  Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);
    }
    private static void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String fname = "Dish.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(rootView.getContext());
        // path to /data/data/yourapp/app_data/imageDir
        //File mediaStorageDir = new File(rootView.getContext().getCacheDir()+ "/imageDir");
        File mediaStorageDir = rootView.getContext().getFilesDir();
        File dir =new File(mediaStorageDir,"imageDir");
        dir.mkdir();
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);


        // Create imageDir
        File mypath=new File(directory,"dish.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    private void loadImageFromStorage(String path)    {

        try {
            File f=new File(path, "dish.jpg");
            Bitmap btm = BitmapFactory.decodeStream(new FileInputStream(f));
            b1.setImageBitmap(btm);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    public Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public Bitmap scaleToFitWidthHeight(Bitmap b, int width, int height) {
        float factorw = width / (float) b.getWidth();
        float factorh = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(scaleBitmap(b, b.getWidth(), b.getHeight()), (int) (b.getWidth() * factorw), (int) (b.getHeight() * factorh), true);

    }

    public void FillAppUserAssistance() {

        try {
            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {

                Toast.makeText(rootView.getContext(), "Check your network connection!!", Toast.LENGTH_LONG).show();
            } else {

                String query = "select * from [AppUserAssistance]  where [userid]=" + activity.edthidenuserid.getText().toString();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                ArrayList<String> assistant = new ArrayList<String>();
                assistant.add("Select Assistant Name Here");
                while (rs.next()) {
                    assistant.add(rs.getString("assistancename"));

                }
                adapterassistant = new ArrayAdapter<String>(rootView.getContext(), R.layout.spinner_item, assistant);
                adapterassistant.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerassistant.setAdapter(adapterassistant);

                // Toast.makeText(ReminderEditScheduleFrag.this, fragment_mapy.edtuseremail.getText().toString(),Toast.LENGTH_LONG).show();
                // spinnerassistant.setSelection(adapterassistant.getPosition("gg"));
                // z = "Loaded Successfully";

                con.close();

            }
        } catch (Exception ex) {
            // Toast.makeText(ReminderEditScheduleFrag.this,"Excp"+ ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            // z = "Invalid data input!";
        }


    }

}



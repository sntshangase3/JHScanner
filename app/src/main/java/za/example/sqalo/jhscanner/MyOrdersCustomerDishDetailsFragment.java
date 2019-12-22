package za.example.sqalo.jhscanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * Created by sibusison on 2017/07/30.
 */
public class MyOrdersCustomerDishDetailsFragment extends Fragment {
    public int counter = 1;

    View rootView;
    ViewPager viewPager;
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    MainActivity activity = MainActivity.instance;
    FragmentManager fragmentManager;
    Bitmap decodebitmap1;
    Spinner spinnercategory;
    ImageView b1, chefprofileImage, share, callno, map;
    TextView txtname, txtdistance,txtaddress, txtdishname, txtingredient, txtestimateprice, txtestimatecookingtime;
    int catergoryclick = 0;
    ArrayList<String> catergory = new ArrayList<String>();
    Bundle bundle;
    String latlon = "";
    String fulladdress_search = "";
    String chefmobileno = "";
    Button btn_accept,btn_postpone,btn_decline;

    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds, mMSeconds;
    DatePickerDialog datePickerDialog;
    Calendar c;
    private Calendar mCalendar;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    String postponeto_date;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dishdetailsorder, container, false);

        callno = (ImageView) rootView.findViewById(R.id.callno);
        map = (ImageView) rootView.findViewById(R.id.map);
        share = (ImageView) rootView.findViewById(R.id.share);
        chefprofileImage = (ImageView) rootView.findViewById(R.id.chefprofileImage);
        b1 = (ImageView) rootView.findViewById(R.id.b1);


        txtname = (TextView) rootView.findViewById(R.id.txtname);
        txtdishname = (TextView) rootView.findViewById(R.id.txtdishname);
        txtdistance = (TextView) rootView.findViewById(R.id.txtdistance);
        txtaddress = (TextView) rootView.findViewById(R.id.txtaddress);
        txtingredient = (TextView) rootView.findViewById(R.id.txtingredient);
        txtestimateprice = (TextView) rootView.findViewById(R.id.txtestimateprice);
        txtestimatecookingtime = (TextView) rootView.findViewById(R.id.txtestimatecookingtime);

        btn_accept = (Button) rootView. findViewById(R.id.btn_accept);
        btn_postpone = (Button) rootView. findViewById(R.id.btn_postpone);
        btn_decline = (Button) rootView. findViewById(R.id.btn_decline);


        mCalendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(mCalendar.getTime());
        postponeto_date=dateForButton;

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";

        bundle = this.getArguments();

        try {
            if (bundle != null) {

                setProfile();
            }
        } catch (Exception ex) {

        }

        callno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent callnoIntent = new Intent(Intent.ACTION_CALL);
                    callnoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callnoIntent.setData(Uri.parse("tel:" + chefmobileno));
                    getActivity().startActivity(callnoIntent);
                } catch (SecurityException ex) {
                    Toast.makeText(rootView.getContext(), "Tel/Cell No Invalid!!", Toast.LENGTH_LONG).show();
                }

            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isGoogleMapsInstalled())
                {
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
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                    builder.setMessage("Install Google Maps");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Install", getGoogleMapsListener());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }



            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Accept();
            }
        });
       btn_postpone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Postpone();
            }
        });
        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Decline();
            }
        });


        return rootView;
    }
    public boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
    public DialogInterface.OnClickListener getGoogleMapsListener()
    {
        return new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en"));
                startActivity(intent);

                //Finish the activity so they can't circumvent the check
               // finish();
            }
        };
    }
    public void Accept() {


        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {
                //Marked DishOrder  isread Accepted
                String commands = "update [DishOrder] set [isread]='Accepted' where [id]='" + bundle.getString("id").toString() + "'";
                PreparedStatement preStmt = con.prepareStatement(commands);
                preStmt.executeUpdate();
                Toast.makeText(rootView.getContext(),"Order "+ bundle.getString("name").toString()+ " has been accepted", Toast.LENGTH_LONG).show();
                Toast.makeText(rootView.getContext(), "Acceptance email sent to Customer...",Toast.LENGTH_LONG).show();

                //To Customer chef
                String query = "select * from [AppUser]  where [id]='" + bundle.getString("userid").toString()+"'";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String queryo = "select * from [DishOrder]  where [id]='" + bundle.getString("id").toString()+"'";
                PreparedStatement pso = con.prepareStatement(queryo);
                ResultSet rso = pso.executeQuery();
                rso.next();

                //From Chef
                String query2= "select * from [AppUser]  where [id]='" + activity.edthidenuserid.getText().toString()+"'";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();
                rs2.next();

                /*String query1 = "select * from [Chef] where [userid]='" + bundle.getString("userid").toString()+"'";
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                rs1.next();*/

                //=========
                try {
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                    String to = rs.getString("email").toString();
                    String from = rs2.getString("email").toString();
                    String subject = "Order Accepted";
                    String message = "Dear "+rs.getString("firstname").toString()+"\nYour Order "+ bundle.getString("name").toString()+ " has been accepted,will be delivered on "+bundle.getString("orderdate").toString() +" at this address:\n"+rso.getString("deliveryaddress") + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";
                    String[] toArr = {to};
                    m.setTo(toArr);
                    m.setFrom(from);
                    m.setSubject(subject);
                    m.setBody(message);
                    m.send();
                } catch (Exception e) {

                }
                //==========
                //=========
                try {
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                    String to = "SibusisoN@sqaloitsolutions.co.za";
                    String from = rs2.getString("email").toString();
                    String subject = "Order Accepted";
                    String message = "Dear "+rs.getString("firstname").toString()+"\nYour Order "+ bundle.getString("name").toString()+ " has been accepted,will be delivered on "+bundle.getString("orderdate").toString() +" at this address:\n"+rso.getString("deliveryaddress") + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";
                    String[] toArr = {to};
                    m.setTo(toArr);
                    m.setFrom(from);
                    m.setSubject(subject);
                    m.setBody(message);
                    m.send();
                } catch (Exception e) {

                }
                //==========
            }
            con.close();
        } catch (Exception ex) {
           // Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
        }
    }
    public void Postpone() {


        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {
//#################

                // Get Current Date
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                mSeconds = c.get(Calendar.SECOND);
                mMSeconds = c.get(Calendar.MILLISECOND);
                datePickerDialog = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
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

                        postponeto_date=(date_format.format(bestbefore));
                        try{
                            //############
                            //Marked DishOrder  isread Accepted
                            String commands = "update [DishOrder] set [isread]='Postpone',[orderdate]='"+postponeto_date+"' where [id]='" + bundle.getString("id").toString() + "'";
                            PreparedStatement preStmt = con.prepareStatement(commands);
                            preStmt.executeUpdate();
                            Toast.makeText(rootView.getContext(),"Order "+ bundle.getString("name").toString()+ " has been postponed", Toast.LENGTH_LONG).show();
                            Toast.makeText(rootView.getContext(), "Postpone email sent to Customer...",Toast.LENGTH_LONG).show();

                            //To Customer chef
                            String query = "select * from [AppUser]  where [id]='" + bundle.getString("userid").toString()+"'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();
                            rs.next();
                            String queryo = "select * from [DishOrder]  where [id]='" + bundle.getString("id").toString()+"'";
                            PreparedStatement pso = con.prepareStatement(queryo);
                            ResultSet rso = pso.executeQuery();
                            rso.next();

                            //From Chef
                            String query2= "select * from [AppUser]  where [id]='" + activity.edthidenuserid.getText().toString()+"'";
                            PreparedStatement ps2 = con.prepareStatement(query2);
                            ResultSet rs2 = ps2.executeQuery();
                            rs2.next();

                            /*String query1 = "select * from [Chef] where [userid]='" + bundle.getString("userid").toString()+"'";
                            PreparedStatement ps1 = con.prepareStatement(query1);
                            ResultSet rs1 = ps1.executeQuery();
                            rs1.next();*/

                            //=========
                            try {
                                Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                                String to = rs.getString("email").toString();
                                String from = rs2.getString("email").toString();
                                String subject = "Order Postponed";
                                String message = "Dear "+rs.getString("firstname").toString()+"\nYour Order "+ bundle.getString("name").toString()+ " has been postponed to "+postponeto_date+",will be delivered at this address:\n"+rso.getString("deliveryaddress") + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";
                                String[] toArr = {to};
                                m.setTo(toArr);
                                m.setFrom(from);
                                m.setSubject(subject);
                                m.setBody(message);
                                m.send();
                            } catch (Exception e) {

                            }
                            //==========
                            //=========
                            try {
                                Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                                String to = "SibusisoN@sqaloitsolutions.co.za";
                                String from = rs2.getString("email").toString();
                                String subject = "Order Postponed";
                                String message = "Dear "+rs.getString("firstname").toString()+"\nYour Order "+ bundle.getString("name").toString()+ " has been postponed to "+postponeto_date+",will be delivered at this address:\n"+rso.getString("deliveryaddress") + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";
                                String[] toArr = {to};
                                m.setTo(toArr);
                                m.setFrom(from);
                                m.setSubject(subject);
                                m.setBody(message);
                                m.send();
                            } catch (Exception e) {

                            }
                            //==========
                        }catch (Exception ex){

                        }


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
            con.close();
        } catch (Exception ex) {
            // Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
        }
    }
    public void Decline() {


        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {
                //Marked DishOrder  isread Accepted
                String commands = "update [DishOrder] set [isread]='Declined' where [id]='" + bundle.getString("id").toString() + "'";
                PreparedStatement preStmt = con.prepareStatement(commands);
                preStmt.executeUpdate();
                Toast.makeText(rootView.getContext(),"Order "+ bundle.getString("name").toString()+ " has been declined", Toast.LENGTH_LONG).show();
                Toast.makeText(rootView.getContext(), "Declined email sent to Customer...",Toast.LENGTH_LONG).show();

                //To Customer chef
                String query = "select * from [AppUser]  where [id]='" + bundle.getString("userid").toString()+"'";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();

                //From Chef
                String query2= "select * from [AppUser]  where [id]='" + activity.edthidenuserid.getText().toString()+"'";
                PreparedStatement ps2 = con.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();
                rs2.next();

                /*String query1 = "select * from [Chef] where [userid]='" + bundle.getString("userid").toString()+"'";
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                rs1.next();*/

                //=========
                try {
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                    String to = rs.getString("email").toString();
                    String from = rs2.getString("email").toString();
                    String subject = "Order Declined";
                    String message = "Dear "+rs.getString("firstname").toString()+"\nYour Order "+ bundle.getString("name").toString()+ " has been declined,not in position to deliver.\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";
                    String[] toArr = {to};
                    m.setTo(toArr);
                    m.setFrom(from);
                    m.setSubject(subject);
                    m.setBody(message);
                    m.send();
                } catch (Exception e) {

                }
                //==========

                //=========
                try {
                    Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                    String to = "SibusisoN@sqaloitsolutions.co.za";
                    String from = rs2.getString("email").toString();
                    String subject = "Order Declined";
                    String message = "Dear "+rs.getString("firstname").toString()+"\nYour Order "+ bundle.getString("name").toString()+ " has been declined,not in position to deliver.\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";
                    String[] toArr = {to};
                    m.setTo(toArr);
                    m.setFrom(from);
                    m.setSubject(subject);
                    m.setBody(message);
                    m.send();
                } catch (Exception e) {

                }
                //==========
            }
            con.close();
        } catch (Exception ex) {
            // Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
        }
    }
    public void setProfile() {


        try {

            ConnectionClass cn = new ConnectionClass();
            con = cn.connectionclass(un, pass, db, ip);
            if (con == null) {
                Toast.makeText(rootView.getContext(), "Check your network connection!!",Toast.LENGTH_LONG).show();
            } else {

               String query1 = "select * from [DishOrder] where [id]='"+ bundle.getString("id").toString()+"' and [userid]='" + bundle.getString("userid").toString() + "' and [name]='" + bundle.getString("name").toString() + "'  and [keyingredient]='" + bundle.getString("keyingredient").toString() + "'";
                PreparedStatement ps1 = con.prepareStatement(query1);
                ResultSet rs1 = ps1.executeQuery();
                rs1.next();

                String query = "select * from [AppUser]  where [id]='" + bundle.getString("userid").toString()+"'";
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
                   /* String query2 = "select * from [Chef] where [userid]='" + bundle.getString("userid").toString()+"'";
                    PreparedStatement ps2 = con.prepareStatement(query2);
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();*/
                    txtname.setText("Order By " + rs.getString("firstname").toString());
                    txtaddress.setText(rs1.getString("deliveryaddress"));
                    txtdishname.setText("Dish:" + bundle.getString("name").toString());
                    txtingredient.setText(bundle.getString("keyingredient").toString());
                    chefmobileno = rs.getString("contact").toString();
                    byte[] decodeString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);


                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inDither = false;
                    options.inSampleSize = 1;
                    options.inScaled = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length,options);


                    int newWidth=rootView.getContext().getResources().getDisplayMetrics().widthPixels;
                    // b1.setImageBitmap(scaleBitmap(decodebitmap,newWidth,300));
                    b1.setImageBitmap(scaleToFitWidthHeight(decodebitmap,newWidth,300));

                    //b1.setImageBitmap(decodebitmap);

                  String query3 = "select * from [Dish] where [userid]='" + activity.edthidenuserid.getText().toString() + "' and [name]='" + bundle.getString("name").toString() + "'  and [keyingredient]='" + bundle.getString("keyingredient").toString() + "'";
                    PreparedStatement ps3 = con.prepareStatement(query3);
                    ResultSet rs3 = ps3.executeQuery();
                    rs3.next();
                    txtestimateprice.setText( rs3.getString("cost").toString());
                    txtestimatecookingtime.setText( rs3.getString("preptime").toString());

                    fulladdress_search = rs1.getString("deliveryaddress").toString().replace(" ", "+");//Making use of address if lonlat fails

                    String lat = rs1.getString("lonlat").toString().substring(10, rs1.getString("lonlat").toString().indexOf(','));
                    String lon = rs1.getString("lonlat").toString().substring(rs1.getString("lonlat").toString().indexOf(',') + 1, rs1.getString("lonlat").toString().indexOf(')'));
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
             Log.d("ReminderService In", "An error occurred: "+ex.getMessage());
        }

    }
    public  Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
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

    public  Bitmap scaleToFitWidthHeight(Bitmap b, int width, int height)
    {
        float factorw = width / (float) b.getWidth();
        float factorh = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(scaleBitmap(b,b.getWidth(),b.getHeight()), (int) (b.getWidth() * factorw), (int) (b.getHeight() * factorh), true);

    }



}



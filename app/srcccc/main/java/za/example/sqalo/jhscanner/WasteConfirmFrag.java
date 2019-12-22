package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by sibusison on 2017/07/30.
 */
public class WasteConfirmFrag extends Fragment  {

    View rootView;
TextView description;
EditText edtquant;
    private Button confirm;
    private Button cancel;
    //---------con--------
    Connection con;
    String un, pass, db, ip;
    Spinner spinnerreason;
    String m_Text_donate="";

    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    Bundle bundle;
    String edtproductId, edtname, edtdescription, edtbestbeforeStatus, edtbestbeforedate, edtsize, edtprice, edtquantity, edtquantityperbulk, edtquantitybulktotal, edtreorderpoint, edttotal;
    String edtpreferbestbeforedays, edtbarcode, edtstorage, edtisscanned, edtuserid, edtcategoryid, edtretailerid, edtweb,encodedImage;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.waste_confirm_reason, container, false);


        description = (TextView) rootView. findViewById(R.id.description);
        edtquant = (EditText) rootView. findViewById(R.id.edtquantity);

        confirm = (Button)rootView. findViewById(R.id.confirm);
        cancel = (Button)rootView. findViewById(R.id.cancel);

        spinnerreason = (Spinner)rootView. findViewById(R.id.spinnerreason);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.wastereason_arrays, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerreason.setAdapter(adapter);

        fragmentManager = getFragmentManager();

        // Declaring Server ip, username, database name and password
        ip = "winsqls01.cpt.wa.co.za";
        db = "JHShopper";
        un = "sqaloits";
        pass = "422q5mfQzU";
        bundle = this.getArguments();
        description.setText(bundle.getString("description"));
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Drawable errorbg = getResources().getDrawable(R.drawable.edittexterror_bground);
                if ((spinnerreason.getSelectedItem().toString().trim().equals("Select Waste Reason"))) {
                        spinnerreason.setBackground(errorbg);
                    }else {

                    try {
                        m_Text_donate = edtquant.getText().toString();
                   ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);
                        String selectedname = bundle.getString("name");
                        String selectedquantity = bundle.getString("quantity");
                        String selectedstatus = bundle.getString("bestbeforeStatus");
                        String query = "select * from [UserProduct] where [userid]='" + activity.edthidenuserid.getText().toString() + "'  and [name]='" + selectedname + "'  and [quantity]='" + selectedquantity + "'  and [bestbeforeStatus]='" + selectedstatus + "'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            //#########################
                            edtproductId = rs.getString("id").toString();
                            edtname = selectedname;
                            edtdescription = rs.getString("description").toString();
                            edtbestbeforeStatus = selectedstatus;
                            edtbestbeforedate = rs.getString("bestbefore").toString();
                            edtsize = rs.getString("size").toString();
                            edtprice = rs.getString("price").toString();
                            edtquantity = selectedquantity;
                            edtquantityperbulk = rs.getString("quantityperbulk").toString();
                            edtreorderpoint = rs.getString("reorderpoint").toString();
                            edttotal = rs.getString("totatitemvalue").toString();
                            edtpreferbestbeforedays = rs.getString("preferbestbeforedays").toString();
                            edtbarcode = rs.getString("Barcode").toString();
                            edtstorage = rs.getString("storage").toString();
                            edtisscanned = rs.getString("isscanned").toString();
                            edtuserid = activity.edthidenuserid.getText().toString();
                            edtcategoryid = rs.getString("categoryid").toString();
                            edtretailerid = rs.getString("retailerid").toString();
                            edtweb = rs.getString("website").toString();
                            encodedImage = rs.getString("image");

                            ///======

                            try {





                                if (Integer.parseInt(m_Text_donate) == Integer.parseInt(edtquantity)) {
                                    //Donate all
                                    Date today = new Date();
                                    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                    String todaydate = date_format.format(today);
                                    String commandsd = "insert into [UserProductWaste] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid],[wastereason])" +
                                            "values ('" + edtbestbeforedate + "','" + edtname + "','" + edtdescription + "','" +
                                            edtbestbeforeStatus + "','" + edtsize + "','" + Double.parseDouble(edtprice) + "','" +
                                            edtstorage + "','" + Integer.parseInt(edtpreferbestbeforedays) + "','" + Integer.parseInt(edtquantity) + "','" + Integer.parseInt(edtquantityperbulk) + "','" + Integer.parseInt(edtreorderpoint) + "','" + Double.parseDouble(edttotal) +
                                            "','" + edtisscanned + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid) + "','" + Integer.parseInt(edtcategoryid) + "','" + Integer.parseInt(edtretailerid) + "','"+spinnerreason.getSelectedItemPosition()+"')";
                                    // encodedImage which is the Base64 String

                                    PreparedStatement preStmtd = con.prepareStatement(commandsd);
                                    preStmtd.executeUpdate();

                                    //Check if need notifications as R.O.P
                                    if (!edtreorderpoint.trim().equals("0")) {
                                        //trigger notification
                                        //**********
                                        try {
                                            String notificationbody = "You have 0 " + edtname + " left";
                                            byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                                            //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                            NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                            Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                            PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            builderd.setContentIntent(contentIntent);
                                            builderd.setAutoCancel(false);
                                            // Add as notification
                                            builderd.build();

                                            NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                            Notification myNotication;

                                            myNotication = builderd.getNotification();
                                            myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                            myNotication.defaults |= Notification.DEFAULT_SOUND;
                                            myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                            manager.notify(0, myNotication);
                                        } catch (Exception ex) {
                                            // Toast.makeText(rootView.getContext(), ex.getMessage().toString()+"Here1",Toast.LENGTH_LONG).show();
                                        }
                                        //**********
                                    }

                                    //Remove completely from [UserProduct]
                                    String commands = "delete from [UserProduct]  where [id]='" + edtproductId + "'";
                                    PreparedStatement preStmt = con.prepareStatement(commands);
                                    preStmt.executeUpdate();
                                    if (!activity.edthidenuserid.equals("")) {
                                        Fragment frag = new MyListFrag();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.mainFrame, frag).commit();
                                    } else {
                                        Fragment frag = new HomePremiumFragment();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.mainFrame, frag).commit();
                                    }

                                    Toast.makeText(rootView.getContext(), "Product Waste Successfully", Toast.LENGTH_LONG).show();
                                } else if (Integer.parseInt(m_Text_donate) < Integer.parseInt(edtquantity)) {
                                    //Donate not all
                                    int diff;
                                    double total;
                                    double totaldonation;

                                    if (!edtquantityperbulk.equals("0")) {

                                        diff = Integer.parseInt(edtquantity) - Integer.parseInt(m_Text_donate);
                                        total = diff * (Double.valueOf(edtprice) / Integer.valueOf(edtquantityperbulk));
                                        totaldonation = Integer.parseInt(m_Text_donate) * (Double.valueOf(edtprice) / Integer.valueOf(edtquantityperbulk));
                                        Log.d("ReminderService In", edtquantityperbulk+"####"+edtquantity+" "+m_Text_donate+" "+totaldonation);
                                    } else {
                                        diff = Integer.parseInt(edtquantity) - Integer.parseInt(m_Text_donate);
                                        total = diff * Double.parseDouble(edtprice);
                                        totaldonation = Integer.parseInt(m_Text_donate) * Double.parseDouble(edtprice);
                                    }

                                    Date today = new Date();
                                    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                    String todaydate = date_format.format(today);
                                    String commandsd = "insert into [UserProductWaste] ([bestbefore],[name],[description],[bestbeforeStatus],[size],[price],[storage],[preferbestbeforedays],[quantity],[quantityperbulk],[reorderpoint],[totatitemvalue],[isscanned],[purchasedate],[image],[userid],[categoryid],[retailerid],[wastereason])" +
                                            "values ('" + edtbestbeforedate + "','" + edtname + "','" + edtdescription + "','" +
                                            edtbestbeforeStatus + "','" + edtsize + "','" + Double.parseDouble(edtprice) + "','" +
                                            edtstorage + "','" + Integer.parseInt(edtpreferbestbeforedays) + "','" + Integer.parseInt(m_Text_donate) + "','" + Integer.parseInt(edtquantityperbulk) + "','" + Integer.parseInt(edtreorderpoint) + "','" + totaldonation +
                                            "','" + edtisscanned + "','" + todaydate + "','" + encodedImage + "','" + Integer.parseInt(edtuserid) + "','" + Integer.parseInt(edtcategoryid) + "','" + Integer.parseInt(edtretailerid) + "','" + spinnerreason.getSelectedItem().toString() + "')";
                                    // encodedImage which is the Base64 String
                                    PreparedStatement preStmtd = con.prepareStatement(commandsd);
                                    preStmtd.executeUpdate();

                                    //Update [UserProduct] qty and total
                                    String commands = "update [UserProduct] set [quantity]='" + diff + "',[totatitemvalue]='" + total + "' where [id]='" + edtproductId + "'";
                                    PreparedStatement preStmt = con.prepareStatement(commands);
                                    preStmt.executeUpdate();

                                    //Check if need notifications as R.O.P
                                    if (diff <= Integer.valueOf(edtreorderpoint) && !edtreorderpoint.trim().equals("0")) {
                                        //trigger notification
                                        //**********
                                        try {
                                            String notificationbody = "You have " + String.valueOf(diff) + " " + edtname + " left";
                                            byte[] decodeString = Base64.decode(encodedImage, Base64.DEFAULT);
                                            //  Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                                            NotificationCompat.Builder builderd = new NotificationCompat.Builder(rootView.getContext()).setTicker("GoingDots R.O.P").setSmallIcon(R.drawable.logos).setContentTitle("R.O.Point Notification").setContentText(notificationbody);
                                            Intent notificationIntent = new Intent(rootView.getContext(), SplashFragment.class);
                                            PendingIntent contentIntent = PendingIntent.getActivity(rootView.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            builderd.setContentIntent(contentIntent);
                                            builderd.setAutoCancel(false);
                                            // Add as notification
                                            builderd.build();

                                            NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                            Notification myNotication;

                                            myNotication = builderd.getNotification();
                                            myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                            myNotication.defaults |= Notification.DEFAULT_SOUND;
                                            myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                            manager.notify(0, myNotication);
                                        } catch (Exception ex) {
                                            Log.d("ReminderService In", ex.getMessage().toString());
                                        }
                                        //**********
                                    }

                                    if (!activity.edthidenuserid.equals("")) {
                                        Fragment frag = new MyListFrag();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.mainFrame, frag).commit();
                                    } else {
                                        Fragment frag = new HomePremiumFragment();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.mainFrame, frag).commit();
                                    }
                                    Toast.makeText(rootView.getContext(), "Partially Waste Successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    // Can not donate with this quantity(Donation must be above 0, less or equal your available quantity)
                                    Toast.makeText(rootView.getContext(), "Can not waste with this quantity(Waste must be above 0, less or equal your available quantity)", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ex) {
                                Log.d("ReminderService In", ex.getMessage().toString());
                            }
                            //===

                        }



                    } catch (Exception ex) {
                        Log.d("ReminderService In", ex.getMessage().toString());
                    }


                }


            }

        });

          return rootView;
    }



}



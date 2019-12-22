package za.example.sqalo.jhscanner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by fabio on 30/01/2016.
 */
public class SensorService extends Service {
    public int counter=0;
    // MainActivity activity =   MainActivity.instance;

    Connection con;
    String un,pass,db,ip;

    public String keycouserid="";
    public String keyassistuserid="";
    public static final String PREFS_NAME = "MyApp_Settings";

    public SensorService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public SensorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("za.example.sqalo.jhscanner.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //mylistselected the timer, to wake up every 1 second
       // timer.mylistselected(timerTask, 1000, 1000); //
        //mylistselected the timer, to wake up every 5 hours
       //####### timer.mylistselected(timerTask, 18000, 18000); #####//

    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
                //@@@@@@@@@@@@@@@

                // Do the task here


                //  WakefulBroadcastReceiver.completeWakefulIntent(intent);
//######
                final NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                //Intent notificationIntent = new Intent(this, ReminderEditActivity.class);
                final Intent notificationIntent = new Intent(SensorService.this, SplashFragment.class);


//===========
                // Log.d("ReminderService In", "0");
                final Notification.Builder builder = new Notification.Builder(SensorService.this);
                final Calendar now = Calendar.getInstance();

                try {
                    // Co-user activity.edthidenuserid.getText().toString();
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    // Reading from SharedPreferences
                    String value = settings.getString("keycouserid", "");
                    keycouserid = value;
                } catch (Exception e) {

                }
                try {
                    // Assistants activity.edthidenuserrole.getText().toString();
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    // Reading from SharedPreferences
                    String value = settings.getString("keyassistuserid", "");
                    keyassistuserid = value;
                } catch (Exception e) {

                }


                if (!keycouserid.equals("")) {

                  //  new Timer().mylistselected(new TimerTask() {
                     //   @Override
                      //  public void run() {
                            String notificationbody = "";//
                            // this code will be executed after 2 seconds  2000
                            try {
                                // Declaring Server ip, username, database name and password
                                ip = "winsqls01.cpt.wa.co.za";
                                db = "JHShopper";
                                un = "sqaloits";
                                pass = "422q5mfQzU";
                                ConnectionClass cn = new ConnectionClass();
                                con = cn.connectionclass(un, pass, db, ip);
                                if (con == null) {

                                    CharSequence text = "Error in internet connection!!";

                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(SensorService.this, text, duration);
                                    toast.show();
                                } else {




                                    String query = "select * from [UserProduct] where [userid]='" + keycouserid + "' and [isread]='No'";
                                    PreparedStatement ps = con.prepareStatement(query);
                                    ResultSet rs = ps.executeQuery();
                                    String dates = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Calendar.getInstance().getTime());


                                 //   if ((now.get(Calendar.HOUR_OF_DAY) == 0 && now.get(Calendar.MINUTE) == 9) && now.get(Calendar.SECOND) == 0) {
                                        while (rs.next()) {
                                          //  Log.d("ReminderService In", "Co-user");
                                            Date bestbefore = null;
                                            Date today = new Date();
                                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                            String todaydate = date_format.format(today);
                                            try {
                                                Date date = new Date(date_format.parse(rs.getString("bestbefore").toString()).getTime());
                                                Date date2 = new Date(date_format.parse(todaydate).getTime());
                                                bestbefore = date;
                                                today = date2;
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            long daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);

                                            if (daysleft == Integer.parseInt(rs.getString("preferbestbeforedays").toString())) {
                                                Log.d("ReminderService In", String.valueOf(daysleft) + " " + rs.getString("preferbestbeforedays").toString());
                                                notificationbody = notificationbody + rs.getString("quantity").toString() + " " + rs.getString("name").toString() + "\n";



                                                //##Update isNotified/Read
                                                String commands = "update [UserProduct] set [isread]='Yes' where [id]='" + rs.getString("id").toString() + "'";
                                                PreparedStatement preStmt = con.prepareStatement(commands);
                                                preStmt.executeUpdate();

                                   // }




                                    // if ((now.get(Calendar.HOUR_OF_DAY)==22 && now.get(Calendar.MINUTE)==48)&& ((now.get(Calendar.SECOND)>=5)||(now.get(Calendar.SECOND)<10)))
                                  //  if ((now.get(Calendar.HOUR_OF_DAY) == 0 && now.get(Calendar.MINUTE) == 9) && now.get(Calendar.SECOND) == 0) {
                                        // if (!notificationbody.toString().equals("")&&!username.equals("")&& now.get(Calendar.HOUR_OF_DAY)==12  && now.get(Calendar.MINUTE)==46 &&(now.get(Calendar.SECOND)==0)&& ((now.get(Calendar.MILLISECOND)>=0)||(now.get(Calendar.MILLISECOND)<=0.5)) ) {
                                        Notification myNotication;
                                        int id = 0;
                                        PendingIntent pendingIntent = PendingIntent.getActivity(SensorService.this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                                        Log.d("ReminderService In", "Time");
                                        builder.setAutoCancel(false);

                                        builder.setTicker("Item about to expire");
                                        builder.setSmallIcon(R.drawable.logos);
                                        builder.setContentIntent(pendingIntent);
                                        builder.setContentTitle(getResources().getString(R.string.notify_new_task_title));
                                        builder.setContentText(notificationbody);


                                        //builder.setSubText("This is subtext...");
                                        // builder.build();

                                        myNotication = builder.setStyle(new Notification.BigTextStyle().bigText(notificationbody)).build();
                                        myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                        // myNotication.defaults |= Notification.DEFAULT_SOUND;
                                        // myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                        mgr.notify(id, myNotication);




                                            }
                                        }
                                        if(!notificationbody.equals("")){
                                            try {
                                                //User logged in send email
                                                String queryu = "select * from [AppUser] where [id]='" + keycouserid + "'";
                                                PreparedStatement psu = con.prepareStatement(queryu);
                                                ResultSet rsu = psu.executeQuery();
                                                rsu.next();
                                                Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                                                //String to = "jabun@ngobeniholdings.co.za";
                                                // String to = "sntshangase3@gmail.com";
                                                // m.setFrom("Info@ngobeniholdings.co.za");
                                                // String to = "SibusisoN@sqaloitsolutions.co.za";
                                                String to = rsu.getString("email").toString();
                                                String from = "info@goingdots.com";
                                                String subject = "Item about to expire";
                                                String message = "Dear " + rsu.getString("firstname").toString() + "\nYou have item/s about to expire:\n" + notificationbody + "\nLogin on your app and check." + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

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
                                    //=========

                                    }


                              //  }
                            } catch (Exception ex) {

                            }
                     //   }

                   // }, 1000);



              /* // if (notificationbody.toString().equals("")&& now.get(Calendar.HOUR_OF_DAY)==0  && now.get(Calendar.MINUTE)==38  ) {

                    Notification myNotication;
                    int id = 0;
                    PendingIntent pendingIntent = PendingIntent.getActivity(SensorService.this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

                    builder.setAutoCancel(false);

                    builder.setTicker("Reminder");
                    builder.setSmallIcon(R.drawable.logos);
                    builder.setContentIntent(pendingIntent);
                    builder.setContentTitle(getResources().getString(R.string.notify_new_task_reminder));
                    builder.setContentText("Remember to check your products");


                    //builder.setSubText("This is subtext...");
                    // builder.build();

                    myNotication = builder.setStyle(new Notification.BigTextStyle().bigText(notificationbody)).build();
                   // myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                    // myNotication.defaults |= Notification.DEFAULT_SOUND;
                    // myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;
                mgr.notify(id, myNotication);*/

                    // }

                    //==========

                    //###

                    //@@@@@@@@@@@@@
                }else if( !keyassistuserid.equals("")){
                    //Assistant login
                    String notificationbody = "";//

                    try {
                        // Declaring Server ip, username, database name and password
                        ip = "winsqls01.cpt.wa.co.za";
                        db = "JHShopper";
                        un = "sqaloits";
                        pass = "422q5mfQzU";
                        ConnectionClass cn = new ConnectionClass();
                        con = cn.connectionclass(un, pass, db, ip);
                        if (con == null) {

                            CharSequence text = "Error in internet connection!!";

                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(SensorService.this, text, duration);
                            toast.show();
                        } else {





                            String query11 = "select * from [AppUserAssistance] where [id]='" + keyassistuserid + "'";
                            PreparedStatement ps11 = con.prepareStatement(query11);
                            ResultSet rs11 = ps11.executeQuery();
                            rs11.next();
                     // rs11.getString("userid").toString();

                            String query = "select * from [UserProductNotification] where [userid]='" + rs11.getString("userid").toString() + "' and [assistancename]='" + rs11.getString("assistancename").toString() + "' and [issent]='No' and [isread]='No'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs = ps.executeQuery();
                            Log.d("ReminderService In", "Assist");

                            //   if ((now.get(Calendar.HOUR_OF_DAY) == 0 && now.get(Calendar.MINUTE) == 9) && now.get(Calendar.SECOND) == 0) {
                            while (rs.next()) {

                                Date bestbefore = null;
                                Date today = new Date();
                                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                String todaydate = date_format.format(today);
                                try {
                                    Date date = new Date(date_format.parse(rs.getString("noticedate").toString()).getTime());
                                    Date date2 = new Date(date_format.parse(todaydate).getTime());
                                    bestbefore = date;
                                    today = date2;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                long daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);
                                //Check for notifications isNotified viewed by relevant assistant
                                if (daysleft == 0 && rs.getRow() != 0) {
                                    Log.d("ReminderService In", String.valueOf(daysleft) + " " + rs.getString("noticedate").toString());




                                   // notificationbody = notificationbody +  rs.getString("name").toString() + "\n";
                                    // if (activity.edthidenuserid.getText().toString().equals("")&&(now.get(Calendar.HOUR_OF_DAY)==1 && now.get(Calendar.MINUTE)==2)&& ((now.get(Calendar.SECOND)==0)))
                                    //##Update isNotified viewed
                                    String commands = "update [UserProductNotification] set [issent]='Yes' where [id]='" + rs.getString("id").toString() + "'";
                                    PreparedStatement preStmt = con.prepareStatement(commands);
                                    preStmt.executeUpdate();
//update [JHShopper].[sqaloits].[UserProductNotification] set [noticedate]=(CONVERT(VARCHAR(10), GETDATE(), 110) )  where (CONVERT(VARCHAR(10), [noticedate], 110) )>=(CONVERT(VARCHAR(10), GETDATE(), 110) ) and [issent]='No'
                                    // }




                                    // if ((now.get(Calendar.HOUR_OF_DAY)==22 && now.get(Calendar.MINUTE)==48)&& ((now.get(Calendar.SECOND)>=5)||(now.get(Calendar.SECOND)<10)))
                                    //  if ((now.get(Calendar.HOUR_OF_DAY) == 0 && now.get(Calendar.MINUTE) == 9) && now.get(Calendar.SECOND) == 0) {
                                    // if (!notificationbody.toString().equals("")&&!username.equals("")&& now.get(Calendar.HOUR_OF_DAY)==12  && now.get(Calendar.MINUTE)==46 &&(now.get(Calendar.SECOND)==0)&& ((now.get(Calendar.MILLISECOND)>=0)||(now.get(Calendar.MILLISECOND)<=0.5)) ) {
                                    Notification myNotication;
                                    int id = 0;
                                    PendingIntent pendingIntent = PendingIntent.getActivity(SensorService.this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                                    Log.d("ReminderService In", "Time");
                                    builder.setAutoCancel(false);

                                    //=====
                                    String query1 = "select * from [RequestType] where [id]='" + rs.getString("requesttypeid").toString() + "'";
                                    PreparedStatement ps1 = con.prepareStatement(query1);
                                    ResultSet rs1 = ps1.executeQuery();
                                    rs1.next();
                                    //====
                                    notificationbody=notificationbody+ rs.getString("name").toString()+ " for "+rs1.getString("requestType").toString()+"\n" ;
                                    builder.setTicker("New House Request");
                                    builder.setSmallIcon(R.drawable.logos);
                                    builder.setContentIntent(pendingIntent);
                                    builder.setContentTitle(getResources().getString(R.string.notify_new_request_title));
                                    builder.setContentText(notificationbody);


                                    //builder.setSubText("This is subtext...");
                                    // builder.build();

                                    myNotication = builder.setStyle(new Notification.BigTextStyle().bigText(notificationbody)).build();
                                    myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                                    // myNotication.defaults |= Notification.DEFAULT_SOUND;
                                    // myNotication.defaults |= Notification.FLAG_AUTO_CANCEL;

                                    mgr.notify(id, myNotication);




                                }
                            }


                        }


                        //  }
                    } catch (Exception ex) {
                        Log.d("ReminderService In", "SensorService exp"+ex.getMessage());
                    }



                }

            }


        };
    }
    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }
public  void ShowPassCodeNotification(){

}
}
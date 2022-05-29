package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.activity.PostDetailActivity;

public class MyService extends Service {
    private static final int ID = 1;
    private static final String CHANNELID ="1";
    private static final String CHANNELNAME = "channel1";

    public MyService() {

    }

    public void createNotice(){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // manager.cancel(1);
        //安卓8.0以上弹出通知需要添加渠道NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建渠道
            /**
             * importance:用于表示渠道的重要程度。这可以控制发布到此频道的中断通知的方式。
             * 有以下6种重要性，是NotificationManager的静态常量，依次递增:
             * IMPORTANCE_UNSPECIFIED（值为-1）意味着用户没有表达重要性的价值。此值用于保留偏好设置，不应与实际通知关联。
             * IMPORTANCE_NONE（值为0）不重要的通知：不会在阴影中显示。
             * IMPORTANCE_MIN（值为1）最低通知重要性：只显示在阴影下，低于折叠。这不应该与Service.startForeground一起使用，因为前台服务应该是用户关心的事情，所以它没有语义意义来将其通知标记为最低重要性。如果您从Android版本O开始执行此操作，系统将显示有关您的应用在后台运行的更高优先级通知。
             * IMPORTANCE_LOW（值为2）低通知重要性：无处不在，但不侵入视觉。
             * IMPORTANCE_DEFAULT （值为3）：默认通知重要性：随处显示，产生噪音，但不会在视觉上侵入。
             * IMPORTANCE_HIGH（值为4）更高的通知重要性：随处显示，造成噪音和窥视。可以使用全屏的Intent。
             */
            NotificationChannel channel = new NotificationChannel(CHANNELID, CHANNELNAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);//开启渠道
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, CHANNELID);
            builder.setContentTitle("Title")//通知标题
                    .setContentText("ContentText")//通知内容
                    .setWhen(System.currentTimeMillis())//通知显示时间
                    .setSmallIcon(R.drawable.ic_baseline_add_24)
                    .setAutoCancel(true)//点击通知取消
                    //.setSound()
                    //第一个参数为手机静止时间，第二个参数为手机震动时间，周而复始
                    .setVibrate(new long[]{0, 1000, 1000, 1000})//手机震动
                    //第一个参数为LED等颜色，第二个参数为亮的时长，第三个参数为灭的时长
//                    .setLights(Color.BLUE,1000,1000)
                    /**表示通知的重要程度
                     * RIORITY_DEFAULT
                     * RIORITY_MIN
                     * RIORITY_LOW
                     * RIORITY_HIGE
                     * RIORITY_MAX
                     **/
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            manager.notify(1, builder.build());
        } else {
            Notification notification = new NotificationCompat.Builder(MyService.this)
                    .setContentTitle("Title")
                    .setContentText("ContentText")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_baseline_add_24)
                    .build();
            manager.notify(1, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    //TODO
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotice();
        return super.onStartCommand(intent, flags, startId);
    }
}
package it.mirea.practice_four;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import it.mirea.practice_four.R;

public class MainActivity extends AppCompatActivity {

    private Button buttonNotification;
    private NotificationManager nm;
    private static final int REQUEST_CODE_PERMISSION_READ_CONTACTS = 1;
    public static final String C_ID = "default_channel_id"; // Айди канала
    private final int N_ID = 1; // Уникальный идентификатор сообщения



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNotification = findViewById(R.id.buttonNotifications); //  привязываем кнопку к переменной, чтобы удобнее использовать в методах
        nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            readContacts();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_PERMISSION_READ_CONTACTS); // активити нынешнее, далее создаём стринговый массив, чтобы считать данные с манифеста, потом возвращаем
        }
    }

    private void readContacts() {
    }

    public void createNotification(View view) {
        buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Увидеть текст
                showNotification(); // Создаём уведомление и отправляем
                Toast.makeText(getApplicationContext(), "Уведомление отправилось", Toast.LENGTH_SHORT).show(); // Выводим сообщение пользователю, что уведомление создалось и отправилось
            }
        }); // Слушаем событие с кнопки "Уведомление" если нажали, то оно создаётся

    }

    public void showNotification() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), C_ID)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_launcher_background) // логотип уведомления
                        .setTicker("New Notification \uD83E\uDD7A")
                        .setContentTitle("ИКБО-27-20") // title уведомления
                        .setContentText("Дмитрий Дмитриевич Комаров") // текст уведомления
                        .setWhen(System.currentTimeMillis()) // время уведомления
                        .setAutoCancel(true); // автоматическое закрытие

        Notification notification = notificationBuilder.build(); // Сборка проектов
        createChannelIfNeeded(nm);
        nm.notify(N_ID, notification);
    }

    public static void createChannelIfNeeded(NotificationManager manager) { // мин версия 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(C_ID, C_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    } // создаём канал для того, чтобы пользователь смог настроить уведомления


    public void viewCamera(View view) {
        Intent intent = new Intent(this, CameraActivity.class); // подключаем к  работе класс Camera
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // отправляем intent с действием (фото) и ждём ответа
        startActivity(intent); // мы ничего не возвращаем, поэтому используем startActivity, иначе использовали бы startActivityForResult()
        startActivity(intent1); // мы ничего не возвращаем, поэтому используем startActivity, иначе использовали бы startActivityForResult()
    }
}

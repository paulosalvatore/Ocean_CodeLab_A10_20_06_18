package br.com.paulosalvatore.ocean_codelab_a10_20_06_18;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by paulo on 20/06/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

	private static final String TAG = "FMService";
	private NotificationManager notificationManager;
	private final int NOTIFY_ID = 1000;
	private long[] vibracao = new long[]{300, 400, 500, 400, 300};

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		RemoteMessage.Notification notification = remoteMessage.getNotification();

		Log.d(TAG, "FCM Message ID: " + remoteMessage.getMessageId());
		Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
		Log.d(TAG, "FCM Notification Message: " + notification);

		if (notification != null) {
			String titulo = notification.getTitle();
			String corpo = notification.getBody();
			Map<String, String> dados = remoteMessage.getData();

			Log.d(TAG, "FCM Notification Title: " + titulo);
			Log.d(TAG, "FCM Notification Body: " + corpo);
			Log.d(TAG, "FCM Notification Data: " + dados);

			criarNotificacao(notification, dados);
		}
	}

	private void criarNotificacao(RemoteMessage.Notification notification, Map<String, String> dados) {
		Intent intent;
		PendingIntent pendingIntent;
		NotificationCompat.Builder builder;

		if (notificationManager == null) {
			notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		}

		String canalId = "CodeLabPushImagens 1";

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			String nome = "CodeLab Push Channel";
			String descricao = "CodeLab Push Channel Description";

			NotificationChannel channel = notificationManager.getNotificationChannel(canalId);

			if (channel == null) {
				int importancia = NotificationManager.IMPORTANCE_HIGH;
				channel = new NotificationChannel(canalId, nome, importancia);
				channel.setDescription(descricao);
				channel.enableVibration(true);
				channel.enableLights(true);
				channel.setVibrationPattern(vibracao);
				notificationManager.createNotificationChannel(channel);
			}
		}

		builder = new NotificationCompat.Builder(this, canalId);

		intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		builder.setContentTitle(notification.getTitle())
				.setSmallIcon(R.drawable.ic_stat_ic_notification)
				.setContentText(notification.getBody())
				.setDefaults(Notification.DEFAULT_ALL)
				.setAutoCancel(true)
				.setContentIntent(pendingIntent)
				.setTicker(notification.getTitle())
				.setPriority(1)
				.setVibrate(vibracao);

		Notification notification_app = builder.build();
		notificationManager.notify(NOTIFY_ID, notification_app);
	}
}

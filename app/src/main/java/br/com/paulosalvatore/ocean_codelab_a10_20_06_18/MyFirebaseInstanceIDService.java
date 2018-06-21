package br.com.paulosalvatore.ocean_codelab_a10_20_06_18;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by paulo on 20/06/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
	private static final String TAG = "FMService";

	@Override
	public void onTokenRefresh() {
		String token = FirebaseInstanceId.getInstance().getToken();
		Log.d(TAG, "FCM Token: " + token);

		FirebaseMessaging.getInstance().subscribeToTopic("PRINCIPAL");
	}
}

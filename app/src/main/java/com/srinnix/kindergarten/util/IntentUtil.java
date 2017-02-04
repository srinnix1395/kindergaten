package com.srinnix.kindergarten.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by DELL on 2/3/2017.
 */

public class IntentUtil {
	public static void openWebPage(final Activity activity, String url) {
		Uri webPage = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
		if (intent.resolveActivity(activity.getPackageManager()) != null) {
			activity.startActivity(intent);
		}
	}
	
	public static void openPhoneCall(final Activity activity, String tel) {
		try {
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse("tel:" + tel));
			activity.startActivity(callIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void openPhoneSms(final Activity activity, String tel) {
		try {
			Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.setType("vnd.android-dir/mms-sms");
			smsIntent.putExtra("address", tel);
			activity.startActivity(smsIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

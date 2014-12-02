package src.com.rcp.day;

import java.util.Calendar;

import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import rcp.com.other.ActivityMeg;
import rcp.com.src.brithUtil.otherUtil;
import src.com.rcp.wheelview.ScreenInfo;
import src.com.rcp.wheelview.WheelMain;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class more extends Activity {

	/** �������� */
	private LinearLayout more_remind;

	private TextView tv_remindTime;
	/** �ʺ����� */
	private LinearLayout more_account;

	/** ���� */
	private ToggleButton tb_password;

	/*** ֪ͨ����פ */
	private ToggleButton tb_notification;

	private int isEnter;

	/** ������ */
	private LinearLayout update;
	/** �û����� */
	private LinearLayout Fbhome;
	private static final String LOG_TAG = more.class.getName();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_4);
		ActivityMeg.getInstance().addActivity(this);
		viewInit();

	}

	public void viewInit() {
		more_remind = (LinearLayout) this.findViewById(R.id.more_remind);
		more_remind.setOnClickListener(onClickListener);
		tv_remindTime = (TextView) this.findViewById(R.id.more_tixingTime);
		more_account = (LinearLayout) this.findViewById(R.id.more_account);
		more_account.setOnClickListener(onClickListener);
		tb_password = (ToggleButton) this.findViewById(R.id.moreset_uselogcode);
		tb_password.setOnCheckedChangeListener(onCheckedChangeListener);
		tb_password.setOnClickListener(onClickListener);
		tb_notification = (ToggleButton) this
				.findViewById(R.id.moreset_notification);
		tb_notification.setOnCheckedChangeListener(onCheckedChangeListener);
		update = (LinearLayout) this.findViewById(R.id.more_about);
		update.setOnClickListener(listener);
		Fbhome = (LinearLayout) this.findViewById(R.id.more_feedback);
		Fbhome.setOnClickListener(onClickListener);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			System.out.println("--------------------------");
			otherUtil.ExitApp(getApplicationContext());
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			int id = buttonView.getId();
			if (id == R.id.moreset_notification) {
				if (isChecked) {
					setNotification();
				} else {
					cancelNotification();
				}
			} else if (id == R.id.moreset_uselogcode) {
				if ((isEnter == -1) && isChecked) {
					Intent code = new Intent(more.this, CodeNum.class);
					code.putExtra("kaiguan", 0);
					more.this.startActivity(code);
				} else if ((isEnter == 1) && (isChecked == false)) {
					Intent code = new Intent(more.this, CodeNum.class);
					code.putExtra("kaiguan", 1);
					more.this.startActivity(code);
				}
			}
		}
	};

	public void isKaiqiPassword() {
		SharedPreferences sharedPreferences = getSharedPreferences(
				"kaiqipassword", MODE_PRIVATE);

		isEnter = sharedPreferences.getInt("iskaiqi", -1);

		if (isEnter == 1) {
			tb_password.setChecked(true);
		} else {
			tb_password.setChecked(false);
		}

		SharedPreferences sharedPre = getSharedPreferences("dingshitixing",
				MODE_PRIVATE);
		String time = sharedPre.getInt("hours", -1) + "-"
				+ sharedPre.getInt("min", -1);
		if (sharedPre.getInt("hours", -1) != -1) {

			tv_remindTime.setText("��������ʱ��:" + time);
		} else {
			tv_remindTime.setText("����ʱ������");
		}
		Log.e("isEnter====>>>>", time);
	}

	OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.more_remind) {
				setmore_remind();
				Log.e("----------->>>", "more_remind�����");
			} else if (id == R.id.more_feedback) {
				Log.e("9999", "66666");
				UMFeedbackService.enableNewReplyNotification(more.this,
						NotificationType.NotificationBar);
				UMFeedbackService.setGoBackButtonVisible();
				UMFeedbackService.openUmengFeedbackSDK(more.this);
			}

		}

	};

	private View.OnClickListener listener = new View.OnClickListener() {
		public void onClick(View v) {

			// ������������ʱ�Զ�����Ƿ���Ҫ���£� ���������д������Activity ��onCreate()�����
			com.umeng.common.Log.LOG = true;
			UmengUpdateAgent.setUpdateOnlyWifi(false); // Ŀǰ����Ĭ����Wi-Fi��������²Ž����Զ����ѡ�����Ҫ���������绷���½��и����Զ����ѣ�������Ӹ��д���
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(updateListener);

			UmengUpdateAgent.setOnDownloadListener(new UmengDownloadListener() {

				public void OnDownloadEnd(int result) {
					Log.i(LOG_TAG, "download result : " + result);
					Toast.makeText(more.this, "download result : " + result,
							Toast.LENGTH_SHORT).show();
				}

			});

			UmengUpdateAgent.update(more.this);
		}
	};

	UmengUpdateListener updateListener = new UmengUpdateListener() {

		public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
			switch (updateStatus) {
			case 0: // has update
				Log.i("--->", "callback result");
				UmengUpdateAgent.showUpdateDialog(more.this, updateInfo);
				break;
			case 1: // has no update
				Toast.makeText(more.this, "û�и���", Toast.LENGTH_SHORT).show();
				break;
			case 2: // none wifi
				Toast.makeText(more.this, "û��wifi���ӣ� ֻ��wifi�¸���",
						Toast.LENGTH_SHORT).show();
				break;
			case 3: // time out
				Toast.makeText(more.this, "��ʱ", Toast.LENGTH_SHORT).show();
				break;
			case 4: // is updating
				/*
				 * Toast.makeText(mContext, "�������ظ���...", Toast.LENGTH_SHORT)
				 * .show();
				 */
				break;
			}

		}
	};

	// ��ӳ�פ֪ͨ
	private void setNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				getString(R.string.app_name), System.currentTimeMillis());
		Intent intent = new Intent(this, Rcp_birthdayActivity.class);
		notification.flags = Notification.FLAG_ONGOING_EVENT; // ���ó�פ Flag
		PendingIntent contextIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notification.setLatestEventInfo(getApplicationContext(),
				getString(R.string.app_name), "����鿴", contextIntent);
		notificationManager.notify(R.string.app_name, notification);
	}

	// ȡ��֪ͨ
	private void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(R.string.app_name);
	}

	/***
	 * 
	 * �������ѷ�ʽ
	 * 
	 */
	public void setmore_remind() {
		LayoutInflater inflater = LayoutInflater.from(more.this);
		View timepickerview = inflater.inflate(R.layout.selectbirthday, null);
		ScreenInfo screenInfo = new ScreenInfo(more.this);
		final WheelMain wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		wheelMain.showHours(calendar.get(Calendar.HOUR),
				calendar.get(Calendar.MINUTE));
		final Dialog dialog1 = new AlertDialog.Builder(this).setView(
				timepickerview).show();
		Window window = dialog1.getWindow();
		window.setGravity(Gravity.BOTTOM); // �˴���������dialog��ʾ��λ��
		window.setWindowAnimations(R.style.mystyle); // ��Ӷ���

		Button btn = (Button) timepickerview
				.findViewById(R.id.btn_datetime_sure);
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String info = wheelMain.getHours() + "-" + wheelMain.getMin();
				tv_remindTime.setText("��������ʱ��:" + info);

				SharedPreferences sharedPreferences = getSharedPreferences(
						"dingshitixing", MODE_PRIVATE);
				// ��ñ༭Ȩ��
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putInt("hours", wheelMain.getHours());
				editor.putInt("min", wheelMain.getMin());
				editor.commit();
				dialog1.dismiss();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isKaiqiPassword();
	}

}

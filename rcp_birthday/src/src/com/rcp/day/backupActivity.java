package src.com.rcp.day;

import rcp.com.backup.BaiduPCSAction;
import rcp.com.backup.PCSDemoInfo;
import rcp.com.other.ActivityMeg;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

/***
 * 
 * ��������
 * 
 * @author toshiba
 * 
 */
public class backupActivity extends Activity {
	
	private Button back;

	private Button backup[] = new Button[3];

	private int backupID[] = { R.id.backup, R.id.recover, R.id.viewcloud };

	BaiduPCSAction loginNote = new BaiduPCSAction();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.backup);
		ActivityMeg.getInstance().addActivity(this);
		viewinit();
	}

	public void viewinit() {
		
		back=(Button)this.findViewById(R.id.backup_back);
		back.setOnClickListener(onClickListener);
		for (int i = 0; i < backup.length; i++) {
			backup[i] = (Button) this.findViewById(backupID[i]);
			backup[i].setOnClickListener(onClickListener);
		}
	}

	OnClickListener onClickListener = new OnClickListener() {
		// 3.257bfa2eb88bb4ea4ffd7dd9f29d790c.2592000.1367411117.1193815910-644507

		public void onClick(View v) {

			int id = v.getId();
			if (id == R.id.backup) {
				// �ϴ����ݿ⵽�ƶ�
				PCSDemoInfo.index = 3;
				loginNote.login(backupActivity.this);
			} else if (id == R.id.recover) {
				// �����ƶ����ݵ�����
				PCSDemoInfo.index = 4;
				loginNote.login(backupActivity.this);
			} else if (id == R.id.viewcloud) {
			} else if (id == R.id.backup_back) {
				Intent intent=new Intent(backupActivity.this, Rcp_birthdayActivity.class);
				backupActivity.this.startActivity(intent);
			}

		}

	};

}

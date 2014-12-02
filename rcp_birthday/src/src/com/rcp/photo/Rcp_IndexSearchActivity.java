package src.com.rcp.photo;

import java.io.InputStream;
import java.util.ArrayList;

import rcp.com.src.brithUtil.PingYinUtil;
import rcp.com.src.imperface.OnIndex;
import src.com.rcp.day.R;
import src.com.rcp.day.rcp_addBrithday;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Rcp_IndexSearchActivity extends Activity {

	private ListView listview;

	/** ��ȡ��Phon���ֶ� **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	/** ��ϵ����ʾ���� **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** �绰���� **/
	private static final int PHONES_NUMBER_INDEX = 1;

	/** ͷ��ID **/
	private static final int PHONES_PHOTO_ID_INDEX = 2;

	/** ��ϵ�˵�ID **/
	private static final int PHONES_CONTACT_ID_INDEX = 3;

	/** ��ϵ������ **/
	private ArrayList<String> mContactsName = new ArrayList<String>();

	/** ��ϵ�˺��� **/
	private ArrayList<String> mContactsNumber = new ArrayList<String>();

	/** ��ϵ��ͷ�� **/
	private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();

	private String title[];
	/** ������ */
	private MyAdapter myAdapter;

	/** ����view���� */
	private Index index;

	/* ������ʾ* */
	private TextView tv;

	private int addactivity_name, addactivity_phone;

	Bitmap kk;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo);
		getPhoneContacts();
		DataInit();
		viewInit();
		bundle();
		kk = BitmapFactory.decodeResource(getResources(),
				R.drawable.default_icon);
	}

	public void bundle() {
		Intent intent = getIntent();
		addactivity_name = intent.getIntExtra("add_index_name", 0);
		addactivity_phone = intent.getIntExtra("add_index_phone", 0);
		// System.out.println("============" + otheractivity);
	}

	/** �õ��ֻ�ͨѶ¼��ϵ����Ϣ **/
	private void getPhoneContacts() {
		ContentResolver resolver = this.getContentResolver();

		// ��ȡ�ֻ���ϵ��
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// �õ��ֻ�����
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// �õ���ϵ������
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				// �õ���ϵ��ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

				// �õ���ϵ��ͷ��ID
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

				// �õ���ϵ��ͷ��Bitamp
				Bitmap contactPhoto = null;

				// photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�
				if (photoid > 0) {
					Uri uri = ContentUris.withAppendedId(
							ContactsContract.Contacts.CONTENT_URI, contactid);
					InputStream input = ContactsContract.Contacts
							.openContactPhotoInputStream(resolver, uri);
					contactPhoto = BitmapFactory.decodeStream(input);
				} else {
					contactPhoto = kk;
				}

				mContactsName.add(contactName);
				mContactsNumber.add(phoneNumber);
				mContactsPhonto.add(contactPhoto);
			}

			phoneCursor.close();
		}
		title = new String[mContactsName.size()];
		Log.e("   mContactsName=", mContactsName.size() + "   ");
	}

	public void DataInit() {
		// ��������
		// Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
		// Arrays.sort(name, cmp);
		for (int i = 0; i < mContactsName.size(); i++) {
			String str = PingYinUtil.converterToFirstSpell(mContactsName.get(i)
					.substring(0, 1));
			title[i] = str;
		}

		for (int i = 0; i < title.length; i++) {
			for (int j = i + 1; j < title.length; j++) {
				if (title[i].charAt(0) > title[j].charAt(0)) {
					String str = title[i];
					title[i] = title[j];
					title[j] = str;
					String str2 = mContactsName.get(i);
					mContactsName.set(i, mContactsName.get(j));
					mContactsName.set(j, str2);
					String str3 = mContactsNumber.get(i);
					mContactsNumber.set(i, mContactsNumber.get(j));
					mContactsNumber.set(j, str3);
					Bitmap temp = mContactsPhonto.get(i);
					mContactsPhonto.set(i, mContactsPhonto.get(j));
					mContactsPhonto.set(j, temp);
				}
			}
		}
	}

	public void viewInit() {
		listview = (ListView) this.findViewById(R.id.listview);
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// // ����ϵͳ��������绰
				// Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri
				// .parse("tel:" + mContactsNumber.get(position)));
				// startActivity(dialIntent);

				if (addactivity_name == 1) {
					Intent intent = new Intent(Rcp_IndexSearchActivity.this,
							rcp_addBrithday.class);
					intent.putExtra("add_name", mContactsName.get(position));
					setResult(rcp_addBrithday.SEND_SEARCH_NAME, intent);
					Rcp_IndexSearchActivity.this.finish();
					System.out.println("----" + mContactsName.get(position));
				}

				if (addactivity_phone == 2) {
					Intent intent = new Intent(Rcp_IndexSearchActivity.this,
							rcp_addBrithday.class);
					intent.putExtra("add_phone", mContactsNumber.get(position));
					setResult(rcp_addBrithday.SEND_SEARCH_PHONE, intent);
					Rcp_IndexSearchActivity.this.finish();
					System.out.println("----" + mContactsNumber.get(position));
				}
			}
		});

		myAdapter = new MyAdapter();
		listview.setAdapter(myAdapter);
		index = (Index) this.findViewById(R.id.index);
		index.setOnIndex(onIndex);
		tv = (TextView) this.findViewById(R.id.tv1);
	}

	OnIndex onIndex = new OnIndex() {

		public void OnIndexSelect(String str) {
			tv.setVisibility(View.VISIBLE);
			tv.setText(str);
			for (int i = 0; i < title.length; i++) {
				if (title[i].equals(str)) {
					listview.setSelection(i);
					break;
				}

			}
		}

		public void OnIndexUp() {
			tv.setVisibility(View.GONE);
		}

	};

	public class MyAdapter extends BaseAdapter {

		public int getCount() {
			// TODO Auto-generated method stub
			return mContactsName.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mContactsName.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewOptimize viewOptimize;

			if (convertView == null) {

				convertView = getLayoutInflater().inflate(R.layout.list_item,
						null);

				viewOptimize = new ViewOptimize();

				viewOptimize.tv_name = (TextView) convertView
						.findViewById(R.id.dianhua_name);

				viewOptimize.tv_phonoNum = (TextView) convertView
						.findViewById(R.id.dianhua_number);

				viewOptimize.img_photo = (ImageView) convertView
						.findViewById(R.id.dianhua_photo);

				viewOptimize.tv_title = (TextView) convertView
						.findViewById(R.id.title);
				convertView.setTag(viewOptimize);
			} else {
				viewOptimize = (ViewOptimize) convertView.getTag();
			}

			String text = mContactsName.get(position);
			viewOptimize.tv_name.setText(text);
			viewOptimize.tv_phonoNum.setText(mContactsNumber.get(position));
//			if (mContactsPhonto.get(position) == null) {
//				Log.e("  ", "  2222222222222222");
//			}
			viewOptimize.img_photo
					.setImageBitmap(mContactsPhonto.get(position));
			String str2 = title[position];
			if (position > 0) {
				String str1 = title[position - 1];
				if (str2.equals(str1)) {
					viewOptimize.tv_title.setVisibility(View.GONE);
				} else {
					viewOptimize.tv_title.setVisibility(View.VISIBLE);
					viewOptimize.tv_title.setText(str2);
				}
			} else {
				viewOptimize.tv_title.setVisibility(View.VISIBLE);
				viewOptimize.tv_title.setText(str2);
			}
			return convertView;
		}

		/**
		 * view ���Ż�
		 * 
		 * @author toshiba
		 * 
		 */
		public class ViewOptimize {

			TextView tv_title;

			TextView tv_name;

			/** �绰���� */
			TextView tv_phonoNum;
			/** ͷ�� */
			ImageView img_photo;

		}

	}
}
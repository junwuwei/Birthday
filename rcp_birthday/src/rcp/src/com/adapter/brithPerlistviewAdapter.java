package rcp.src.com.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rcp.com.src.brithUtil.imageUtil;
import rcp.com.src.brithUtil.otherUtil;
import rcp.com.src.volues.sqlVolue;
import src.com.rcp.Sql.sql_brith;
import src.com.rcp.day.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * �����˵�������
 * 
 * @author toshiba
 * 
 */
public class brithPerlistviewAdapter extends BaseAdapter {

	public List<brithPerlistItem> brithData = new ArrayList<brithPerlistItem>();

	private LayoutInflater layoutInflater;

	private Context context;

	private sql_brith db;

	private boolean isEdit;

	private HashMap<Integer, Bitmap> hashMap;

	float DownX, UpX;

	public brithPerlistviewAdapter(Context context,
			HashMap<Integer, Bitmap> hashMap) {
		this.context = context;

		layoutInflater = LayoutInflater.from(context);
		db = new sql_brith(context);
		this.hashMap = hashMap;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;

	}

	public void dataInit() {
		brithData.clear();
		brithData = readDataToSql();
		for (int i = 0; i < brithData.size(); i++) {

			Log.e("---->>", brithData.get(i).getBrithday());
		}
	}

	public int getItemID(int index) {
		return brithData.get(index).ItemID;
	}

	public int getCount() {
		// TODO Auto-generated method stub

		return brithData.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return brithData.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		Item item;

		brithPerlistItem data = brithData.get(position);
		if (convertView == null) {

			item = new Item();

			convertView = layoutInflater.inflate(R.layout.mainbirthday_item,
					null);
			item.delete = (ImageButton) convertView
					.findViewById(R.id.btn_delete);

			item.photo = (ImageView) convertView
					.findViewById(R.id.mainbirthday_avatar);

			item.name = (TextView) convertView
					.findViewById(R.id.mainbirthday_name);

			item.brithday = (TextView) convertView
					.findViewById(R.id.mainbirthday_date);

			item.brith1 = (TextView) convertView
					.findViewById(R.id.mainbirthday_countdown_year);

			item.brith3 = (TextView) convertView
					.findViewById(R.id.mainbirthday_day);
			item.brith2 = (TextView) convertView
					.findViewById(R.id.mainbirthday_countdown_day);
			item.cake = (ImageView) convertView.findViewById(R.id.cake);

			convertView.setTag(item);
		} else {
			item = (Item) convertView.getTag();
		}

		if (!data.getPhoto().equals("null")) {

			System.out.println("------------->>>>" + data.getPhoto() + "");
			Bitmap bitmap = new imageUtil().getBitmapTodifferencePath(data.getPhoto()
					+ "", context);
			item.photo.setImageBitmap(bitmap);

			// if (bitmap.isRecycled()) {
			// bitmap.recycle();
			// }
			BitmpCache(position, bitmap);
		} else {
			if (data.getSex().equals("��")) {
				item.photo.setImageBitmap(brithPerlistviewAdapter.this
						.readBitMap(context, R.drawable.defaultboy));

			} else {
				item.photo.setImageBitmap(brithPerlistviewAdapter.this
						.readBitMap(context, R.drawable.defaultgirl));
			}
			// item.photo
			// .setBackgroundResource( ? R.drawable.defaultboy
			// : R.drawable.defaultgirl);
		}
		Log.e("iiiiiiii", data.getPhoto());

		item.name.setText(data.getName());
		item.brithday.setText(data.brithday);
		item.brith1.setText(data.brithdayInfo);
		if (data.brithdayInfo_day.equals("0")) {
			item.brith1.setText("������" + data.getName() + "������");
			item.brith2.setVisibility(View.GONE);
			item.cake.setVisibility(View.VISIBLE);
			item.brith3.setVisibility(View.GONE);
		} else {
			item.brith2.setText(data.brithdayInfo_day);
		}
//		item.delete.setAnimation(AnimationUtils.loadAnimation(context,
//				R.anim.test3));
		if (!isEdit) {
			// item.compile.setBackgroundResource(R.drawable.icon15);
			// item.compile.setVisibility(View.GONE);
			item.delete.setVisibility(View.GONE);
		} else {
			item.delete.setVisibility(View.VISIBLE);
		}

		item.delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				db.delete(getItemID(position));
				brithData.remove(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	class Item {

		ImageButton delete;

		ImageView photo;

		ImageView cake;
		TextView name;

		TextView brithday;

		TextView brith1;

		TextView brith2;

		TextView brith3;
	}

	public Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);

	}

	private void BitmpCache(int position, Bitmap bitmap) {

		if (!hashMap.containsKey(position)) {

			hashMap.put(position, bitmap);

		}

	}

	/***
	 * 
	 * �����ݿ��ȡ������ϵ�˵�����
	 * 
	 * @return
	 */
	public List<brithPerlistItem> readDataToSql() {

		List<brithPerlistItem> data = new ArrayList<brithPerlistItem>();

		Cursor cursor = null;

		try {

			cursor = db.db.query(sqlVolue.TABLE_brith_name, null, null, null,
					null, null, null);
			if (cursor != null) {
				// �α��±����
				cursor.moveToPosition(0);
				// ȡ�α��е�ֵ
				while (true) {
					// �ж��Ƿ������
					if (cursor.isAfterLast()) {
						break;
					}
					brithPerlistItem list = new brithPerlistItem();
					list.setItemID(cursor.getInt(0));
					String name = cursor.getString(1);
					list.setName(name);
					String photo = cursor.getString(4);
					list.setPhoto(photo);
					String mouth = cursor.getString(6);
					String date = cursor.getString(7);
					String brith = mouth + "��" + date + "��";
					list.setBrithday(brith);
					String sex = cursor.getString(3);
					list.setSex(sex);
					System.out.println("------>>>>" + sex);
					String sex1 = sex.equals("��") ? "������ũ������" : "������ũ������";
					list.setBrithdayInfo(sex1);
					String info = otherUtil.getForMyBrithday(
							otherUtil.getCurYear() + "", mouth, date)
							+ "";
					Log.e("===========", info+"kkk");
					list.setBrithdayInfo_day(info);
					list.setBrithdayInfo_day1(info);
					data.add(list);
					cursor.moveToNext();
				}
			}
			// sqlDatebase.closeSQl();
		} finally {
			if (cursor != null) {

				try {

					cursor.close();

				} catch (Exception e) {

					// ignore this

				}
			}
		}
		return data;
	}

}

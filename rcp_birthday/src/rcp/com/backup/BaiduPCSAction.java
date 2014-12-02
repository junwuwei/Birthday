package rcp.com.backup;

import java.util.ArrayList;
import java.util.List;

import rcp.com.src.volues.activityVolues;
import src.com.rcp.day.R;
import src.com.rcp.day.rcp_addBrithday;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.baidu.oauth2.BaiduOAuth;
import com.baidu.oauth2.BaiduOAuthViaDialog;
import com.baidu.pcs.BaiduPCSAPI;
import com.baidu.pcs.BaiduPCSStatusListener;
import com.baidu.pcs.PCSActionInfo;

public class BaiduPCSAction {
	private Handler uiThreadHandler;
	// 04-08 16:08:33.630: I/System.out(18943): Invalid parameter

	public BaiduOAuth mbOauth = null;

	// Get access_token
	public void login(final Context context) {

		 uiThreadHandler = new Handler();
		if (null != PCSDemoInfo.access_token) {
			// download(context);
			indexDoing(context);

		} else {
			mbOauth = new BaiduOAuthViaDialog(PCSDemoInfo.app_key);
			try {
				// Start OAUTH dialog
				mbOauth.startDialogAuth(context, new String[] { "basic",
						"netdisk" }, new BaiduOAuthViaDialog.DialogListener() {

					// Login successful
					public void onComplete(Bundle values) {
						// Get access_token
						PCSDemoInfo.access_token = values
								.getString("access_token");
						Log.e("-------------->>>>", PCSDemoInfo.index + "");
						indexDoing(context);
					}

					// TODO: the error code need be redefined
					@SuppressWarnings("unused")
					public void onError(int error) {
						Toast.makeText(context, R.string.fail,
								Toast.LENGTH_SHORT).show();
					}

					public void onCancel() {
						Toast.makeText(context, R.string.back,
								Toast.LENGTH_SHORT).show();
					}

					public void onException(String arg0) {

						Toast.makeText(context, "��������", Toast.LENGTH_SHORT)
								.show();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void indexDoing(final Context context) {
		switch (PCSDemoInfo.index) {

		case 1:
			delete(context, "name.txt");
			// context.deleteFile("name.txt");
			Intent intent = new Intent(context, rcp_addBrithday.class);
			intent.putExtra("center", 100);
			context.startActivity(intent);
			break;
		case 3:
			delete(context, "rcpBrithday.db");
			break;

		case 4:
			download(context, "rcpBrithday.db",
					context.getDatabasePath("rcpBrithday.db").toString());
			break;
		}

	}

	// Upload files to cloud
	public void upload(final Context context, final String sourcrFile,
			String name) {

		final String path = PCSDemoInfo.mbRootpath + name;

		if (null != PCSDemoInfo.access_token) {

			Thread workThread = new Thread(new Runnable() {

				public void run() {

					BaiduPCSAPI api = new BaiduPCSAPI();

					// Set access_token for pcs api
					api.setAccessToken(PCSDemoInfo.access_token);
					System.out.println("=================111");
					// Use pcs uploadFile API to uplaod files
					final PCSActionInfo.PCSFileInfoResponse uploadResponse = api
							.uploadFile(sourcrFile, path,
									new BaiduPCSStatusListener() {

										@Override
										public void onProgress(long bytes,
												long total) {
											System.out.println(bytes);
											// TODO Auto-generated method stub
										}
									});
					System.out.println("=================222");

					// The interface of the thread UI
				uiThreadHandler.post(new Runnable() {

						public void run() {

							if (uploadResponse.error_code == 0) {
								// �ϴ��ɹ���,����Ҫ���ݵ�������0
								activityVolues.backupCount = 0;
								Toast.makeText(context, "�ϴ��ɹ�",
										Toast.LENGTH_SHORT).show();

								// Delete temp file
								// File file = new File(PCSDemoInfo.sourceFile);
								// file.delete();

								// Back to the content activity
								// back(context);
								switch (PCSDemoInfo.index) {
								case 1:
									download(context, "name.txt",
											context.getFilesDir() + "/name.txt");

									break;
								}

							} else {
								// 31064
								Toast.makeText(context,
										"������룺" + uploadResponse.error_code,
										Toast.LENGTH_SHORT).show();
								switch (PCSDemoInfo.index) {
								case 1:
									download(context, "name.txt",
											context.getFilesDir() + "/name.txt");

									break;
								}
							}

						}
					});

				}
			});

			workThread.start();
		}
	}

	// // This function to display the list of contents
	// public void list(final Context context) {
	//
	// if (null != PCSDemoInfo.access_token) {
	//
	// Thread workThread = new Thread(new Runnable() {
	//
	// public void run() {
	//
	// BaiduPCSAPI api = new BaiduPCSAPI();
	// api.setAccessToken(PCSDemoInfo.access_token);
	//
	// // The path to file storage on the cloud
	// String path = PCSDemoInfo.mbRootpath;
	//
	// // Use list api
	// final PCSActionInfo.PCSListInfoResponse listResponse = api
	// .list(path, "name", "asc");
	//
	// PCSDemoInfo.uiThreadHandler.post(new Runnable() {
	//
	// public void run() {
	//
	// ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,
	// String>>();
	//
	// if (!listResponse.list.isEmpty()) {
	//
	// for (Iterator<PCSFileInfoResponse> i = listResponse.list
	// .iterator(); i.hasNext();) {
	//
	// HashMap<String, String> map = new HashMap<String, String>();
	//
	// PCSFileInfoResponse info = i.next();
	//
	// // Get the file name
	// String path = info.path;
	// String fileName = path.substring(
	// PCSDemoInfo.mbRootpath.length(),
	// path.lastIndexOf("."));
	//
	// // Get the last modified time
	// Date date = new Date(info.mTime * 1000);
	//
	// // Modify the format of the time
	// SimpleDateFormat formatter = new SimpleDateFormat(
	// "MM-dd HH:mm");
	// String dateString = formatter.format(date);
	//
	// map.put("file_name", fileName);
	// map.put("time", dateString);
	//
	// // Add item to content list
	// list.add(map);
	// PCSDemoInfo.fileNameList.add(fileName);
	// }
	// } else {
	//
	// // Clear content list
	// list.clear();
	// Toast.makeText(context, "�����ļ���Ϊ�գ�",
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// SimpleAdapter listAdapter = new SimpleAdapter(
	// context, list, R.layout.content,
	// new String[] { "file_name", "time" },
	// new int[] { R.id.file_name, R.id.time });
	//
	// // Set listview to display content
	// ((ListActivity) context)
	// .setListAdapter(listAdapter);
	//
	// Toast.makeText(context, R.string.refresh,
	// Toast.LENGTH_SHORT).show();
	//
	// }
	// });
	//
	// }
	// });
	//
	// workThread.start();
	//
	// }
	// }

	public void download(final Context context, final String name,
			final String sourceFile) {

		if (null != PCSDemoInfo.access_token) {

			Thread workThread = new Thread(new Runnable() {

				public void run() {

					BaiduPCSAPI api = new BaiduPCSAPI();
					api.setAccessToken(PCSDemoInfo.access_token);

					// Get the download file storage path on cloud
					// PCSDemoInfo.sourceFile = PCSDemoInfo.mbRootpath
					// + "/rcpBrithday.db";

					String sorce = PCSDemoInfo.mbRootpath + name;

					// Set the download file storage path
					// final String path1 = context.getFilesDir()
					// + "/rcpBrithday.db";
					// final String path1 = context.getDatabasePath(
					// "rcpBrithday.db").toString();

					// Call PCS downloadFile API

					System.out.println("000000000000000000000000000");

					final PCSActionInfo.PCSSimplefiedResponse downloadResponse = api
							.downloadFile(sorce, sourceFile,
									new BaiduPCSStatusListener() {

										@Override
										public void onProgress(long bytes,
												long total) {
											// TODO Auto-generated method stub

										}
									});
					System.out.println("2222222222222222222222222222");

					uiThreadHandler.post(new Runnable() {
						public void run() {

							if (downloadResponse.error_code == 0) {
								// try {
								// // The local store download files
								// File file = new File(path1);
								// FileInputStream inStream = new
								// FileInputStream(
								// file);
								//
								// int length = inStream.available();
								//
								// byte[] buffer = new byte[length];
								//
								// inStream.read(buffer);
								//
								// PCSDemoInfo.fileContent = EncodingUtils
								// .getString(buffer, "UTF-8");
								// inStream.close();
								//
								// } catch (Exception e) {
								// // TODO: handle exception
								//
								// Toast.makeText(context, "��ȡ�ļ�ʧ�ܣ�",
								// Toast.LENGTH_SHORT).show();
								// }
								// �ϴ��ɹ���,����Ҫ���ݵ�������0
								activityVolues.backupCount = 0;
								Toast.makeText(context, "���سɹ�",
										Toast.LENGTH_SHORT).show();

							} else {

								System.out.println(downloadResponse.error_code
										+ "---" + downloadResponse.message);
								Toast.makeText(context, "����ʧ�ܣ��ļ�δ�ϴ�",
										Toast.LENGTH_SHORT).show();

							}
						}
					});
				}
			});

			workThread.start();
		}
	}

	// // Delete the file on the cloud
	public void delete(final Context context, final String name) {
		if (null != PCSDemoInfo.access_token) {

			Thread workThread = new Thread(new Runnable() {
				public void run() {

					BaiduPCSAPI api = new BaiduPCSAPI();
					// Set access_token
					api.setAccessToken(PCSDemoInfo.access_token);

					List<String> list = new ArrayList<String>();

					String path = PCSDemoInfo.mbRootpath + name;

					list.add(path);
					Log.e("=====>>>", path);
					// Call delete api

					final PCSActionInfo.PCSSimplefiedResponse deleteResponse = api
							.deleteFiles(list);

					uiThreadHandler.post(new Runnable() {
						public void run() {
							if (0 == deleteResponse.error_code) {
								Toast.makeText(context, "ɾ���ɹ���",
										Toast.LENGTH_SHORT).show();
								switch (PCSDemoInfo.index) {
								case 1:
									upload(context, context.getFilesDir()
											+ "/name.txt", "name.txt");
									break;
								case 3:
									upload(context,
											context.getDatabasePath(
													"rcpBrithday.db")
													.toString(),
											"rcpBrithday.db");
									break;
								}

							} else {
								Toast.makeText(context,
										"ɾ��ʧ�ܣ�" + deleteResponse.error_code,
										Toast.LENGTH_SHORT).show();
								System.out.println(deleteResponse.message);
								switch (PCSDemoInfo.index) {
								case 1:
									upload(context, context.getFilesDir()
											+ "/name.txt", "name.txt");
									break;
								case 3:
									upload(context,
											context.getDatabasePath(
													"rcpBrithday.db")
													.toString(),
											"rcpBrithday.db");
									break;
								}
							}
						}
					});
				}
			});

			workThread.start();
		}
	}

	//
	public void save(Context context) {
		//
		// try {
		// PCSDemoInfo.sourceFile = context.getFilesDir() + "/"
		// +"kkkk.db";
		//
		// String saveFile = "kkkk.db";
		//
		// FileOutputStream outputStream = context.openFileOutput(saveFile,
		// Context.MODE_PRIVATE);
		//
		// if (!PCSDemoInfo.fileContent.equals("")) {
		// // save file
		// outputStream.write(PCSDemoInfo.fileContent.getBytes());
		//
		// } else {
		//
		// byte bytes = 0;
		// outputStream.write(bytes);
		// }
		//
		// outputStream.close();

		if (PCSDemoInfo.statu == 0) {
			// Upload the file to cloud
			// upload(context);

		} else {
			// If it is edited file save, the first remove the clouds
			// existing file, and then upload
			if (PCSDemoInfo.statu == 1) {

				// delete(context);
			}
		}

		// } catch (Exception e) {
		// Toast.makeText(context, "����", Toast.LENGTH_SHORT).show();
		// }
	}
	//
	// // Back to the content activity
	// public void back(Context context) {
	// Intent content = new Intent();
	// content.setClass(context, ContentActivity.class);
	// context.startActivity(content);
	// }
	//
	// // Finish the program
	// public void exit(final Context context) {
	//
	// AlertDialog.Builder exitAlert = new AlertDialog.Builder(context);
	// exitAlert.setIcon(R.drawable.alert_dark).setTitle("��ʾ...")
	// .setMessage("��ȷ��Ҫ�뿪�ͻ�����");
	// exitAlert.setPositiveButton("ȷ��",
	// new DialogInterface.OnClickListener() {
	//
	// public void onClick(DialogInterface dialog, int which) {
	// PCSDemoInfo.flag = 1;
	// Intent intent = new Intent();
	// intent.setClass(context, PCSDemoNoteActivity.class);// ��ת��login���棬���ݲ����˳�
	// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //
	// ע�Ȿ�е�FLAG����,clear����Activity��¼
	// context.startActivity(intent);// ע�Ⱑ������ת��ҳ���н��м����˳�
	//
	// }
	// });
	//
	// exitAlert.setNegativeButton("ȡ��",
	// new DialogInterface.OnClickListener() {
	//
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.cancel();
	// }
	// }).create();
	//
	// exitAlert.show();
	// }

}

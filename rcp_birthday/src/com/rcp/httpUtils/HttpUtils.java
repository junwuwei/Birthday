package com.rcp.httpUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;

public class HttpUtils {

	// ����������˵�url
	private static String PATH = "http://192.168.1.105:8080/http//servlet/LoginAction";
	private static URL url;
	
	  private String srcPath = "/sdcard/jsonUtil.txt";
	  private String actionUrl = "http://192.168.1.101:8080/upload_file_service/upload.jsp";

	public HttpUtils() {
		// TODO Auto-generated constructor stub

	}

	static {
		try {
			url = new URL(PATH);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param params
	 *            ��д��url�Ĳ���
	 * @param encode
	 *            �ֽڱ���
	 * @return
	 */
	public static String sendPostMessage(Map<String, String> params,
			String encode) {
		// ��ΪStringBuffer��ʼ�����ַ���
		StringBuffer buffer = new StringBuffer();
		try {
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					// ���ת�����
					buffer.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), encode))
							.append("&");
				}
				buffer.deleteCharAt(buffer.length() - 1);
			}
			// System.out.println(buffer.toString());
			// ɾ��������һ��&

			System.out.println("-->>" + buffer.toString());
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setConnectTimeout(3000);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);// ��ʾ�ӷ�������ȡ����
			urlConnection.setDoOutput(true);// ��ʾ�������д����
			// ����ϴ���Ϣ���ֽڴ�С�Լ�����
			byte[] mydata = buffer.toString().getBytes();
			// ��ʾ������������������ı�����
			urlConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Content-Length",
					String.valueOf(mydata.length));

			// ��������,��������������
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();
			// ��÷�������Ӧ�Ľ����״̬��
			int responseCode = urlConnection.getResponseCode();
			if (responseCode == 200) {
				return changeInputStream(urlConnection.getInputStream(), encode);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * ��һ��������ת����ָ��������ַ���
	 * 
	 * @param inputStream
	 * @param encode
	 * @return
	 */
	private static String changeInputStream(InputStream inputStream,
			String encode) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = "";
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					outputStream.write(data, 0, len);
				}
				result = new String(outputStream.toByteArray(), encode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("username", "admin");
//		params.put("password", "123");
//		String result = HttpUtils.sendPostMessage(params, "utf-8");
//		System.out.println("--result->>" + result);
//	}
	 /* �ϴ��ļ���Server�ķ��� */
	  private void uploadFile()
	  {

	    String uploadUrl = "http://192.168.1.101:8080/upload_file_service/UploadServlet";
	    String end = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "******";
	    try
	    {
	      URL url = new URL(uploadUrl);
	      HttpURLConnection httpURLConnection = (HttpURLConnection) url
	          .openConnection();
	      httpURLConnection.setDoInput(true);
	      httpURLConnection.setDoOutput(true);
	      httpURLConnection.setUseCaches(false);
	      httpURLConnection.setRequestMethod("POST");
	      httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
	      httpURLConnection.setRequestProperty("Charset", "UTF-8");
	      httpURLConnection.setRequestProperty("Content-Type",
	          "multipart/form-data;boundary=" + boundary);

	      DataOutputStream dos = new DataOutputStream(
	          httpURLConnection.getOutputStream());
	      dos.writeBytes(twoHyphens + boundary + end);
	      dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
	          + srcPath.substring(srcPath.lastIndexOf("/") + 1)
	          + "\""
	          + end);
	      dos.writeBytes(end);

	      FileInputStream fis = new FileInputStream(srcPath);
	      byte[] buffer = new byte[8192]; // 8k
	      int count = 0;
	      while ((count = fis.read(buffer)) != -1)
	      {
	        dos.write(buffer, 0, count);

	      }
	      fis.close();

	      dos.writeBytes(end);
	      dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
	      dos.flush();

	      InputStream is = httpURLConnection.getInputStream();
	      InputStreamReader isr = new InputStreamReader(is, "utf-8");
	      BufferedReader br = new BufferedReader(isr);
	      String result = br.readLine();

//	      Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	      dos.close();
	      is.close();

	    } catch (Exception e)
	    {
	      e.printStackTrace();
//	      setTitle(e.getMessage());
	    }

	  }


}

package CST8284_Assignment2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.javafx.collections.SetListenerHelper;

public class Address {
	private String streetNumber;
	private String streetName;
	private String streetType;
	private String streetOrientation;
	private String cityName;
	private String province;
	private String postCode;
	private Object lat;
	private Object lng;

	public Object getLat() {
		return lat;
	}

	public void setLat(Object lat) {
		this.lat = lat;
	}

	public Object getLng() {
		return lng;
	}

	public void setLng(Object lng) {
		this.lng = lng;
	}

/**
 * Retrieve location info from Google API by using current address
 * @throws IOException
 */
	public  void convertLocationFromAddress() throws IOException {
		final String KEY = "AIzaSyCKykF0kDqQlLUxeASXTK8Mz8o6RDn_buw";

		HttpURLConnection httpConn = null;
		URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="
				+ URLEncoder.encode(this.toString(), "UTF8") + "&key=" + KEY);
		httpConn = (HttpsURLConnection) url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));

		String line;
		String result = "";
		while ((line = in.readLine()) != null) {
			result += line;
		}
		in.close();
		httpConn.disconnect();
		JSONObject obj=new JSONObject(result);
		JSONArray results = obj.getJSONArray("results");
		JSONObject geometryObj = (JSONObject)results.get(0);
		JSONObject geometry = geometryObj.getJSONObject("geometry");
		JSONObject location = geometry.getJSONObject("location");
		setLat(location.get("lat"));
		setLng(location.get("lng"));
	}

	

	public void setStreetNumber(String streetNumber) {
		if (streetNumber.contains("-"))
			this.streetNumber = streetNumber.split("-")[1];
		else
			this.streetNumber = streetNumber;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetType(String streetType) {
		this.streetType = streetType;
	}

	public String getStreetType() {
		return streetType;
	}

	public void setStreetOrientation(String streetOrientation) {
		if (streetOrientation != null && streetOrientation.length() != 0) {
			this.streetOrientation = streetOrientation.substring(0, 1);
		} else {
			this.streetOrientation = "";
		}
	}

	public String getStreetOrientation() {
		return streetOrientation;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setProvince(String province) {
		this.province = province;
		//address is ready for lat and lng
		try {
			convertLocationFromAddress();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public String getProvince() {
		return province;
	}

	public void setPostalCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPostalCode() {
		return postCode;
	}

	@Override
	public String toString() {
		return streetNumber + " " + streetName + " " + streetType + " "
				+ (streetOrientation == ("") ? streetOrientation + " " : "") + cityName + " " + province;
	}
}
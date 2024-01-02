package com.fps.pb02.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fps.pb02.Object.DmNSD;
import com.fps.pb02.Repositories.DmNSDRepository;

import java.util.List;

/**
 *
 */
@Component
public class ApiJob {

	@Value("${api.url}")
	private String apiUrl;

	@Value("${api.username}")
	private String username;

	@Value("${api.password}")
	private String password;

	@Value("${api.timeJob}")
	private String timeJob;
	RestTemplate restTemplate = new RestTemplate();
	private final Random random = new Random();
	private String originalString = "\"dataPrivate\": \"IaWLzVuyU7MXrs50nHVBoDprGiQ/iGiOa+w06jLnKuvKpM3Qjj338Qh9fP8n3dLx\\r\\nE/E+bchvoIyoOiZcWMXKy1iM8CHlOaHFJawxAxXpgpRi7lYrjpV4HtrJuZzC8nOW\\r\\n5nnerFvZWJLyDX2byZGJ6xKtVj/FCDJTxyH4EftYi1M=\", ";
	@Autowired
	private DmNSDRepository dmNSDRepository;

	@Scheduled(cron = "0 0 8 * * MON-FRI") // Chạy mỗi ngày từ thứ 2 đến thứ 6 lúc 8:00
	public void runCheckIn() {
	    List<DmNSD> litDmNSD = dmNSDRepository.findAll();
	    int sorandom = 0;
	    
	    for (DmNSD dmNSD : litDmNSD) {
	        int minRange = 5 + sorandom;
	        int maxRange = 10 + sorandom;
	        int randomMinutesOut = random.nextInt(maxRange - minRange + 1) + minRange;

	        LocalTime randomTimeOut = LocalTime.of(8, randomMinutesOut);
	        
	        while (true) {
	            LocalTime currentTime = LocalTime.now();
	            currentTime = currentTime.truncatedTo(ChronoUnit.MINUTES);

	            if (currentTime.equals(randomTimeOut)) {
	                callCheckInAllApi(getToken(dmNSD.getUsername(), dmNSD.getPassword()), dmNSD.getDeviceId(), dmNSD.getDeviceName());
	                System.out.println("CheckOut .");
	                break; // Exit the loop
	            } else {
	                System.out.println("Chưa tới giờ .");
	            }

	            try {
	                Thread.sleep(10000); // 10 seconds = 10,000 milliseconds
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }

	        sorandom = sorandom + 5;
	    }
	}


	@Scheduled(cron = "0 30 17 * * MON-FRI") // Chạy mỗi ngày từ thứ 2 đến thứ 6 lúc 17:30
	public void runCheckOut() {
		  List<DmNSD> litDmNSD = dmNSDRepository.findAll();
		    int sorandom = 0;
		    
		    for (DmNSD dmNSD : litDmNSD) {
		        int minRange = 40 + sorandom;
		        int maxRange = 50 + sorandom;
		        int randomMinutesOut = random.nextInt(maxRange - minRange + 1) + minRange;

		        LocalTime randomTimeOut = LocalTime.of(17, randomMinutesOut);
		        
		        while (true) {
		            LocalTime currentTime = LocalTime.now();
		            currentTime = currentTime.truncatedTo(ChronoUnit.MINUTES);

		            if (currentTime.equals(randomTimeOut)) {
		                callCheckoutAllApi(getToken(dmNSD.getUsername(), dmNSD.getPassword()), dmNSD.getDeviceId(), dmNSD.getDeviceName());
		                System.out.println("CheckOut .");
		                break; // Exit the loop
		            } else {
		                System.out.println("Chưa tới giờ .");
		            }

		            try {
		                Thread.sleep(10000); // 10 seconds = 10,000 milliseconds
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		        }

		        sorandom = sorandom + 10;
		    }

	}

	public String getToken(String username, String pass) {

		String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + pass + "\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		String loginUrl = apiUrl + "/api/login";
		ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, requestEntity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			String responseBody = response.getBody();
			String token = getTokenFromJson(response.getBody());
			return token;
		} else {
			System.err.println("API call failed with status code: " + response.getStatusCode());
			return "API call failed with status code: " + response.getStatusCode();
		}
	}

	private String getTokenFromJson(String jsonResponse) {
		JSONObject json = new JSONObject(jsonResponse);
		JSONObject data = json.getJSONObject("data");
		String token = data.getString("token");
		return token;
	}

	private void callCheckoutAllApi(String token , String device_id , String deviceName) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(getCheckoutAllRequestBody(device_id, deviceName ), headers);
		String checkoutAllUrl = apiUrl + "/api/checkout_all";
		ResponseEntity<String> checkoutAllResponse = restTemplate.postForEntity(checkoutAllUrl, requestEntity,
				String.class);
		if (checkoutAllResponse.getStatusCode().is2xxSuccessful()) {
			String responseBody = checkoutAllResponse.getBody();
			System.out.println("API checkout_all response: " + responseBody);
		} else {
			System.err.println("API checkout_all failed with status code: " + checkoutAllResponse.getStatusCode());
		}
	}

	private void callCheckInAllApi(String token, String device_id , String deviceName) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(getCheckoutAllRequestBody(device_id, deviceName), headers);
		String checkInAllUrl = apiUrl + "/api/checkin_all";
		ResponseEntity<String> checkoutAllResponse = restTemplate.postForEntity(checkInAllUrl, requestEntity,
				String.class);
		if (checkoutAllResponse.getStatusCode().is2xxSuccessful()) {
			String responseBody = checkoutAllResponse.getBody();
			System.out.println("API checkout_all response: " + responseBody);
		} else {
			System.err.println("API checkout_all failed with status code: " + checkoutAllResponse.getStatusCode());
		}
	}

	private String getCheckoutAllRequestBody(  String device_id , String device_Name) {
		
		String deviceId = "\"deviceId\":\" "+device_id+ "\" , ";
		String deviceName = "\"deviceName\": \""+device_Name+"\", ";
		StringBuilder requestBody = new StringBuilder();
		requestBody.append("{");
		requestBody.append(deviceId);
		requestBody.append(deviceName);
		requestBody.append("\"ipGateway\": \"10.15.188.1\", ");
		requestBody.append("\"type\": 0, ");
		requestBody.append(
				"\"dataPrivate\": \"IaWLzVuyU7MXrs50nHVBoDprGiQ/iGiOa+w06jLnKuvKpM3Qjj338Qh9fP8n3dLx\\r\\nE/E+bchvoIyoOiZcWMXKy1iM8CHlOaHFJawxAxXpgpRi7lYrjpV4HtrJuZzC8nOW\\r\\n5nnerFvZWJLyDX2byZGJ6xKtVj/FCDJTxyH4EftYi1M=\", ");
		requestBody.append("\"isCheckDevice\": false");
		requestBody.append("}");
		return requestBody.toString();
	}

	private String getDataPrivate() {
		String dataPrivate = insertRandomdataPrivate(originalString, "\\r\\n");
		StringBuilder requestBody = new StringBuilder();
		requestBody.append("{");
		requestBody.append("\"deviceId\":\"TSKBLO29-LQJD-ZSWJ-W8AB-NMLESY0HUHD5\" , ");
		requestBody.append("\"deviceName\": \"Android\", ");
		requestBody.append("\"ipGateway\": \"10.15.188.1\", ");
		requestBody.append("\"type\": 0, ");
		requestBody.append(dataPrivate);
		requestBody.append("\"isCheckDevice\": false");
		requestBody.append("}");
		return requestBody.toString();
	}

	private static String insertRandomdataPrivate(String input, String target) {
		StringBuilder result = new StringBuilder();
		char randomChar = Math.random() < 0.5 ? getRandomLowercaseLetter() : getRandomUppercaseLetter();
		int index = input.indexOf(target);
		int lastIndex = 0;

		while (index != -1) {
			result.append(input, lastIndex, index).append(randomChar).append(target).append(randomChar);
			lastIndex = index + target.length();
			index = input.indexOf(target, lastIndex);
		}

		result.append(input.substring(lastIndex));

		return result.toString();
	}

	private static char getRandomLowercaseLetter() {
		return (char) ('a' + Math.random() * ('z' - 'a' + 1));
	}

	private static char getRandomUppercaseLetter() {
		return (char) ('A' + Math.random() * ('Z' - 'A' + 1));
	}

}

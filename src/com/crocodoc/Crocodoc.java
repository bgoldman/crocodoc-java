package com.crocodoc;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Provides access to the Crocodoc API. This is a base class that can be used
 * standalone with full access to the other Crocodoc API clients (Document,
 * Download, and Session), and is also used internally by the other Crocodoc
 * API clients for generic methods including error and request.
 */
public class Crocodoc {
	/**
	 * The developer's Crocodoc API token
	 * 
	 * @var string;	
	 */
	public static String apiToken;

	/**
	 * The default protocol (Crocodoc uses HTTPS)
	 * 
	 * @var string;	
	 */
	public static String protocol = "https";

	/**
	 * The default host
	 * 
	 * @var string;	
	 */
	public static String host = "crocodoc.com";

	/**
	 * The default base path on the server where the API lives
	 * 
	 * @var string;	
	 */
	public static String basePath = "/api/v2";

	/**
	 * An API path relative to the base API path
	 * 
	 * @var string
	 */
	public static String path = "/";
	
	/**
	 * A JSONParser instance for general use
	 * 
	 * @var object
	 */
	private static final JSONParser _jsonParser = new JSONParser();
	
	/**
	 * Handle an error. We handle errors by throwing an exception.
	 * 
	 * @param string error An error code representing the error
	 *   (use_underscore_separators)
	 * @param string client Which API client the error is being called from
	 * @param string method Which method the error is being called from
	 * @param object response This is a representation of the response, usually
	 *   from JSON, but can also be a string
	 * 
	 * @throws CrocodocException
	 */
	protected static void _error(String error, String client, String method, Object response) throws CrocodocException {
		String message = "Crocodoc: [" + error + "] " + client + "::" + method + "\n\n";
		message += JSONValue.toJSONString(response);
		throw new CrocodocException(message);
	}
	
	/**
	 * Make a request to the server and return the response as a string.
	 * 
	 * @param string path The path on the server relative to the base path
	 * @param string method The method to call on the server path; appended to
	 * 	 the server path 
	 * @param object getParams A key-value pair of GET params
	 * @param object postParams A key-value pair of POST params

	 * @return The response is an HttpEntity object
	 * @throws CrocodocException
	 */
	protected static HttpEntity _request(String path, String method, Map<String, Object> getParams, Map<String, Object> postParams) throws CrocodocException {
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(protocol);
		uriBuilder.setHost(host);
		uriBuilder.setPath(basePath + path + method);
		
		HttpClient httpClient = new DefaultHttpClient();
   		URI url = null;
		HttpRequest request = null;
   		HttpResponse response = null;
		
	   	try {
	   		if (getParams != null && getParams.size() > 0) {
	   			for (Map.Entry<String, Object> param : getParams.entrySet()) {
	   				uriBuilder.addParameter(param.getKey(), param.getValue().toString());
	   			}
	   		}
	
	   		if (postParams != null && postParams.size() > 0) {
	   			postParams.put("token",  apiToken);
	   		} else {
	   			uriBuilder.addParameter("token", apiToken);
	   		}
	   		
	   		url = uriBuilder.build();
		
			if (postParams != null && postParams.size() > 0) {
				HttpPost httpPost = new HttpPost(url);
				MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
					
	   			for (Map.Entry<String, Object> param : postParams.entrySet()) {
					if (param.getValue().getClass().equals(File.class)) {
						mpEntity.addPart(param.getKey(), new FileBody((File) param.getValue()));
					} else {
						StringBody stringBody = new StringBody(param.getValue().toString(), "text/plain", Charset.forName("UTF-8"));
						mpEntity.addPart(param.getKey(), stringBody);
					}
	   			}
					
				httpPost.setEntity(mpEntity);
				request = httpPost;
			} else {
				request = new HttpGet(url);
			}
			
	   		HttpUriRequest httpUriRequest = (HttpUriRequest) request;
			response = httpClient.execute(httpUriRequest);
		} catch (Exception e) {
			Map<String, Object> errorParams = new HashMap<String, Object>();
			errorParams.put("error", e.getMessage());
			errorParams.put("exception", e.getClass());
			errorParams.put("url", url);
			errorParams.put("getParams", getParams);
			errorParams.put("postParams", postParams);
			_error("connection_error", "Crocodoc", "_request", errorParams);
		}
	   	
	   	Integer httpCode = response.getStatusLine().getStatusCode();
	   	HttpEntity responseEntity = response.getEntity();
	   	
		Map<Integer, String> http4xxErrorCodes = new HashMap<Integer, String>();
		http4xxErrorCodes.put(400, "bad_request");
		http4xxErrorCodes.put(401, "unauthorized");
		http4xxErrorCodes.put(404, "not_found");
		http4xxErrorCodes.put(405, "method_not_allowed");

		if (http4xxErrorCodes.containsKey(httpCode)) {
			String error = "server_error_" + httpCode + "_" + http4xxErrorCodes.get(httpCode);
			Map<String, Object> errorParams = new HashMap<String, Object>();
			errorParams.put("url", url);
			errorParams.put("getParams", getParams);
			errorParams.put("postParams", postParams);
			_error(error, "Crocodoc", "_request", errorParams);
		}

		if (httpCode >= 500 && httpCode < 600) {
			String error = "server_error_" + httpCode + "_unknown";
			Map<String, Object> errorParams = new HashMap<String, Object>();
			errorParams.put("url", url);
			errorParams.put("getParams", getParams);
			errorParams.put("postParams", postParams);
			_error(error, "Crocodoc", "_request", errorParams);
		}
		
		return responseEntity;
	}
	
	/**
	 * Make an HTTP request.
	 * 
	 * @param string path The path on the server to make the request to
	 *   relative to the base path
	 * @param string method This is just an addition to the path, for example,
	 *   in "/documents/upload" the method would be "upload"
	 * @param object getParams A key-value pair of GET params
	 * @param object postParams A key-value pair of POST params
	 * 
	 * @return object The response is an object converted from JSON
	 * @throws CrocodocException
	 */
	protected static Object _requestJson(String path, String method, Map<String, Object> getParams, Map<String, Object> postParams) throws CrocodocException {
		HttpEntity responseEntity = (HttpEntity) _request(path, method, getParams, postParams);
		String result = "";

		try {
			result = EntityUtils.toString(responseEntity);
		} catch (Exception e) {
			Map<String, Object> errorParams = new HashMap<String, Object>();
			errorParams.put("error", e.getMessage());
			errorParams.put("responseEntity", responseEntity);
			errorParams.put("getParams", getParams);
			errorParams.put("postParams", postParams);
			_error("invalid_response", "Crocodoc", "_request", errorParams);
		}
		
		Object json = null;
		
		try {
			json = _jsonParser.parse(result);
		} catch (ParseException e) {
		}
		
		if (json == null) {
			Map<String, Object> errorParams = new HashMap<String, Object>();
			errorParams.put("response", result);
			errorParams.put("getParams", getParams);
			errorParams.put("postParams", postParams);
			_error("server_response_not_valid_json", "Crocodoc", "_error", errorParams);
		}

		// if the json response is not a JSONObject, return it
		// if it is a JSONObject, we can check it for errors
		if (!json.getClass().equals(JSONObject.class)) {
			return json;
		}
		
		JSONObject jsonObject = (JSONObject) json;

		if (jsonObject.containsKey("error")) {
			Map<String, Object> errorParams = new HashMap<String, Object>();
			errorParams.put("error", jsonObject.get("error").toString());
			errorParams.put("getParams", getParams);
			errorParams.put("postParams", postParams);
			_error("server_error", "Crocodoc", "_error", errorParams);
		}
		
		return jsonObject;
	}
	
	/**
	 * Get the API token
	 * 
	 * @return string The API token
	 */
	public static String getApiToken() {
		return apiToken;
	}
	
	/**
	 * Set the API token
	 * 
	 * @param string apiToken The API token
	 */
	public static void setApiToken(String newApiToken) {
		apiToken = newApiToken;
	}
}

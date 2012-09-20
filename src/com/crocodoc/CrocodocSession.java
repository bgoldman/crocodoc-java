package com.crocodoc;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

/**
 * Provides access to the Crocodoc Session API. The Session API is used to
 * to create sessions for specific documents that can be used to view a
 * document using a specific session-based URL.
 */
class CrocodocSession extends Crocodoc {
	/**
	 * The Download API path relative to the base API path
	 * 
	 * @var string
	 */
	public static String path = "/session/";
	
	/**
	 * Create a session for a specific document by UUID.
	 * 
	 * @param string uuid The uuid of the file to create a session for
	 * 
	 * @return string A unique session key for the document
	 * @throws CrocodocException 
	 */
	public static String create(String uuid) throws CrocodocException {
		return create(uuid, null);
	}
	
	/**
	 * Create a session for a specific document by UUID that is optionally
	 * editable and can use user ID and name info from your application,
	 * can filter annotations, can grant admin permissions, can be
	 * downloadable, can be copy-protected, and can prevent changes from being
	 * persisted.
	 * 
	 * @param string uuid The uuid of the file to create a session for
	 * @param hashtable params A hashtable representing:
	 *   bool 'isEditable' Can users create annotations and comments while
	 *     viewing the document with this session key?
	 *   array 'userInfo' An array with keys "id" and "name" representing
	 *     a user's unique ID and name in your application; "id" must be a
	 *     non-negative signed 32-bit integer; this field is required if
	 *     isEditable is true
	 *   string 'filter' Which annotations should be included if any - this
	 *     is usually a string, but could also be an array if it's a
	 *     comma-separated list of user IDs as the filter
	 *   bool 'isAdmin' Can users modify or delete any annotations or comments
	 *     belonging to other users?
	 *   bool 'isDownloadable' Can users download the original document?
	 *   bool 'isCopyprotected' Can text be selected in the document?
	 *   bool 'isDemo' Should we prevent any changes from being persisted?
	 * 
	 * @return string A unique session key for the document
	 * @throws CrocodocException 
	 */
	public static String create(String uuid, Map<String, Object>params) throws CrocodocException {
		Map<String, Object> postParams = new HashMap<String, Object>();
		postParams.put("uuid",  uuid);
		
		if (params == null) {
			params = new HashMap<String, Object>();
		}

		if (params.containsKey("isEditable")) {
			Boolean isEditable = (Boolean) params.get("isEditable");
			postParams.put("editable", isEditable ? 1 : 0);
			
			if (
				isEditable
				&& params.containsKey("userInfo")
				&& Map.class.isInstance(params.get("userInfo"))
			) {
				@SuppressWarnings("unchecked")
				Map<String, Object> userInfo = (Map<String, Object>) params.get("userInfo");
				
				if (!userInfo.containsKey("id")) {
					_error("missing_user_id", "CrocodocSession", "create", null);
				}
				
				if (!userInfo.containsKey("name")) {
					_error("missing_user_name", "CrocodocSession", "create", null);
				}
				
				String user = userInfo.get("id").toString() + "," + userInfo.get("name").toString();
				postParams.put("user", user);
			}
		}
		
		if (params.containsKey("filter")) {
			String filter;
			
			if (List.class.isInstance(params.get("filter"))) {
				@SuppressWarnings("unchecked")
				ArrayList<String> filterList = (ArrayList<String>) params.get("filter");
				StringBuilder sb = new StringBuilder();

				for (String s : filterList) {
				    sb.append(s);
				    sb.append(",");
				}
				
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}

				filter = sb.toString();
			} else {
				filter = params.get("filter").toString();
			}

			if (filter.length() > 0) {
				postParams.put("filter", filter);
			}
		}
		
		if (params.containsKey("isAdmin")) {
			postParams.put("admin", (Boolean) params.get("isAdmin"));
		}
		
		if (params.containsKey("isDownloadable")) {
			postParams.put("downloadable", (Boolean) params.get("isDownloadable"));
		}
		
		if (params.containsKey("isCopyprotected")) {
			postParams.put("copyrighted", (Boolean) params.get("isCopyprotected"));
		}
		
		if (params.containsKey("isDemo")) {
			postParams.put("demo", (Boolean) params.get("isDemo"));
		}
		
		JSONObject session = (JSONObject) _requestJson(path, "create", null, postParams);

		if (!session.containsKey("session")) {
			_error("missing_session_key", "CrocodocSession", "create", session);
		}
		
		return session.get("session").toString();
	}
}

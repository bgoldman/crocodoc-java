package com.crocodoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Provides access to the Crocodoc Document API. The Document API is used for
 * uploading, checking status, and deleting documents.
 */
public class CrocodocDocument extends Crocodoc {
    /**
     * The Document API path relative to the base API path
     * 
     * @var string
     */
    public static String path = "/document/";

    /**
     * Delete a file on Crocodoc by UUID.
     * 
     * @param string
     *            uuid The uuid of the file to delete
     * 
     * @return boolean Was the file deleted?
     * @throws CrocodocException
     */
    public static Boolean delete(String uuid) throws CrocodocException {
        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("uuid", uuid);
        return (Boolean) _requestJson(path, "delete", null, postParams);
    }

    /**
     * Check the status of a file on Crocodoc by UUID. Takes one UUID string and
     * returns one status array for that UUID.
     * 
     * @param string
     *            uuid The uuid of the file to check the status of
     * 
     * @return array An array of the uuid, status, and viewable bool, or an
     *         array of the uuid and an error
     * @throws CrocodocException
     */
    public static Map<String, Object> status(String uuid)
            throws CrocodocException {
        ArrayList<String> uuids = new ArrayList<String>();
        uuids.add(uuid);
        ArrayList<Map<String, Object>> statuses = status(uuids);
        Map<String, Object> status = statuses.get(0);

        if (!status.containsKey("uuid") || status.get("uuid") == null
                || status.get("uuid") == "") {
            _error("missing_uuid", "CrocodocDocument", "status", status);
        }

        return status;
    }

    /**
     * Check the status of a file on Crocodoc by UUID. Takes an array of UUIDs
     * and return an array of status arrays about those UUIDs, or can also take
     * a one UUID string and return one status array for that UUID.
     * 
     * @param array
     *            uuids An array of the uuids of the file to check the status of
     * 
     * @return array An array of the uuid, status, and viewable bool, or an
     *         array of the uuid and an error
     * @throws CrocodocException
     */
    public static ArrayList<Map<String, Object>> status(ArrayList<String> uuids)
            throws CrocodocException {
        Map<String, Object> getParams = new HashMap<String, Object>();
        String uuidsString = StringUtils.join(uuids, ",");
        getParams.put("uuids", uuidsString);
        JSONArray response = (JSONArray) _requestJson(path, "status",
                getParams, null);
        @SuppressWarnings("unchecked")
        ArrayList<Map<String, Object>> responseArray = (ArrayList<Map<String, Object>>) response;
        return responseArray;
    }

    /**
     * Upload a file to Crocodoc with a URL.
     * 
     * @param string
     *            url The url of the file to upload
     * 
     * @return string The uuid of the newly-uploaded file
     * @throws CrocodocException
     */
    public static String upload(String url) throws CrocodocException {
        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("url", url);
        JSONObject response = (JSONObject) _requestJson(path, "upload", null,
                postParams);

        if (!response.containsKey("uuid")) {
            _error("missing_uuid", "CrocodocDocument", "upload", response);
        }

        return response.get("uuid").toString();
    }

    /**
     * Upload a file to Crocodoc with a URL.
     * 
     * @param object
     *            file The file resource to be uploaded
     * 
     * @return string The uuid of the newly-uploaded file
     * @throws CrocodocException
     */
    public static String upload(File file) throws CrocodocException {
        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("file", file);
        JSONObject response = (JSONObject) _requestJson(path, "upload", null,
                postParams);

        if (!response.containsKey("uuid")) {
            _error("missing_uuid", "CrocodocDocument", "upload", response);
        }

        return response.get("uuid").toString();
    }
}

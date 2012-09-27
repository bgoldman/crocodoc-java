package com.crocodoc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;

/**
 * Provides access to the Crocodoc Download API. The Download API is used for
 * downloading an original of a document, a PDF of a document, a thumbnail of a
 * document, and text extracted from a document.
 */
class CrocodocDownload extends Crocodoc {
    /**
     * The Download API path relative to the base API path
     * 
     * @var string
     */
    public static String path = "/download/";

    /**
     * Download a document's original file from Crocodoc without any options.
     * 
     * @param string
     *            uuid The uuid of the file to download
     * 
     * @return object An HttpEntity of the downloaded file
     * @throws CrocodocException
     */
    public static HttpEntity document(String uuid) throws CrocodocException {
        return document(uuid, false, false, (String) null);
    }

    /**
     * Download a document's original file from Crocodoc. The file can
     * optionally be downloaded as a PDF, as another filename and with
     * annotations.
     * 
     * @param string
     *            uuid The uuid of the file to download
     * @param bool
     *            isPdf Should the file be downloaded as a PDF?
     * @param bool
     *            isAnnotated Should the file be downloaded with annotations?
     * 
     * @return object An HttpEntity of the downloaded file
     * @throws CrocodocException
     */
    public static HttpEntity document(String uuid, Boolean isPdf,
            Boolean isAnnotated) throws CrocodocException {
        return document(uuid, isPdf, isAnnotated, (String) null);
    }

    /**
     * Download a document's original file from Crocodoc. The file can
     * optionally be downloaded as a PDF, as another filename, with annotations,
     * and with filtered annotations.
     * 
     * @param string
     *            uuid The uuid of the file to download
     * @param bool
     *            isPdf Should the file be downloaded as a PDF?
     * @param bool
     *            isAnnotated Should the file be downloaded with annotations?
     * @param array
     *            filter Which annotations should be included if any as a list
     * 
     * @return object An HttpEntity of the downloaded file
     * @throws CrocodocException
     */
    public static HttpEntity document(String uuid, Boolean isPdf,
            Boolean isAnnotated, List<String> filter) throws CrocodocException {
        String filterString = null;

        if (filter != null) {
            StringBuilder sb = new StringBuilder();

            for (String s : filter) {
                sb.append(s);
                sb.append(",");
            }

            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }

            filterString = sb.toString();
        }

        return document(uuid, isPdf, isAnnotated, filterString);
    }

    /**
     * Download a document's original file from Crocodoc. The file can
     * optionally be downloaded as a PDF, as another filename, with annotations,
     * and with filtered annotations.
     * 
     * @param string
     *            uuid The uuid of the file to download
     * @param bool
     *            isPdf Should the file be downloaded as a PDF?
     * @param bool
     *            isAnnotated Should the file be downloaded with annotations?
     * @param string
     *            filter Which annotations should be included if any as a
     *            comma-separated list of user IDs as the filter
     * 
     * @return object An HttpEntity of the downloaded file
     * @throws CrocodocException
     */
    public static HttpEntity document(String uuid, Boolean isPdf,
            Boolean isAnnotated, String filter) throws CrocodocException {
        Map<String, Object> getParams = new HashMap<String, Object>();
        getParams.put("uuid", uuid);

        if (isPdf) {
            getParams.put("pdf", true);
        }

        if (isAnnotated) {
            getParams.put("annotated", true);

            if (filter != null && filter.length() > 0) {
                getParams.put("filter", filter);
            }
        }

        return (HttpEntity) _request(path, "document", getParams, null);
    }

    /**
     * Download a document's extracted text from Crocodoc.
     * 
     * @param string
     *            uuid The uuid of the file to extract text from
     * 
     * @return object An HttpEntity of the downloaded file
     * @throws CrocodocException
     */
    public static HttpEntity text(String uuid) throws CrocodocException {
        Map<String, Object> getParams = new HashMap<String, Object>();
        getParams.put("uuid", uuid);
        return (HttpEntity) _request(path, "text", getParams, null);
    }

    /**
     * Download a document's thumbnail from Crocodoc with an optional size.
     * 
     * @param string
     *            uuid The uuid of the file to download the thumbnail from
     * 
     * @return object An HttpEntity of the downloaded file
     * @throws CrocodocException
     */
    public static HttpEntity thumbnail(String uuid) throws CrocodocException {
        return thumbnail(uuid, null);
    }

    /**
     * Download a document's thumbnail from Crocodoc with an optional size.
     * 
     * @param string
     *            uuid The uuid of the file to download the thumbnail from
     * @param string
     *            size WIDTHxHEIGHT (integer X integer)
     * 
     * @return object An HttpEntity of the downloaded file
     * @throws CrocodocException
     */
    public static HttpEntity thumbnail(String uuid, String size)
            throws CrocodocException {
        Map<String, Object> getParams = new HashMap<String, Object>();
        getParams.put("uuid", uuid);

        if (size != null && size.length() > 0) {
            getParams.put("size", size);
        }

        return (HttpEntity) _request(path, "thumbnail", getParams, null);
    }

    /**
     * Download a document's thumbnail from Crocodoc with an optional size.
     * 
     * @param string
     *            uuid The uuid of the file to download the thumbnail from
     * @param int width The width you want the thumbnail to be
     * @param int height The height you want the thumbnail to be
     * 
     * @return object An HttpEntity of the downloaded file
     * @throws CrocodocException
     */
    public static HttpEntity thumbnail(String uuid, Integer width,
            Integer height) throws CrocodocException {
        String size = null;

        if (width != null && height != null) {
            size = width.toString() + "x" + height.toString();
        }

        return thumbnail(uuid, size);
    }
}

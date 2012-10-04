# crocodoc-java

## Introduction

crocodoc-java is a Java Wrapper for the Crocodoc API.
The Crocodoc API lets you upload documents and then generate secure and customized viewing sessions for them.
Our API is based on REST principles and generally returns JSON encoded responses,
and in Java are converted to Maps and Lists unless otherwise noted.

## Installation

First, get the library.
You can download the JAR here: https://github.com/downloads/crocodoc/crocodoc-java/crocodoc.jar

Add crocodoc.jar to your classpath.
	
## Getting Started

You can see a number of examples on how to use this library in CrocodocExamples.java.
These examples are interactive and you can run this file to see crocodoc-java in action.

To run these examples, download the source, open up CrocodocExamples.java and change this line to show your API token:

    public static String apiToken = "YOUR_API_TOKEN";
    
Save the file, make sure the example-files directory is writeable, and then run CrocodocExamples.java.
    
You should see 15 examples run with output.
You can inspect the CrocodocExamples.java code to see each API call being used.

To start using crocodoc-java in your code, instantiate the Crocodoc API library:

    Crocodoc.setApiToken(apiToken);

Read on to find out more how to use crocodoc-java.
You can also find more detailed information about our API here:
https://crocodoc.com/docs/api/

## Using the Crocodoc API Library

### Errors

Errors are handled by throwing exceptions.
We throw instances of CrocodocException.

Note that any Crocodoc API call can throw an exception.
When making API calls, put them in a try/catch block.
You can see CrocodocExamples.java to see working code for each method using try/catch blocks.

### Document

These methods allow you to upload, check the status of, and delete documents.

#### Upload

https://crocodoc.com/docs/api/#doc-upload  
To upload a document, use CrocodocDocument.upload().
Pass in a url (as a string) or a file resource object.
This function returns a UUID of the file.

	// with a url
    String uuid = CrocodocDocument.upload(url);
    
    // with a file
	File file = new File(filePath);
    String uuid = CrocodocDocument.upload(file);
    
#### Status

https://crocodoc.com/docs/api/#doc-status  
To check the status of a document, use CrocodocDocument.status().
Pass in the UUID of the file you want to check the status of.
This function returns a map containing a "status" string" and a "viewable" boolean.
If you passed in a list instead of a string, this function returns a list of maps containing the status for each file.

    // status contains status.get("status") and status.get("viewable")
	Map<String, Object> status = CrocodocDocument.status(uuid);
	
    // statuses contains a list of status maps
	ArrayList<String> uuids = new ArrayList<String>();
	uuids.add(uuid);
	uuids.add(uuid2);
	ArrayList<Map<String, Object>> statuses = CrocodocDocument.status(uuids);
    
#### Delete

https://crocodoc.com/docs/api/#doc-delete  
To delete a document, use CrocodocDocument.delete().
Pass in the UUID of the file you want to delete.
This function returns a boolean of whether the document was successfully deleted or not.

    Boolean deleted = CrocodocDocument.delete(uuid);
    
### Download

These methods allow you to download documents from Crocodoc in different ways.
You can download originals, PDFs, extracted text, and thumbnails.

#### Document

https://crocodoc.com/docs/api/#dl-doc  
To download a document, use CrocodocDownload.document().
Pass in the uuid,
an optional boolean of whether or not the file should be downloaded as a PDF,
an optional boolean or whether or not the file should be annotated,
and an optional filter string.
This function returns an HttpEntity of the file contents, which you probably want to save to a file.

    // with no optional arguments
	HttpEntity fileContent = CrocodocDownload.document(uuid);
	FileOutputStream stream = new FileOutputStream(filename);
	fileContent.writeTo(stream);
    
    // with all optional arguments
	HttpEntity fileContent = CrocodocDownload.document(uuid, true, true, "all");
	FileOutputStream stream = new FileOutputStream(filename);
	fileContent.writeTo(stream);
    
#### Thumbnail

https://crocodoc.com/docs/api/#dl-thumb  
To download a thumbnail, use CrocodocDownload.thumbnail().
Pass in the uuid and optionally the width and height.
This function returns an HttpEntity of the file contents, which you probably want to save to a file.

    // with no optional size arguments
    HttpEntity fileContent = CrocodocDownload.thumbnail(uuid);
	FileOutputStream stream = new FileOutputStream(filename);
	fileContent.writeTo(stream);
    
    // with optional size arguments (width 77, height 100)
    HttpEntity fileContent = CrocodocDownload.thumbnail(uuid, 77, 100);
	FileOutputStream stream = new FileOutputStream(filename);
	fileContent.writeTo(stream);

#### Text

https://crocodoc.com/docs/api/#dl-text  
To download extracted text from a document, use CrocodocDownload.text().
Pass in the uuid.
This function returns an HttpEntity of the extracted text, which you probably want to save to a file.

    HttpEntity fileContent = CrocodocDownload.text(uuid);
	FileOutputStream stream = new FileOutputStream(filename);
	fileContent.writeTo(stream);
	
You can also get the text as a string:

    HttpEntity fileContent = CrocodocDownload.text(uuid);
    String text = fileContent.getContent().toString();
    
### Session

The session method allows you to create a session for viewing documents in a secure manner.

#### Create

https://crocodoc.com/docs/api/#session-create  
To get a session key, use CrocodocSession.create().
Pass in the uuid and optionally a params map.
The params map can contain an "isEditable" boolean,
a "userInfo" map with "id" and "name" fields,
and booleans for "isAdmin", "isDownloadable", "isCopyprotected", and "isDemo".
This function returns a session key.

	// without optional params
    String sessionKey = CrocodocSession.create(uuid);
    
	// with optional params
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("isEditable", true);
	Map<String, Object> userParams = new HashMap<String, Object>();
	userParams.put("id", 1);
	userParams.put("name", "John Crocodile");
	params.put("user", userParams);
	params.put("filter", "all");
	params.put("isAdmin", true);
	params.put("isDownloadable", true);
	params.put("isCopyprotected", false);
	params.put("isDemo", false);
	params.put("sidebar", "visible");
    String sessionKey = CrocodocSession.create(uuid, params);
    
## Support

Please use github's issue tracker for API library support.
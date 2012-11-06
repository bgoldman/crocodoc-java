package com.crocodoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;

public class CrocodocExamples {
    public static String apiToken = "YOUR_API_TOKEN";

    public static void main(String[] args) {
        Crocodoc.setApiToken(apiToken);
        String uuid = example1();
        example2(uuid);
        String uuid2 = example3();
        example4(uuid, uuid2);
        example5(uuid, uuid2);
        example6(uuid);
        example7(uuid2);
        example8(uuid2);
        example9(uuid2);
        example10(uuid2);
        example11(uuid2);
        example12(uuid2);
        example13(uuid2);
        example14(uuid2);
        example15(uuid2);
    }

    /*
     * Example #1
     * 
     * Upload a file to Crocodoc. We're uploading Form W4 from the IRS by URL.
     */
    public static String example1() {
        System.out.println("Example #1 - Upload Form W4 from the IRS by URL.");
        String formW4Url = "http://www.irs.gov/pub/irs-pdf/fw4.pdf";
        System.out.print("  Uploading... ");
        String uuid = null;

        try {
            uuid = CrocodocDocument.upload(formW4Url);
            System.out.println("success :)");
            System.out.println("  UUID is " + uuid);
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        return uuid;
    }

    /*
     * Example #2
     * 
     * Check the status of the file from Example #1.
     */
    public static void example2(String uuid) {
        System.out.println();
        System.out
                .println("Example #2 - Check the status of the file we just uploaded.");

        System.out.print("  Checking status... ");

        try {
            Map<String, Object> status = CrocodocDocument.status(uuid);

            if (!status.containsKey("error")) {
                System.out.println("success :)");
                System.out.println("  File status is " + status.get("status"));
                Boolean isViewable = (Boolean) status.get("viewable");
                System.out.println("  File " + (isViewable ? "is" : "is not")
                        + " viewable.");
            } else {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + status.get("error"));
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #3
     * 
     * Upload another file to Crocodoc. We're uploading Form W4 from the IRS as
     * a PDF.
     */
    public static String example3() {
        System.out.println();
        System.out.println("Example #3 - Upload a sample pdf file.");
        File file = new File("./example-files/form-w4.pdf");
        System.out.print("  Uploading... ");
        String uuid = null;

        try {
            uuid = CrocodocDocument.upload(file);
            System.out.println("success :)");
            System.out.println("  UUID is " + uuid);
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        return uuid;
    }

    /*
     * Example #4
     * 
     * Check the status of both files we uploaded in Examples #1 and #3.
     */
    public static void example4(String uuid, String uuid2) {
        System.out.println();
        System.out
                .println("Example #4 - Check the status of both files at the same time.");
        System.out.print("Checking statuses... ");
        ArrayList<String> uuids = new ArrayList<String>();
        uuids.add(uuid);
        uuids.add(uuid2);

        try {
            ArrayList<Map<String, Object>> statuses = CrocodocDocument
                    .status(uuids);

            if (statuses.size() > 0) {
                System.out.println("success :)");
                Map<String, Object> status1 = statuses.get(0);

                if (!status1.containsKey("error")) {
                    System.out.println("  File #1 status is "
                            + status1.get("status") + ".");
                    Boolean isViewable = (Boolean) status1.get("viewable");
                    System.out.println("  File #1 "
                            + (isViewable ? "is" : "is not") + " viewable.");
                } else {
                    System.out.println("  File #1 failed :(");
                    System.out.println("  Error Message: "
                            + status1.get("error"));
                }

                Map<String, Object> status2 = statuses.get(1);

                if (!status2.containsKey("error")) {
                    System.out.println("  File #2 status is "
                            + status2.get("status") + ".");
                    Boolean isViewable = (Boolean) status2.get("viewable");
                    System.out.println("  File #2 "
                            + (isViewable ? "is" : "is not") + " viewable.");
                } else {
                    System.out.println("  File #2 failed :(");
                    System.out.println("  Error Message: "
                            + status2.get("error"));
                }
            } else {
                System.out.println("failed :(");
                System.out.println("  Statuses were not returned.");
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #5
     * 
     * Wait ten seconds and check the status of both files again.
     */
    public static void example5(String uuid, String uuid2) {
        System.out.println();
        System.out
                .println("Example #5 - Wait ten seconds and check the statuses again.");
        System.out.print("  Waiting... ");

        try {
            Thread.sleep(10000);
            System.out.println("done.");
        } catch (InterruptedException e) {
            System.out.println("failed :(");
            System.out.println("  " + e.getMessage());
        }

        System.out.println("  Checking statuses... ");
        ArrayList<String> uuids = new ArrayList<String>();
        uuids.add(uuid);
        uuids.add(uuid2);

        try {
            ArrayList<Map<String, Object>> statuses = CrocodocDocument
                    .status(uuids);

            if (statuses.size() > 0) {
                System.out.println("success :)");
                Map<String, Object> status1 = statuses.get(0);

                if (!status1.containsKey("error")) {
                    System.out.println("  File #1 status is "
                            + status1.get("status") + ".");
                    Boolean isViewable = (Boolean) status1.get("viewable");
                    System.out.println("  File #1 "
                            + (isViewable ? "is" : "is not") + " viewable.");
                } else {
                    System.out.println("  File #1 failed :(");
                    System.out.println("  Error Message: "
                            + status1.get("error"));
                }

                Map<String, Object> status2 = statuses.get(1);

                if (!status2.containsKey("error")) {
                    System.out.println("  File #2 status is "
                            + status2.get("status") + ".");
                    Boolean isViewable = (Boolean) status2.get("viewable");
                    System.out.println("  File #2 "
                            + (isViewable ? "is" : "is not") + " viewable.");
                } else {
                    System.out.println("  File #2 failed :(");
                    System.out.println("  Error Message: "
                            + status2.get("error"));
                }
            } else {
                System.out.println("failed :(");
                System.out.println("  Statuses were not returned.");
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #6
     * 
     * Delete the file we uploaded from Example #1.
     */
    public static void example6(String uuid) {
        System.out.println();
        System.out.println("Example #6 - Delete the first file we uploaded.");
        System.out.print("  Deleting... ");

        try {
            Boolean deleted = CrocodocDocument.delete(uuid);

            if (deleted) {
                System.out.println("success :)");
                System.out.println("  File was deleted.");
            } else {
                System.out.println("failed :(");
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #7
     * 
     * Download the file we uploaded from Example #3 as an original
     */
    public static void example7(String uuid2) {
        System.out.println();
        System.out.println("Example #7 - Download a file as an original.");
        System.out.print("  Downloading... ");

        try {
            HttpEntity fileContent = CrocodocDownload.document(uuid2);
            String filename = "./example-files/test-original.pdf";
            FileOutputStream stream = null;

            try {
                stream = new FileOutputStream(filename);
                fileContent.writeTo(stream);
                System.out.println("success :)");
                System.out
                        .println("  File was downloaded to " + filename + ".");
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #8
     * 
     * Download the file we uploaded from Example #3 as a PDF
     */
    public static void example8(String uuid2) {
        System.out.println();
        System.out.println("Example #8 - Download a file as a PDF.");
        System.out.print("  Downloading... ");

        try {
            HttpEntity fileContent = CrocodocDownload.document(uuid2, true,
                    false);
            String filename = "./example-files/test.pdf";
            FileOutputStream stream = null;

            try {
                stream = new FileOutputStream(filename);
                fileContent.writeTo(stream);
                System.out.println("success :)");
                System.out
                        .println("  File was downloaded to " + filename + ".");
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #9
     * 
     * Download the file we uploaded from Example #3 with all options
     */
    public static void example9(String uuid2) {
        System.out.println();
        System.out.println("Example #9 - Download a file with all options.");
        System.out.print("  Downloading... ");

        try {
            HttpEntity fileContent = CrocodocDownload.document(uuid2, true,
                    true, "all");
            String filename = "./example-files/test-with-options.pdf";
            FileOutputStream stream = null;

            try {
                stream = new FileOutputStream(filename);
                fileContent.writeTo(stream);
                System.out.println("success :)");
                System.out
                        .println("  File was downloaded to " + filename + ".");
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #10
     * 
     * Download the file we uploaded from Example #3 as a default thumbnail
     */
    public static void example10(String uuid2) {
        System.out.println();
        System.out
                .println("Example #10 - Download a default thumbnail from a file.");
        System.out.print("  Downloading... ");

        try {
            HttpEntity fileContent = CrocodocDownload.thumbnail(uuid2);
            String filename = "./example-files/thumbnail.png";
            FileOutputStream stream = null;

            try {
                stream = new FileOutputStream(filename);
                fileContent.writeTo(stream);
                System.out.println("success :)");
                System.out
                        .println("  File was downloaded to " + filename + ".");
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #11
     * 
     * Download the file we uploaded from Example #3 as a large thumbnail
     */
    public static void example11(String uuid2) {
        System.out.println();
        System.out
                .println("Example #11 - Download a large thumbnail from a file.");
        System.out.print("  Downloading... ");

        try {
            HttpEntity fileContent = CrocodocDownload
                    .thumbnail(uuid2, 250, 250);
            String filename = "./example-files/thumbnail-large.png";
            FileOutputStream stream = null;

            try {
                stream = new FileOutputStream(filename);
                fileContent.writeTo(stream);
                System.out.println("success :)");
                System.out
                        .println("  File was downloaded to " + filename + ".");
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #12
     * 
     * Download extracted text from the file we uploaded from Example #3
     */
    public static void example12(String uuid2) {
        System.out.println();
        System.out
                .println("Example #12 - Download extracted text from a file.");
        System.out.print("  Downloading... ");

        try {
            HttpEntity fileContent = CrocodocDownload.text(uuid2);
            String filename = "./example-files/text.txt";
            FileOutputStream stream = null;

            try {
                stream = new FileOutputStream(filename);
                fileContent.writeTo(stream);
                System.out.println("success :)");
                System.out
                        .println("  File was downloaded to " + filename + ".");
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #13
     * 
     * Create a session key for the file we uploaded from Example #3 with
     * default options.
     */
    public static void example13(String uuid2) {
        System.out.println();
        System.out
                .println("Example #13 - Create a session key for a file with default options.");
        System.out.print("  Creating... ");
        String sessionKey = null;

        try {
            sessionKey = CrocodocSession.create(uuid2);
            System.out.println("success :)");
            System.out.println("  The session key is " + sessionKey + ".");
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #14
     * 
     * Create a session key for the file we uploaded from Example #3 all of the
     * options.
     */
    public static void example14(String uuid2) {
        System.out.println();
        System.out
                .println("Example #14 - Create a session key for a file with all of the options.");
        System.out.print("  Creating... ");
        String sessionKey = null;

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

        try {
            sessionKey = CrocodocSession.create(uuid2, params);
            System.out.println("success :)");
            System.out.println("  The session key is " + sessionKey + ".");
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }

    /*
     * Example #15
     * 
     * Delete the file we uploaded from Example #2.
     */
    public static void example15(String uuid2) {
        System.out.println();
        System.out.println("Example #15 - Delete the second file we uploaded.");
        System.out.print("  Deleting... ");

        try {
            Boolean deleted = CrocodocDocument.delete(uuid2);

            if (deleted) {
                System.out.println("success :)");
                System.out.println("  File was deleted.");
            } else {
                System.out.println("failed :(");
            }
        } catch (CrocodocException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }
    }
}
package com.zhicloud.op.message.sms;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by sean on 7/8/15.
 */
public class ResourceUtil {

    public static String getResourceFullText(String path) {
        return getResourceFullText(path, "UTF-8");
    }

    public static String getResourceFullText(String path, String encoding) {

        FileSystemResource resource = new FileSystemResource(path);
        StringWriter writer = new StringWriter();

        try {
            IOUtils.copy(resource.getInputStream(), writer, encoding);
        } catch (IOException e) {
            throw new RuntimeException("cannot load resource data", e);
        }
        return writer.toString();
    }
}


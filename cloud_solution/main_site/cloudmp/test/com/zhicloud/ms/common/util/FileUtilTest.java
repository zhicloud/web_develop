package com.zhicloud.ms.common.util;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by sean on 2/25/16.
 */
public class FileUtilTest {

    public String fileName = File.separator + "Users" + File.separator + "sean" +File.separator + "test.txt";
    public File file = new File(fileName);

    @Test public void testReadFromFileToCache() throws Exception {

        FileInputStream fileInputStream = new FileInputStream(fileName);
        FileUtil.readFromFileToCache(fileInputStream);
    }
}

package com.zhicloud.ms.util;

import org.apache.log4j.Logger;


/**
 * Created by sean on 6/29/15.
 */
public class PlatformHelper {

    private static final Logger logger = Logger.getLogger(PlatformHelper.class);

    public static PlatformHelper singleton = new PlatformHelper();

    private PlatformHelper()
    {
        logger.info("PlatformHelper.PlatformHelper() > ["+Thread.currentThread().getId()+"] initialized");
    }

}

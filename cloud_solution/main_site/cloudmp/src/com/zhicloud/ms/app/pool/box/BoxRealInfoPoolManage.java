package com.zhicloud.ms.app.pool.box;


import com.zhicloud.ms.vo.BoxRealInfoVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 11/16/15.
 */
public class BoxRealInfoPoolManage {

    private final static Logger logger = Logger.getLogger(BoxRealInfoPoolManage.class);
    private static BoxRealInfoPoolManage instance = null;
    private final BoxRealInfoPool pool;
    private final static Object handler = new Object();
    @Resource private SqlSession sqlSession;

    // 单例
    public synchronized static BoxRealInfoPoolManage singleton() {
        if (BoxRealInfoPoolManage.instance == null) {
            BoxRealInfoPoolManage.instance = new BoxRealInfoPoolManage();
        }

        return instance;

    }

    private BoxRealInfoPoolManage() {
        pool = new BoxRealInfoPool();
        // 扫描线程
        Thread timeoutThread = new Thread(new TimeoutRunnable());
        timeoutThread.setDaemon(true);
        timeoutThread.start();
    }

    public BoxRealInfoPool getPool() {
        return pool;
    }

    private class TimeoutRunnable implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (handler) {
                    try {
                        handler.wait(60 * 60 * 1000);
                    } catch (InterruptedException exception) {
                        logger.error("the timeout thread in box real info pool manager fail to wait.", exception);
                    }
                }// 60分钟扫描一次
                BoxRealInfoVO[] list = BoxRealInfoPoolManage.this.pool.getAll();
                List<BoxRealInfoVO> boxRealInfoList = new ArrayList<BoxRealInfoVO>();
                for (int i = 0; i < list.length; i++) {
                    BoxRealInfoVO boxRealInfoVO = list[i];
                    if ((System.currentTimeMillis() - boxRealInfoVO.getLastAliveDate().getTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
                        boxRealInfoList.add(boxRealInfoVO);
                        BoxRealInfoPoolManage.this.pool.remove(boxRealInfoVO);
                    }
                }

//                this.sqlSession.getMapper(BoxRealInfoMapper.class)
            }
        }

    }
}

package com.wz.tnews.controller;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.wz.tnews.bean.News;

/**
 * @author wangzhan
 * @version 2018-04-09
 */

class BaseController {
    public static ConcurrentHashMap<String, List<News>> hashMap = new ConcurrentHashMap();
    public static final ConcurrentHashMap<Integer, Boolean> autoRefreshMap = new
            ConcurrentHashMap<>();
}

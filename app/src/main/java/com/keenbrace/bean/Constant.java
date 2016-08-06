package com.keenbrace.bean;

import com.keenbrace.greendao.HistoryRecord;
import com.keenbrace.greendao.LongPlan;
import com.keenbrace.greendao.ShortPlan;
import com.keenbrace.greendao.User;

//这里用于放全局变量 ken

/**
 * Created by zrq on 16/1/21.
 */
public class Constant {

    public static User user;

    public static ShortPlan shortPlan;  //当前要用的单次计划 (一天)

    public static LongPlan longPlan;    //当前要加载的长期计划

    public static HistoryRecord historyRecord;

}

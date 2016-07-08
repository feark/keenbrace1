package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

//通过GreenDao生成数据文件

public class GreenDaoGenerate {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.keenbrace.greendao");

        addUser(schema);
        //addRunWaring(schema);
        //addKeenBraceSports(schema);

        //addRunWaring(schema);

        addRunResult(schema);

        addCommonSportsResult(schema);

        addSportsStructure(schema);

        addShortPlan(schema);

        addLongPlan(schema);

        addHistoryRecord(schema);



        new DaoGenerator().generateAll(schema,"/home/ken/workspace/GreenDao_gen");
    }

    //User Info
    private static void addUser(Schema schema){

        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("userID");
        user.addStringProperty("Nickname");
        user.addIntProperty("gender");
        user.addDateProperty("birthday");
        user.addIntProperty("height");
        user.addIntProperty("weight");
        user.addStringProperty("fitness_level");
        user.addIntProperty("experience");  //运动经验
        user.addIntProperty("big_goal");    //运动目标
        user.addIntProperty("target_weight");
        user.addIntProperty("target_distance"); //如果跑步 目标距离
        user.addIntProperty("target_speed");
        user.addIntProperty("times");       //准备一周运动几次
        user.addStringProperty("equipment");//设施偏好
        user.addIntProperty("fields");      //场所
        user.addStringProperty("medals");   //获得奖章
        user.addStringProperty("email");
        user.addStringProperty("mobile");
        user.addStringProperty("picturePath");
        user.addStringProperty("loginName").unique();
        user.addStringProperty("password");

        user.addStringProperty("fileName");
    }


    private static void addRunWaring(Schema schema)
    {

        Entity user = schema.addEntity("RunWaring");
        user.addIdProperty();
        user.addStringProperty("index");
        user.addLongProperty("createTime");
        user.addDoubleProperty("latitude");
        user.addDoubleProperty("longitude");
        user.addLongProperty("runId");
        user.addIntProperty("lc");

    }


    //跑步的运动结果
    private static void addRunResult(Schema schema)
    {
        Entity user = schema.addEntity("RunResult");
        user.addIdProperty();

        user.addIntProperty("mileage");
        user.addLongProperty("duration");
        user.addIntProperty("speed");
        user.addIntProperty("cadence");
        user.addByteArrayProperty("speedPerKm");
        user.addByteArrayProperty("cadencePerKm");
        user.addIntProperty("stride");
        user.addIntProperty("kneePress");
        user.addLongProperty("step");
        user.addIntProperty("vertOsci");
        user.addIntProperty("emgDecrease");
        user.addLongProperty("calorie");
        user.addIntProperty("stability");

        user.addLongProperty("startTime");
        user.addLongProperty("endTime");

        user.addDoubleProperty("startlatitude");
        user.addDoubleProperty("startlongitude");
        user.addDoubleProperty("endlatitude");
        user.addDoubleProperty("endlongitude");

        user.addStringProperty("latLngs");
        user.addByteArrayProperty("notification");  //出现过的提示


    }

    //其他动作训练的运动结果
    private static void addCommonSportsResult(Schema schema)
    {
        Entity user = schema.addEntity("CommonResult");
        user.addIdProperty();

        user.addIntProperty("type");
        user.addIntProperty("set");
        user.addByteArrayProperty("reps");
        user.addIntProperty("load");
        user.addIntProperty("RM");
        user.addLongProperty("duration");
        user.addLongProperty("restTime");
        user.addLongProperty("wasteTime");
        user.addByteArrayProperty("newRecord");
    }

    //单项运动的数据结构
    private static void addSportsStructure(Schema schema)
    {
        Entity user = schema.addEntity("SportsStructure");
        user.addIdProperty();

        user.addIntProperty("type");
        user.addByteArrayProperty("equipment");
        user.addStringProperty("target1");
        user.addStringProperty("target2");
        user.addStringProperty("target3");

        user.addStringProperty("gif");
        user.addIntProperty("gif_len");

        user.addIntProperty("checkNum");
        user.addByteArrayProperty("checkX");
        user.addByteArrayProperty("checkY");
        user.addByteArrayProperty("checkZ");

        user.addLongProperty("bestTime");
    }

    //运动计划 单次 单次里面就可能包含多种运动 一个训练天里就包含一个单次计划
    private static void addShortPlan(Schema schema)
    {
        Entity user = schema.addEntity("ShortPlan");
        user.addIdProperty();

        user.addIntProperty("singleTrainID");   //单次计划的ID号

        //单次 跑步
        user.addIntProperty("warmUpTime");      //热身时间
        user.addIntProperty("distance");
        user.addIntProperty("section");         //分几次跑
        user.addByteArrayProperty("content");   //具体内容 是跑还是休息
        user.addByteArrayProperty("duration");  //内容的时长
        user.addIntProperty("cadence");         //限定步频
        user.addIntProperty("speed");
        user.addIntProperty("totalTime");       //总时长

        //单次 无氧
        user.addByteArrayProperty("type");      //做哪几种运动
        user.addByteArrayProperty("set");
        user.addByteArrayProperty("reps");
        user.addIntProperty("restBtwType");      //每种运动间休息时间
        user.addByteArrayProperty("restBtwSet");       //整个运动的休息时间

    }

    //运动计划 长期
    private static void addLongPlan(Schema schema)
    {
        Entity user = schema.addEntity("LongPlan");
        user.addIdProperty();

        user.addIntProperty("weekdays");    //一周几天练
        user.addDateProperty("start_date"); //训练计划的开始日期
        user.addDateProperty("end_date");   //结束日期
        user.addByteArrayProperty("types"); //每次都练的啥
        user.addIntProperty("times");       //总次数
        user.addByteArrayProperty("interval");  //间隔天
        user.addByteArrayProperty("finishStatus");  //完成情况

        user.addByteArrayProperty("trainIDset"); //每一天都做哪一种单次计划
    }

    //个人最佳和历史总计 包含身体各部分肌肉的运动频率
    private static void addHistoryRecord(Schema schema)
    {
        Entity user = schema.addEntity("HistoryRecord");
        user.addIdProperty();

        //各部分训练次数
        user.addLongProperty("triceps");
        user.addLongProperty("biceps");
        user.addLongProperty("shoulder");
        user.addLongProperty("forearm");
        user.addLongProperty("chest");
        user.addLongProperty("back");
        user.addLongProperty("abs");
        user.addLongProperty("glutes");
        user.addLongProperty("upperleg");
        user.addLongProperty("lowerleg");
        user.addLongProperty("cardio");

        //reps
        user.addLongProperty("tricepsReps");
        user.addLongProperty("bicepsReps");
        user.addLongProperty("shoulderReps");
        user.addLongProperty("forearmReps");
        user.addLongProperty("chestReps");
        user.addLongProperty("backReps");
        user.addLongProperty("absReps");
        user.addLongProperty("glutesReps");
        user.addLongProperty("upperlegReps");
        user.addLongProperty("lowerlegReps");

        user.addLongProperty("totalWorkout");
        user.addLongProperty("totalDistance");
        user.addIntProperty("averSpeed");
        user.addLongProperty("totalCalorie");
        user.addLongProperty("totalTime");

        //历史最佳
        user.addIntProperty("RM");
        user.addIntProperty("longestDistance");
        user.addLongProperty("longestTime");
        user.addIntProperty("fastestSpeed");
        user.addLongProperty("mostCalorie");

        //每项运动应该有对应的一个历史最佳次数 最大负重 最长时间

    }


    private static void addKeenBraceSports(Schema schema)
    {
        Entity user = schema.addEntity("KeenBrace_Sports");
        user.addIdProperty();
        user.addIntProperty("type");
        user.addLongProperty("startTime");
        user.addLongProperty("timelength");
        user.addIntProperty("sumscore");
        user.addIntProperty("cadence");
        user.addIntProperty("stride");
        user.addIntProperty("mileage");
        user.addStringProperty("fileName");
        user.addLongProperty("endTime");
        user.addStringProperty("latLngs");//x,y;x,y  地图上的运动路径
        user.addIntProperty("sumwarings");

        user.addDoubleProperty("latitude");
        user.addDoubleProperty("longitude");
        user.addDoubleProperty("endlatitude");
        user.addDoubleProperty("endlongitude");

    }

}

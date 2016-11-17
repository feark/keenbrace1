package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

//通过GreenDao生成数据文件

public class GreenDaoGenerate {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.keenbrace.greendao");

        addUser(schema);

        addCommonSportsResult(schema);

        addSportsStructure(schema);

        addShortPlan(schema);

        addLongPlan(schema);

        addHistoryRecord(schema);

        addChallenge(schema);


        new DaoGenerator().generateAll(schema,"/home/ken/workspace/GreenDao_gen");
    }

    //User Info Constant 全局
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

        //目标三围体脂等
        user.addIntProperty("goal_weight");
        user.addIntProperty("goal_bodyFat");
        user.addIntProperty("goal_waist");
        user.addIntProperty("goal_chest");
        user.addIntProperty("goal_arms");
        user.addIntProperty("goal_forArms");
        user.addIntProperty("goal_shoulder");
        user.addIntProperty("goal_hips");
        user.addIntProperty("goal_thighs");
        user.addIntProperty("goal_calves");
        user.addIntProperty("goal_neck");

        user.addStringProperty("fileName");
    }

    //所有运动的结果合并到一处
    //其他动作训练的运动结果 每次开始运动就新建一个对象
    private static void addCommonSportsResult(Schema schema)
    {
        Entity user = schema.addEntity("CommonResult");
        user.addIdProperty();

        user.addIntProperty("type");

        user.addIntProperty("set");
        user.addByteArrayProperty("reps");
        user.addByteArrayProperty("rep_duration");
        user.addIntProperty("load");
        user.addIntProperty("RM");
        user.addLongProperty("duration");
        user.addLongProperty("restTime");
        user.addLongProperty("wasteTime");
        user.addByteArrayProperty("newRecord");

        user.addIntProperty("mileage");
        user.addIntProperty("speed");
        user.addIntProperty("cadence");
        user.addByteArrayProperty("speedPerMinute");
        user.addIntProperty("minuteCount");
        user.addByteArrayProperty("cadencePerKm"); //**
        user.addIntProperty("stride");
        user.addByteArrayProperty("kneePress"); //**
        user.addLongProperty("step");
        user.addByteArrayProperty("vertOsci");  //**
        user.addByteArrayProperty("emgDecrease"); //**
        user.addLongProperty("calorie");
        user.addByteArrayProperty("stability"); //**

        user.addLongProperty("startTime");
        user.addLongProperty("endTime");

        user.addDoubleProperty("startlatitude");
        user.addDoubleProperty("startlongitude");
        user.addDoubleProperty("endlatitude");
        user.addDoubleProperty("endlongitude");

        user.addStringProperty("latLngs");
        user.addByteArrayProperty("notification");  //出现过的提示

        user.addStringProperty("comment");
        user.addStringProperty("picturePath");

        user.addStringProperty("dataFileName");

        //三围体脂等
        user.addIntProperty("weight");
        user.addIntProperty("bodyFat");
        user.addIntProperty("waist");
        user.addIntProperty("chest");
        user.addIntProperty("arms");
        user.addIntProperty("forArms");
        user.addIntProperty("shoulder");
        user.addIntProperty("hips");
        user.addIntProperty("thighs");
        user.addIntProperty("calves");
        user.addIntProperty("neck");

    }

    //单项运动的数据结构 这个需要用到类似 UtilConstants的HashMap方法管理  ++
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

    //运动计划 单次 单次里面就可能包含多种运动 即包含多个SportsStructure的索引ID 一个训练天里就包含一个单次计划
    //HashMap管理训练日的date
    private static void addShortPlan(Schema schema)
    {
        Entity user = schema.addEntity("ShortPlan");
        user.addIdProperty();

        //根据登录名找到对应的训练计划
        user.addStringProperty("ShortPlanName").unique();

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

    //运动计划 长期 这个只需要update一个表即可
    private static void addLongPlan(Schema schema)
    {
        Entity user = schema.addEntity("LongPlan");
        user.addIdProperty();

        user.addStringProperty("LongPlanName").unique();

        user.addIntProperty("weekdays");    //一周几天练
        user.addDateProperty("start_date"); //训练计划的开始日期
        user.addDateProperty("end_date");   //结束日期
        user.addByteArrayProperty("types"); //每次都练的啥
        user.addIntProperty("times");       //总次数
        user.addByteArrayProperty("interval");  //间隔天
        user.addByteArrayProperty("finishStatus");  //完成情况

        user.addByteArrayProperty("trainIDset"); //每一天都做哪一种单次计划
    }

    //个人最佳和历史总计 包含身体各部分肌肉的运动频率  update一个表
    private static void addHistoryRecord(Schema schema)
    {
        Entity user = schema.addEntity("HistoryRecord");
        user.addIdProperty();

        user.addStringProperty("loginName").unique();

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

        //最近一周的训练与否
        user.addBooleanProperty("mondayTrain");
        user.addBooleanProperty("tuesdayTrain");
        user.addBooleanProperty("wednesdayTrain");
        user.addBooleanProperty("thursdayTrain");
        user.addBooleanProperty("fridayTrain");
        user.addBooleanProperty("saturdayTrain");
        user.addBooleanProperty("sundayTrain");

    }

    private static void addChallenge(Schema schema)
    {
        Entity user = schema.addEntity("Challenge");
        user.addIdProperty();

        user.addIntProperty("challengeID");
        user.addIntProperty("challengeLogo");

        user.addStringProperty("title");
        user.addStringProperty("rules");
        user.addStringProperty("description");

        user.addIntProperty("rounds");
        user.addIntProperty("reps");
        user.addIntProperty("workoutsNumber");
        user.addByteArrayProperty("workouts");

        user.addIntProperty("distance");
        user.addIntProperty("section");         //分几次跑
        user.addByteArrayProperty("content");   //具体内容 是跑还是休息
        user.addByteArrayProperty("duration");  //内容的时长
        user.addIntProperty("cadence");         //限定步频
        user.addIntProperty("speed");
        user.addIntProperty("totalTime");       //时间限制
    }


}

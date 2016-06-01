package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerate {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.keenbrace.greendao");

        addUser(schema);
        addRunWaring(schema);
        addKeenBrace(schema);
        new DaoGenerator().generateAll(schema,"/Users/manor/work/keenbrace3/app/src/main/java");
    }


    private static void addUser(Schema schema){

        Entity user = schema.addEntity("User");
        user.addLongProperty("id").primaryKey();
        user.addStringProperty("userID");
        user.addStringProperty("Nickname");
        user.addIntProperty("sex");
        user.addStringProperty("birthday");
        user.addStringProperty("email");
        user.addStringProperty("mobile");
        user.addStringProperty("picturePath");
        user.addIntProperty("height");
        user.addIntProperty("weight");
        user.addStringProperty("loginName").unique();
        user.addStringProperty("password");

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
    private static void addKeenBrace(Schema schema)
    {
        Entity user = schema.addEntity("KeenBrace");
        user.addIdProperty();
        user.addLongProperty("userID");
        user.addIntProperty("type");
        user.addLongProperty("startTime");
        user.addLongProperty("timelength");
        user.addIntProperty("sumscore");
        user.addIntProperty("cadence");
        user.addIntProperty("stride");
        user.addIntProperty("mileage");
        user.addStringProperty("fileName");
        user.addLongProperty("endTime");
        user.addStringProperty("latLngs");//x,y;x,y
        user.addIntProperty("sumwarings");
        user.addDoubleProperty("latitude");
        user.addDoubleProperty("longitude");
        user.addDoubleProperty("endlatitude");
        user.addDoubleProperty("endlongitude");
        user.addIntProperty("state");


    }

}

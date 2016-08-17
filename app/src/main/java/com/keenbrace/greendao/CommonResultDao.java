package com.keenbrace.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.keenbrace.greendao.CommonResult;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMMON_RESULT".
*/
public class CommonResultDao extends AbstractDao<CommonResult, Long> {

    public static final String TABLENAME = "COMMON_RESULT";

    /**
     * Properties of entity CommonResult.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Type = new Property(1, Integer.class, "type", false, "TYPE");
        public final static Property Set = new Property(2, Integer.class, "set", false, "SET");
        public final static Property Reps = new Property(3, byte[].class, "reps", false, "REPS");
        public final static Property Rep_duration = new Property(4, byte[].class, "rep_duration", false, "REP_DURATION");
        public final static Property Load = new Property(5, Integer.class, "load", false, "LOAD");
        public final static Property RM = new Property(6, Integer.class, "RM", false, "RM");
        public final static Property Duration = new Property(7, Long.class, "duration", false, "DURATION");
        public final static Property RestTime = new Property(8, Long.class, "restTime", false, "REST_TIME");
        public final static Property WasteTime = new Property(9, Long.class, "wasteTime", false, "WASTE_TIME");
        public final static Property NewRecord = new Property(10, byte[].class, "newRecord", false, "NEW_RECORD");
        public final static Property Mileage = new Property(11, Integer.class, "mileage", false, "MILEAGE");
        public final static Property Speed = new Property(12, Integer.class, "speed", false, "SPEED");
        public final static Property Cadence = new Property(13, Integer.class, "cadence", false, "CADENCE");
        public final static Property SpeedPerMinute = new Property(14, byte[].class, "speedPerMinute", false, "SPEED_PER_MINUTE");
        public final static Property MinuteCount = new Property(15, Integer.class, "minuteCount", false, "MINUTE_COUNT");
        public final static Property CadencePerKm = new Property(16, byte[].class, "cadencePerKm", false, "CADENCE_PER_KM");
        public final static Property Stride = new Property(17, Integer.class, "stride", false, "STRIDE");
        public final static Property KneePress = new Property(18, byte[].class, "kneePress", false, "KNEE_PRESS");
        public final static Property Step = new Property(19, Long.class, "step", false, "STEP");
        public final static Property VertOsci = new Property(20, byte[].class, "vertOsci", false, "VERT_OSCI");
        public final static Property EmgDecrease = new Property(21, byte[].class, "emgDecrease", false, "EMG_DECREASE");
        public final static Property Calorie = new Property(22, Long.class, "calorie", false, "CALORIE");
        public final static Property Stability = new Property(23, byte[].class, "stability", false, "STABILITY");
        public final static Property StartTime = new Property(24, Long.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(25, Long.class, "endTime", false, "END_TIME");
        public final static Property Startlatitude = new Property(26, Double.class, "startlatitude", false, "STARTLATITUDE");
        public final static Property Startlongitude = new Property(27, Double.class, "startlongitude", false, "STARTLONGITUDE");
        public final static Property Endlatitude = new Property(28, Double.class, "endlatitude", false, "ENDLATITUDE");
        public final static Property Endlongitude = new Property(29, Double.class, "endlongitude", false, "ENDLONGITUDE");
        public final static Property LatLngs = new Property(30, String.class, "latLngs", false, "LAT_LNGS");
        public final static Property Notification = new Property(31, byte[].class, "notification", false, "NOTIFICATION");
        public final static Property Comment = new Property(32, String.class, "comment", false, "COMMENT");
        public final static Property PicturePath = new Property(33, String.class, "picturePath", false, "PICTURE_PATH");
        public final static Property Weight = new Property(34, Integer.class, "weight", false, "WEIGHT");
        public final static Property BodyFat = new Property(35, Integer.class, "bodyFat", false, "BODY_FAT");
        public final static Property Waist = new Property(36, Integer.class, "waist", false, "WAIST");
        public final static Property Chest = new Property(37, Integer.class, "chest", false, "CHEST");
        public final static Property Arms = new Property(38, Integer.class, "arms", false, "ARMS");
        public final static Property ForArms = new Property(39, Integer.class, "forArms", false, "FOR_ARMS");
        public final static Property Shoulder = new Property(40, Integer.class, "shoulder", false, "SHOULDER");
        public final static Property Hips = new Property(41, Integer.class, "hips", false, "HIPS");
        public final static Property Thighs = new Property(42, Integer.class, "thighs", false, "THIGHS");
        public final static Property Calves = new Property(43, Integer.class, "calves", false, "CALVES");
        public final static Property Neck = new Property(44, Integer.class, "neck", false, "NECK");
    };


    public CommonResultDao(DaoConfig config) {
        super(config);
    }
    
    public CommonResultDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMMON_RESULT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TYPE\" INTEGER," + // 1: type
                "\"SET\" INTEGER," + // 2: set
                "\"REPS\" BLOB," + // 3: reps
                "\"REP_DURATION\" BLOB," + // 4: rep_duration
                "\"LOAD\" INTEGER," + // 5: load
                "\"RM\" INTEGER," + // 6: RM
                "\"DURATION\" INTEGER," + // 7: duration
                "\"REST_TIME\" INTEGER," + // 8: restTime
                "\"WASTE_TIME\" INTEGER," + // 9: wasteTime
                "\"NEW_RECORD\" BLOB," + // 10: newRecord
                "\"MILEAGE\" INTEGER," + // 11: mileage
                "\"SPEED\" INTEGER," + // 12: speed
                "\"CADENCE\" INTEGER," + // 13: cadence
                "\"SPEED_PER_MINUTE\" BLOB," + // 14: speedPerMinute
                "\"MINUTE_COUNT\" INTEGER," + // 15: minuteCount
                "\"CADENCE_PER_KM\" BLOB," + // 16: cadencePerKm
                "\"STRIDE\" INTEGER," + // 17: stride
                "\"KNEE_PRESS\" BLOB," + // 18: kneePress
                "\"STEP\" INTEGER," + // 19: step
                "\"VERT_OSCI\" BLOB," + // 20: vertOsci
                "\"EMG_DECREASE\" BLOB," + // 21: emgDecrease
                "\"CALORIE\" INTEGER," + // 22: calorie
                "\"STABILITY\" BLOB," + // 23: stability
                "\"START_TIME\" INTEGER," + // 24: startTime
                "\"END_TIME\" INTEGER," + // 25: endTime
                "\"STARTLATITUDE\" REAL," + // 26: startlatitude
                "\"STARTLONGITUDE\" REAL," + // 27: startlongitude
                "\"ENDLATITUDE\" REAL," + // 28: endlatitude
                "\"ENDLONGITUDE\" REAL," + // 29: endlongitude
                "\"LAT_LNGS\" TEXT," + // 30: latLngs
                "\"NOTIFICATION\" BLOB," + // 31: notification
                "\"COMMENT\" TEXT," + // 32: comment
                "\"PICTURE_PATH\" TEXT," + // 33: picturePath
                "\"WEIGHT\" INTEGER," + // 34: weight
                "\"BODY_FAT\" INTEGER," + // 35: bodyFat
                "\"WAIST\" INTEGER," + // 36: waist
                "\"CHEST\" INTEGER," + // 37: chest
                "\"ARMS\" INTEGER," + // 38: arms
                "\"FOR_ARMS\" INTEGER," + // 39: forArms
                "\"SHOULDER\" INTEGER," + // 40: shoulder
                "\"HIPS\" INTEGER," + // 41: hips
                "\"THIGHS\" INTEGER," + // 42: thighs
                "\"CALVES\" INTEGER," + // 43: calves
                "\"NECK\" INTEGER);"); // 44: neck
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMMON_RESULT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CommonResult entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(2, type);
        }
 
        Integer set = entity.getSet();
        if (set != null) {
            stmt.bindLong(3, set);
        }
 
        byte[] reps = entity.getReps();
        if (reps != null) {
            stmt.bindBlob(4, reps);
        }
 
        byte[] rep_duration = entity.getRep_duration();
        if (rep_duration != null) {
            stmt.bindBlob(5, rep_duration);
        }
 
        Integer load = entity.getLoad();
        if (load != null) {
            stmt.bindLong(6, load);
        }
 
        Integer RM = entity.getRM();
        if (RM != null) {
            stmt.bindLong(7, RM);
        }
 
        Long duration = entity.getDuration();
        if (duration != null) {
            stmt.bindLong(8, duration);
        }
 
        Long restTime = entity.getRestTime();
        if (restTime != null) {
            stmt.bindLong(9, restTime);
        }
 
        Long wasteTime = entity.getWasteTime();
        if (wasteTime != null) {
            stmt.bindLong(10, wasteTime);
        }
 
        byte[] newRecord = entity.getNewRecord();
        if (newRecord != null) {
            stmt.bindBlob(11, newRecord);
        }
 
        Integer mileage = entity.getMileage();
        if (mileage != null) {
            stmt.bindLong(12, mileage);
        }
 
        Integer speed = entity.getSpeed();
        if (speed != null) {
            stmt.bindLong(13, speed);
        }
 
        Integer cadence = entity.getCadence();
        if (cadence != null) {
            stmt.bindLong(14, cadence);
        }
 
        byte[] speedPerMinute = entity.getSpeedPerMinute();
        if (speedPerMinute != null) {
            stmt.bindBlob(15, speedPerMinute);
        }
 
        Integer minuteCount = entity.getMinuteCount();
        if (minuteCount != null) {
            stmt.bindLong(16, minuteCount);
        }
 
        byte[] cadencePerKm = entity.getCadencePerKm();
        if (cadencePerKm != null) {
            stmt.bindBlob(17, cadencePerKm);
        }
 
        Integer stride = entity.getStride();
        if (stride != null) {
            stmt.bindLong(18, stride);
        }
 
        byte[] kneePress = entity.getKneePress();
        if (kneePress != null) {
            stmt.bindBlob(19, kneePress);
        }
 
        Long step = entity.getStep();
        if (step != null) {
            stmt.bindLong(20, step);
        }
 
        byte[] vertOsci = entity.getVertOsci();
        if (vertOsci != null) {
            stmt.bindBlob(21, vertOsci);
        }
 
        byte[] emgDecrease = entity.getEmgDecrease();
        if (emgDecrease != null) {
            stmt.bindBlob(22, emgDecrease);
        }
 
        Long calorie = entity.getCalorie();
        if (calorie != null) {
            stmt.bindLong(23, calorie);
        }
 
        byte[] stability = entity.getStability();
        if (stability != null) {
            stmt.bindBlob(24, stability);
        }
 
        Long startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(25, startTime);
        }
 
        Long endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindLong(26, endTime);
        }
 
        Double startlatitude = entity.getStartlatitude();
        if (startlatitude != null) {
            stmt.bindDouble(27, startlatitude);
        }
 
        Double startlongitude = entity.getStartlongitude();
        if (startlongitude != null) {
            stmt.bindDouble(28, startlongitude);
        }
 
        Double endlatitude = entity.getEndlatitude();
        if (endlatitude != null) {
            stmt.bindDouble(29, endlatitude);
        }
 
        Double endlongitude = entity.getEndlongitude();
        if (endlongitude != null) {
            stmt.bindDouble(30, endlongitude);
        }
 
        String latLngs = entity.getLatLngs();
        if (latLngs != null) {
            stmt.bindString(31, latLngs);
        }
 
        byte[] notification = entity.getNotification();
        if (notification != null) {
            stmt.bindBlob(32, notification);
        }
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(33, comment);
        }
 
        String picturePath = entity.getPicturePath();
        if (picturePath != null) {
            stmt.bindString(34, picturePath);
        }
 
        Integer weight = entity.getWeight();
        if (weight != null) {
            stmt.bindLong(35, weight);
        }
 
        Integer bodyFat = entity.getBodyFat();
        if (bodyFat != null) {
            stmt.bindLong(36, bodyFat);
        }
 
        Integer waist = entity.getWaist();
        if (waist != null) {
            stmt.bindLong(37, waist);
        }
 
        Integer chest = entity.getChest();
        if (chest != null) {
            stmt.bindLong(38, chest);
        }
 
        Integer arms = entity.getArms();
        if (arms != null) {
            stmt.bindLong(39, arms);
        }
 
        Integer forArms = entity.getForArms();
        if (forArms != null) {
            stmt.bindLong(40, forArms);
        }
 
        Integer shoulder = entity.getShoulder();
        if (shoulder != null) {
            stmt.bindLong(41, shoulder);
        }
 
        Integer hips = entity.getHips();
        if (hips != null) {
            stmt.bindLong(42, hips);
        }
 
        Integer thighs = entity.getThighs();
        if (thighs != null) {
            stmt.bindLong(43, thighs);
        }
 
        Integer calves = entity.getCalves();
        if (calves != null) {
            stmt.bindLong(44, calves);
        }
 
        Integer neck = entity.getNeck();
        if (neck != null) {
            stmt.bindLong(45, neck);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public CommonResult readEntity(Cursor cursor, int offset) {
        CommonResult entity = new CommonResult( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // type
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // set
            cursor.isNull(offset + 3) ? null : cursor.getBlob(offset + 3), // reps
            cursor.isNull(offset + 4) ? null : cursor.getBlob(offset + 4), // rep_duration
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // load
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // RM
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // duration
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // restTime
            cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9), // wasteTime
            cursor.isNull(offset + 10) ? null : cursor.getBlob(offset + 10), // newRecord
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // mileage
            cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12), // speed
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // cadence
            cursor.isNull(offset + 14) ? null : cursor.getBlob(offset + 14), // speedPerMinute
            cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15), // minuteCount
            cursor.isNull(offset + 16) ? null : cursor.getBlob(offset + 16), // cadencePerKm
            cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17), // stride
            cursor.isNull(offset + 18) ? null : cursor.getBlob(offset + 18), // kneePress
            cursor.isNull(offset + 19) ? null : cursor.getLong(offset + 19), // step
            cursor.isNull(offset + 20) ? null : cursor.getBlob(offset + 20), // vertOsci
            cursor.isNull(offset + 21) ? null : cursor.getBlob(offset + 21), // emgDecrease
            cursor.isNull(offset + 22) ? null : cursor.getLong(offset + 22), // calorie
            cursor.isNull(offset + 23) ? null : cursor.getBlob(offset + 23), // stability
            cursor.isNull(offset + 24) ? null : cursor.getLong(offset + 24), // startTime
            cursor.isNull(offset + 25) ? null : cursor.getLong(offset + 25), // endTime
            cursor.isNull(offset + 26) ? null : cursor.getDouble(offset + 26), // startlatitude
            cursor.isNull(offset + 27) ? null : cursor.getDouble(offset + 27), // startlongitude
            cursor.isNull(offset + 28) ? null : cursor.getDouble(offset + 28), // endlatitude
            cursor.isNull(offset + 29) ? null : cursor.getDouble(offset + 29), // endlongitude
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // latLngs
            cursor.isNull(offset + 31) ? null : cursor.getBlob(offset + 31), // notification
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // comment
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // picturePath
            cursor.isNull(offset + 34) ? null : cursor.getInt(offset + 34), // weight
            cursor.isNull(offset + 35) ? null : cursor.getInt(offset + 35), // bodyFat
            cursor.isNull(offset + 36) ? null : cursor.getInt(offset + 36), // waist
            cursor.isNull(offset + 37) ? null : cursor.getInt(offset + 37), // chest
            cursor.isNull(offset + 38) ? null : cursor.getInt(offset + 38), // arms
            cursor.isNull(offset + 39) ? null : cursor.getInt(offset + 39), // forArms
            cursor.isNull(offset + 40) ? null : cursor.getInt(offset + 40), // shoulder
            cursor.isNull(offset + 41) ? null : cursor.getInt(offset + 41), // hips
            cursor.isNull(offset + 42) ? null : cursor.getInt(offset + 42), // thighs
            cursor.isNull(offset + 43) ? null : cursor.getInt(offset + 43), // calves
            cursor.isNull(offset + 44) ? null : cursor.getInt(offset + 44) // neck
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CommonResult entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setType(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setSet(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setReps(cursor.isNull(offset + 3) ? null : cursor.getBlob(offset + 3));
        entity.setRep_duration(cursor.isNull(offset + 4) ? null : cursor.getBlob(offset + 4));
        entity.setLoad(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setRM(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setDuration(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setRestTime(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setWasteTime(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
        entity.setNewRecord(cursor.isNull(offset + 10) ? null : cursor.getBlob(offset + 10));
        entity.setMileage(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setSpeed(cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12));
        entity.setCadence(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setSpeedPerMinute(cursor.isNull(offset + 14) ? null : cursor.getBlob(offset + 14));
        entity.setMinuteCount(cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15));
        entity.setCadencePerKm(cursor.isNull(offset + 16) ? null : cursor.getBlob(offset + 16));
        entity.setStride(cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17));
        entity.setKneePress(cursor.isNull(offset + 18) ? null : cursor.getBlob(offset + 18));
        entity.setStep(cursor.isNull(offset + 19) ? null : cursor.getLong(offset + 19));
        entity.setVertOsci(cursor.isNull(offset + 20) ? null : cursor.getBlob(offset + 20));
        entity.setEmgDecrease(cursor.isNull(offset + 21) ? null : cursor.getBlob(offset + 21));
        entity.setCalorie(cursor.isNull(offset + 22) ? null : cursor.getLong(offset + 22));
        entity.setStability(cursor.isNull(offset + 23) ? null : cursor.getBlob(offset + 23));
        entity.setStartTime(cursor.isNull(offset + 24) ? null : cursor.getLong(offset + 24));
        entity.setEndTime(cursor.isNull(offset + 25) ? null : cursor.getLong(offset + 25));
        entity.setStartlatitude(cursor.isNull(offset + 26) ? null : cursor.getDouble(offset + 26));
        entity.setStartlongitude(cursor.isNull(offset + 27) ? null : cursor.getDouble(offset + 27));
        entity.setEndlatitude(cursor.isNull(offset + 28) ? null : cursor.getDouble(offset + 28));
        entity.setEndlongitude(cursor.isNull(offset + 29) ? null : cursor.getDouble(offset + 29));
        entity.setLatLngs(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setNotification(cursor.isNull(offset + 31) ? null : cursor.getBlob(offset + 31));
        entity.setComment(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setPicturePath(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setWeight(cursor.isNull(offset + 34) ? null : cursor.getInt(offset + 34));
        entity.setBodyFat(cursor.isNull(offset + 35) ? null : cursor.getInt(offset + 35));
        entity.setWaist(cursor.isNull(offset + 36) ? null : cursor.getInt(offset + 36));
        entity.setChest(cursor.isNull(offset + 37) ? null : cursor.getInt(offset + 37));
        entity.setArms(cursor.isNull(offset + 38) ? null : cursor.getInt(offset + 38));
        entity.setForArms(cursor.isNull(offset + 39) ? null : cursor.getInt(offset + 39));
        entity.setShoulder(cursor.isNull(offset + 40) ? null : cursor.getInt(offset + 40));
        entity.setHips(cursor.isNull(offset + 41) ? null : cursor.getInt(offset + 41));
        entity.setThighs(cursor.isNull(offset + 42) ? null : cursor.getInt(offset + 42));
        entity.setCalves(cursor.isNull(offset + 43) ? null : cursor.getInt(offset + 43));
        entity.setNeck(cursor.isNull(offset + 44) ? null : cursor.getInt(offset + 44));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CommonResult entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CommonResult entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}

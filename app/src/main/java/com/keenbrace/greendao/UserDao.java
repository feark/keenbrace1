package com.keenbrace.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserID = new Property(1, String.class, "userID", false, "USER_ID");
        public final static Property Nickname = new Property(2, String.class, "Nickname", false, "NICKNAME");
        public final static Property Gender = new Property(3, Integer.class, "gender", false, "GENDER");
        public final static Property Birthday = new Property(4, java.util.Date.class, "birthday", false, "BIRTHDAY");
        public final static Property Height = new Property(5, Integer.class, "height", false, "HEIGHT");
        public final static Property Weight = new Property(6, Integer.class, "weight", false, "WEIGHT");
        public final static Property Fitness_level = new Property(7, String.class, "fitness_level", false, "FITNESS_LEVEL");
        public final static Property Experience = new Property(8, Integer.class, "experience", false, "EXPERIENCE");
        public final static Property Big_goal = new Property(9, Integer.class, "big_goal", false, "BIG_GOAL");
        public final static Property Target_weight = new Property(10, Integer.class, "target_weight", false, "TARGET_WEIGHT");
        public final static Property Target_distance = new Property(11, Integer.class, "target_distance", false, "TARGET_DISTANCE");
        public final static Property Target_speed = new Property(12, Integer.class, "target_speed", false, "TARGET_SPEED");
        public final static Property Times = new Property(13, Integer.class, "times", false, "TIMES");
        public final static Property Equipment = new Property(14, String.class, "equipment", false, "EQUIPMENT");
        public final static Property Fields = new Property(15, Integer.class, "fields", false, "FIELDS");
        public final static Property Medals = new Property(16, String.class, "medals", false, "MEDALS");
        public final static Property Email = new Property(17, String.class, "email", false, "EMAIL");
        public final static Property Mobile = new Property(18, String.class, "mobile", false, "MOBILE");
        public final static Property PicturePath = new Property(19, String.class, "picturePath", false, "PICTURE_PATH");
        public final static Property LoginName = new Property(20, String.class, "loginName", false, "LOGIN_NAME");
        public final static Property Password = new Property(21, String.class, "password", false, "PASSWORD");
        public final static Property Goal_weight = new Property(22, Integer.class, "goal_weight", false, "GOAL_WEIGHT");
        public final static Property Goal_bodyFat = new Property(23, Integer.class, "goal_bodyFat", false, "GOAL_BODY_FAT");
        public final static Property Goal_waist = new Property(24, Integer.class, "goal_waist", false, "GOAL_WAIST");
        public final static Property Goal_chest = new Property(25, Integer.class, "goal_chest", false, "GOAL_CHEST");
        public final static Property Goal_arms = new Property(26, Integer.class, "goal_arms", false, "GOAL_ARMS");
        public final static Property Goal_forArms = new Property(27, Integer.class, "goal_forArms", false, "GOAL_FOR_ARMS");
        public final static Property Goal_shoulder = new Property(28, Integer.class, "goal_shoulder", false, "GOAL_SHOULDER");
        public final static Property Goal_hips = new Property(29, Integer.class, "goal_hips", false, "GOAL_HIPS");
        public final static Property Goal_thighs = new Property(30, Integer.class, "goal_thighs", false, "GOAL_THIGHS");
        public final static Property Goal_calves = new Property(31, Integer.class, "goal_calves", false, "GOAL_CALVES");
        public final static Property Goal_neck = new Property(32, Integer.class, "goal_neck", false, "GOAL_NECK");
        public final static Property FileName = new Property(33, String.class, "fileName", false, "FILE_NAME");
    };


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USER_ID\" TEXT," + // 1: userID
                "\"NICKNAME\" TEXT," + // 2: Nickname
                "\"GENDER\" INTEGER," + // 3: gender
                "\"BIRTHDAY\" INTEGER," + // 4: birthday
                "\"HEIGHT\" INTEGER," + // 5: height
                "\"WEIGHT\" INTEGER," + // 6: weight
                "\"FITNESS_LEVEL\" TEXT," + // 7: fitness_level
                "\"EXPERIENCE\" INTEGER," + // 8: experience
                "\"BIG_GOAL\" INTEGER," + // 9: big_goal
                "\"TARGET_WEIGHT\" INTEGER," + // 10: target_weight
                "\"TARGET_DISTANCE\" INTEGER," + // 11: target_distance
                "\"TARGET_SPEED\" INTEGER," + // 12: target_speed
                "\"TIMES\" INTEGER," + // 13: times
                "\"EQUIPMENT\" TEXT," + // 14: equipment
                "\"FIELDS\" INTEGER," + // 15: fields
                "\"MEDALS\" TEXT," + // 16: medals
                "\"EMAIL\" TEXT," + // 17: email
                "\"MOBILE\" TEXT," + // 18: mobile
                "\"PICTURE_PATH\" TEXT," + // 19: picturePath
                "\"LOGIN_NAME\" TEXT UNIQUE ," + // 20: loginName
                "\"PASSWORD\" TEXT," + // 21: password
                "\"GOAL_WEIGHT\" INTEGER," + // 22: goal_weight
                "\"GOAL_BODY_FAT\" INTEGER," + // 23: goal_bodyFat
                "\"GOAL_WAIST\" INTEGER," + // 24: goal_waist
                "\"GOAL_CHEST\" INTEGER," + // 25: goal_chest
                "\"GOAL_ARMS\" INTEGER," + // 26: goal_arms
                "\"GOAL_FOR_ARMS\" INTEGER," + // 27: goal_forArms
                "\"GOAL_SHOULDER\" INTEGER," + // 28: goal_shoulder
                "\"GOAL_HIPS\" INTEGER," + // 29: goal_hips
                "\"GOAL_THIGHS\" INTEGER," + // 30: goal_thighs
                "\"GOAL_CALVES\" INTEGER," + // 31: goal_calves
                "\"GOAL_NECK\" INTEGER," + // 32: goal_neck
                "\"FILE_NAME\" TEXT);"); // 33: fileName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userID = entity.getUserID();
        if (userID != null) {
            stmt.bindString(2, userID);
        }
 
        String Nickname = entity.getNickname();
        if (Nickname != null) {
            stmt.bindString(3, Nickname);
        }
 
        Integer gender = entity.getGender();
        if (gender != null) {
            stmt.bindLong(4, gender);
        }
 
        java.util.Date birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindLong(5, birthday.getTime());
        }
 
        Integer height = entity.getHeight();
        if (height != null) {
            stmt.bindLong(6, height);
        }
 
        Integer weight = entity.getWeight();
        if (weight != null) {
            stmt.bindLong(7, weight);
        }
 
        String fitness_level = entity.getFitness_level();
        if (fitness_level != null) {
            stmt.bindString(8, fitness_level);
        }
 
        Integer experience = entity.getExperience();
        if (experience != null) {
            stmt.bindLong(9, experience);
        }
 
        Integer big_goal = entity.getBig_goal();
        if (big_goal != null) {
            stmt.bindLong(10, big_goal);
        }
 
        Integer target_weight = entity.getTarget_weight();
        if (target_weight != null) {
            stmt.bindLong(11, target_weight);
        }
 
        Integer target_distance = entity.getTarget_distance();
        if (target_distance != null) {
            stmt.bindLong(12, target_distance);
        }
 
        Integer target_speed = entity.getTarget_speed();
        if (target_speed != null) {
            stmt.bindLong(13, target_speed);
        }
 
        Integer times = entity.getTimes();
        if (times != null) {
            stmt.bindLong(14, times);
        }
 
        String equipment = entity.getEquipment();
        if (equipment != null) {
            stmt.bindString(15, equipment);
        }
 
        Integer fields = entity.getFields();
        if (fields != null) {
            stmt.bindLong(16, fields);
        }
 
        String medals = entity.getMedals();
        if (medals != null) {
            stmt.bindString(17, medals);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(18, email);
        }
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(19, mobile);
        }
 
        String picturePath = entity.getPicturePath();
        if (picturePath != null) {
            stmt.bindString(20, picturePath);
        }
 
        String loginName = entity.getLoginName();
        if (loginName != null) {
            stmt.bindString(21, loginName);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(22, password);
        }
 
        Integer goal_weight = entity.getGoal_weight();
        if (goal_weight != null) {
            stmt.bindLong(23, goal_weight);
        }
 
        Integer goal_bodyFat = entity.getGoal_bodyFat();
        if (goal_bodyFat != null) {
            stmt.bindLong(24, goal_bodyFat);
        }
 
        Integer goal_waist = entity.getGoal_waist();
        if (goal_waist != null) {
            stmt.bindLong(25, goal_waist);
        }
 
        Integer goal_chest = entity.getGoal_chest();
        if (goal_chest != null) {
            stmt.bindLong(26, goal_chest);
        }
 
        Integer goal_arms = entity.getGoal_arms();
        if (goal_arms != null) {
            stmt.bindLong(27, goal_arms);
        }
 
        Integer goal_forArms = entity.getGoal_forArms();
        if (goal_forArms != null) {
            stmt.bindLong(28, goal_forArms);
        }
 
        Integer goal_shoulder = entity.getGoal_shoulder();
        if (goal_shoulder != null) {
            stmt.bindLong(29, goal_shoulder);
        }
 
        Integer goal_hips = entity.getGoal_hips();
        if (goal_hips != null) {
            stmt.bindLong(30, goal_hips);
        }
 
        Integer goal_thighs = entity.getGoal_thighs();
        if (goal_thighs != null) {
            stmt.bindLong(31, goal_thighs);
        }
 
        Integer goal_calves = entity.getGoal_calves();
        if (goal_calves != null) {
            stmt.bindLong(32, goal_calves);
        }
 
        Integer goal_neck = entity.getGoal_neck();
        if (goal_neck != null) {
            stmt.bindLong(33, goal_neck);
        }
 
        String fileName = entity.getFileName();
        if (fileName != null) {
            stmt.bindString(34, fileName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Nickname
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // gender
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // birthday
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // height
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // weight
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // fitness_level
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // experience
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // big_goal
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // target_weight
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // target_distance
            cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12), // target_speed
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // times
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // equipment
            cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15), // fields
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // medals
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // email
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // mobile
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // picturePath
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // loginName
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // password
            cursor.isNull(offset + 22) ? null : cursor.getInt(offset + 22), // goal_weight
            cursor.isNull(offset + 23) ? null : cursor.getInt(offset + 23), // goal_bodyFat
            cursor.isNull(offset + 24) ? null : cursor.getInt(offset + 24), // goal_waist
            cursor.isNull(offset + 25) ? null : cursor.getInt(offset + 25), // goal_chest
            cursor.isNull(offset + 26) ? null : cursor.getInt(offset + 26), // goal_arms
            cursor.isNull(offset + 27) ? null : cursor.getInt(offset + 27), // goal_forArms
            cursor.isNull(offset + 28) ? null : cursor.getInt(offset + 28), // goal_shoulder
            cursor.isNull(offset + 29) ? null : cursor.getInt(offset + 29), // goal_hips
            cursor.isNull(offset + 30) ? null : cursor.getInt(offset + 30), // goal_thighs
            cursor.isNull(offset + 31) ? null : cursor.getInt(offset + 31), // goal_calves
            cursor.isNull(offset + 32) ? null : cursor.getInt(offset + 32), // goal_neck
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33) // fileName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNickname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGender(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setBirthday(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setHeight(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setWeight(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setFitness_level(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setExperience(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setBig_goal(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setTarget_weight(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setTarget_distance(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setTarget_speed(cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12));
        entity.setTimes(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setEquipment(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setFields(cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15));
        entity.setMedals(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setEmail(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setMobile(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setPicturePath(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setLoginName(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setPassword(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setGoal_weight(cursor.isNull(offset + 22) ? null : cursor.getInt(offset + 22));
        entity.setGoal_bodyFat(cursor.isNull(offset + 23) ? null : cursor.getInt(offset + 23));
        entity.setGoal_waist(cursor.isNull(offset + 24) ? null : cursor.getInt(offset + 24));
        entity.setGoal_chest(cursor.isNull(offset + 25) ? null : cursor.getInt(offset + 25));
        entity.setGoal_arms(cursor.isNull(offset + 26) ? null : cursor.getInt(offset + 26));
        entity.setGoal_forArms(cursor.isNull(offset + 27) ? null : cursor.getInt(offset + 27));
        entity.setGoal_shoulder(cursor.isNull(offset + 28) ? null : cursor.getInt(offset + 28));
        entity.setGoal_hips(cursor.isNull(offset + 29) ? null : cursor.getInt(offset + 29));
        entity.setGoal_thighs(cursor.isNull(offset + 30) ? null : cursor.getInt(offset + 30));
        entity.setGoal_calves(cursor.isNull(offset + 31) ? null : cursor.getInt(offset + 31));
        entity.setGoal_neck(cursor.isNull(offset + 32) ? null : cursor.getInt(offset + 32));
        entity.setFileName(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
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

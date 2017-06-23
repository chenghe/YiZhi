package de.greenrobot.dao.attention;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.greenrobot.dao.attention.AttentionNotices;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ATTENTION_NOTICES".
*/
public class AttentionNoticesDao extends AbstractDao<AttentionNotices, Long> {

    public static final String TABLENAME = "ATTENTION_NOTICES";

    /**
     * Properties of entity AttentionNotices.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "ID");
        public final static Property PatientId = new Property(1, String.class, "patientId", false, "PATIENT_ID");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property Time = new Property(3, Long.class, "time", false, "TIME");
        public final static Property CreateTime = new Property(4, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property Status = new Property(5, Integer.class, "status", false, "STATUS");
        public final static Property IsActive = new Property(6, Boolean.class, "isActive", false, "IS_ACTIVE");
    };


    public AttentionNoticesDao(DaoConfig config) {
        super(config);
    }
    
    public AttentionNoticesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ATTENTION_NOTICES\" (" + //
                "\"ID\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"PATIENT_ID\" TEXT," + // 1: patientId
                "\"CONTENT\" TEXT," + // 2: content
                "\"TIME\" INTEGER," + // 3: time
                "\"CREATE_TIME\" INTEGER," + // 4: createTime
                "\"STATUS\" INTEGER," + // 5: status
                "\"IS_ACTIVE\" INTEGER);"); // 6: isActive
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ATTENTION_NOTICES\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AttentionNotices entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String patientId = entity.getPatientId();
        if (patientId != null) {
            stmt.bindString(2, patientId);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(4, time);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(5, createTime);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(6, status);
        }
 
        Boolean isActive = entity.getIsActive();
        if (isActive != null) {
            stmt.bindLong(7, isActive ? 1L: 0L);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AttentionNotices readEntity(Cursor cursor, int offset) {
        AttentionNotices entity = new AttentionNotices( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // patientId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // content
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // time
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // createTime
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // status
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0 // isActive
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AttentionNotices entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setPatientId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTime(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setCreateTime(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setStatus(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setIsActive(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(AttentionNotices entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(AttentionNotices entity) {
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
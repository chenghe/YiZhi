package de.greenrobot.dao.attention;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.greenrobot.dao.attention.RadiotherapySuspendedRecords;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RADIOTHERAPY_SUSPENDED_RECORDS".
*/
public class RadiotherapySuspendedRecordsDao extends AbstractDao<RadiotherapySuspendedRecords, Long> {

    public static final String TABLENAME = "RADIOTHERAPY_SUSPENDED_RECORDS";

    /**
     * Properties of entity RadiotherapySuspendedRecords.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "ID");
        public final static Property RadiotherapyRecordId = new Property(1, Long.class, "radiotherapyRecordId", false, "RADIOTHERAPY_RECORD_ID");
        public final static Property StartTime = new Property(2, Long.class, "startTime", false, "START_TIME");
        public final static Property SuspendDays = new Property(3, Integer.class, "suspendDays", false, "SUSPEND_DAYS");
        public final static Property Reason = new Property(4, Integer.class, "reason", false, "REASON");
        public final static Property PatientId = new Property(5, String.class, "patientId", false, "PATIENT_ID");
        public final static Property IsActive = new Property(6, Boolean.class, "isActive", false, "IS_ACTIVE");
    };


    public RadiotherapySuspendedRecordsDao(DaoConfig config) {
        super(config);
    }
    
    public RadiotherapySuspendedRecordsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RADIOTHERAPY_SUSPENDED_RECORDS\" (" + //
                "\"ID\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"RADIOTHERAPY_RECORD_ID\" INTEGER," + // 1: radiotherapyRecordId
                "\"START_TIME\" INTEGER," + // 2: startTime
                "\"SUSPEND_DAYS\" INTEGER," + // 3: suspendDays
                "\"REASON\" INTEGER," + // 4: reason
                "\"PATIENT_ID\" TEXT," + // 5: patientId
                "\"IS_ACTIVE\" INTEGER);"); // 6: isActive
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RADIOTHERAPY_SUSPENDED_RECORDS\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RadiotherapySuspendedRecords entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        Long radiotherapyRecordId = entity.getRadiotherapyRecordId();
        if (radiotherapyRecordId != null) {
            stmt.bindLong(2, radiotherapyRecordId);
        }
 
        Long startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(3, startTime);
        }
 
        Integer suspendDays = entity.getSuspendDays();
        if (suspendDays != null) {
            stmt.bindLong(4, suspendDays);
        }
 
        Integer reason = entity.getReason();
        if (reason != null) {
            stmt.bindLong(5, reason);
        }
 
        String patientId = entity.getPatientId();
        if (patientId != null) {
            stmt.bindString(6, patientId);
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
    public RadiotherapySuspendedRecords readEntity(Cursor cursor, int offset) {
        RadiotherapySuspendedRecords entity = new RadiotherapySuspendedRecords( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // radiotherapyRecordId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // startTime
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // suspendDays
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // reason
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // patientId
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0 // isActive
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RadiotherapySuspendedRecords entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setRadiotherapyRecordId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setStartTime(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setSuspendDays(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setReason(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setPatientId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIsActive(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RadiotherapySuspendedRecords entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(RadiotherapySuspendedRecords entity) {
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

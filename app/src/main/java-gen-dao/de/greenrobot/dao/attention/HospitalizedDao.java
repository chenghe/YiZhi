package de.greenrobot.dao.attention;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.greenrobot.dao.attention.Hospitalized;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HOSPITALIZED".
*/
public class HospitalizedDao extends AbstractDao<Hospitalized, Long> {

    public static final String TABLENAME = "HOSPITALIZED";

    /**
     * Properties of entity Hospitalized.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "ID");
        public final static Property InTime = new Property(1, long.class, "inTime", false, "IN_TIME");
        public final static Property Type = new Property(2, int.class, "type", false, "TYPE");
        public final static Property Status = new Property(3, Integer.class, "status", false, "STATUS");
        public final static Property DayCount = new Property(4, String.class, "dayCount", false, "DAY_COUNT");
        public final static Property PatientId = new Property(5, String.class, "patientId", false, "PATIENT_ID");
        public final static Property Description = new Property(6, String.class, "description", false, "DESCRIPTION");
        public final static Property HospitalName = new Property(7, String.class, "hospitalName", false, "HOSPITAL_NAME");
        public final static Property HospitalId = new Property(8, long.class, "hospitalId", false, "HOSPITAL_ID");
        public final static Property DoctorName = new Property(9, String.class, "doctorName", false, "DOCTOR_NAME");
        public final static Property IsActive = new Property(10, boolean.class, "isActive", false, "IS_ACTIVE");
    };


    public HospitalizedDao(DaoConfig config) {
        super(config);
    }
    
    public HospitalizedDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HOSPITALIZED\" (" + //
                "\"ID\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"IN_TIME\" INTEGER NOT NULL ," + // 1: inTime
                "\"TYPE\" INTEGER NOT NULL ," + // 2: type
                "\"STATUS\" INTEGER," + // 3: status
                "\"DAY_COUNT\" TEXT," + // 4: dayCount
                "\"PATIENT_ID\" TEXT NOT NULL ," + // 5: patientId
                "\"DESCRIPTION\" TEXT," + // 6: description
                "\"HOSPITAL_NAME\" TEXT," + // 7: hospitalName
                "\"HOSPITAL_ID\" INTEGER NOT NULL ," + // 8: hospitalId
                "\"DOCTOR_NAME\" TEXT," + // 9: doctorName
                "\"IS_ACTIVE\" INTEGER NOT NULL );"); // 10: isActive
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HOSPITALIZED\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Hospitalized entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getInTime());
        stmt.bindLong(3, entity.getType());
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(4, status);
        }
 
        String dayCount = entity.getDayCount();
        if (dayCount != null) {
            stmt.bindString(5, dayCount);
        }
        stmt.bindString(6, entity.getPatientId());
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(7, description);
        }
 
        String hospitalName = entity.getHospitalName();
        if (hospitalName != null) {
            stmt.bindString(8, hospitalName);
        }
        stmt.bindLong(9, entity.getHospitalId());
 
        String doctorName = entity.getDoctorName();
        if (doctorName != null) {
            stmt.bindString(10, doctorName);
        }
        stmt.bindLong(11, entity.getIsActive() ? 1L: 0L);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Hospitalized readEntity(Cursor cursor, int offset) {
        Hospitalized entity = new Hospitalized( //
            cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // inTime
            cursor.getInt(offset + 2), // type
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // status
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // dayCount
            cursor.getString(offset + 5), // patientId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // description
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // hospitalName
            cursor.getLong(offset + 8), // hospitalId
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // doctorName
            cursor.getShort(offset + 10) != 0 // isActive
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Hospitalized entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setInTime(cursor.getLong(offset + 1));
        entity.setType(cursor.getInt(offset + 2));
        entity.setStatus(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setDayCount(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPatientId(cursor.getString(offset + 5));
        entity.setDescription(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setHospitalName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setHospitalId(cursor.getLong(offset + 8));
        entity.setDoctorName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setIsActive(cursor.getShort(offset + 10) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Hospitalized entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Hospitalized entity) {
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
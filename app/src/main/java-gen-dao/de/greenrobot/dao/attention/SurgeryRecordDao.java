package de.greenrobot.dao.attention;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.greenrobot.dao.attention.SurgeryRecord;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SURGERY_RECORD".
*/
public class SurgeryRecordDao extends AbstractDao<SurgeryRecord, Long> {

    public static final String TABLENAME = "SURGERY_RECORD";

    /**
     * Properties of entity SurgeryRecord.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "ID");
        public final static Property HospitalId = new Property(1, long.class, "hospitalId", false, "HOSPITAL_ID");
        public final static Property TherapeuticId = new Property(2, long.class, "therapeuticId", false, "THERAPEUTIC_ID");
        public final static Property Time = new Property(3, long.class, "time", false, "TIME");
        public final static Property HospitalRecordId = new Property(4, Long.class, "hospitalRecordId", false, "HOSPITAL_RECORD_ID");
        public final static Property Notes = new Property(5, String.class, "notes", false, "NOTES");
        public final static Property DoctorName = new Property(6, String.class, "doctorName", false, "DOCTOR_NAME");
        public final static Property TherapeuticName = new Property(7, String.class, "therapeuticName", false, "THERAPEUTIC_NAME");
        public final static Property PatientId = new Property(8, String.class, "patientId", false, "PATIENT_ID");
        public final static Property HospitalName = new Property(9, String.class, "hospitalName", false, "HOSPITAL_NAME");
        public final static Property IsActive = new Property(10, boolean.class, "isActive", false, "IS_ACTIVE");
    };


    public SurgeryRecordDao(DaoConfig config) {
        super(config);
    }
    
    public SurgeryRecordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SURGERY_RECORD\" (" + //
                "\"ID\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"HOSPITAL_ID\" INTEGER NOT NULL ," + // 1: hospitalId
                "\"THERAPEUTIC_ID\" INTEGER NOT NULL ," + // 2: therapeuticId
                "\"TIME\" INTEGER NOT NULL ," + // 3: time
                "\"HOSPITAL_RECORD_ID\" INTEGER," + // 4: hospitalRecordId
                "\"NOTES\" TEXT," + // 5: notes
                "\"DOCTOR_NAME\" TEXT," + // 6: doctorName
                "\"THERAPEUTIC_NAME\" TEXT," + // 7: therapeuticName
                "\"PATIENT_ID\" TEXT," + // 8: patientId
                "\"HOSPITAL_NAME\" TEXT," + // 9: hospitalName
                "\"IS_ACTIVE\" INTEGER NOT NULL );"); // 10: isActive
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SURGERY_RECORD\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SurgeryRecord entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getHospitalId());
        stmt.bindLong(3, entity.getTherapeuticId());
        stmt.bindLong(4, entity.getTime());
 
        Long hospitalRecordId = entity.getHospitalRecordId();
        if (hospitalRecordId != null) {
            stmt.bindLong(5, hospitalRecordId);
        }
 
        String notes = entity.getNotes();
        if (notes != null) {
            stmt.bindString(6, notes);
        }
 
        String doctorName = entity.getDoctorName();
        if (doctorName != null) {
            stmt.bindString(7, doctorName);
        }
 
        String therapeuticName = entity.getTherapeuticName();
        if (therapeuticName != null) {
            stmt.bindString(8, therapeuticName);
        }
 
        String patientId = entity.getPatientId();
        if (patientId != null) {
            stmt.bindString(9, patientId);
        }
 
        String hospitalName = entity.getHospitalName();
        if (hospitalName != null) {
            stmt.bindString(10, hospitalName);
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
    public SurgeryRecord readEntity(Cursor cursor, int offset) {
        SurgeryRecord entity = new SurgeryRecord( //
            cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // hospitalId
            cursor.getLong(offset + 2), // therapeuticId
            cursor.getLong(offset + 3), // time
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // hospitalRecordId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // notes
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // doctorName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // therapeuticName
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // patientId
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // hospitalName
            cursor.getShort(offset + 10) != 0 // isActive
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SurgeryRecord entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setHospitalId(cursor.getLong(offset + 1));
        entity.setTherapeuticId(cursor.getLong(offset + 2));
        entity.setTime(cursor.getLong(offset + 3));
        entity.setHospitalRecordId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setNotes(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDoctorName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTherapeuticName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPatientId(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setHospitalName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setIsActive(cursor.getShort(offset + 10) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SurgeryRecord entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SurgeryRecord entity) {
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

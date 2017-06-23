package de.greenrobot.dao.attention;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.greenrobot.dao.attention.MedicineRecord;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MEDICINE_RECORD".
*/
public class MedicineRecordDao extends AbstractDao<MedicineRecord, Long> {

    public static final String TABLENAME = "MEDICINE_RECORD";

    /**
     * Properties of entity MedicineRecord.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "ID");
        public final static Property Status = new Property(1, Long.class, "status", false, "STATUS");
        public final static Property MedicineId = new Property(2, Long.class, "medicineId", false, "MEDICINE_ID");
        public final static Property MedicineItemId = new Property(3, Long.class, "medicineItemId", false, "MEDICINE_ITEM_ID");
        public final static Property PatientId = new Property(4, String.class, "patientId", false, "PATIENT_ID");
        public final static Property StartTime = new Property(5, Long.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(6, Long.class, "endTime", false, "END_TIME");
        public final static Property ChemicalName = new Property(7, String.class, "chemicalName", false, "CHEMICAL_NAME");
        public final static Property MedicineName = new Property(8, String.class, "medicineName", false, "MEDICINE_NAME");
        public final static Property IsActive = new Property(9, boolean.class, "isActive", false, "IS_ACTIVE");
    };


    public MedicineRecordDao(DaoConfig config) {
        super(config);
    }
    
    public MedicineRecordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MEDICINE_RECORD\" (" + //
                "\"ID\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"STATUS\" INTEGER," + // 1: status
                "\"MEDICINE_ID\" INTEGER," + // 2: medicineId
                "\"MEDICINE_ITEM_ID\" INTEGER," + // 3: medicineItemId
                "\"PATIENT_ID\" TEXT," + // 4: patientId
                "\"START_TIME\" INTEGER," + // 5: startTime
                "\"END_TIME\" INTEGER," + // 6: endTime
                "\"CHEMICAL_NAME\" TEXT," + // 7: chemicalName
                "\"MEDICINE_NAME\" TEXT," + // 8: medicineName
                "\"IS_ACTIVE\" INTEGER NOT NULL );"); // 9: isActive
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MEDICINE_RECORD\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MedicineRecord entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        Long status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(2, status);
        }
 
        Long medicineId = entity.getMedicineId();
        if (medicineId != null) {
            stmt.bindLong(3, medicineId);
        }
 
        Long medicineItemId = entity.getMedicineItemId();
        if (medicineItemId != null) {
            stmt.bindLong(4, medicineItemId);
        }
 
        String patientId = entity.getPatientId();
        if (patientId != null) {
            stmt.bindString(5, patientId);
        }
 
        Long startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(6, startTime);
        }
 
        Long endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindLong(7, endTime);
        }
 
        String chemicalName = entity.getChemicalName();
        if (chemicalName != null) {
            stmt.bindString(8, chemicalName);
        }
 
        String medicineName = entity.getMedicineName();
        if (medicineName != null) {
            stmt.bindString(9, medicineName);
        }
        stmt.bindLong(10, entity.getIsActive() ? 1L: 0L);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MedicineRecord readEntity(Cursor cursor, int offset) {
        MedicineRecord entity = new MedicineRecord( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // status
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // medicineId
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // medicineItemId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // patientId
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // startTime
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // endTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // chemicalName
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // medicineName
            cursor.getShort(offset + 9) != 0 // isActive
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MedicineRecord entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setStatus(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setMedicineId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setMedicineItemId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setPatientId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setStartTime(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setEndTime(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setChemicalName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setMedicineName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setIsActive(cursor.getShort(offset + 9) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(MedicineRecord entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(MedicineRecord entity) {
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
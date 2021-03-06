package de.greenrobot.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.greenrobot.dao.Medicine;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MEDICINE".
*/
public class MedicineDao extends AbstractDao<Medicine, Long> {

    public static final String TABLENAME = "MEDICINE";

    /**
     * Properties of entity Medicine.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property MedicineId = new Property(0, long.class, "medicineId", true, "MEDICINE_ID");
        public final static Property UpdateTime = new Property(1, Long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property ChemicalId = new Property(2, Long.class, "chemicalId", false, "CHEMICAL_ID");
        public final static Property Insurance = new Property(3, Integer.class, "insurance", false, "INSURANCE");
        public final static Property Classify = new Property(4, Integer.class, "classify", false, "CLASSIFY");
        public final static Property Imports = new Property(5, Integer.class, "imports", false, "IMPORTS");
        public final static Property Type = new Property(6, Integer.class, "type", false, "TYPE");
        public final static Property Special = new Property(7, Integer.class, "special", false, "SPECIAL");
        public final static Property Prescription = new Property(8, Integer.class, "prescription", false, "PRESCRIPTION");
        public final static Property Status = new Property(9, Integer.class, "status", false, "STATUS");
        public final static Property ShowName = new Property(10, String.class, "showName", false, "SHOW_NAME");
        public final static Property ChemicalName = new Property(11, String.class, "chemicalName", false, "CHEMICAL_NAME");
        public final static Property MedicineName = new Property(12, String.class, "medicineName", false, "MEDICINE_NAME");
        public final static Property PinyinName = new Property(13, String.class, "pinyinName", false, "PINYIN_NAME");
        public final static Property PriceMax = new Property(14, String.class, "priceMax", false, "PRICE_MAX");
        public final static Property PriceMin = new Property(15, String.class, "priceMin", false, "PRICE_MIN");
        public final static Property IsActive = new Property(16, boolean.class, "isActive", false, "IS_ACTIVE");
    };


    public MedicineDao(DaoConfig config) {
        super(config);
    }
    
    public MedicineDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MEDICINE\" (" + //
                "\"MEDICINE_ID\" INTEGER PRIMARY KEY NOT NULL ," + // 0: medicineId
                "\"UPDATE_TIME\" INTEGER," + // 1: updateTime
                "\"CHEMICAL_ID\" INTEGER," + // 2: chemicalId
                "\"INSURANCE\" INTEGER," + // 3: insurance
                "\"CLASSIFY\" INTEGER," + // 4: classify
                "\"IMPORTS\" INTEGER," + // 5: imports
                "\"TYPE\" INTEGER," + // 6: type
                "\"SPECIAL\" INTEGER," + // 7: special
                "\"PRESCRIPTION\" INTEGER," + // 8: prescription
                "\"STATUS\" INTEGER," + // 9: status
                "\"SHOW_NAME\" TEXT," + // 10: showName
                "\"CHEMICAL_NAME\" TEXT," + // 11: chemicalName
                "\"MEDICINE_NAME\" TEXT," + // 12: medicineName
                "\"PINYIN_NAME\" TEXT," + // 13: pinyinName
                "\"PRICE_MAX\" TEXT," + // 14: priceMax
                "\"PRICE_MIN\" TEXT," + // 15: priceMin
                "\"IS_ACTIVE\" INTEGER NOT NULL );"); // 16: isActive
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MEDICINE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Medicine entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getMedicineId());
 
        Long updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(2, updateTime);
        }
 
        Long chemicalId = entity.getChemicalId();
        if (chemicalId != null) {
            stmt.bindLong(3, chemicalId);
        }
 
        Integer insurance = entity.getInsurance();
        if (insurance != null) {
            stmt.bindLong(4, insurance);
        }
 
        Integer classify = entity.getClassify();
        if (classify != null) {
            stmt.bindLong(5, classify);
        }
 
        Integer imports = entity.getImports();
        if (imports != null) {
            stmt.bindLong(6, imports);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(7, type);
        }
 
        Integer special = entity.getSpecial();
        if (special != null) {
            stmt.bindLong(8, special);
        }
 
        Integer prescription = entity.getPrescription();
        if (prescription != null) {
            stmt.bindLong(9, prescription);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(10, status);
        }
 
        String showName = entity.getShowName();
        if (showName != null) {
            stmt.bindString(11, showName);
        }
 
        String chemicalName = entity.getChemicalName();
        if (chemicalName != null) {
            stmt.bindString(12, chemicalName);
        }
 
        String medicineName = entity.getMedicineName();
        if (medicineName != null) {
            stmt.bindString(13, medicineName);
        }
 
        String pinyinName = entity.getPinyinName();
        if (pinyinName != null) {
            stmt.bindString(14, pinyinName);
        }
 
        String priceMax = entity.getPriceMax();
        if (priceMax != null) {
            stmt.bindString(15, priceMax);
        }
 
        String priceMin = entity.getPriceMin();
        if (priceMin != null) {
            stmt.bindString(16, priceMin);
        }
        stmt.bindLong(17, entity.getIsActive() ? 1L: 0L);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Medicine readEntity(Cursor cursor, int offset) {
        Medicine entity = new Medicine( //
            cursor.getLong(offset + 0), // medicineId
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // updateTime
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // chemicalId
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // insurance
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // classify
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // imports
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // type
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // special
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // prescription
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // status
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // showName
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // chemicalName
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // medicineName
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // pinyinName
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // priceMax
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // priceMin
            cursor.getShort(offset + 16) != 0 // isActive
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Medicine entity, int offset) {
        entity.setMedicineId(cursor.getLong(offset + 0));
        entity.setUpdateTime(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setChemicalId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setInsurance(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setClassify(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setImports(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setType(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setSpecial(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setPrescription(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setStatus(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setShowName(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setChemicalName(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setMedicineName(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setPinyinName(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setPriceMax(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setPriceMin(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setIsActive(cursor.getShort(offset + 16) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Medicine entity, long rowId) {
        entity.setMedicineId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Medicine entity) {
        if(entity != null) {
            return entity.getMedicineId();
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

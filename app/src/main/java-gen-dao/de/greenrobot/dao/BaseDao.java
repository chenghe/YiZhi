package de.greenrobot.dao;

/**
 * Description: 预制数据，带索引部分,公用部分参数
 * 需要在每次运行 Daogenerator 之后手动修改继承关系
 * 目前需要继承的有 Indicator、Inspection、Interpretation、Tumor、City
 *
 * Created by Chengbin He on 2016/4/22.
 */
public class BaseDao {
    /** Not-null value. */
    public String name;
    /** Not-null value. */
    public String pinyinName;

    @Override
    public String toString() {
        return name ;
    }
}

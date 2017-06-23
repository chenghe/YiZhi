package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:医生详情中的医疗案例实体
 * user: Chong Chen
 * time：2016/1/14 20:19
 * 邮箱：cchen@ideabinder.com
 */
public class DoctDetailMedicase extends BaseBean {
    private DoctDetailMedicaseData data;

    public DoctDetailMedicaseData getData() {
        return data;
    }

    public void setData(DoctDetailMedicaseData data) {
        this.data = data;
    }

    public class DoctDetailMedicaseData implements Serializable {
        private List<Medicalcase> sourceItems;
        private int totalCount;
        private int totalPage;
        private int indexPage;
        private int startCount;
        private int endCount;
        private int pageSize;

        public List<Medicalcase> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<Medicalcase> sourceItems) {
            this.sourceItems = sourceItems;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getIndexPage() {
            return indexPage;
        }

        public void setIndexPage(int indexPage) {
            this.indexPage = indexPage;
        }

        public int getStartCount() {
            return startCount;
        }

        public void setStartCount(int startCount) {
            this.startCount = startCount;
        }

        public int getEndCount() {
            return endCount;
        }

        public void setEndCount(int endCount) {
            this.endCount = endCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

    public class Medicalcase implements Serializable {
        private String id;
        private String createTime;
        private String updateTime;
        private boolean isActive;
        private String doctorId;
        private String writer;
        private String content;
        private long writeTime;
        private String therapeuticId;
        private String therapeuticName;
        private List<Diseases> diseases;

        public List<Diseases> getDiseases() {
            return diseases;
        }

        public void setDiseases(List<Diseases> diseases) {
            this.diseases = diseases;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getWriter() {
            return writer;
        }

        public void setWriter(String writer) {
            this.writer = writer;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getWriteTime() {
            return writeTime;
        }

        public void setWriteTime(long writeTime) {
            this.writeTime = writeTime;
        }

        public String getTherapeuticId() {
            return therapeuticId;
        }

        public void setTherapeuticId(String therapeuticId) {
            this.therapeuticId = therapeuticId;
        }

        public String getTherapeuticName() {
            return therapeuticName;
        }

        public void setTherapeuticName(String therapeuticName) {
            this.therapeuticName = therapeuticName;
        }
    }

    public class Diseases implements Serializable {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}

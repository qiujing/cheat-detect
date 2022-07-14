package edu.hrbust.cheatreview.mapper;

import edu.hrbust.cheatreview.bean.Record;
import org.apache.ibatis.annotations.Param;

public interface RecordMapper {
    /**
     * Assign task
     *
     * @param reviewer_id Reviewer id
     * @param limit       Number of tasks
     * @return Unreviewed records
     */
    Integer assignTask(@Param("reviewer_id") Integer reviewer_id, @Param("limit") Integer limit);

    Record[] getUnprocessedRecord(@Param("reviewer_id") Integer reviewer_id, @Param("limit") int limit);

    void setResult(Record record);

    Integer getTotal(@Param("state") Integer state, @Param("recordId") Integer recordId);

    Record[] getResult(@Param("state") Integer state, @Param("page") Integer page, @Param("pageSize") Integer pageSize, @Param("recordId") Integer recordId);

    String getQContentByRecordId(Integer recordId);

    String getMContentByRecordId(Integer recordId);
}

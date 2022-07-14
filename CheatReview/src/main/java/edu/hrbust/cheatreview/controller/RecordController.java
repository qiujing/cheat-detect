package edu.hrbust.cheatreview.controller;

import edu.hrbust.cheatreview.bean.Record;
import edu.hrbust.cheatreview.bean.RecordResult;
import edu.hrbust.cheatreview.bean.StringResult;
import edu.hrbust.cheatreview.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecordController {
    @Autowired
    private RecordMapper recordMapper;

    /**
     * Get a group of unreviewed records
     *
     * @param reviewer_id Reviewer
     * @param limit       Number of records
     * @return Unreviewed records
     */
    Record[] next(Integer reviewer_id, Integer limit) {
        Record[] records = recordMapper.getUnprocessedRecord(reviewer_id, limit);
        if (records != null) {
            if (records.length < limit) {
                Integer count = recordMapper.assignTask(reviewer_id, limit - records.length);
                if (count > 0) {
                    records = recordMapper.getUnprocessedRecord(reviewer_id, limit);
                }
            }
        } else {
            Integer count = recordMapper.assignTask(reviewer_id, limit);
            if (count > 0) {
                records = recordMapper.getUnprocessedRecord(reviewer_id, limit);
            }
        }

        return records;
    }

    /**
     * Get unreviewed records
     *
     * @return Unreviewed records
     */
    @GetMapping("/api/get-unprocessed-record")
    public Record[] getUnprocessedRecord(@RequestParam("reviewer_id") Integer reviewer_id, @RequestParam("limit") Integer limit) {
        return next(reviewer_id, limit);
    }

    /**
     * Commit review result
     *
     * @return Unreviewed records
     */
    @PostMapping("/api/set-result")
    public Record[] setResult(@RequestBody Record[] record, @RequestParam("limit") Integer limit) {
        if (record == null) {
            return null;
        }
        for (Record r : record) {
            recordMapper.setResult(r);
        }

        return limit > 0 ? next(record[0].getReviewer_id(), limit) : null;
    }

    /**
     * Load review result
     *
     * @param state    cheat/not cheat
     * @param page     Page index
     * @param pageSize Page size
     * @return Records
     */
    @GetMapping("/api/load-result")
    public RecordResult loadResult(@RequestParam("state") Integer state, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam("recordId") Integer recordId) {
        RecordResult r = new RecordResult();
        r.setTotal(recordMapper.getTotal(state, recordId));
        r.setData(recordMapper.getResult(state, page * pageSize, pageSize, recordId));
        return r;
    }

    /**
     * Get content of questions
     *
     * @param recordId Record id
     * @return Content
     */
    @GetMapping("/api/get-q-content")
    public StringResult getQContentByRecordId(@RequestParam("record_id") Integer recordId) {
        StringResult r = new StringResult();
        r.setData(recordMapper.getQContentByRecordId(recordId));
        return r;
    }

    /**
     * Get reference answer
     *
     * @param recordId Record id
     * @return Reference answer
     */
    @GetMapping("/api/get-m-content")
    public StringResult getMContentByRecordId(@RequestParam("record_id") Integer recordId) {
        StringResult r = new StringResult();
        r.setData(recordMapper.getMContentByRecordId(recordId));
        return r;
    }
}

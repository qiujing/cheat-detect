package edu.hrbust.cheatreview.controller;

import edu.hrbust.cheatreview.bean.RankItem;
import edu.hrbust.cheatreview.bean.Reviewer;
import edu.hrbust.cheatreview.mapper.ReviewerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewerController {
    @Autowired
    ReviewerMapper reviewerMapper;

    /**
     * Register
     *
     * @param reviewer Reviewer
     * @return Reviewer
     */
    @PostMapping("/api/reg")
    public Reviewer reg(@RequestBody Reviewer reviewer) {
        Reviewer reviewer2 = reviewerMapper.getReviewer(reviewer.getUser_name());
        if (reviewer2 == null) {
            reviewerMapper.insertReviewer(reviewer);
            return reviewer;
        }
        return reviewer2;
    }

    /**
     * Rank
     *
     * @return Rank
     */
    @GetMapping("/api/get-rank-list")
    public RankItem[] getRankList() {
        RankItem[] list = reviewerMapper.getRankList();
        for (int i = 0; i < list.length; i++) {
            list[i].setId(i + 1);
        }

        return list;
    }
}

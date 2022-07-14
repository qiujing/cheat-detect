package edu.hrbust.cheatreview.mapper;

import edu.hrbust.cheatreview.bean.RankItem;
import edu.hrbust.cheatreview.bean.Reviewer;

public interface ReviewerMapper {
    Reviewer getReviewer(String user_name);

    Integer insertReviewer(Reviewer reviewer);

    RankItem[] getRankList();
}

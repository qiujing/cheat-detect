package edu.hrbust.cheatreview.bean;

public class CodeSimilarityResult {
    private Double similarity;
    private String[] graphJson;

    public CodeSimilarityResult() {
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public String[] getGraphJson() {
        return graphJson;
    }

    public void setGraphJson(String[] graphJson) {
        this.graphJson = graphJson;
    }
}

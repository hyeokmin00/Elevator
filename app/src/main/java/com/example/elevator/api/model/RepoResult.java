package com.example.elevator.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RepoResult {

    @SerializedName("")
    @Expose
    private ReportList reportList;

    public ReportList getReportList() {
        return reportList;
    }

    public void setReportList(ReportList reportList) {
        this.reportList = reportList;
    }
}

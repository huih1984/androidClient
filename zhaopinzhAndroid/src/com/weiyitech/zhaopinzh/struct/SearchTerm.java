package com.weiyitech.zhaopinzh.struct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-27
 * Time: 下午5:58
 * To change this template use File | Settings | File Templates.
 */
public class SearchTerm {
    public String keyword;
    public Date beginTime;
    public Date endTime;
    public int runningInterval;
    public int province;
    public int city;
    public int expReq;
    public int eduReq;
    public List<Integer> industryTypes;
    public List<Integer> jobTypes;
    public List<Integer> majorReqs;
    public Pager pager;
    public int fairId;
    public int employerId;
    public int orderBy;

    public SearchTerm(){
        industryTypes = new ArrayList<Integer>();
        jobTypes = new ArrayList<Integer>();
        majorReqs = new ArrayList<Integer>();
    }

    public boolean searchConditionItemEqual(SearchTerm searchTerm) {
        if (searchTerm.industryTypes.size() == this.industryTypes.size()) {
            for (int i = 0; i < searchTerm.industryTypes.size(); ++i) {
                if(searchTerm.industryTypes.get(i) != this.industryTypes.get(i)){
                     return false;
                }
            }
        }else {
            return false;
        }

        if (searchTerm.jobTypes.size() == this.jobTypes.size()) {
            for (int i = 0; i < searchTerm.jobTypes.size(); ++i) {
                if(searchTerm.jobTypes.get(i) != this.jobTypes.get(i)){
                    return false;
                }
            }
        } else {
            return false;
        }

        if (searchTerm.majorReqs.size() == this.majorReqs.size()) {
            for (int i = 0; i < searchTerm.majorReqs.size(); ++i) {
                if(searchTerm.majorReqs.get(i) != this.majorReqs.get(i)){
                    return false;
                }
            }
        }  else {
            return false;
        }

        if (searchTerm.runningInterval != this.runningInterval) {
            return false;
        }
        if (searchTerm.expReq != this.expReq) {
            return false;
        }
        if (searchTerm.eduReq != this.eduReq) {
            return false;
        }
        return true;
    }

}

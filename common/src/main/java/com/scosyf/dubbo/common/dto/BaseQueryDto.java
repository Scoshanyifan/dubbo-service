package com.scosyf.dubbo.common.dto;

import java.io.Serializable;

/**
 * @program: bucks
 * @description:
 * @author: kunbu
 * @create: 2019-08-27 16:30
 **/
public abstract class BaseQueryDto implements Serializable {

    private boolean usePage = true;
    private int pageNum = 1;
    private int pageSize = 10;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isUsePage() {
        return usePage;
    }

    public void setUsePage(boolean usePage) {
        this.usePage = usePage;
    }

    @Override
    public String toString() {
        return "BaseQueryDto{" +
                "usePage=" + usePage +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}

package com.example.xiaoying.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private Integer currentPage;
    private boolean showPrevious;
    private boolean showNext;
    private boolean showFirstPage;
    private boolean showEndPage;
    private Integer totalPage;
    private Integer totalCount;
    private List<Integer> pages=new ArrayList<>();

    public void setPagination(Integer pageIndex) {
        //让pages存储展示在界面上的页数，遵循左三右三
        this.currentPage=pageIndex;
        this.pages.add(pageIndex);
        for (int i=1;i<=3;i++){
            if (pageIndex-i>0){
                this.pages.add(0,pageIndex-i);
            }
            if (pageIndex+i<=totalPage){
                this.pages.add(pageIndex+i);
            }
        }
        //是否展示第一页
        if (pageIndex-4>=1){ //或者pages.contains(1)
            this.showFirstPage=true;
        }else {
            this.showFirstPage=false;
        }
        //是否展示最后一页
        if (pageIndex+4<=totalPage){
            this.showEndPage=true;
        }else {
            this.showEndPage=false;
        }
        //是否展示上一页
        if (pageIndex==1){
            this.showPrevious=false;
        }else {
            this.showPrevious=true;
        }
        //是否展示下一页
        if (pageIndex<totalPage){
            this.showNext=true;
        }else {
            this.showNext=false;
        }

    }
}

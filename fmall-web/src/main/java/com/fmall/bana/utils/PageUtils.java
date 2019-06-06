package com.fmall.bana.utils;

import java.util.HashMap;

public class PageUtils {
    public Integer firstRow;
    public Integer listRows;
    public Integer totalRows;
    public Integer totalPages;
    public Integer rollPage = 5;
    public String url = "";

    private Integer nowPage = 1;

    private HashMap<String, String> config = new HashMap<String, String>() {{
        put("header", "<li class=\"\"><span>共 %TOTAL_ROW% 条记录</span></li>");
        put("prev", "上一页");
        put("next", "下一页");
        put("first", "首页");
        put("last", "尾页");
        put("footer", "<li class=\"btn\"><span>%NOW_PAGE%/%TOTAL_PAGE%</span></li>");
//        put("theme", "%HEADER% %FIRST% %UP_PAGE% %LINK_PAGE% %DOWN_PAGE% %END% %FOOTER%");
        put("theme", "%FIRST% %UP_PAGE% %LINK_PAGE% %DOWN_PAGE% %END% %FOOTER%");
    }};

    public PageUtils(Integer nowPage, Integer listRows, Integer totalRows, String url) {
        this.nowPage = Math.max(nowPage, 1);
        this.listRows = listRows;
        this.totalRows = totalRows;
        this.url = url;
        this.firstRow = this.listRows * (this.nowPage - 1);
    }

    public String show() {
        if (this.totalRows == 0) return "";
        //计算分页信息
        this.totalPages = (int) Math.ceil((float) this.totalRows / (float) this.listRows);

        if (this.totalPages != 0 && this.nowPage > this.totalPages) {
            this.nowPage = this.totalPages;
        }

        //计算分页零时变量
        Integer nowCoolPage = this.rollPage / 2;
        Integer nowCoolPageCeil = (int) Math.ceil((float) nowCoolPage);

        //上一页
        Integer upRow = this.nowPage - 1;
        String upPage = upRow > 0 ? "<li class='btn pageNum'><a href=\"" + this.url + upRow + "\">" + this.config.get("prev") + "</a></li>" : "<li class='btn'><span>" + this.config.get("prev") + "</span></li>";

        //下一页
        Integer downRow = this.nowPage + 1;
        String downPage = downRow <= this.totalPages ? "<li class='btn pageNum'><a href=\"" + this.url + downRow + "\">" + this.config.get("next") + "</a></li>" : "<li class='btn'><span>" + this.config.get("next") + "</span></li>";

        //第一页
        String firstPage = "";
        if (this.totalPages > this.rollPage && (this.nowPage - nowCoolPage) >= 1) {
            firstPage = "<li class='btn pageNum'><a href=\"" + this.url + "1\">" + this.config.get("first") + "</a></li>";
        }

        //最后一页
        String lastPage = "";
        if (this.totalPages > this.rollPage && (this.nowPage + nowCoolPage) >= this.totalPages) {
            lastPage = "<li class='btn pageNum'><a href=\"" + this.url + this.totalPages + "\">" + this.config.get("last") + "</a></li>";
        }

        //数字链接
        String linkPage = "";
        for (int i = 0; i <= this.rollPage; i++) {
            Integer page = i;
            if ((this.nowPage - nowCoolPage) <= 0) {
                page = i;
            } else if ((this.nowPage + nowCoolPage - 1) >= this.totalPages) {
                page = this.totalPages - this.rollPage + i;
            } else {
                page = this.nowPage - nowCoolPageCeil + i;
            }
            if (page > 0 && page.compareTo(this.nowPage) != 0) {
                if (page <= this.totalPages) {
                    linkPage += "<li class='number pageNum'><a href=\"" + this.url + page + "\">" + page + "</a></li>";
                } else {
                    break;
                }
            } else {
                if (page > 0) {
                    linkPage += "<li class=\"number pageNum cur\"><span>" + page + "</span></li>";
                }
            }
        }
        //替换分页主题
        String theme = this.config.get("theme");
        theme = theme.replace("%HEADER%", this.config.get("header"));
        theme = theme.replace("%FOOTER%", this.config.get("footer"));
        theme = theme.replace("%NOW_PAGE%", this.nowPage.toString());
        theme = theme.replace("%UP_PAGE%", upPage);
        theme = theme.replace("%DOWN_PAGE%", downPage);
        theme = theme.replace("%FIRST%", firstPage);
        theme = theme.replace("%LINK_PAGE%", linkPage);
        theme = theme.replace("%END%", lastPage);
        theme = theme.replace("%TOTAL_ROW%", this.totalRows.toString());
        theme = theme.replace("%TOTAL_PAGE%", this.totalPages.toString());
        return "<ul class=\"paging\">" + theme + "</ul>";
    }
}
package com.hanyu.zhihuluanbao.commons;

import com.hanyu.zhihuluanbao.models.NewsModel;

/**
 * Created by Dell on 2016/10/25.
 */
public class HtmlFile {
    public String html ="";
    public NewsModel newsModel;
    public HtmlFile(NewsModel newsModel){
        this.newsModel = newsModel;

        html = "<!DOCTYPE html>\n"
                + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "<head>\n"
                + "\t<meta charset=\"utf-8\" />"
                + "<link rel=\"stylesheet\" href=\"file:///android_asset/news.css\" type=\"text/css\">"
                + "\n</head>\n"
                + newsModel.body.replace("<div class=\"img-place-holder\">", "")
                + "</body></html>";
    }

}

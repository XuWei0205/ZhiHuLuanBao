package com.hanyu.zhihuluanbao.models;

/**
 * Created by Dell on 2016/10/11.
 */
public class News {
    private int id;
    private String title;
    private String image;
    private String imageResource;

    public int getId(){
        return id;
    }


    public void setId(int id){
        this.id = id;

    }

    public void setTitle (String title){
        this.title = title ;

    }

    public String getTitle(){
        return title;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
        return image;
    }

    public void setImageResource(String resource){
        this.imageResource = resource;
    }
    public String getImageResource(){
        return imageResource;
    }

}

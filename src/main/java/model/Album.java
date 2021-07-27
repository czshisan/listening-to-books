package model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class Album {
    public Integer aid;
    public String name; //专辑名称
    public String cover;
    public Integer count; //播放数
    public String header;
    public String brief;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createdAt;

    public List<Story> storyList;
}

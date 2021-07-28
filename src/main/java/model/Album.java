package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

public class Album {
    public Integer aid;
    public String name; //专辑名称
    public String cover;
    public Integer count; //播放数
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String header;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String brief;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Story> storyList;
}

package dao;

import model.Story;
import util.DBUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoryDao {
    //通过 sid 查找故事
    public Story selectOneUsingSid(int sid) throws SQLException {
        try(Connection c = DBUtil.getConnection()) {
            String sql = "select name, created_at, count from story where sid = ?";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                s.setInt(1, sid);

                try (ResultSet rs = s.executeQuery()) {
                    if (!rs.next()) {
                        //null 表示没有找到
                        return null;
                    }

                    Story story = new Story();
                    story.sid = sid;
                    story.name = rs.getString("name");
                    story.createdAt = rs.getDate("created_at");
                    story.count = rs.getInt("count");
                    return story;
                }
            }
        }
    }

    public List<Story> selectListUsingAid(int aid) throws SQLException {
        List<Story> storyList = new ArrayList<>();
        try(Connection c = DBUtil.getConnection()){
            String sql = "select sid, name, created_at, count from story where aid = ? order by sid ASC";
            try (PreparedStatement s = c.prepareStatement(sql)){
                s.setInt(1,aid);
                try (ResultSet rs = s.executeQuery()){
                    while (rs.next()) {
                        Story story = new Story();
                        story.sid = rs.getInt("sid");
                        story.name = rs.getString("name");
                        story.createdAt = rs.getDate("created_at");
                        story.count = rs.getInt("count");

                        storyList.add(story);
                    }
                }
            }
        }
        return storyList;
    }

    public InputStream selectOneAudioColumnUsingSid(int sid) throws  SQLException{
        try(Connection c = DBUtil.getConnection()){
            String sql = "select audio from story where sid = ?";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                s.setInt(1, sid);
                try (ResultSet rs = s.executeQuery()) {
                    if(!rs.next()){
                        return null;
                    }

                    return rs.getBinaryStream("audio");
                }
            }
        }
    }
}

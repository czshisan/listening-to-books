package dao;

import model.Album;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDao {
    //根据一个专辑去查另一个专辑
    public Album selectOneUsingAid(int aid) throws SQLException {
        try (Connection c = DBUtil.getConnection()) {
            String sql = "select uid, name, cover, header, brief, created_at, count from album where aid = ?";
            try (PreparedStatement s = c.prepareStatement(sql)){
                s.setInt(1, aid);
                try(ResultSet rs = s.executeQuery()){
                    if(!rs.next()){
                        //null 表示没找到
                        return null;
                    }

                    Album album = new Album();
                    album.aid = aid;
                    album.uid = rs.getInt("uid");
                    album.name = rs.getString("name");
                    album.cover = rs.getString("cover");
                    album.count = rs.getInt("count");
                    album.header = rs.getString("header");
                    album.brief = rs.getString("brief");
                    album.createdAt = rs.getDate("created_at");
                    return album;
                }
            }
        }
    }

    public List<Album> selectListDefault() throws SQLException {
        List<Album> albumList = new ArrayList<>();

        try (Connection c = DBUtil.getConnection()) {
            String sql = "select aid, name, cover, count from album order by aid desc limit 20";
            try (PreparedStatement s = c.prepareStatement(sql)){
                try (ResultSet rs = s.executeQuery()){
                    while(rs.next()){
                        Album album = new Album();

                        album.aid = rs.getInt("aid");
                        album.name = rs.getString("name");
                        album.cover = rs.getString("cover");
                        album.count = rs.getInt("count");

                        albumList.add(album);
                    }
                }
            }
        }
        return albumList;
    }

    public  List<Album> selectListLikeName(String likeName) throws  SQLException {
        List<Album> albumList = new ArrayList<>();

        try (Connection c = DBUtil.getConnection()) {
            String sql = "select aid, name, cover, count from album where name like ? order by aid desc limit 20";
            try (PreparedStatement s = c.prepareStatement(sql)){
                s.setString(1,"%" + likeName + "%");
                try (ResultSet rs = s.executeQuery()){
                    while(rs.next()){
                        Album album = new Album();

                        album.aid = rs.getInt("aid");
                        album.name = rs.getString("name");
                        album.cover = rs.getString("cover");
                        album.count = rs.getInt("count");

                        albumList.add(album);
                    }
                }
            }
        }
        return albumList;
    }

    public int insert(int uid, String name, String brief, String cover, String header) throws SQLException{
        try (Connection c = DBUtil.getConnection()){
            String sql = "insert into album (uid, name, brief, cover, header) values (?, ?, ?, ?, ?)";
            try (PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                s.setInt(1,uid);
                s.setString(2,name);
                s.setString(3, brief);
                s.setString(4, cover);
                s.setString(5,header);

                s.executeUpdate();

                try(ResultSet rs = s.getGeneratedKeys()) {
                    rs.next();
                    return rs.getInt(1);
                }
            }
        }
    }

    public List<Album> selectListUsingUid(int uid) throws SQLException {
        List<Album> albumList = new ArrayList<>();
        String sql = "select aid, name, cover, count from album where uid = ? order by aid desc";
        try (Connection c = DBUtil.getConnection()) {
            try (PreparedStatement s = c.prepareStatement(sql)){
                s.setInt(1, uid);

                try (ResultSet rs = s.executeQuery()){
                    while (rs.next()) {
                        Album album = new Album();
                        album.aid = rs.getInt("aid");
                        album.name = rs.getString("name");
                        album.cover = rs.getString("cover");
                        album.count = rs.getInt("count");
                        albumList.add(album);
                    }
                }
            }
        }
        return albumList;
    }
}

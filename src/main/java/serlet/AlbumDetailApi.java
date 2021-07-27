package serlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Album;
import model.Story;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/album-detail.json")
public class AlbumDetailApi extends HttpServlet {
    public final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        String aid = req.getParameter("aid");
        if(aid == null || aid.trim().isEmpty()){
            //400 给出错误原因 请求方式不对
            resp.setStatus(400);
            Object result = new Object(){
                public final boolean success = false;
                public final String reason = "缺少必要参数: aid";
            };
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            writer.println(json);
            return;
        }

        int aidInt = Integer.parseInt(aid); //把 aid 转成 int 类型
        Album album = new Album();

        //进行查询
        //从 album 和 story 表中查询
        try (Connection c = DBUtil.getConnection()){
            {
                String sql = "select aid, name, cover, header, brief, created_at, count from album where aid = ?";
                try (PreparedStatement s = c.prepareStatement(sql)){
                    s.setInt(1, aidInt);
                    try(ResultSet rs = s.executeQuery()){
                        if(!rs.next()){
                            //404 aid对应的专辑不存在 给出原因
                            resp.setStatus(404);
                            Object result = new Object(){
                                public final boolean success = false;
                                public final String reason = "aid 没有对应的专辑";
                            };
                            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
                            writer.println(json);
                            return;
                        }

                        album.aid = aidInt;
                        album.name = rs.getString("name");
                        album.cover = rs.getString("cover");
                        album.count = rs.getInt("count");
                        album.header = rs.getString("header");
                        album.brief = rs.getString("brief");
                        album.createdAt = rs.getDate("created_at");
                        album.storyList = new ArrayList<>();
                    }
                }
            }

            {
                String sql = "select sid, name, created_at, count from story where aid = ? order by sid ASC";
                try (PreparedStatement s = c.prepareStatement(sql)){
                    s.setInt(1,aidInt);
                    try (ResultSet rs = s.executeQuery()){
                        while (rs.next()) {
                            Story story = new Story();
                            story.sid = rs.getInt("sid");
                            story.name = rs.getString("name");
                            story.createdAt = rs.getDate("created_at");
                            story.count = rs.getInt("count");

                            album.storyList.add(story);
                        }
                    }
                }
            }
        } catch (SQLException exc){
            //500
            exc.printStackTrace();
            resp.setStatus(500);
            Object result = new Object(){
                public final boolean success = false;
                public final String reason = exc.getMessage();
            };
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            writer.println(json);
            return;
        }

        Object result = new Object(){
            public final boolean success = true;
            public final Object data = album;
        };
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        writer.println(json);
    }
}

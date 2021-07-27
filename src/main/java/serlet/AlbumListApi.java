package serlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Album;
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
import java.util.List;

@WebServlet("/api/album-list.json")
public class AlbumListApi extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String keyword = req.getParameter("keyword"); //用户查询时输入的关键字
        if(keyword != null && keyword.trim().isEmpty()){
            keyword = null;
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        String sql;
        if(keyword != null){
            //有查询关键字
            sql = "select aid, name, cover, count from album where name like ? order by aid desc limit 20";
        } else {
            sql = "select aid, name, cover, count from album order by aid desc limit 20";
        }

        List<Album> albumList = new ArrayList<>(); //存贮专辑列表
        try (Connection c = DBUtil.getConnection()) {
            try (PreparedStatement s = c.prepareStatement(sql)){
                if(keyword != null){
                    //SQL: WHERE name LIKE %hello%  只要名字中有hello就匹配
                    s.setString(1,"%" + keyword + "%");
                }

                //执行查询

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

            Object result = new Object(){
                public final boolean success = true;
                public final Object data = albumList;
            };
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            writer.println(json);
        } catch (SQLException exc){
            exc.printStackTrace();

            Object result = new Object(){
                public final boolean success = false;
                public final String reason = exc.getMessage();
            };
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            writer.println(json);
        }
    }
}

package serlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Album;
import service.AlbumService;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/album-list.json")
public class AlbumListApi extends HttpServlet {
    private final AlbumService albumService = new AlbumService();
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

        try {
            List<Album> albumList = albumService.latestAlbumList(keyword); //存贮专辑列表
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

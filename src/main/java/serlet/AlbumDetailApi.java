package serlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Album;
import service.AlbumService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/album-detail.json")
public class AlbumDetailApi extends HttpServlet {
    private final AlbumService albumService = new AlbumService();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        //Album album = new Album();

        //进行查询
        //从 album 和 story 表中查询
        try {
            Album album = albumService.detail(aidInt);
            if(album == null){
                //404 给出错误原因 请求方式不对
                resp.setStatus(404);
                Object result = new Object(){
                    public final boolean success = false;
                    public final String reason = "aid 对应的专辑不存在";
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
    }
}

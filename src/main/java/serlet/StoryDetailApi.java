package serlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.client.dispatch.PacketDispatch;
import model.Story;
import service.StoryService;
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

@WebServlet("/api/story-detail.json")
public class StoryDetailApi extends HttpServlet {
    private final StoryService storyService = new StoryService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        String sid = req.getParameter("sid");
        if(sid == null || sid.trim().isEmpty()){
            //400
            return;
        }

        int sidInt = Integer.parseInt(sid);
        try {
            Story story = storyService.detail(sidInt);
            if(story == null){
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

            //正常处理
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

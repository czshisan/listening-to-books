package serlet;

import service.StoryService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@WebServlet("/get-audio")
public class GetAudioServlet extends HttpServlet {
    private final StoryService storyService = new StoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sid = req.getParameter("sid");
        if(sid == null || sid.trim().isEmpty()) {
            resp.sendError(400, "参数 sid 是必须的");
            return;
        }

        int sidInt = Integer.parseInt(sid);
        try {
            InputStream is = storyService.getAudio(sidInt);
            if(is == null) {
                resp.sendError(404, "sid 对应的声音不存在");
                return;
            }

            //进行声音输出：按照二进制方式把声音写入 resp 的输出流中
            ServletOutputStream os = resp.getOutputStream();
            byte[] buf = new byte[1024];
            int len;

            while(true) {
                len = is.read(buf);
                if (len == -1) {
                    break;
                }

                os.write(buf, 0, len);
            }
            os.flush();
            is.close();
        } catch (SQLException exc) {
            throw new ServletException(exc);
        }
    }
}

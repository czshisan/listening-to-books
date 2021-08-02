package servlet;

import model.User;
import service.AlbumService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.SQLException;
import java.util.UUID;

@MultipartConfig
@WebServlet("/new-album")
public class NewAlbumServlet extends HttpServlet {
    private final AlbumService albumService = new AlbumService();
    private ServletContext servletContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        servletContext = config.getServletContext();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User)session.getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendError(401, "必须先登录");
            return;
        }

        req.setCharacterEncoding("utf-8");
        String name = req.getParameter("name");
        String brief = req.getParameter("brief");

        Part coverPart = req.getPart("cover");
        Part headerPart = req.getPart("header");

        String coverPath = saveImage(coverPart);
        String headerPath = saveImage(headerPart);

        int aid = 0;
        try {
            aid = albumService.save(currentUser.uid, name, brief, coverPath, headerPath);
            resp.sendRedirect("/album-editor.html?aid=" + aid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    private String saveImage(Part part) throws IOException {
        //把图片保存成文件， 保存在 target/...-1.0-SNAPSHOT/img/
        //保存在这里，图片可以立即被访问
        //缺点：一旦删除了 target， 这些图片就全部 404 了

        //图片名称，使用UUID，保证不重复，图片后缀，使用 submittedFilename中的后缀
        //理论上有安全问题
        String submittedFileName = part.getSubmittedFileName();
        int i = submittedFileName.lastIndexOf('.');
        String extension = submittedFileName.substring(i);
        String uuid = UUID.randomUUID().toString();
        String filename = "/img/" + uuid + extension;

        String realPath = servletContext.getRealPath(filename);
        try (OutputStream os = new FileOutputStream(realPath)) {
            byte[] buf = new byte[1024];
            int len;
            InputStream is = part.getInputStream();
            while (true) {
                len = is.read(buf);
                if (len == -1) {
                    break;
                }

                os.write(buf, 0, len);
            }
            os.flush();
        }

        return filename;
    }
}

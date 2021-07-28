package api;

import model.Album;
import service.AlbumService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/api/album-detail.json")
public class AlbumDetailApi extends AbsApiServlet {
    private final AlbumService albumService = new AlbumService();

    @Override
    protected Object doGetInternal(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ApiException {

        String aid = req.getParameter("aid");
        if (aid == null || aid.trim().isEmpty()) {
            throw new ApiException(400, "参数 aid 是必须的");
        }

        int aidInt = Integer.parseInt(aid); //把 aid 转成 int 类型
        Album album = albumService.detail(aidInt);
        if (album == null) {
            throw new ApiException(404, "aid 对应的专辑不存在");
        }
        return album;
    }
}

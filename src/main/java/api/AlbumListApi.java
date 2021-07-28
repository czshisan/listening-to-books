package api;


import service.AlbumService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/api/album-list.json")
public class AlbumListApi extends AbsApiServlet {
    private final AlbumService albumService = new AlbumService();

    @Override
    protected Object doGetInternal(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ApiException {
        String keyword = req.getParameter("keyword"); //用户查询时输入的关键字
        if(keyword != null && keyword.trim().isEmpty()){
            keyword = null;
        }

        return albumService.latestAlbumList(keyword);
    }
}

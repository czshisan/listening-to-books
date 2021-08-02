package api;

import model.User;
import service.AlbumService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/api/my-album-list.json")
public class MyAlbumListApi extends AbsApiServlet {
    private final AlbumService albumService = new AlbumService();

    @Override
    protected Object doGetInternal(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ApiException {
        //1.先获取当前登录用户
        User currentUser = (User)req.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            throw new ApiException(401, "必须是登陆后才能使用");
        }

        //2.根据当前用户，获取当前用户专辑列表
        return albumService.listUsingUid(currentUser.uid);
    }
}

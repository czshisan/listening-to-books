package serlet;

import com.sun.xml.internal.ws.client.dispatch.PacketDispatch;
import model.Story;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/api/story-detail.json")
public class StoryDetailApi extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sid = req.getParameter("sid");
        if(sid == null || sid.trim().isEmpty()){
            //400
            return;
        }

        int sidInt = Integer.parseInt(sid);
        Story story = new Story();

        try(Connection c = DBUtil.getConnection()){
            String sql = "select sid, name, created_at, count from story where sid = ?";
            try(PreparedStatement s = c.prepareStatement(sql)){
                s.setInt(1, sidInt);

                try(ResultSet rs = s.executeQuery()){
                    if(!rs.next()){
                        //404
                        return;
                    }

                    story.sid = sidInt;
                    story.name = rs.getString("name");
                    story.createdAt = rs.getDate("created_at");
                    story.count = rs.getInt("count");
                }
            }
        } catch (SQLException exc){
            //500
        }

        //JSON
    }
}

package servlet;

import model.User;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.读取用户填写的信息
        req.setCharacterEncoding("utf-8");
        String username = req.getParameter("username");
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");

        //数据库中不保存明文密码，把密码利用 SHA-512 算法做哈希

        //2.进行用户注册——插入数据库表

        //处理密码存储格式
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] digest = messageDigest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : digest){
                sb.append(String.format("%02x",b));
            }
            password = sb.toString();

            User user = new User();
            user.username = username;
            user.nickname = nickname;
            String sql = "insert into user (username,nickname,password) values (?, ?, ?)";
            try(Connection c = DBUtil.getConnection()){
                try(PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    s.setString(1,username);
                    s.setString(2,nickname);
                    s.setString(3,password);

                    s.executeUpdate();

                    //获取插入后的自增UID
                    try(ResultSet rs = s.getGeneratedKeys()){
                        rs.next();
                        user.uid = rs.getInt(1);
                    }
                }
            } catch (SQLException exc) {
                throw new ServletException(exc);
            }

            //设定用户登录——把当前登陆的用户写到 session 中
            HttpSession session = req.getSession();
            session.setAttribute("currentUser",user);

            //执行重定向
            resp.sendRedirect("/");
        } catch (NoSuchAlgorithmException exc) {
            throw new ServletException(exc);
        }

    }
}

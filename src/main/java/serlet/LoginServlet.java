package serlet;

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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取用户输入
        req.setCharacterEncoding("utf-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //2.给明文密码做 哈希
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] digest = messageDigest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            password = sb.toString();

            //3.去数据库中执行查询 WHERE username= ？ AND password = ?
            // username是唯一键
            //所以，如果用户名密码正确，可以查到一条信息
            //如果用户名密码中有1个错误，一条信息都查不到

            String sql = "select uid, nickname from user where username = ? and password = ?";
            User user = new User();
            try (Connection c = DBUtil.getConnection()) {
                try (PreparedStatement s = c.prepareStatement(sql)) {
                    s.setString(1, username);
                    s.setString(2, password);

                    try (ResultSet rs = s.executeQuery()) {
                        if (!rs.next()) {
                            //一条信息都没有，说明登陆失败
                            resp.sendRedirect("/login.html");
                            return;

                        }
                        user.uid = rs.getInt("uid");
                        user.nickname = rs.getString("nickname");
                    }
                }
            }

            //设置登录
            HttpSession session = req.getSession();
            session.setAttribute("currentUser",user);

            //执行重定向
            resp.sendRedirect("/");
        }catch(SQLException exc){
            throw new ServletException(exc);
        } catch (NoSuchAlgorithmException exc) {
            throw new ServletException(exc);
        }
    }
}

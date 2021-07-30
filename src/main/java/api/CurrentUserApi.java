package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/current-user.json")
public class CurrentUserApi extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //输出JSON格式，Content-Type:application/json
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        //判断用户是否已经登陆——通过session获取当前已经登陆的用户信息
        //如果无法获取，则用户未登录
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        Object result;
        if(currentUser == null){
            //无法从session中获得当前用户，代表没有登录

            //这个对象是要被序列化成 JSON 对象
            //使用匿名类的好处，可以省略类的定义，减少类的定义
            result = new Object(){
                public final boolean logged = false;
            };
        } else {
            //已经登录，当前是用户CurrentUser
            result = new Object(){
                public final boolean logged = true;
                public final User user = currentUser;
            };
        }
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        writer.println(json);
    }
}

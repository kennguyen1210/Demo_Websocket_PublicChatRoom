package ra.demowebsocket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model){
        String username = (String) request.getSession().getAttribute("username");
        if(username == null || username.isEmpty()){
            return "redirect:/login";
        }
        model.addAttribute("username",username);
        return "chat";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @PostMapping("/login")
    public String doLogin(HttpServletRequest request, @RequestParam(defaultValue = "") String username){
        username = username.trim();
        if(username.isEmpty()){
            return "login";
        }
        request.getSession().setAttribute("username",username);
        return "redirect:/";
    }
    @RequestMapping("/logout")
    public String logOut(HttpServletRequest request){
        request.getSession(true).invalidate();
        return "redirect:/login";
    }
}

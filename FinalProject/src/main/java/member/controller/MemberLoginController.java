package member.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import member.model.MemberBean;
import member.model.MemberDao;

@Controller
public class MemberLoginController {
    
    private final String command = "/loginForm.mb";
    private final String getPage = "userLoginForm";
    private final String gotoPage = "loginSuccess";
    private final String my_page="memberMypage";
    
    @Autowired
    MemberDao memberDao;
    
    @RequestMapping(value = command, method = RequestMethod.GET)
    public String loginForm() {
        return getPage;
    }
    
    @RequestMapping(value = command, method = RequestMethod.POST)
    public ModelAndView loginForm(MemberBean member, HttpSession session, HttpServletResponse response,
            Model model, @RequestParam(value = "pageNumber", required = false) String pageNumber) {
        System.out.println(this.getClass() + " Post : " + member.getUser_email() + " / " + pageNumber);
        
        ModelAndView mav = new ModelAndView();
        
        MemberBean mb = memberDao.getMemberByEmail(member.getUser_email());
        System.out.println("mb:" + mb);
        
        try {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            if (mb == null) { // �ش� ���̵� �������� �ʴ´�.
                out.println("<script>");
                out.println("alert('�ش� ���̵�� �������� �ʽ��ϴ�.');");
                out.println("</script>");
                out.flush();
                return new ModelAndView(getPage);
                
            } else { // �ش� ���̵� �����Ѵ�.
                if ("Y".equals(mb.getUser_status())) { // ���� ������ ���
                    out.println("<script>");
                    out.println("alert('�ش� ������ ���� �����Դϴ�. �����ڿ��� �������ּ���.');");
                    out.println("</script>");
                    out.flush();
                    return new ModelAndView(getPage);
                }
                
                if (mb.getUser_passwd().equals(member.getUser_passwd())) { // ��й�ȣ ��ġ
                    session.setAttribute("loginInfo", mb); // loginInfo: �α����� ����� ����
                    System.out.println("��й�ȣ ��ġ");
                    System.out.println("destination:" + (String) session.getAttribute("destination"));
                    
                    model.addAttribute("pageNumber", pageNumber);
                    mav.setViewName(gotoPage);
                } else { // ��й�ȣ ����ġ
                    System.out.println("��й�ȣ ����ġ");
                    out.println("<script>");
                    out.println("alert('��й�ȣ�� ��ġ���� �ʽ��ϴ�.');");
                    out.println("</script>");
                    out.flush();
                    return new ModelAndView(getPage);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return mav;
    }
   /*
    * @RequestMapping(value = "/memberMypage", method = RequestMethod.GET) public
    * String myPage(HttpSession session, Model model) { // ���ǿ��� �α��� ���� ��������
    * MemberBean loginInfo = (MemberBean) session.getAttribute("loginInfo"); if
    * (loginInfo == null) { // �α��� ������ ������ �α��� �������� redirect return "redirect:" +
    * command; }
    * 
    * // �α��� ������ ������ ������������ �̵� model.addAttribute("user_id",
    * loginInfo.getUser_id()); return my_page; }
    */
}

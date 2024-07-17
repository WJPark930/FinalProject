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
	private final String gotoPage = "../board/CommunityMain";
	
	@Autowired
	MemberDao memberDao;
	//��ǰ�߰��ϱ� ��ư�� �����µ� �α��� ������ ���� �� 
	// productList.jsp=>�߰��ϱ�=>ProductInsertController=>redirect:/loginForm.mb
	@RequestMapping(value=command, method=RequestMethod.GET)
	public String loginForm() {
		return getPage;
	}
	
	
	//post��û�Ѱ��� member/memberLoginForm.jsp���� �α��� Ŭ��������(id,password)
	@RequestMapping(value=command, method=RequestMethod.POST)
	public ModelAndView loginForm(MemberBean member, HttpSession session,HttpServletResponse response,
			Model model,
			@RequestParam(value = "pageNumber", required = false ) String pageNumber) {
		System.out.println(this.getClass() + " Post : " + member.getUser_email() + " / "+ pageNumber);
		
		ModelAndView mav = new ModelAndView();

		MemberBean mb = memberDao.getMemberByEmail(member.getUser_email());  
		//System.out.println("mb:" + mb);

		try {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(mb == null){ // �ش� ���̵� �������� �ʴ´�.
				out.println("<script>");
				out.println("alert('�ش� ���̵�� �������� �ʽ��ϴ�.');");
				out.println("</script>");
				out.flush();
				//mav.setViewName(getPage);
				return new ModelAndView( getPage ) ;
				
			}else{ // �ش� ���̵� �����Ѵ�.
				if(mb.getUser_passwd().equals(member.getUser_passwd())) { // ��� ��ġ
					
					session.setAttribute("loginInfo", mb); // loginInfo:�α����� ����� ����
					System.out.println("��� ��ġ");
					System.out.println("destination:"+(String)session.getAttribute("destination"));
				
					//destination:redirect:/insert.prd
					
					//destination:redirect:/update.prd?num=13
					
					//destination:"redirect:/detail.prd" =>productDetialController=>productDetail.jsp(p,w,k)
					
				//	mav.setViewName(( ModelAndView( (String)session.getAttribute("destination") ) ;
					
					model.addAttribute("pageNumber", pageNumber);
					mav.setViewName(gotoPage);
					//return new ModelAndView((String)session.getAttribute("destination")) ;

					
				}else { // ��� ����ġ
					System.out.println("��� ����ġ");
					out.println("<script>");
					out.println("alert('��� ����ġ�Դϴ�.');");
					out.println("</script>");
					out.flush();
					return new ModelAndView( getPage ) ;
				}
			}

		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return mav;
	}
}
package community.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import community.model.BoardBean;
import community.model.BoardDao;
import utility.BoardPaging;

@Controller
public class BoardWriteController {
	private final String command = "/write.bd";
	private final String getPage = "WriteForm";
	private final String gotoPage = "redirect:/list.bd";

	@Autowired
	BoardDao bdao;

	@Autowired
	ServletContext servletContext;

	@RequestMapping(value = command, method = RequestMethod.GET)
	public String doAction(
			@RequestParam(value="whatColumn", required=false) String whatColumn,
			@RequestParam(value="keyword", required=false) String keyword,
			@RequestParam(value="pageNumber", required=false) String pageNumber,
			HttpSession session,
			HttpServletRequest request,
			Model model) {
		if (session.getAttribute("loginInfo") == null) {
			session.setAttribute("destination", "redirect:/write.bd");
			return "redirect:/loginForm.mb";
		} else {
			Map<String, String> map = new HashMap<String, String>();
	        map.put("whatColumn", whatColumn);
	        map.put("keyword", "%" + keyword + "%");

	        int totalCount = bdao.getArticleCount(map);
	        String url = request.getContextPath() + this.command;

	        BoardPaging pageInfo = new BoardPaging(pageNumber, null, totalCount, url, whatColumn, keyword);

	        List<BoardBean> BoardLists = bdao.getBoardList(map, pageInfo);
	        model.addAttribute("BoardLists", BoardLists);
	        model.addAttribute("pageInfo", pageInfo);
			
			return getPage;
		}
	}

	@RequestMapping(value = command, method = RequestMethod.POST)
	public ModelAndView doAction(@ModelAttribute("board") @Valid BoardBean board, BindingResult result,
			RedirectAttributes redirectAttributes) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			mav.setViewName(getPage);
			return mav;
		}

		// ���� ���ε� �� ���� ����
		MultipartFile multi = board.getUpload();
		if (multi != null && !multi.isEmpty()) {
			String uploadPath = servletContext.getRealPath("/") + "resources/BoardImage/";
			File destination = new File(uploadPath + multi.getOriginalFilename());
			try {
				multi.transferTo(destination);
				board.setImageFilename(multi.getOriginalFilename());
				System.out.println("���� ���ε� ����: " + destination.getAbsolutePath());
				System.out.println("Image Filename: " + board.getImageFilename());
			} catch (IOException e) {
				e.printStackTrace();
				mav.setViewName(getPage);
				return mav;
			}
		}

		int cnt = bdao.insertArticle(board);
		if (cnt != -1) {
			System.out.println("�Խù� ���� ����");
			System.out.println("Image Filename: " + board.getImageFilename());
			System.out.println("Content: " + board.getContent());
			mav.setViewName(gotoPage);
		} else {
			System.out.println("�Խù� ���� ����");
			mav.setViewName(getPage);
		}
		return mav;
	}


}
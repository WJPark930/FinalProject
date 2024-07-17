package community.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import community.model.Nested_Comment_RecommendBean;
import community.model.Nested_Comment_RecommendDao;
import member.model.MemberBean;

@Controller
@RequestMapping("/nested_comment")
public class BoardNested_CommentRecommendController {

    @Autowired
    Nested_Comment_RecommendDao ncDao;

    @PostMapping(value = "/nestedcommentrecommend.bd")
    @ResponseBody
    public ResponseEntity<Integer> recommendNestedComment(@RequestParam("nested_comment_id") int nested_comment_id, HttpSession session) {
        MemberBean loginInfo = (MemberBean) session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return ResponseEntity.status(401).body(null); // �α��� �ʿ�
        }
        
//        System.out.println("���� ��õ������ �Ѿ���� login���� : " + loginInfo.getUser_id());
//        System.out.println("���� ��õ ������ �Ѿ���� ���� ���� : " + nested_comment_id);
        
        int user_id = loginInfo.getUser_id();
        Nested_Comment_RecommendBean ncBean = new Nested_Comment_RecommendBean(user_id,nested_comment_id);

        // ����ڰ� �̹� �ش� ����� ��õ�ߴ��� üũ
        int userRecommendCheck = ncDao.checkUserRecommend(user_id , nested_comment_id);
        if (userRecommendCheck > 0) {
            return ResponseEntity.status(409).body(null); // �̹� ��õ�� ���
        }

        // ��� ��õ ���� ����
        int cnt = ncDao.insertRecommend(ncBean);
        if (cnt != -1) {
            // ��õ �� ��ȸ �� ��ȯ
            int recommendCount = ncDao.getRecommendCount(nested_comment_id);
            return ResponseEntity.ok(recommendCount);
        } else {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @GetMapping(value = "/getRecommendCount.bd") //���� ��õ�� �������� ��û
    @ResponseBody
    public ResponseEntity<Integer> getRecommendCount(@RequestParam("nested_comment_id") int nested_comment_id) {
        try {
            int recommendCount = ncDao.getRecommendCount(nested_comment_id);
            return ResponseEntity.ok(recommendCount);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(0);// �߸��� ��� ID
        }
    }
}

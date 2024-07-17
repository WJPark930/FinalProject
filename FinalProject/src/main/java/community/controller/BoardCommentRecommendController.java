package community.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import community.model.Comment_RecommendBean;
import community.model.Comment_RecommendDao;
import member.model.MemberBean;

@Controller
@RequestMapping("/comment")
public class BoardCommentRecommendController {

    @Autowired
    Comment_RecommendDao crDao;

    @PostMapping(value = "/commentrecommend.bd")
    @ResponseBody
    public ResponseEntity<Integer> recommendComment(@RequestParam("comment_id") String comment_id_str,
                                                    HttpSession session) {
        try {
            int comment_id = Integer.parseInt(comment_id_str);
            MemberBean loginInfo = (MemberBean) session.getAttribute("loginInfo");
            if (loginInfo == null) {
                return ResponseEntity.status(401).body(null); // �α��� �ʿ�
            }

            int user_id = loginInfo.getUser_id();
            Comment_RecommendBean crBean = new Comment_RecommendBean(comment_id, user_id);

            // ����ڰ� �̹� �ش� ����� ��õ�ߴ��� üũ
            int userRecommendCheck = crDao.checkUserRecommend(comment_id, user_id);
            if (userRecommendCheck > 0) {
                return ResponseEntity.status(409).body(null); // �̹� ��õ�� ���
            }

            // ��� ��õ ���� ����
            int cnt = crDao.insertRecommend(crBean);
            if (cnt != -1) {
                // ��õ �� ��ȸ �� ��ȯ
                int recommendCount = crDao.getRecommendCount(comment_id);
                return ResponseEntity.ok(recommendCount);
            } else {
                return ResponseEntity.status(500).body(null);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);// �߸��� ��� ID
        }
    }

    @GetMapping(value = "/getRecommendCount.bd")
    @ResponseBody
    public ResponseEntity<Integer> getRecommendCount(@RequestParam("comment_id") String comment_id_str) {
        try {
            int comment_id = Integer.parseInt(comment_id_str);
            int recommendCount = crDao.getRecommendCount(comment_id);
            return ResponseEntity.ok(recommendCount);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(0);// �߸��� ��� ID
        }
    }
    
    
}

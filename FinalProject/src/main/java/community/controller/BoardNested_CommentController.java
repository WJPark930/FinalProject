package community.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import community.model.Nested_CommentBean;
import community.model.Nested_CommentDao;
import member.model.MemberBean;

@Controller
@RequestMapping("/nested_comment")
public class BoardNested_CommentController {

    @Autowired
    private Nested_CommentDao nestedCommentDao;

    @PostMapping("/add.bd")
    @ResponseBody
    public ResponseEntity<?> addNestedComment(
            @RequestParam("content") String content,
            @RequestParam("parent_id") int parent_id,
            @RequestParam("user_id") int user_id,
            HttpSession session) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        MemberBean loginInfo = (MemberBean) session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("�α����� �ʿ��մϴ�.");
        }

        Nested_CommentBean nestedComment = new Nested_CommentBean();
        nestedComment.setUser_id(user_id);
        nestedComment.setContent(content);
        nestedComment.setComment_id(parent_id);
        nestedComment.setCreated_at(new Date());
        nestedComment.setUpdated_at(new Date());

        try {
            nestedCommentDao.insertNestedComment(nestedComment);
            return ResponseEntity.ok("������ ���������� �ۼ��Ǿ����ϴ�.");
        } catch (Exception e) {
            logger.error("���� �ۼ� �� ���� �߻�: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("���� �ۼ��� �����߽��ϴ�.");
        }
    }

    @GetMapping("/list.bd")
    @ResponseBody
    public ResponseEntity<?> listNestedComments(@RequestParam("parent_id") int parent_id) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        try {
            List<Nested_CommentBean> nestedComments = nestedCommentDao.selectNestedCommentsByCommentId(parent_id);
            return ResponseEntity.ok(nestedComments);
        } catch (Exception e) {
            logger.error("���� ��� ��ȸ �� ���� �߻�: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("���� ��� ��ȸ�� �����߽��ϴ�.");
        }
    }
    
    
    @PostMapping("/delete.bd") //deleteMapping ���!
    @ResponseBody
    public ResponseEntity<String> deleteComment(@RequestParam("commentId") int commentId,
                                                HttpSession session) {
//      System.out.println("�Ѿ���� ���� id : " + commentId);
        // �α��� ���� ��������
        MemberBean loginInfo = (MemberBean) session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("�α����� �ʿ��մϴ�.");
        }

        // ���� �ۼ����� user_id ��������
        int NestedCommentUserId = nestedCommentDao.getNestedCommentUserId(commentId);

        // ���� �α����� ������� user_id
        int currentUserId = loginInfo.getUser_id();

        // ���� Ȯ��
        if (currentUserId == NestedCommentUserId) {
            // ���� ���� ����
            int deleteResult = nestedCommentDao.deleteNestedComment(commentId);
            if (deleteResult > 0) {
                return ResponseEntity.ok("������ �����Ǿ����ϴ�.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("��� ������ �����߽��ϴ�.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("��� �ۼ��ڸ� ������ �� �ֽ��ϴ�.");
        }
    }

}

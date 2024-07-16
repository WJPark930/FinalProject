package community.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import community.model.CommentBean;
import community.model.CommentDao;
import community.model.Comment_RecommendDao;
import member.model.MemberBean;

@RestController
@RequestMapping("/comment")
public class BoardCommentController {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private Comment_RecommendDao crDao;

    @PostMapping("/add.bd")
    public CommentBean addComment(
            @RequestParam("bid") int boardId,
            @RequestParam("content") String content,
            @RequestParam("userId") String userId) {

        CommentBean newComment = new CommentBean();
        newComment.setBoard_id(boardId);
        newComment.setContent(content);
        newComment.setUser_id(Integer.parseInt(userId));
        newComment.setCreated_at(new Date());
        newComment.setUpdated_at(new Date());

        commentDao.addComment(newComment);

        // �߰��� ��� ��ü�� Ŭ���̾�Ʈ�� ��ȯ
        return newComment;
    }

    @GetMapping("/list.bd")
    public ResponseEntity<List<Map<String, Object>>> getComments(@RequestParam("bid") int boardId) {
        // Ư�� �Խñ�(boardId)�� ���� ��� ����� ������
        List<CommentBean> comments = commentDao.getCommentsByBoardId(boardId);

        // �� ����� ��õ ���� �����ͼ� Map ���·� ��ȯ
        List<Map<String, Object>> commentsWithRecommendCount = new ArrayList<Map<String, Object>>();
        for (CommentBean comment : comments) {
            int recommendCount = crDao.getRecommendCount(comment.getId());

            Map<String, Object> commentMap = new HashMap<String, Object>();
            commentMap.put("id", comment.getId());
            commentMap.put("content", comment.getContent());
            commentMap.put("user_email", comment.getUser_email());
            commentMap.put("created_at", comment.getCreated_at());
            commentMap.put("updated_at", comment.getUpdated_at());
            commentMap.put("recommend_count", recommendCount);

            commentsWithRecommendCount.add(commentMap);
        }

        // ��� ��ϰ� �� ����� ��õ ���� ResponseEntity�� ��ȯ
        return ResponseEntity.ok().body(commentsWithRecommendCount);
    }
    
    @PostMapping("/delete.bd")
    @ResponseBody
    public ResponseEntity<String> deleteComment(@RequestParam("comment_id") int commentId,
                                                HttpSession session) {
        // �α��� ���� ��������
        MemberBean loginInfo = (MemberBean) session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("�α����� �ʿ��մϴ�.");
        }

        // ��� �ۼ����� user_id ��������
        int boardUserId = commentDao.getCommentUserId(commentId);

        // ���� �α����� ������� user_id
        int currentUserId = loginInfo.getUser_id();

        // ���� Ȯ��
        if (currentUserId == boardUserId) {
            // ���� ���� ����
            int deleteResult = commentDao.deleteComment(commentId);
            if (deleteResult > 0) {
                return ResponseEntity.ok("����� �����Ǿ����ϴ�.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("��� ������ �����߽��ϴ�.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("��� �ۼ��ڸ� ������ �� �ֽ��ϴ�.");
        }
    }
}

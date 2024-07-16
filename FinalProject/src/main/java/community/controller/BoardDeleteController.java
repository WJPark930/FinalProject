package community.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import community.model.BoardDao;

@Controller
public class BoardDeleteController {
    private final String command = "/delete.bd";
    
    @Autowired
    BoardDao bdao;

    @RequestMapping(value = command, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doAction(@RequestParam(value="bid", required=true) int bid,
                                        @RequestParam(value="passwd", required=true) String passwd,
                                        @RequestParam(value="pageNumber", required=false) String pageNumber) {
        Map<String, Object> response = new HashMap<String, Object>();
        
        int result = bdao.deleteArticle(bid, passwd);
        
        if (result == 1) { // ��й�ȣ�� ��ġ�ϰ� ���� ����
            response.put("success", true);
        } else { // ��й�ȣ ����ġ �Ǵ� ���� ����
            response.put("success", false);
        }

        return response;
    }
}

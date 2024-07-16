package community.model;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class BoardBean {
    private int bid;
    private int user_id;
    private String user_email;
    private int cate_id;
    private Date created_at;
    private Date updated_at;
    @NotEmpty(message = "������ �Է��ϼ���")
    private String title;
    @NotEmpty(message = "������ �Է��ϼ���")
    private String content;
    private int readcount;
    @NotEmpty(message = "��й�ȣ�� �Է��ϼ���")
    private String passwd;

    // �߰��� �ʵ�
    private MultipartFile upload;
    private String imageFilename;

    // �⺻ ������
    public BoardBean() {
        super();
    }

    // �Ű������� �޴� ������
    public BoardBean(int bid, int user_id, String user_email, int cate_id, Date created_at, Date updated_at,
                     String title, String content, int readcount, String passwd, String imageFilename) {
        super();
        this.bid = bid;
        this.user_id = user_id;
        this.user_email = user_email;
        this.cate_id = cate_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.title = title;
        this.content = content;
        this.readcount = readcount;
        this.passwd = passwd;
        this.imageFilename = imageFilename;
    }

    // getters and setters
    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReadcount() {
        return readcount;
    }

    public void setReadcount(int readcount) {
        this.readcount = readcount;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public MultipartFile getUpload() {
        return upload;
    }

    public void setUpload(MultipartFile upload) {
        this.upload = upload;
        if (upload != null) {
            this.imageFilename = upload.getOriginalFilename();
        }
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }
}

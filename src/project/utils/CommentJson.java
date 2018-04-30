package project.utils;

import java.util.Date;
import project.models.Comment;

public class CommentJson {
	private int commentid;
	private String content;
	private Date created;
	private UserJson user;
	
	public CommentJson(Comment comment) {
		this.setCommentid(comment.getCommentid());
		this.setContent(comment.getContent());
		this.setCreated(new Date(comment.getCreated().getTime()));
		this.setUser(new UserJson(comment.getUser()));
	}
	
	public UserJson getUser() {
		return user;
	}
	public void setUser(UserJson user) {
		this.user = user;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCommentid() {
		return commentid;
	}
	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}
}

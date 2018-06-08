package project.dto;

import java.util.Date;
import project.models.Comment;

public class CommentDto {
	private int commentid;
	private String content;
	private Date created;
	private UserDto user;
	
	public CommentDto(Comment comment) {
		this.setCommentid(comment.getCommentid());
		this.setContent(comment.getContent());
		this.setCreated(new Date(comment.getCreated().getTime()));
		this.setUser(new UserDto(comment.getUser()));
	}
	
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
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

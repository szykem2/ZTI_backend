package project.dto;

import java.util.Date;
import project.models.Comment;

/**
 * Klasa s�u��ca do przesy�ania obiektu komentarza
 */
public class CommentDto {
	/**
	 * ID komentarza
	 */
	private int commentid;
	
	/**
	 * Zawarto�� komentarza
	 */
	private String content;
	
	/**
	 * Data utworzenia
	 */
	private Date created;
	
	/**
	 * Autor komentarza
	 */
	private UserDto user;
	
	/**
	 * Konstruktor obiektu
	 * @param comment obiekt komentarza, kt�ry ma zosta� przes�any
	 * @see project.models.Comment
	 */
	public CommentDto(Comment comment) {
		this.setCommentid(comment.getCommentid());
		this.setContent(comment.getContent());
		this.setCreated(new Date(comment.getCreated().getTime()));
		this.setUser(new UserDto(comment.getUser()));
	}
	
	/**
	 * {@link CommentDto#user}
	 */
	public UserDto getUser() {
		return user;
	}
	
	/**
	 * {@link CommentDto#user}
	 */
	public void setUser(UserDto user) {
		this.user = user;
	}

	/**
	 * {@link CommentDto#created}
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * {@link CommentDto#created}
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * {@link CommentDto#content}
	 */
	public String getContent() {
		return content;
	}

	/**
	 * {@link CommentDto#content}
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * {@link CommentDto#commentid}
	 */
	public int getCommentid() {
		return commentid;
	}

	/**
	 * {@link CommentDto#commentid}
	 */
	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}
}

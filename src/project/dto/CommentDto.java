package project.dto;

import java.sql.Timestamp;
import java.util.Date;
import project.models.Comment;
import project.models.Itemstatus;

/**
 * Klasa służąca do przesyłania obiektu komentarza
 */
public class CommentDto {
	/**
	 * ID komentarza
	 */
	private int commentid;
	
	/**
	 * Zawartość komentarza
	 */
	private String content;
	
	/**
	 * Data utworzenia
	 */
	private Timestamp created;
	
	/**
	 * Autor komentarza
	 */
	private UserDto user;
	
	/**
	 * Konstruktor obiektu
	 * @param comment obiekt komentarza, który ma zostać przesłany
	 * @see project.models.Comment
	 */
	public CommentDto(Comment comment) {
		this.setCommentid(comment.getCommentid());
		this.setContent(comment.getContent());
		this.setCreated(new Timestamp(comment.getCreated().getTime()));
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
	public Timestamp getCreated() {
		return created;
	}

	/**
	 * {@link CommentDto#created}
	 */
	public void setCreated(Timestamp created) {
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

	/**
	 * Metoda służy do porównywania ze sobą obiektów typu CommentDto
	 * @param obj obiekt, który chcemy porównać do aktualnego
	 */
	@Override
	public boolean equals(Object obj) {
		return this.commentid == ((CommentDto)obj).getCommentid();
	}
}
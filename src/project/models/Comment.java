package project.models;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

/**
 * Klasa trwa³oœci dla tabeli COMMENTS w bazie danych
 */
@Entity
@Table(name="COMMENTS")
@NamedQuery(name="Comment.findAll", query="SELECT c FROM Comment c")
public class Comment implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID komentarza
	 */
	private int commentid;
	
	/**
	 * Zawartoœæ komentarza
	 */
	private String content;
	
	/**
	 * Czas utworzenia
	 */
	private Timestamp created;
	
	/**
	 * Element, do którego przypisany jest komentarz
	 */
	private Item item;
	
	/**
	 * Autor komentarza
	 */
	private User user;

	public Comment() {
	}

	/**
	 * {@link Comment#commentid}
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getCommentid() {
		return this.commentid;
	}

	/**
	 * {@link Comment#commentid}
	 */
	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}

	/**
	 * {@link Comment#commentid}
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * {@link Comment#commentid}
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * {@link Comment#commentid}
	 */
	public Timestamp getCreated() {
		return this.created;
	}

	/**
	 * {@link Comment#commentid}
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}

	/**
	 * {@link Comment#commentid}
	 */
	@ManyToOne
	@JoinColumn(name="ITEMID")
	public Item getItem() {
		return this.item;
	}

	/**
	 * {@link Comment#commentid}
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * {@link Comment#commentid}
	 */
	@ManyToOne
	@JoinColumn(name="USERID")
	public User getUser() {
		return this.user;
	}

	/**
	 * {@link Comment#commentid}
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
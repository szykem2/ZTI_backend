package project.models;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

/**
 * Klasa trwałości dla tabeli COMMENTS w bazie danych
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
	 * Zawartość komentarza
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
	 * {@link Comment#content}
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * {@link Comment#content}
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * {@link Comment#created}
	 */
	public Timestamp getCreated() {
		return this.created;
	}

	/**
	 * {@link Comment#created}
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}

	/**
	 * {@link Comment#item}
	 */
	@ManyToOne
	@JoinColumn(name="ITEMID")
	public Item getItem() {
		return this.item;
	}

	/**
	 * {@link Comment#item}
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * {@link Comment#user}
	 */
	@ManyToOne
	@JoinColumn(name="USERID")
	public User getUser() {
		return this.user;
	}

	/**
	 * {@link Comment#user}
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Metoda służy do porównywania ze sobą obiektów typu Comment
	 * @param obj obiekt, który chcemy porównać do aktualnego
	 */
	@Override
	public boolean equals(Object obj) {
		return this.commentid == ((Comment)obj).getCommentid();
	}
}
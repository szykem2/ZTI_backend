package project.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the COMMENTS database table.
 * 
 */
@Entity
@Table(name="COMMENTS")
@NamedQuery(name="Comment.findAll", query="SELECT c FROM Comment c")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	private int commentid;
	private String content;
	private Date created;
	private Item item;
	private User user;

	public Comment() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getCommentid() {
		return this.commentid;
	}

	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}


	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	@Temporal(TemporalType.DATE)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	//bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name="ITEMID")
	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}


	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USERID")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
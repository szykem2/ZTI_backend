package project.models;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="comments")
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int commentID;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	private int userID;
	
	@ManyToOne
	@JoinColumn(name = "itemid")
	private int itemID;
	private String content;
	private Date created;
	
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int id) {
		this.commentID = id;
	}
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int id) {
		this.userID = id;
	}
	
	public int getItemID() {
		return itemID;
	}
	public void setItemID(int id) {
		this.itemID = id;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String cnt) {
		this.content = cnt;
	}
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
}

package project.utils;

import javax.ws.rs.core.Response;

public class HeaderException extends Throwable{
	private static final long serialVersionUID = 1L;
	private Response resp;
	public HeaderException(Response resp ) {
		this.resp = resp;
	}
	public Response getResponse() {
		return resp;
	}
}

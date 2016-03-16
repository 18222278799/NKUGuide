package uguide.nankai.po;

import java.io.Serializable;

public class bus implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String jingdu;
	private String weidu;
	public String getId() {
		return id;
	}
	public void setId(String name) {
		this.id = name;
	}
	public String getJingdu() {
		return jingdu;
	}
	public void setJingdu(String jingdu) {
		this.jingdu = jingdu;
	}
	public String getWeidu() {
		return weidu;
	}
	public void setWeidu(String weidu) {
		this.weidu = weidu;
	}
	
}

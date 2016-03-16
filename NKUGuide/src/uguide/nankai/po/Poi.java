package uguide.nankai.po;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Poi implements Serializable{
	private String name;
	private double jingdu;
	private double weidu;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getJingdu() {
		return jingdu;
	}
	public void setJingdu(double jingdu) {
		this.jingdu = jingdu;
	}
	public double getWeidu() {
		return weidu;
	}
	public void setWeidu(double weidu) {
		this.weidu = weidu;
	}
}

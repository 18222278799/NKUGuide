package uguide.nankai.po;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Scenery implements Serializable {
	private int id;				//编号
	private String name;		//地点名称*
	private String detail;		//详情介绍*
	private String address;		//详细地址
	private int pic;			//贴图ID*
	private int region;			//校区（默认本部）*
	private double longitude;			//经度
	private double latitude;			//纬度
	
	public boolean hasLocation=false;
	public final static int NANKAI = 0;
	public final static int JINNAN = 1;
	public Scenery set(String name, String detail, int pic, int region) {
		this.name = name;
		this.detail = detail;
		this.pic = pic;
		this.region = region;
		return this;
	}
	public Scenery setLocation(double latitude,double longitude){
		this.longitude=longitude;
		this.latitude=latitude;
		hasLocation=true;
		return this;
	}
	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPic() {
		return pic;
	}

	public void setPic(int pic) {
		this.pic = pic;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}
}

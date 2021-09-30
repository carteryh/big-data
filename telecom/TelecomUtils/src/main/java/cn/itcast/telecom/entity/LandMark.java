
package cn.itcast.telecom.entity;

public class LandMark {
    //此处需要与landMark.xml配置文件内的内容完全相同

    private double lowerLeftLon;//左下角经度
    private double lowerLeftLat;//左下角纬度
    private double topRightLon;//右上角经度
    private double topRightLat;//右上角纬度
    private String category;//一级地标（所属分类）
    private String landMarkName;//二级地标 （地标名称）

    public LandMark() {
    }

    public double getLowerLeftLon() {
        return this.lowerLeftLon;
    }

    public void setLowerLeftLon(double lowerLeftLon) {
        this.lowerLeftLon = lowerLeftLon;
    }

    public double getLowerLeftLat() {
        return this.lowerLeftLat;
    }

    public void setLowerLeftLat(double lowerLeftLat) {
        this.lowerLeftLat = lowerLeftLat;
    }

    public double getTopRightLon() {
        return this.topRightLon;
    }

    public void setTopRightLon(double topRightLon) {
        this.topRightLon = topRightLon;
    }

    public double getTopRightLat() {
        return this.topRightLat;
    }

    public void setTopRightLat(double topRightLat) {
        this.topRightLat = topRightLat;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLandMarkName() {
        return this.landMarkName;
    }

    public void setLandMarkName(String landMarkName) {
        this.landMarkName = landMarkName;
    }
}

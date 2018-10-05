package trithe.modelproject.model;

public class Thongke {
    private String ten;
    private String sotien;

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSotien() {
        return sotien;
    }

    public void setSotien(String sotien) {
        this.sotien = sotien;
    }

    public Thongke(String ten, String sotien) {

        this.ten = ten;
        this.sotien = sotien;
    }
}

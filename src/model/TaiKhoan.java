
package model;

import java.util.Date;

public class TaiKhoan {
    String tenDangNhap, matKhau,maNhanVien, hoTen, loaiTaiKhoan;
    Date ngayTao ;
    public TaiKhoan() {
    }

    public TaiKhoan(String tenDangNhap, String matKhau, String maNhanVien, String hoTen, String loaiTaiKhoan, Date ngayTao) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.ngayTao = ngayTao;
    }

    public TaiKhoan(String tenDangNhap, String maNhanVien,String loaiTaiKhoan, Date ngayTao) {
        this.tenDangNhap = tenDangNhap;
        this.maNhanVien = maNhanVien;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.ngayTao = ngayTao;
    }

    public TaiKhoan(String tenDangNhap, String matKhau, String maNhanVien, String loaiTaiKhoan, Date ngayTao) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maNhanVien = maNhanVien;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.ngayTao = ngayTao;
    }
    
    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(String loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }
    
   
}

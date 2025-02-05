
package controller;

import DAO.KetNoiCoSoDuLieu;
import DAO.ShareData;
import java.math.BigInteger;
import java.net.PasswordAuthentication;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.function.Consumer;
import javax.mail.Session;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.JFrame;
import model.ChucVu;
import model.LoaiThietBi;
import model.NhaSanXuat;
import model.NhanVien;
import model.TaiKhoan;
import model.ThietBi;
import view.FormTrangChu_NhanVien;
import view.FormTrangChu_QuanLy;

public class XuLy {
    private TaiKhoan taiKhoan;
    private ChucVu chucVu;
    private NhanVien nhanvien;
    private LoaiThietBi loaiThietBi;
    private NhaSanXuat nhaSanXuat;
    private ThietBi thietBi;
    private Random generator = new Random();
     public boolean DangNhap(String tenDangNhap, String matKhau, JFrame jFrame) throws SQLException{
        boolean ketQua = false;
        String sql ="EXECUTE SP_DANGNHAP ?,?";
        
        try{
            Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                TaiKhoan tk = new TaiKhoan();
                tk.setTenDangNhap(rs.getString("TENDANGNHAP"));
                tk.setHoTen(rs.getString("HOTEN"));
                tk.setLoaiTaiKhoan(rs.getString("LOAITAIKHOAN"));
                ShareData.nguoiDangNhap = tk;
                if(rs.getString("LOAITAIKHOAN").equals("Nhân viên")){
                    FormTrangChu_NhanVien trangChu_NhanVien = new FormTrangChu_NhanVien();
                    jFrame.dispose();
                    trangChu_NhanVien.setVisible(true);
                }
                else if(rs.getString("LOAITAIKHOAN").equals("Quản lý")){
                    FormTrangChu_QuanLy trangChu_QuanLy = new FormTrangChu_QuanLy();
                    jFrame.dispose();
                    trangChu_QuanLy.setVisible(true);
                }
            ketQua = true;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return ketQua;
    }
     public int soNgauNhien(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }
     public String matKhauNgauNhien(int n){
         String chuThuong = "abcdefghijklmnopqrstuvwxyz";
         String chuHoa = chuThuong.toUpperCase();
         String chuSo = "0123456789";
         String chuoiNgauNhien = chuThuong + chuHoa + chuSo  ;
         StringBuilder sb = new StringBuilder();
         List<String> kq = new ArrayList<>();
         Consumer<String> appendChar = s -> {
             int num = soNgauNhien(0,s.length()-1);
             kq.add(""+s.charAt(num));
         };
         appendChar.accept(chuSo);
         while(kq.size() < n){
             appendChar.accept(chuoiNgauNhien);
         }
         Collections.shuffle(kq,generator);
         return String.join("", kq);
     }
     public ArrayList layTaiKhoan(){
        ArrayList ar = new ArrayList();
        String sql = "EXEC SP_LAYTAIKHOAN";
        try{
                Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
                PreparedStatement ps = ketNoi.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    ar.add(taiKhoan = new TaiKhoan(rs.getString(1), rs.getString(3),rs.getString(4), rs.getDate(5)));
                }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ar;
    }
    
    public boolean ktDoiMatKhau(String tenDangNhap, String matKhau){
        String sql ="EXEC SP_KTDOIMATKHAU ?,? ";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        boolean kq = false;
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                kq = true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return kq;
    }
    
    public void doiMatKhau(String tenDangNhap, String matKhau){
        String sql ="EXEC SP_DOIMATKHAU "+ tenDangNhap +", ?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, matKhau);
            ps.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList layChucVu(){
        ArrayList ar = new ArrayList();
        
        String sql="EXEC SP_LAYCHUCVU";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ar.add(chucVu = new ChucVu(rs.getString(1), rs.getString(2), rs.getInt(3)));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ar;
    }
    public String xuLyLuong(float luong){
        
        Locale localeVN = new Locale("vi","VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String vnd = currencyVN.format(luong);
        return vnd;
    }
    public ArrayList layNhanVien(){
        ArrayList arr = new ArrayList();
        String sql = "EXEC SP_LAYNHANVIEN ";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                arr.add(nhanvien = new NhanVien(rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getDate(5), rs.getString(6),
                        rs.getString(7), rs.getString(8), rs.getString(9),rs.getFloat(10)));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return arr;
    }
    public ArrayList layTTNhanVien(String maNhanVien){
        ArrayList arr = new ArrayList();
        String sql ="EXEC SP_LAYTHONGTINNHANVIEN ?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                arr.add(nhanvien = new NhanVien(rs.getString(1), rs.getString(2), rs.getDate(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getFloat(8), rs.getString(9)));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return arr;
    }
    public int layMaNhanVien(String maCV){
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql = "EXEC SP_LAYMANHANVIEN ?";
        int max =0;
        ArrayList list = new ArrayList();
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maCV);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(rs.getString(1).trim().substring(2,rs.getString(1).trim().length()));
            }
            if(list.size() == 0){
                return list.size();
            }
            else{
                for(int i= 0; i<list.size();i++){
                    if(max < Integer.parseInt(list.get(i).toString())){
                        max = Integer.parseInt(list.get(i).toString());
                    }
                    else{
                        continue;
                    }
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    return max;
    }
    public boolean ktThemNhanVien(String maNV){
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql ="EXEC SP_KTTHEMNHANVIEN ?";
        boolean kq = true;
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                kq = false;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return kq;
    }
    public void themNhanVien(String maNhanVien, String hoTen, Date ngaySinh, String gioiTinh, String diaChi, String sdt, String email,
            float bacLuong, String maChucVu){
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql ="EXEC SP_THEMNHANVIEN ?,?,?,?,?,?,?,?,?";
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ps.setString(2, hoTen);
            ps.setDate(3, ngaySinh);
            ps.setString(4, gioiTinh);
            ps.setString(5, diaChi);
            ps.setString(6, sdt);
            ps.setString(7, email);
            ps.setFloat(8, bacLuong);
            ps.setString(9, maChucVu);
            ps.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void xoaNhanVien(String maNhanVien){
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql = "EXEC SP_XOANHANVIEN ?";
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ps.executeUpdate();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }  
    public void suaNhanVien(String maNhanVien, String hoTen, Date ngaySinh, String gioiTinh, String diaChi, String sdt, String email,
            float bacLuong, String maChucVu, String maNhanVienThayDoi){
        String sql ="EXEC SP_SUANHANVIEN ?,?,?,?,?,?,?,?,?,?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ps.setString(2, hoTen);
            ps.setDate(3, ngaySinh);
            ps.setString(4, gioiTinh);
            ps.setString(5, diaChi);
            ps.setString(6, sdt);
            ps.setString(7, email);
            ps.setFloat(8, bacLuong);
            ps.setString(9, maChucVu);
            ps.setString(10, maNhanVienThayDoi);
            ps.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public boolean ktThemChucVu(String maChucVu){
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql ="EXEC SP_KTTHEMCHUCVU ?";
        boolean kq = true;
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maChucVu);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                kq = false;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return kq;
    }
    public void themChucVu(String maChucVu, String tenChucVu, float luongCoBan){
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql = "EXEC SP_THEMCHUCVU ?,?,?";
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maChucVu);
            ps.setString(2, tenChucVu);
            ps.setFloat(3, luongCoBan);
            ps.executeUpdate();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void suaChucVu(String maChucVu, String tenChucVu, float luongCoBan){
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql = "EXEC SP_SUACHUCVU ?,?,?";
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maChucVu);
            ps.setString(2, tenChucVu);
            ps.setFloat(3, luongCoBan);
            ps.executeUpdate();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void xoaChucVu(String maChucVu){
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql = "EXEC SP_XOACHUCVU ?";
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maChucVu);
            ps.executeUpdate();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public String layTenNV(String maNhanVien){
        String array = new String();
        String sql ="SELECT * FROM NHANVIEN WHERE MANHANVIEN = ?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                array = rs.getString("HOTEN");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return array;
    }
    public String layNgaySinh(String maNhanVien){
        String array = new String();
        String sql ="SELECT * FROM NHANVIEN WHERE MANHANVIEN = ?";
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try{
            PreparedStatement ps =  ketNoi.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                array = dateformat.format(rs.getDate("NGAYSINH"));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return array;
    }
    public boolean ktTaiKhoan(String tenDangNhap){
        String sql ="SELECT * FROM TAIKHOAN WHERE TENDANGNHAP = ?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        boolean kq = true;
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, tenDangNhap);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                kq = false;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return kq;
    }
    public void themTaiKhoan(String tenDangNhap, String matKhau, String maNhanVien, Date ngayTao,String loaiTaiKhoan){
         Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
         String sql ="EXEC SP_THEMTAIKHOAN ?,?,?,?,?";
         try{
             PreparedStatement ps = ketNoi.prepareStatement(sql);
             ps.setString(1, tenDangNhap);
             ps.setString(2, matKhau);
             ps.setString(3, maNhanVien);
             ps.setDate(4, ngayTao);
             ps.setString(5, loaiTaiKhoan);
             ps.executeUpdate();
         }
         catch(Exception ex){
             ex.printStackTrace();
         }
    }
    public void suaTaiKhoan(String tenDangNhap, String loaiTaiKhoan,Date ngayTao, String tenDangNhapThayDoi){
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
         String sql ="EXEC SP_SUATAIKHOAN ?,?,?,?";
         try{
             PreparedStatement ps = ketNoi.prepareStatement(sql);
             ps.setString(1, tenDangNhap);
             ps.setString(2, loaiTaiKhoan);
             ps.setDate(3, ngayTao);
             ps.setString(4, tenDangNhapThayDoi);
             ps.executeUpdate();
         }
         catch(Exception ex){
             ex.printStackTrace();
         }
    }
    public void xoaTaiKhoan(String tenDangNhap){
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql ="EXEC SP_XOATAIKHOAN ?";
        try{
             PreparedStatement ps = ketNoi.prepareStatement(sql);
             ps.setString(1, tenDangNhap);
             ps.executeUpdate();
         }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void guiMail(String toMail, String title, String text) throws AddressException, MessagingException{
        Properties mailServerProperties;
        Session getMailSession;
        MimeMessage mailMessage;
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        mailMessage = new MimeMessage(getMailSession);
        mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
        mailMessage.setSubject(title);
        mailMessage.setText(text);
        Transport transport = getMailSession.getTransport("smtp");
        transport.connect("smtp.gmail.com", "nguyentuanphung270301@gmail.com", "odwavnvxyjtwekjs"); 
        transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
        transport.close();
    }
    public String maHoaMatKhau(String matKhau){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(matKhau.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch(NoSuchAlgorithmException ex){
            throw new RuntimeException(ex);
        }
    }
    public ArrayList layLoaiThietBi(){
        ArrayList arr = new ArrayList();
        String sql ="SELECT * FROM LOAITHIETBI";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                arr.add(loaiThietBi = new LoaiThietBi(rs.getString(1), rs.getString(2)));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return arr;
    }
    public ArrayList layNhaSanXuat(){
        ArrayList arr = new ArrayList();
        String sql ="SELECT * FROM NHASANXUAT";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try{
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                arr.add(nhaSanXuat = new NhaSanXuat(rs.getString(1).trim(), rs.getString(2).trim(),rs.getString(3).trim(),rs.getString(4).trim(),rs.getString(5).trim()));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return arr;
    }
    public LoaiThietBi layLoaiThietBi(String maLoai) {
        String sql = "EXEC SP_LAYLOAITHIETBI ?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maLoai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                loaiThietBi = new LoaiThietBi(rs.getString(1), rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loaiThietBi;
    }
    public NhaSanXuat layNhaSanXuat(String maNSX) {
        String sql = "EXEC SP_LAYNHASANXUAT ?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maNSX);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nhaSanXuat = new NhaSanXuat(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nhaSanXuat;
    }
    public ArrayList layThietBi() {
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        ArrayList arry = new ArrayList();
        String sql = "SELECT * FROM THIETBI";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arry.add(thietBi = new ThietBi(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getFloat(7), rs.getBytes(8)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arry;
    }
    public int layMaThietBi(String maThietBi) {
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql = "EXEC SP_LAYMATHIETBI ?";
        int max = 0;
        ArrayList list = new ArrayList();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maThietBi);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("MATHIETBI").trim().substring(2, rs.getString("MATHIETBI").trim().length()));
            }
            if (list.size() == 0) {
                return max;
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (max < Integer.parseInt(list.get(i).toString())) {
                        max = Integer.parseInt(list.get(i).toString());
                    } else {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max;
    }
     public boolean ktThemThietBi(String maThietBi) {
        String sql = "EXEC SP_LAYMATHIETBI ?";
        boolean ketQua = true;
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maThietBi);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ketQua = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ketQua;
    }
    public void themThietBi(String maThietBi, String tenThietBi, String maLoai, String maNSX, String tgBaoHanh, String gia, byte[] path) {
        String sql = "EXEC SP_THEMTHIETBI ?,?,?,?,?,?,?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maThietBi);
            ps.setString(2, tenThietBi);
            ps.setString(3, maLoai);
            ps.setString(4, maNSX);
            ps.setString(5, tgBaoHanh);
            ps.setString(6, gia );
            
            Blob hinh = new SerialBlob(path);
            ps.setBlob(7, hinh);
            
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
      public void suaThietBi(String maThietBi, String tenThietBi, String maLoai, String maNSX, String tgBaoHanh, String gia, byte[] path, String maThietBiThayDoi) {
        String sql = "EXEC SP_SUATHIETBI ?,?,?,?,?,?,?,?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maThietBi);
            ps.setString(2, tenThietBi);
            ps.setString(3, maLoai);
            ps.setString(4, maNSX);
            ps.setString(5, tgBaoHanh);
            ps.setString(6, gia );
            
            Blob hinh = new SerialBlob(path);
            ps.setBlob(7, hinh);
            
            ps.setString(8, maThietBiThayDoi);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
      public void xoaThietBi(String maThietBi) {
        String sql = "EXEC SP_XOATHIETBI ?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maThietBi);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public boolean ktLoaiThietBi(String maLoai) {
        boolean kq = true;
        String sql = "EXEC SP_LAYLOAITHIETBI ?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maLoai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kq;
    }
    public void themLoaiThietBi(String maLoai, String tenLoai) {
        String sql = "SP_THEMLOAITHIETBI ?,?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maLoai);
            ps.setString(2, tenLoai);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void suaLoaiThietBi(String maLoai, String tenLoai, String maLoaiThayDoi) {
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql = "EXEC SP_SUALOAITHIETBI ?,?,?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maLoai);
            ps.setString(2, tenLoai);
            ps.setString(3, maLoaiThayDoi);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
     public void xoaLoaiThietBi(String maLoai) {
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql = "EXEC SP_XOALOAITHIETBI ?";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maLoai);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
     public boolean ktNhaSanXuat(String maNSX) {
        boolean kq = true;
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        String sql = "SELECT * FROM NHASANXUAT";
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (maNSX.equals(rs.getString("MANSX").toString().trim())) {
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return kq;
    }
    public void themNhaSanXuat(String maNSX, String Ten, String diaChi, String sdt, String email) {
        String sql = "EXEC SP_THEMNHASANXUAT ?,?,?,?,?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maNSX);
            ps.setString(2, Ten);
            ps.setString(3, diaChi);
            ps.setString(4, sdt);
            ps.setString(5, email);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
     public void suaNhaSanXuat(String maNSX, String Ten, String diaChi, String sdt, String email, String maNSXChange) {
        String sql = "EXEC SP_SUANHASANXUAT ?,?,?,?,?,?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maNSX);
            ps.setString(2, Ten);
            ps.setString(3, diaChi);
            ps.setString(4, sdt);
            ps.setString(5, email);
            ps.setString(6, maNSXChange);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
     public void xoaNhaSanXuat(String maNSX) {
        String sql = "EXEC SP_XOANHASANXUAT ?";
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ps.setString(1, maNSX);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
     public ArrayList timthietBi(String sql) {
        ArrayList arry = new ArrayList();
        Connection ketNoi = KetNoiCoSoDuLieu.layKetNoi();
        try {
            PreparedStatement ps = ketNoi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arry.add(thietBi = new ThietBi(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getFloat(7), rs.getBytes(8)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arry;
    }
}

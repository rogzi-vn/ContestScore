package futuresky.projects.tracnghiem.chamthitracnghiem.Database.BaiThi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import futuresky.projects.tracnghiem.chamthitracnghiem.DataStruct.BaiThi.BaiThi;

public class BaiThiDatabase {
    private SQLiteDatabase database;
    private BaiThiHelper dbHelper;

    // Phương thức kiểm tra và tạo bảng nếu chưa có đồng thời cũng là phương thức khởi tạo
    public BaiThiDatabase(Context myContext) {
        this.dbHelper = new BaiThiHelper(myContext);
        this.database = this.dbHelper.getWritableDatabase();
    }

    // Phương thức dùng để thêm bài thi mới vào CSDL
    public void ThemBaiThiMoi(BaiThi baiThi) {
        ContentValues DuLieuBaiThi = new ContentValues();
        DuLieuBaiThi.put(dbHelper.ID, baiThi.getId());
        DuLieuBaiThi.put(dbHelper.TEN, baiThi.getTen());
        DuLieuBaiThi.put(dbHelper.NGAY_TAO, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(
                baiThi.getNgayTao()
        ));
        DuLieuBaiThi.put(dbHelper.LOAI_GIAY, baiThi.getLoaiGiay());
        DuLieuBaiThi.put(dbHelper.SO_CAU, baiThi.getSoCau());
        DuLieuBaiThi.put(dbHelper.HE_DIEM, baiThi.getHeDiem());

        this.database = this.dbHelper.getWritableDatabase();
        this.database.insert(dbHelper.TABLE_BaiThi, null, DuLieuBaiThi);
    }

    public void ThayTheBaiThi(BaiThi baiThi) {
        ContentValues DuLieuBaiThi = new ContentValues();
        DuLieuBaiThi.put(dbHelper.ID, baiThi.getId());
        DuLieuBaiThi.put(dbHelper.TEN, baiThi.getTen());
        DuLieuBaiThi.put(dbHelper.NGAY_TAO, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(
                baiThi.getNgayTao()
        ));
        DuLieuBaiThi.put(dbHelper.LOAI_GIAY, baiThi.getLoaiGiay());
        DuLieuBaiThi.put(dbHelper.SO_CAU, baiThi.getSoCau());
        DuLieuBaiThi.put(dbHelper.HE_DIEM, baiThi.getHeDiem());

        this.database = this.dbHelper.getWritableDatabase();
        this.database.replace(dbHelper.TABLE_BaiThi, null, DuLieuBaiThi);
    }

    // Phương thức để đếm số lượng bài thi có trong CSDL
    public int SoLuong() {
        this.database = dbHelper.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(this.database, this.dbHelper.TABLE_BaiThi);
    }

    // Phương thức để lấy số ID lớn nhất của danh sách bài thi
    public int MaxID() {
        this.database = this.dbHelper.getReadableDatabase();
        return (int) this.database.compileStatement("SELECT MAX(" + this.dbHelper.ID + ") FROM "
                + this.dbHelper.TABLE_BaiThi).simpleQueryForLong();
    }

    // Phương thức để lấy bài thi có mã là id
    public BaiThi LayBaiThi(int id) {
        this.database = this.dbHelper.getReadableDatabase();
        Cursor myCursor = this.database.query(true, this.dbHelper.TABLE_BaiThi, new String[]
                        {this.dbHelper.ID, this.dbHelper.TEN, this.dbHelper.NGAY_TAO, this.dbHelper.LOAI_GIAY, this.dbHelper.SO_CAU, this.dbHelper.HE_DIEM},
                this.dbHelper.ID + "=?", new String[]{Integer.toString(id)}, null, null, null, null, null);
        if (myCursor != null)
            myCursor.moveToFirst();
        return new BaiThi(myCursor.getString(0), myCursor.getString(1)
                , myCursor.getString(2), myCursor.getString(3)
                , myCursor.getString(4), myCursor.getString(5));
    }

    // Phương thức để lấy tất cả các bài thi hiện có
    public ArrayList<BaiThi> TatCaBaiThi() {
        ArrayList<BaiThi> ds = new ArrayList<BaiThi>();
        this.database = this.dbHelper.getReadableDatabase();
        Cursor mCursor = this.database.rawQuery("SELECT * FROM " + this.dbHelper.TABLE_BaiThi, null);
        // Nếu có bản ghi
        if (mCursor.moveToFirst()) {
            do {
                // Tạo một đôi tượng tạm thời va thêm các thông tin cho nó
                BaiThi baiThi = new BaiThi(mCursor.getString(0), mCursor.getString(1)
                        , mCursor.getString(2), mCursor.getString(3)
                        , mCursor.getString(4), mCursor.getString(5));
                // Sau đó thêm nó vào danh sách
                ds.add(baiThi);
            } while (mCursor.moveToNext());
        }
        return ds;
    }

    // Phương thức để xóa bài thi có mã là id
    public long XoaBaiThi(int id) {
        this.database = this.dbHelper.getReadableDatabase();
        Cursor mCursor = this.database.query(true, this.dbHelper.TABLE_BaiThi,
                new String[]{this.dbHelper.ID},
                this.dbHelper.ID + "=?", new String[]{Integer.toString(id)},
                null, null, null, null, null);
        this.database = this.dbHelper.getWritableDatabase();
        if (mCursor.getCount() == 0) {
            return -1;
        }
        return (long) this.database.delete(this.dbHelper.TABLE_BaiThi, this.dbHelper.ID + "=?", new String[]{Integer.toString(id)});
    }

    // Phương thức để xóa tât cả các bài thi
    public void XoaTatCa() {
        this.database = this.dbHelper.getReadableDatabase();
        Cursor mCursor = this.database.rawQuery("SELECT  * FROM " + this.dbHelper.TABLE_BaiThi, null);
        if (mCursor.moveToFirst()) {
            do {
                XoaBaiThi(Integer.parseInt(mCursor.getString(0)));
            } while (mCursor.moveToNext());
        }
    }

    // Phương thưc đếm sô phiếu trả lời đã quét trong bài thi có mã là id
    public int SoPhieu(int id) {
        return 0;
    }
}

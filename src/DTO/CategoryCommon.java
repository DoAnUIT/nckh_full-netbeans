package DTO;

public enum CategoryCommon {

    DEFAULT(0), THOI_SU(1), THE_GIOI(2), KINH_DOANH(3), GIAO_DUC(4), THE_THAO(5), GIAI_TRI(6), KHOA_HOC_CONG_NGHE(7);

    private final int value;

    private CategoryCommon(int a) {
        // TODO Auto-generated constructor stub
        this.value = a;
    }

    public int getValue() {
        return this.value;
    }
}

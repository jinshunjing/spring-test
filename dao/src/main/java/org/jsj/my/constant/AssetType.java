package org.jsj.my.constant;

/**
 * 资产类型
 *
 * @author JSJ
 */
public enum AssetType {
    /**
     * 0-普通资产
     * 3-股权
     */
    NORMAL(0),
    EQUITY(3);

    private int type;

    AssetType(int type) {
        this.type = type;
    }

    public int type() {
        return type;
    }

    public static AssetType valueOf(int type) {
        for (AssetType aType : AssetType.values()) {
            if (aType.type == type) {
                return aType;
            }
        }
        return NORMAL;
    }
}

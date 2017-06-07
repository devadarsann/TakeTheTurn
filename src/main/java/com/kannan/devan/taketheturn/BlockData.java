package com.kannan.devan.taketheturn;

/**
 * Created by devan on 15/1/17.
 */

public class BlockData {

    String blockCause;
    String updateDate;
    String blockLatitude;
    String blockLongitude;

    public BlockData() {
    }

    public BlockData(String blockCause, String updateDate, String blockLatitude, String blockLongitude) {
        this.blockCause = blockCause;
        this.updateDate = updateDate;
        this.blockLatitude = blockLatitude;
        this.blockLongitude = blockLongitude;
    }

    public String getBlockLongitude() {
        return this.blockLongitude;
    }

    public void setBlockLongitude(String blockLongitude) {
        this.blockLongitude = blockLongitude;
    }

    public String getBlockLatitude() {
        return this.blockLatitude;
    }

    public void setBlockLatitude(String blockLatitude) {
        this.blockLatitude = blockLatitude;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBlockCause() {
        return this.blockCause;
    }

    public void setBlockCause(String blockCause) {
        this.blockCause = blockCause;
    }

}

package parquet.column.values.delta;


import parquet.Preconditions;
import parquet.bytes.BytesInput;
import parquet.bytes.BytesUtils;

import java.io.IOException;
import java.io.InputStream;

class DeltaBinaryPackingConfig {
   final int blockSizeInValues;
   final int miniBlockNum;
   final int miniBlockSizeInValues;

  public DeltaBinaryPackingConfig(int blockSizeInValues, int miniBlockNum) {
    this.blockSizeInValues=blockSizeInValues;
    this.miniBlockNum=miniBlockNum;
    double miniSize = (double) blockSizeInValues / miniBlockNum;
    Preconditions.checkArgument(miniSize % 8 == 0, "miniBlockSize must be multiple of 8, but it's " + miniSize );
    this.miniBlockSizeInValues = (int) miniSize;
  }

  public BytesInput toBytesInput(){
    return BytesInput.concat(
            BytesInput.fromUnsignedVarInt(blockSizeInValues),
            BytesInput.fromUnsignedVarInt(miniBlockNum));
  }

  public static DeltaBinaryPackingConfig readConfig(InputStream in) throws IOException {
    return new DeltaBinaryPackingConfig(BytesUtils.readUnsignedVarInt(in),
            BytesUtils.readUnsignedVarInt(in));
  }
}
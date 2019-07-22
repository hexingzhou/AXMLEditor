package me.souleo.axml.content.res;

/**
 * 给StringChunk添加一个非空String，同时会删除StringChunk中的styles
 */
public class AddStringEdit implements Edit<Integer> {

  private String forAdd;

  public AddStringEdit(String forAdd) {
    this.forAdd = forAdd;
  }

  public Integer edit(AXML axml) {
    if (forAdd == null || forAdd.equals("")) {
      throw new IllegalArgumentException("String for add == null or empty");
    }
    StringChunk chunk = axml.stringChunk;
    char[] array = forAdd.toCharArray();
    int[] stringOffsets = new int[chunk.stringOffsets == null ? 1 : chunk.stringOffsets.length + 1];
    if (chunk.stringOffsets != null) {
      System.arraycopy(chunk.stringOffsets, 0, stringOffsets, 0, chunk.stringOffsets.length);
    }

    int lastStringOffset = 0;
    int lastStringLength = 0;
    if (chunk.stringOffsets != null && chunk.stringOffsets.length > 0) {
      lastStringOffset = chunk.stringOffsets[chunk.stringOffsets.length - 1];
      lastStringLength = getShort(chunk.strings, lastStringOffset);
    }
    int stringsByteSize = 0;
    if (chunk.strings != null) {
      stringsByteSize = chunk.strings.length * 4;
    }
    if (lastStringLength == 0) {
      stringOffsets[stringOffsets.length - 1] = 0;
    } else {
      // 偏移计算时需要增加4个byte，2个为String的长度本身，2个为String与后一个String之间的空格
      stringOffsets[stringOffsets.length - 1] = lastStringOffset + lastStringLength * 2 + 4;
    }
    int stringsByteDiff = stringOffsets[stringOffsets.length - 1] + array.length * 2 + 2 - stringsByteSize;
    int stringsIntDiff = 0;
    if (stringsByteDiff > 0) {
      // 最后一个String在数组中也需要保证存在至少2个byte的空格
      stringsIntDiff = stringsByteDiff / 4 + 1;
    }

    int[] strings = new int[chunk.strings == null ? stringsIntDiff : chunk.strings.length + stringsIntDiff];
    if (chunk.strings != null) {
      System.arraycopy(chunk.strings, 0, strings, 0, chunk.strings.length);
    }
    setString(array, strings, stringOffsets[stringOffsets.length - 1]);

    chunk.stringOffsets = stringOffsets;
    chunk.strings = strings;
    chunk.styles = null;
    chunk.styleOffsets = null;
    chunk.chunkSize = 28 + chunk.stringOffsets.length * 4 + chunk.strings.length * 4;

    return chunk.stringOffsets.length - 1;
  }

  private static int getShort(int[] array, int offset) {
    int value = array[offset / 4];
    if ((offset % 4) / 2 == 0) {
      return (value & 0xFFFF);
    } else {
      return (value >>> 16);
    }
  }

  private static void setString(char[] chars, int[] array, int offset) {
    setShort((char) chars.length, array, offset);
    for (char c : chars) {
      offset += 2;
      setShort(c, array, offset);
    }
  }

  private static void setShort(char c, int[] array, int offset) {
    int index = offset / 4;
    if ((offset % 4) / 2 == 0) {
      array[index] = ((int) c) & 0xFFFF;
    } else {
      int value = ((int) c) & 0xFFFF;
      for (int i = 0; i < 16; i++) {
        int low = (value & 0x1) << 31;
        value = value >>> 1;
        value = value | low;
      }
      value = value & 0xFFFF0000;
      array[index] = array[index] & 0xFFFF;
      array[index] = value | array[index];
    }
  }
}

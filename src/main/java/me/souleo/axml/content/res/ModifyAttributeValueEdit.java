package me.souleo.axml.content.res;

import android.util.TypedValue;

public class ModifyAttributeValueEdit implements Edit<Void> {

  private final int index;
  private final int attributeIndex;
  private final int stringIndex;

  public ModifyAttributeValueEdit(int index, int attributeIndex, int stringIndex) {
    this.index = index;
    this.attributeIndex = attributeIndex;
    this.stringIndex = stringIndex;
  }

  @Override
  public Void edit(AXML axml) {
    StartTagChunk chunk = (StartTagChunk) axml.contentChunks.get(index);
    int offset = attributeIndex * StartTagChunk.ATTRIBUTE_LENGTH;
    chunk.attributes[offset + StartTagChunk.ATTRIBUTE_IX_VALUE_STRING] = stringIndex;
    chunk.attributes[offset + StartTagChunk.ATTRIBUTE_IX_VALUE_TYPE] = TypedValue.TYPE_STRING << 24;
    chunk.attributes[offset + StartTagChunk.ATTRIBUTE_IX_VALUE_DATA] = stringIndex;
    return null;
  }
}

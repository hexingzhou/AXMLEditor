package me.souleo.axml.content.res;

import me.souleo.axml.utils.IntReader;
import me.souleo.axml.utils.IntWriter;
import me.souleo.axml.xml.data.AttributeData;
import android.util.TypedValue;
import java.io.IOException;
import java.io.PrintStream;

public class StartTagChunk extends TagChunk {
  StartTagChunk() {
    super(CHUNK_TYPE);
  }

  int flags;
  int attributeCount;
  int classAttribute;
  int[] attributes;

  @Override
  void readTagContent(IntReader reader) throws IOException {
    flags = reader.readInt();
    attributeCount = reader.readInt();
    classAttribute = reader.readInt();
    attributes = reader.readIntArray(attributeCount * ATTRIBUTE_LENGTH);
  }

  @Override
  void writeTagContent(IntWriter writer) throws IOException {
    writer.writeInt(flags);
    writer.writeInt(attributeCount);
    writer.writeInt(classAttribute);
    writer.writeIntArray(attributes, 0, attributeCount * ATTRIBUTE_LENGTH);
  }

  public int getAttributeCount() {
    return attributeCount;
  }

  public AttributeData getAttribute(int index, StringChunk chunk) {
    if (index < 0 || index >= attributeCount) {
      return null;
    }
    int offset = getAttributeOffset(index);
    if (offset >= attributes.length) {
      return null;
    }
    int namespaceUri = attributes[offset + ATTRIBUTE_IX_NAMESPACE_URI];
    int name = attributes[offset + ATTRIBUTE_IX_NAME];
    int valueString = attributes[offset + ATTRIBUTE_IX_VALUE_STRING];
    int valueType = attributes[offset + ATTRIBUTE_IX_VALUE_TYPE] >> 24;
    int valueData = attributes[offset + ATTRIBUTE_IX_VALUE_DATA];

    AttributeData data = new AttributeData();
    data.setNamespaceUri(chunk.getString(namespaceUri));
    data.setName(chunk.getString(name));
    data.setValueString(chunk.getString(valueString));
    data.setValueType(valueType);
    if (valueType == TypedValue.TYPE_STRING) {
      data.setValueData(chunk.getString(valueString));
    }
    return data;
  }

  private int getAttributeOffset(int index) {
    return index * ATTRIBUTE_LENGTH;
  }

  @Override
  public void print(PrintStream stream) {
    super.print(stream);
    stream.println("\t-> StartTagChunk");
    stream.println("\t-> Attributes");
    if (attributes != null) {
      for (int i = 0; i < attributeCount; i++) {
        int offset = getAttributeOffset(i);
        int namespaceUri = attributes[offset + ATTRIBUTE_IX_NAMESPACE_URI];
        int name = attributes[offset + ATTRIBUTE_IX_NAME];
        int valueString = attributes[offset + ATTRIBUTE_IX_VALUE_STRING];
        int valueType = attributes[offset + ATTRIBUTE_IX_VALUE_TYPE] >> 24;
        int valueData = attributes[offset + ATTRIBUTE_IX_VALUE_DATA];
        stream.println("\t\t[" + i + "]: {"
                       + namespaceUri + ", "
                       + name + ", "
                       + valueString + ", "
                       + valueType + ", "
                       + valueData + "}");
      }
    }
  }

  public static final int ATTRIBUTE_IX_NAMESPACE_URI = 0;
  public static final int ATTRIBUTE_IX_NAME = 1;
  public static final int ATTRIBUTE_IX_VALUE_STRING = 2;
  public static final int ATTRIBUTE_IX_VALUE_TYPE = 3;
  public static final int ATTRIBUTE_IX_VALUE_DATA = 4;
  public static final int ATTRIBUTE_LENGTH = 5;

  public static final int CHUNK_TYPE = 0x00100102;
}

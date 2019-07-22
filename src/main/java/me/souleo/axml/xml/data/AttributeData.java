package me.souleo.axml.xml.data;

public class AttributeData {
  private String namespaceUri;
  private String name;
  private String valueString;
  private int valueType;
  private String valueData;

  public void setNamespaceUri(String namespaceUri) {
    this.namespaceUri = namespaceUri;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setValueString(String valueString) {
    this.valueString = valueString;
  }

  public String getValueString() {
    return this.valueString;
  }

  public void setValueType(int valueType) {
    this.valueType = valueType;
  }

  public int getValueType() {
    return this.valueType;
  }

  public void setValueData(String valueData) {
    this.valueData = valueData;
  }

  public String getValueData() {
    return this.valueData;
  }

  @Override
  public String toString() {
    return "[" + namespaceUri + ", "
           + name + ", "
           + valueString + ", "
           + valueType + ", "
           + valueData + "]";
  }
}

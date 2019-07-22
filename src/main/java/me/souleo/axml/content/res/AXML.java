package me.souleo.axml.content.res;

import me.souleo.axml.xml.data.AttributeData;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

public final class AXML {
  StringChunk stringChunk;
  ResourceChunk resourceChunk;
  List<XMLContentChunk> contentChunks = new ArrayList<>();

  public AXML() {
    this(new Builder());
  }

  AXML(Builder builder) {
    this.stringChunk = builder.stringChunk;
    this.resourceChunk = builder.resourceChunk;
    for (XMLContentChunk chunk : builder.contentChunks) {
      this.contentChunks.add(chunk);
    }
  }

  public int getContentChunkSize() {
    return contentChunks.size();
  }

  public List<AttributeData> getAttributeDataList(int index) {
    StartTagChunk chunk = getStartTagChunk(index);
    if (chunk == null) {
      return null;
    }
    List<AttributeData> attributes = new ArrayList<>();
    for (int i = 0; i < chunk.getAttributeCount(); i++) {
      AttributeData attribute = chunk.getAttribute(i, stringChunk);
      if (attribute != null) {
        attributes.add(attribute);
      }
    }
    return attributes;
  }

  public String getStartTagChunkName(int index) {
    StartTagChunk chunk = getStartTagChunk(index);
    if (chunk == null) {
      return null;
    }
    return chunk.getName(stringChunk);
  }

  public StartTagChunk getStartTagChunk(int index) {
    XMLContentChunk chunk = getContentChunk(index);
    if (chunk instanceof StartTagChunk) {
      return (StartTagChunk) chunk;
    }
    return null;
  }

  private XMLContentChunk getContentChunk(int index) {
    if (index < 0 || index >= contentChunks.size()) {
      throw new IllegalStateException(
              "Illegal index " + index + " of content chunk list");
    }
    return contentChunks.get(index);
  }

  public ContentChunkType getContentChunkType(int index) {
    if (index < 0 || index >= contentChunks.size()) {
      throw new IllegalStateException(
              "Illegal index " + index + " of content chunk list");
    }
    XMLContentChunk chunk = contentChunks.get(index);
    if (chunk instanceof StartTagChunk) {
      return ContentChunkType.START_TAG;
    }
    if (chunk instanceof EndTagChunk) {
      return ContentChunkType.END_TAG;
    }
    if (chunk instanceof StartNamespaceChunk) {
      return ContentChunkType.START_NAMESPACE;
    }
    if (chunk instanceof EndNamespaceChunk) {
      return ContentChunkType.END_NAMESPACE;
    }
    if (chunk instanceof TextChunk) {
      return ContentChunkType.TEXT;
    }
    throw new IllegalStateException(
            "Unknown type of content chunk at index " + index);
  }

  public final void print(PrintStream stream) {
    if (stringChunk != null) {
      stringChunk.print(stream);
    }
    if (resourceChunk != null) {
      resourceChunk.print(stream);
    }
    for (AXMLChunk chunk : contentChunks) {
      chunk.print(stream);
    }
  }

  public static final class Builder {
    private StringChunk stringChunk;
    private ResourceChunk resourceChunk;
    private List<XMLContentChunk> contentChunks = new ArrayList<>();

    public Builder stringChunk(StringChunk chunk) {
      if (chunk == null) {
        throw new IllegalArgumentException("StringChunk == null");
      }
      if (this.stringChunk != null) {
        throw new IllegalStateException(
                "StringChunk " + this.stringChunk + " has set");
      }
      this.stringChunk = chunk;
      return this;
    }

    public Builder resourceChunk(ResourceChunk chunk) {
      if (chunk == null) {
        throw new IllegalArgumentException("ResourceChunk == null");
      }
      if (this.resourceChunk != null) {
        throw new IllegalStateException(
                "ResourceChunk " + this.resourceChunk + "has set");
      }
      this.resourceChunk = chunk;
      return this;
    }

    public Builder addContentChunk(XMLContentChunk chunk) {
      if (chunk == null) {
        throw new IllegalArgumentException("XMLContentChunk == null");
      }
      this.contentChunks.add(chunk);
      return this;
    }

    public AXML build() {
      return new AXML(this);
    }
  }

  public enum ContentChunkType {
    START_TAG,
    END_TAG,
    START_NAMESPACE,
    END_NAMESPACE,
    TEXT;
  }
}

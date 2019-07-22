package me.souleo.axml.jobs;

import me.souleo.axml.content.res.AXML;
import me.souleo.axml.content.res.AddStringEdit;
import me.souleo.axml.content.res.ModifyAttributeValueEdit;
import me.souleo.axml.factory.Job;
import me.souleo.axml.xml.data.AttributeData;
import android.util.TypedValue;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ChangeAttributeValueJob implements Job {
  private final String findName;
  private final String findNameAttribute;
  private final Map<String, String> changeTo;

  public ChangeAttributeValueJob(String findName, String findNameAttribute,
                                 Map<String, String> changeTo) {
    this.findName = findName;
    this.findNameAttribute = findNameAttribute;
    this.changeTo = changeTo;
    if (changeTo == null) {
      throw new IllegalArgumentException("No changeTo in change attribute job!");
    }
  }

  @Override
  public void run(Chain chain) throws IOException {
    AXML axml = chain.axml();
    for (int i = 0; i < axml.getContentChunkSize(); i++) {
      String name = axml.getStartTagChunkName(i);
      if (name == null) {
        continue;
      }
      if (!name.equals(findName)) {
        continue;
      }
      boolean findOne = false;
      List<AttributeData> attributes = axml.getAttributeDataList(i);
      if (attributes != null) {
        for (AttributeData attribute : attributes) {
          String a_name = attribute.getName();
          if (a_name == null || !a_name.equals("name")) {
            continue;
          }
          int a_vtype = attribute.getValueType();
          String a_vdata = attribute.getValueData();
          if (a_vtype == TypedValue.TYPE_STRING
              && a_vdata != null
              && a_vdata.equals(findNameAttribute)) {
            findOne = true;
            break;
          }
        }
      }
      if (!findOne) {
        continue;
      }

      for (int index = 0; index < attributes.size(); index++) {
        AttributeData attribute = attributes.get(index);
        String a_name = attribute.getName();
        if (a_name == null) continue;
        if (changeTo.containsKey(a_name)) {
          AddStringEdit addEdit = new AddStringEdit(changeTo.get(a_name));
          int stringIndex = addEdit.edit(axml);
          ModifyAttributeValueEdit modifyEdit = new ModifyAttributeValueEdit(i, index, stringIndex);
          modifyEdit.edit(axml);
        }
      }

    }
    chain.proceed(axml);
  }
}

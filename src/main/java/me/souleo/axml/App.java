package me.souleo.axml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import me.souleo.axml.content.res.AXML;
import me.souleo.axml.content.res.AXMLReader;
import me.souleo.axml.content.res.AXMLWriter;
import me.souleo.axml.utils.IntReader;
import me.souleo.axml.jobs.ChangeAttributeValueJob;
import me.souleo.axml.utils.IntWriter;

public class App {
  public static void main(String[] args) throws IOException {
    copyApkAndSetChannel(args[0], args[1], args[2], args[3]);
  }

  private static void setChannel(File srcFile, File targetFile, String channel, String channelId) throws IOException {
    AXMLReader reader =
        new AXMLReader(new IntReader(new FileInputStream(srcFile), false));
    reader.open();
    AXML axml = reader.read();
    reader.close();

    Scheduler.Builder builder = new Scheduler.Builder();

    Map<String, String> channelChangeTo = new HashMap<>();
    channelChangeTo.put("value", channel);
    builder.addJob(new ChangeAttributeValueJob("meta-data", "channel", channelChangeTo));

    Map<String, String> channelIdChangeTo = new HashMap<>();
    channelIdChangeTo.put("value", channelId);
    builder.addJob(new ChangeAttributeValueJob("meta-data", "channel_id", channelIdChangeTo));
    Scheduler scheduler = builder.build();
    scheduler.call(axml).execute();

    AXMLWriter writer =
        new AXMLWriter(new IntWriter(new FileOutputStream(targetFile), false));
    writer.write(axml);
    writer.flush();
    writer.close();
  }

  private static void copyApkAndSetChannel(String srcApk, String targetApk, String channel, String channelId) throws IOException {
    ZipInputStream zin = new ZipInputStream(new FileInputStream(srcApk));
    ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(targetApk));
    ZipEntry entry;
    byte[] buffer = new byte[1024];
    while ((entry = zin.getNextEntry()) != null) {
      if (entry.isDirectory()
          || "CERT.SF".equals(entry.getName())
          || "CERT.RSA".equals(entry.getName())
          || "MANIFEST.MF".equals(entry.getName())) {
        // empty
      } else if ("AndroidManifest.xml".equals(entry.getName())) {
        File tempFile = new File(new File(srcApk).getParentFile(), "tmp.xml");
        FileOutputStream fout = new FileOutputStream(tempFile);
        int length;
        while ((length = zin.read(buffer, 0, 1024)) > 0) {
          fout.write(buffer, 0, length);
        }
        fout.flush();
        fout.close();

        File tempChangedFile = new File(new File(srcApk).getParentFile(), "tmp_changed.xml");
        setChannel(tempFile, tempChangedFile, channel, channelId);
        tempFile.delete();

        FileInputStream fin = new FileInputStream(tempChangedFile);
        ZipEntry outEntry = new ZipEntry(entry);
        outEntry.setCompressedSize(-1L);
        zout.putNextEntry(outEntry);
        while ((length = fin.read(buffer, 0, 1024)) > 0) {
          zout.write(buffer, 0, length);
        }
        zout.flush();
        zout.closeEntry();

        tempChangedFile.delete();
      } else {
        ZipEntry outEntry = new ZipEntry(entry);
        outEntry.setCompressedSize(-1L);
        zout.putNextEntry(outEntry);
        int length;
        while ((length = zin.read(buffer, 0, 1024)) > 0) {
          zout.write(buffer, 0, length);
        }
        zout.flush();
        zout.closeEntry();
      }
      zin.closeEntry();
    }
    zin.close();
    zout.flush();
    zout.close();
  }
}

package com.keenbrace.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	public static void zip(String src, String dest) throws IOException {
		// �ṩ��һ��������ѹ����һ��ZIP�鵵�����
		ZipOutputStream out = null;
		try {

			File outFile = new File(dest);// Դ�ļ�����Ŀ¼
			File fileOrDirectory = new File(src);// ѹ���ļ�·��
			out = new ZipOutputStream(new FileOutputStream(outFile));
			// ������ļ���һ���ļ�������Ϊfalse��
			if (fileOrDirectory.isFile()) {
				zipFileOrDirectory(out, fileOrDirectory, "");
			} else {
				// ����һ���ļ�������С�
				File[] entries = fileOrDirectory.listFiles();
				for (int i = 0; i < entries.length; i++) {
					// �ݹ�ѹ��������curPaths
					zipFileOrDirectory(out, entries[i], "");
				}
			}
		} catch (IOException ex) {
			Log.e("","",ex);
			ex.printStackTrace();
		} finally {
			// �ر������
			if (out != null) {
				try {
					out.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private static void zipFileOrDirectory(ZipOutputStream out,
			File fileOrDirectory, String curPath) throws IOException {
		// ���ļ��ж�ȡ�ֽڵ�������
		FileInputStream in = null;
		try {
			// ������ļ���һ��Ŀ¼�����򷵻�false��
			if (!fileOrDirectory.isDirectory()) {
				// ѹ���ļ�
				byte[] buffer = new byte[4096];
				int bytes_read;
				in = new FileInputStream(fileOrDirectory);
				// ʵ������һ����Ŀ�ڵ�ZIP�鵵
				ZipEntry entry = new ZipEntry(curPath
						+ fileOrDirectory.getName());
				// ��Ŀ����Ϣд��ײ���
				out.putNextEntry(entry);
				while ((bytes_read = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytes_read);
				}
				out.closeEntry();
			} else {
				// ѹ��Ŀ¼
				File[] entries = fileOrDirectory.listFiles();
				for (int i = 0; i < entries.length; i++) {
					// �ݹ�ѹ��������curPaths
					zipFileOrDirectory(out, entries[i], curPath
							+ fileOrDirectory.getName() + "/");
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			// throw ex;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void unzip(String zipFileName, String outputDirectory)
			throws IOException {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zipFileName);
			Enumeration e = zipFile.entries();
			ZipEntry zipEntry = null;
			File dest = new File(outputDirectory);
			dest.mkdirs();
			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				String entryName = zipEntry.getName();
				InputStream in = null;
				FileOutputStream out = null;
				try {
					if (zipEntry.isDirectory()) {
						String name = zipEntry.getName();
						name = name.substring(0, name.length() - 1);

						File f = new File(outputDirectory + File.separator
								+ name);
						f.mkdirs();
					} else {
						int index = entryName.lastIndexOf("\\");
						if (index != -1) {
							File df = new File(outputDirectory + File.separator
									+ new String(entryName.substring(0, index).getBytes(),"GBK"));
							df.mkdirs();
						}
						index = entryName.lastIndexOf("/");
						if (index != -1) {
							File df = new File(outputDirectory + File.separator
									+ new String(entryName.substring(0, index).getBytes(),"GBK"));
							df.mkdirs();
						}
						File f = new File(outputDirectory + File.separator
								+ new String(zipEntry.getName().getBytes(),"GBK"));
						// f.createNewFile();
						in = zipFile.getInputStream(zipEntry);
						out = new FileOutputStream(f);

						int c;
						byte[] by = new byte[1024];

						while ((c = in.read(by)) != -1) {
							out.write(by, 0, c);
						}
						out.flush();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
					throw new IOException("��ѹʧ�ܣ�" + ex.toString());
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException ex) {
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (IOException ex) {
						}
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IOException("��ѹʧ�ܣ�" + ex.toString());
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
					
				} catch (IOException ex) {
				}
			}
		}
	}
}

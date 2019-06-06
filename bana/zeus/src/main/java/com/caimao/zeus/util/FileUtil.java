package com.caimao.zeus.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Locale;
import java.util.Map;

public class FileUtil {

	public static void generateHTML(String folderName, String ftl, String htmlName, Map<String, Object> map, ServletContext servletContext, String path) throws Exception {
		Configuration cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(servletContext, File.separator + folderName);
		cfg.setEncoding(Locale.getDefault(), "UTF-8");
		Template template = cfg.getTemplate(ftl);
		template.setEncoding("UTF-8");
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path + htmlName));
		File htmlFile = new File(path + htmlName);
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8"));
		template.process(map, out);
		bufferedWriter.close();
		out.flush();
		out.close();
	}

}

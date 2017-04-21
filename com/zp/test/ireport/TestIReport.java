package com.zp.test.ireport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class TestIReport {
	public static void main(String[] args) throws Exception, FileNotFoundException {
		OutputStreamExporterOutput output = null;  
		File filePath = new File("F:\\ireport\\report1.jrxml");
		List<Map<String, ?>> listMap = getMapSource();
		JRDataSource jrds = new JRMapArrayDataSource(listMap.toArray());
		Map<String, Object> parameters = new HashMap<String, Object>();
		InputStream is = new FileInputStream(filePath);
		JasperReport jasperReport = JasperCompileManager.compileReport(is);
		JasperPrint report = JasperFillManager.fillReport(jasperReport, parameters, jrds);
		JRXlsExporter exporter = new JRXlsExporter();
		ExporterInput input = new SimpleExporterInput(report);
		output = new SimpleOutputStreamExporterOutput(new FileOutputStream("F:\\ireport\\excel1.xls"));
		exporter.setExporterInput(input);
		exporter.setExporterOutput(output);
		exporter.exportReport();
	}
	
	static List<Map<String, ?>> getMapSource(){
		List<Map<String, ?>> listMap = new ArrayList<Map<String, ?>>();
		for (int i=0; i<10 ;i++) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("aa", "aa"+i);
			m.put("bb", "bb"+i);
			m.put("cc", "cc"+i);
			m.put("dd", "dd"+i);
			m.put("ee", "ee"+i);
			listMap.add(m);
		}
		return listMap;
	}
	
	
}

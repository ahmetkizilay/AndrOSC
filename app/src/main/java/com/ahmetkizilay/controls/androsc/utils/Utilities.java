package com.ahmetkizilay.controls.androsc.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import android.content.Context;

public class Utilities {
	public static String readAssetFileContents(Context context, String filePath) {
		InputStream is = null;
		try {
			is = context.getAssets().open(filePath);
			Reader reader = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			return builder.toString();
		}
		catch(Exception exp) {
			// TODO: Logging and error handling
			exp.printStackTrace();			
			return null;
		}
		finally {
			if(is != null) {
				try {is.close(); }catch(Exception e) {}
			}
		}
	}
	
	
	public static String readFileContents(String filePath) {
		InputStream is = null;
		try {
			is = new FileInputStream(filePath);
			Reader reader = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			return builder.toString();
		}
		catch(Exception exp) {
			// TODO: Logging and error handling
			exp.printStackTrace();			
			return null;
		}
		finally {
			if(is != null) {
				try {is.close(); }catch(Exception e) {}
			}
		}
	}
	
	public static boolean readStringIntoFile(String contents, String filePath) {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(new File(filePath)));
			writer.append(contents);
			return true;
		} catch (IOException e) {
			return false;
		}
		finally {
			if(writer != null) { 
				try { writer.close(); }catch(Exception exp){}
			}
		}		
	}	
}

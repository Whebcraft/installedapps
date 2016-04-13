package com.awaa.installedapps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class installedapps extends CordovaPlugin { 
	
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
      
        if (action.equals("getIcon")) {
        	final String packageName = args.getJSONObject(0).getString("package");
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                	//Setup
                    PackageManager pm = cordova.getActivity().getPackageManager();
                    String appPackageName = cordova.getActivity().getPackageName();
                    String appDir = null;
                    try {
                         PackageInfo pmi = pm.getPackageInfo(appPackageName, 0);
                         appDir = pmi.applicationInfo.dataDir;      
                    } catch (NameNotFoundException e) {
                        //Log.w("yourtag", "Error Package name not found ", e);
                    } 
                        
                    File iconDir = new File(appDir+"/icons");
                    if(!iconDir.exists()){
                        iconDir.mkdirs();
                    }
                    //end setup
                    
        			try {
        				ApplicationInfo choosenApp = pm.getApplicationInfo(packageName, 128);		
                        Drawable appIcon = choosenApp.loadIcon(pm);             
                        
                        File file = new File(iconDir, "/"+packageName+".png");

                        FileOutputStream foStream = null;

                        Bitmap bitmap = ((BitmapDrawable)appIcon).getBitmap();

                        ByteArrayOutputStream oStream = new ByteArrayOutputStream();  
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream); //bm is the bitmap object   

                        byte[]	b = oStream.toByteArray();				
                        try {
                            foStream = new FileOutputStream(file);
                            oStream.write(b);
                            oStream.writeTo(foStream);
                            oStream.close();
                            foStream.close();					
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        appIcon = null;
                        file = null;
                        b = null;
                        foStream = null;
                        oStream = null;
                        bitmap = null;
        		        try {
							callbackContext.success(new JSONObject().put("returnVal", "file://"+appDir+"/icons/"+ packageName+".png"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
        			} catch (NameNotFoundException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}               	
                	
                }
            });
            return true;
        }        
        
        if (action.equals("getIcons")) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {

                	//Setup
                    PackageManager pm = cordova.getActivity().getPackageManager();
                    String appPackageName = cordova.getActivity().getPackageName();
                    String appDir = null;
                    try {
                         PackageInfo pmi = pm.getPackageInfo(appPackageName, 0);
                         appDir = pmi.applicationInfo.dataDir;      
                    } catch (NameNotFoundException e) {
                        //Log.w("yourtag", "Error Package name not found ", e);
                    } 
                        
                    File iconDir = new File(appDir+"/icons");
                    if(!iconDir.exists()){
                        iconDir.mkdirs();
                    }
                    //end setup
                    
                    JSONArray jArray = new JSONArray();
                    
                    List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                    for (ApplicationInfo packageInfo : packages) {

                        Intent appActivity = pm.getLaunchIntentForPackage(packageInfo.packageName);
                        try {
                        	
                            if(appActivity != null){
                                String pkgName = packageInfo.packageName;		
                                String appName = packageInfo.loadLabel(pm).toString();
                                
                                
                                //start icon
                                Drawable appIcon = packageInfo.loadIcon(pm);                                                      
                                File file = new File(iconDir, "/"+pkgName+".png");
                                FileOutputStream foStream = null;
                                Bitmap bitmap = ((BitmapDrawable)appIcon).getBitmap();
                                ByteArrayOutputStream oStream = new ByteArrayOutputStream();  
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream); //bm is the bitmap object   
                                byte[]	b = oStream.toByteArray();				
                                try {
                                    foStream = new FileOutputStream(file);
                                    oStream.write(b);
                                    oStream.writeTo(foStream);
                                    oStream.close();
                                    foStream.close();					
                                } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                appIcon = null;
                                file = null;
                                b = null;
                                foStream = null;
                                oStream = null;
                                bitmap = null;
                                //end icon
                                
                 
                                String[] appIntent = appActivity.toString().split("/");

                                String appIFormated = appIntent[1].substring(0, appIntent[1].length() - 2);

                                JSONObject json = new JSONObject();
								json.put("name", appName).put("activity", appIFormated).put("package", pkgName).put("path", "file://"+appDir+"/icons/"+ pkgName+".png");
								jArray.put(json);
                            }
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}							
                    }
                    packages = null;
                    System.gc();
                    String results = jArray.toString();
                    try {
						callbackContext.success(new JSONObject().put("returnVal", results));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}                  	
                	
                	
                }
            });
            return true;
        }
            
        if (action.equals("getApps")) {         
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                	
                	
                	//Setup
                    PackageManager pm = cordova.getActivity().getPackageManager();
                    String appPackageName = cordova.getActivity().getPackageName();
                    String appDir = null;
                    try {
                         PackageInfo pmi = pm.getPackageInfo(appPackageName, 0);
                         appDir = pmi.applicationInfo.dataDir;      
                    } catch (NameNotFoundException e) {
                        //Log.w("yourtag", "Error Package name not found ", e);
                    }    
                    //end setup            	
                	
                	 JSONArray jArray = new JSONArray();
                	 JSONArray jArray2 = new JSONArray();
					 JSONObject json = new JSONObject();

                     List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                     for (ApplicationInfo packageInfo : packages) {

                         try {
                             String pkgName = packageInfo.packageName;

                             Intent appActivity = pm.getLaunchIntentForPackage(packageInfo.packageName);

                             

                             //Code to send package information to Eclipse Log.
                             //Log.d(id, "Name:" + appName);
                             //Log.d(id, "Package:" + pkgName);
                             //Log.d(id, "Activity:" + appActivity);
 
                             if(appActivity != null){
                                 String appName = packageInfo.loadLabel(pm).toString();
                                 String[] appIntent = appActivity.toString().split("/");

                                 String appIFormated = appIntent[1].substring(0, appIntent[1].length() - 2);

                                
						// com.apptest	
      						
    if(pkgName.equals("com.chronicles.sb")){ String pkgName = "null";}
	else if(pkgName.equals("com.smartOS")){ String pkgName = "null"; }
    else if(pkgName.equals("com.extwebtech.bibliography")){ String pkgName = "null"; }
    else if(pkgName.equals("com.Phaedrus.EarwigFree")){ String pkgName = "null"; }
    else if(pkgName.equals("com.funbox.englishidioms")){ String pkgName = "null"; }
    else if(pkgName.equals("binh.app.englishidiom")){ String pkgName = "null"; }
    else if(pkgName.equals("air.com.revisionbuddies.gcsemaths")){ String pkgName = "null"; }
    else if(pkgName.equals("com.educationapps.gojimo")){ String pkgName = "null"; }
    else if(pkgName.equals("com.solvus_lab.android.BibleEN_kjv")){ String pkgName = "null"; }
    else if(pkgName.equals("org.khanacademy.android")){ String pkgName = "null"; }
    else if(pkgName.equals("com.learnerscloud.igcsemaths.videos")){ String pkgName = "null"; }
    else if(pkgName.equals("org.zezi.gb")){ String pkgName = "null"; }
    else if(pkgName.equals("com.mobisystems.msdict.embedded.wireless.oxford.dictionaryofenglish")){ String pkgName = "null"; }
    else if(pkgName.equals("com.aspartame.RemindMe")){ String pkgName = "null"; }
    else if(pkgName.equals("com.zapp2learn.app")){ String pkgName = "null"; }
    else if(pkgName.equals("com.kuntec.braincafe")){ String pkgName = "null"; }
    else if(pkgName.equals("com.icenta.sudoku.ui")){ String pkgName = "null"; }
    else if(pkgName.equals("com.underwater.geoquiz")){ String pkgName = "null"; }
    else if(pkgName.equals("org.pondar.mathquizfree")){ String pkgName = "null"; }
    else if(pkgName.equals("air.com.panapps.PeriodicTable")){ String pkgName = "null"; }
    else if(pkgName.equals("com.microblink.photomath")){ String pkgName = "null"; }
    else if(pkgName.equals("com.physics.quiz.game1")){ String pkgName = "null"; }
    else if(pkgName.equals("com.codeansoftware.speakforme")){ String pkgName = "null"; }
    else if(pkgName.equals("org.urbian.android.quiz.bible.demo")){ String pkgName = "null"; }
    else if(pkgName.equals("com.socratica.mobile.words")){ String pkgName = "null"; }
    else if(pkgName.equals("lammar.flags")){ String pkgName = "null"; }
    else if(pkgName.equals("org.unver.derstakvimi")){ String pkgName = "null"; }
    else if(pkgName.equals("com.learningmate.goclass.view")){ String pkgName = "null"; }
    else if(pkgName.equals("org.edx.mobile")){ String pkgName = "null"; }
    else if(pkgName.equals("de.rakuun.MyClassSchedule.free")){ String pkgName = "null"; }
    else if(pkgName.equals("com.speedAnatomy.speedAnatomyLite")){ String pkgName = "null"; }
    else if(pkgName.equals("com.studyblue")){ String pkgName = "null"; }
    else if(pkgName.equals("com.class123.teacher")){ String pkgName = "null"; }
    else if(pkgName.equals("com.udacity.android")){ String pkgName = "null"; }
    else if(pkgName.equals("com.udemy.android")){ String pkgName = "null"; }
    else if(pkgName.equals("bbc.mobile.news.ww")){ String pkgName = "null"; }
    else if(pkgName.equals("com.google.android.apps.blogger")){ String pkgName = "null"; }
    else if(pkgName.equals("com.cnn.mobile.android.phone")){ String pkgName = "null"; }
    else if(pkgName.equals("com.google.android.apps.plus")){ String pkgName = "null"; }
    else if(pkgName.equals("com.facebook.katana")){ String pkgName = "null"; }
    else if(pkgName.equals("com.google.android.youtube")){ String pkgName = "null"; }
    else if(pkgName.equals("com.skype.raider")){ String pkgName = "null"; }
    else if(pkgName.equals("com.twitter.android")){ String pkgName = "null"; }
    else if(pkgName.equals("com.whatsapp")){ String pkgName = "null"; }
    else if(pkgName.equals("com.mybio.apps")){ String pkgName = "null"; }
    else if(pkgName.equals("com.school.app")){ String pkgName = "null"; }
    else if(pkgName.equals("com.flyersoft.moonreader")){ String pkgName = "null"; }
    else if(pkgName.equals("com.hed.app")){ String pkgName = "null"; }
    else if(pkgName.equals("com.app.dailymanna2015")){ String pkgName = "null"; }
    else if(pkgName.equals("com.youth.life")){ String pkgName = "null"; }	
    else if(pkgName.equals("com.google.android.apps.docs.editors.docs")){ String pkgName = "null"; }
    else if(pkgName.equals("ng.com.efiko.quiz")){ String pkgName = "null"; }
    else if(pkgName.equals("com.google.android.apps.classroom")){ String pkgName = "null"; }
    else if(pkgName.equals("com.gidimo")){ String pkgName = "null"; }
    else if(pkgName.equals("com.google.android.apps.docs.editors.sheets")){ String pkgName = "null"; }
    else if(pkgName.equals("com.twitter.android")){ String pkgName = "null"; }
    else if(pkgName.equals("com.whatsapp")){ String pkgName = "null"; }
    else if(pkgName.equals("com.facebook.katana")){ String pkgName = "null"; }
    else if(pkgName.equals("com.linkedin.android")){ String pkgName = "null"; }
    else if(pkgName.equals("com.google.android.apps.plus")){ String pkgName = "null"; }
    else if(pkgName.equals("com.bbm")){ String pkgName = "null"; }
    else if(pkgName.equals("com.tencent.mm")){ String pkgName = "null"; }
    else if(pkgName.equals("com.skype.raider")){ String pkgName = "null"; }
    else if(pkgName.equals("com.sbreader")){ String pkgName = "null"; }
	else {
		json.put("name", appName).put("activity", appIFormated).put("package", pkgName).put("path", "file://"+appDir+"/icons/"+ pkgName+".png");
        jArray.put(json);
	}
								 
                             }
							 
														 

                         } catch (Exception e) {
                             // TODO Auto-generated catch block
                             e.printStackTrace();
                         }

                     }// Turns the JSON array into a string and returns the value. 

                     // String results = jArray.toString();				
                     String results = jArray.toString();				

                     try {
						callbackContext.success(new JSONObject().put("returnVal", results));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
                	
                }
            });
            return true;
        }      

        return true;       
	}  
}

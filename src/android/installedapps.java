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
      						
    if(pkgName.equals("com.chronicles.sb") || pkgName.equals("com.smartOS") || pkgName.equals("com.extwebtech.bibliography") || pkgName.equals("com.Phaedrus.EarwigFree") || pkgName.equals("com.funbox.englishidioms") || pkgName.equals("binh.app.englishidiom") || pkgName.equals("air.com.revisionbuddies.gcsemaths") || pkgName.equals("com.educationapps.gojimo") || pkgName.equals("com.solvus_lab.android.BibleEN_kjv") || pkgName.equals("org.khanacademy.android") || pkgName.equals("com.learnerscloud.igcsemaths.videos") || pkgName.equals("org.zezi.gb") || pkgName.equals("com.mobisystems.msdict.embedded.wireless.oxford.dictionaryofenglish") || pkgName.equals("com.aspartame.RemindMe") || pkgName.equals("com.zapp2learn.app") || pkgName.equals("com.kuntec.braincafe") || pkgName.equals("com.icenta.sudoku.ui") || pkgName.equals("com.underwater.geoquiz") || pkgName.equals("org.pondar.mathquizfree") || pkgName.equals("air.com.panapps.PeriodicTable") || pkgName.equals("com.microblink.photomath") || pkgName.equals("com.physics.quiz.game1") || pkgName.equals("com.codeansoftware.speakforme") || pkgName.equals("org.urbian.android.quiz.bible.demo") || pkgName.equals("com.socratica.mobile.words") || pkgName.equals("lammar.flags") || pkgName.equals("org.unver.derstakvimi") || pkgName.equals("com.learningmate.goclass.view") || pkgName.equals("org.edx.mobile") || pkgName.equals("de.rakuun.MyClassSchedule.free") || pkgName.equals("com.speedAnatomy.speedAnatomyLite") || pkgName.equals("com.studyblue") || pkgName.equals("com.class123.teacher") || pkgName.equals("com.udacity.android") || pkgName.equals("com.udemy.android") || pkgName.equals("bbc.mobile.news.ww") || pkgName.equals("com.google.android.apps.blogger") || pkgName.equals("com.cnn.mobile.android.phone") || pkgName.equals("com.google.android.apps.plus") || pkgName.equals("com.facebook.katana") || pkgName.equals("com.google.android.youtube") || pkgName.equals("com.skype.raider") || pkgName.equals("com.twitter.android") || pkgName.equals("com.whatsapp") || pkgName.equals("com.mybio.apps") || pkgName.equals("com.school.app") || pkgName.equals("com.flyersoft.moonreader") || pkgName.equals("com.hed.app") || pkgName.equals("com.app.dailymanna2015") || pkgName.equals("com.youth.life") || pkgName.equals("com.google.android.apps.docs.editors.docs") || pkgName.equals("ng.com.efiko.quiz") || pkgName.equals("com.google.android.apps.classroom") || pkgName.equals("com.gidimo") || pkgName.equals("com.google.android.apps.docs.editors.sheets") || pkgName.equals("com.twitter.android") || pkgName.equals("com.whatsapp") || pkgName.equals("com.facebook.katana") || pkgName.equals("com.linkedin.android") || pkgName.equals("com.google.android.apps.plus") || pkgName.equals("com.bbm") || pkgName.equals("com.tencent.mm") || pkgName.equals("com.skype.raider") || pkgName.equals("com.sbreader")){
		
	}
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

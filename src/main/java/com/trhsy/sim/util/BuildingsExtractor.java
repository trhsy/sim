package com.trhsy.sim.util;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @ClassName BuildingsExtractor
 * @Description todo
 * @Author TRHSY
 * @Date 2022/12/1620:02
 **/
public class BuildingsExtractor {
    public BuildingsExtractor() {
    }
    public static void extractBuildings(File targetDir) {
        try {
            String lang = FMLCommonHandler.instance().getCurrentLanguage();
            String bName="buildings_"+lang;
            InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("sim", "buildings/"+bName+".zip")).getInputStream();
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(inputStream);

            for(ZipEntry zipEntry = zis.getNextEntry(); zipEntry != null; zipEntry = zis.getNextEntry()) {
                File newFile;
                if (zipEntry.isDirectory()) {
                    newFile = new File(targetDir, zipEntry.toString());
                    newFile.mkdirs();
                } else {
                    newFile = new File(targetDir, zipEntry.getName());
                    FileOutputStream fos = new FileOutputStream(newFile);

                    int len;
                    while((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                }
            }

            zis.closeEntry();
            zis.close();
        }catch (Exception e){

        }
    }
}

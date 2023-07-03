package com.trhsy.sim.util;

import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @ClassName BuildingsExtractor
 * @Description todo 解压建筑蓝图
 * @Author TRHSY
 * @Date 2022/12/822:06
 **/
public class BuildingsExtractor {
    public BuildingsExtractor() {
    }
    public static void extractBuildings(File targetDir) {
        try {
            String lang = FMLCommonHandler.instance().getCurrentLanguage();
            String bName="buildings_"+lang;
            ModSimLoader.log.info("当前语言："+lang+",开始解压建筑蓝图");
            InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("sim", "buildings/"+bName+".zip")).getInputStream();
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(inputStream);

            for(ZipEntry zipEntry = zis.getNextEntry(); zipEntry != null; zipEntry = zis.getNextEntry()) {
                File newFile;
                if (zipEntry.isDirectory()) {
                    newFile = new File(targetDir, zipEntry.toString());
                    newFile.delete();
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

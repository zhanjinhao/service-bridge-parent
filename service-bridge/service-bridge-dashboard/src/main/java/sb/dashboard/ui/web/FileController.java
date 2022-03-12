package sb.dashboard.ui.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sb.core.init.config.ApplicationConfig;
import sb.core.single.pojo.Result;
import sb.core.single.pojo.buffer.ProviderSystem;
import sb.core.single.pojo.buffer.ProviderSystemFactory;
import sb.core.single.pojo.buffer.URLClassLoaderWrap;
import sb.core.ui.pojo.DownloadInfo;
import sb.core.ui.pojo.PageResult;
import sb.core.util.PaginationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/file")
public class FileController {

    @ResponseBody
    @PostMapping("/upload")
    public Result uploadJar(@RequestParam("jar") MultipartFile jar) {
        String jarPath = ApplicationConfig.JAR_PATH + File.separator + jar.getOriginalFilename();

        File file = new File(jarPath);
        if (file.exists()) {
            return new Result(701, "jarExist");
        }

        try {
            jar.transferTo(new File(jarPath));
        } catch (IOException e) {
            return Result.getError();
        }
        return Result.getSuccess();
    }


    @RequestMapping("download")
    public void downloadFile(@RequestParam("filename") String fileName, HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html;charset=utf-8");

        String filePath = ApplicationConfig.JAR_PATH + File.separator + fileName;

        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());

        ) {

            long fileLength = new File(filePath).length();
            response.setHeader("Content-disposition", "attachment; filename=" + new String(new File(filePath).getName().getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/getAllFileInfos")
    public ResponseEntity getAllFileInfos(
            @RequestParam("nameLike") String nameLike,
            @RequestParam("page") int page,
            @RequestParam("row") int row) {
        String jarPath = ApplicationConfig.JAR_PATH;
        File file = new File(jarPath);
        File[] files = file.listFiles();
        List<DownloadInfo> downloadInfos = new ArrayList<>();
        List<String> list = new ArrayList<>();
        Map<String, ProviderSystem> providerSystemMap = ProviderSystemFactory.getProviderSystemMap();
        Set<Map.Entry<String, ProviderSystem>> entries = providerSystemMap.entrySet();
        Iterator<Map.Entry<String, ProviderSystem>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ProviderSystem> next = iterator.next();
            ProviderSystem providerSystem = next.getValue();
            Map<String, URLClassLoaderWrap> urlClassLoaderWrapBuffer = providerSystem.getUrlClassLoaderWrapBuffer();
            Collection<URLClassLoaderWrap> values = urlClassLoaderWrapBuffer.values();
            Iterator<URLClassLoaderWrap> iterator1 = values.iterator();
            while (iterator1.hasNext()) {
                URLClassLoaderWrap next1 = iterator1.next();
                String jarName = next1.getJarName();
                list.add(jarName);
            }
        }
        for (File f : files) {
            String name = f.getName();
            if (nameLike == null || "".equals(nameLike) || name.contains(nameLike)) {
                DownloadInfo downloadInfo = new DownloadInfo(f.getName());
                if (f.getName().endsWith("jar")) {
                    downloadInfo.setType(DownloadInfo.JAR);
                } else {
                    downloadInfo.setType(DownloadInfo.IDL);
                }
                if (list.contains(name)) {
                    downloadInfo.setState(DownloadInfo.USING);
                } else {
                    downloadInfo.setState(DownloadInfo.NO_USE);
                }
                downloadInfos.add(downloadInfo);
            }
        }

        List pagination = PaginationUtils.Pagination(downloadInfos, page, row);

        PageResult pageResult = new PageResult(downloadInfos.size(), pagination);
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/deleteFile")
    public Result deleteFile(@RequestParam("filename") String fileName) {
        String filePath = ApplicationConfig.JAR_PATH + File.separator + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            return Result.getSuccess();
        }
        return Result.getError();
    }
}
